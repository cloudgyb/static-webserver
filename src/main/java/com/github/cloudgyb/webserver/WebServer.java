package com.github.cloudgyb.webserver;

import com.github.cloudgyb.webserver.config.SSLConfig;
import com.github.cloudgyb.webserver.config.WebServerConfig;
import com.github.cloudgyb.webserver.http.Http2RequestHandler;
import com.github.cloudgyb.webserver.http.HttpRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http2.Http2FrameCodecBuilder;
import io.netty.handler.ssl.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;

/**
 * 用于启动web服务器
 *
 * @author cloudgyb
 */
public class WebServer {
    private final static Logger logger = LoggerFactory.getLogger(WebServer.class);

    public static void start(WebServerConfig config) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        HttpRequestHandler httpRequestHandler = new HttpRequestHandler(config);
        SSLConfig sslConfig = config.getSslConfig();
        boolean hasSSLConfig = false;
        boolean enableHttp2 = config.enableHttp2();
        SslContext sslContext = null;
        if (sslConfig != null) {
            try {
                SslContextBuilder sslContextBuilder = SslContextBuilder
                        .forServer(sslConfig.getCert(), sslConfig.getPrivateKey())
                        .clientAuth(ClientAuth.NONE);
                if (enableHttp2) {
                    ApplicationProtocolConfig apn = new ApplicationProtocolConfig(
                            ApplicationProtocolConfig.Protocol.ALPN,
                            ApplicationProtocolConfig.SelectorFailureBehavior.NO_ADVERTISE,
                            ApplicationProtocolConfig.SelectedListenerFailureBehavior.CHOOSE_MY_LAST_PROTOCOL,
                            ApplicationProtocolNames.HTTP_2);
                    sslContextBuilder.applicationProtocolConfig(apn);
                }
                sslContext = sslContextBuilder.build();
                hasSSLConfig = true;
            } catch (SSLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            boolean finalHasSSLConfig = hasSSLConfig;
            SslContext finalSslContext = sslContext;
            ChannelFuture cf = serverBootstrap.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, config.getTcpBacklog())
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) {
                            if (finalHasSSLConfig) {
                                SSLEngine sslEngine = finalSslContext.newEngine(channel.alloc());
                                sslEngine.setUseClientMode(false);
                                SslHandler sslHandler = new SslHandler(sslEngine, false);
                                channel.pipeline()
                                        .addLast("ssl", sslHandler);
                            }
                            if (enableHttp2) {
                                channel.pipeline()
                                        .addLast(getServerAPNHandler(config));
                            } else {
                                channel.pipeline()
                                        .addLast("httpRequestDecoder", new HttpServerCodec())
                                        .addLast("httpAgg", new HttpObjectAggregator(512))
                                        .addLast("chunked", new ChunkedWriteHandler())
                                        .addLast("httpRequestHandler", httpRequestHandler);
                            }

                        }
                    })
                    .bind(config.getHost(), config.getPort());
            logger.info("Web Server listen at " + config.getHost() + ":" + config.getPort() + "...");
            cf.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.info("被中断退出...");
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }

    public static ApplicationProtocolNegotiationHandler getServerAPNHandler(WebServerConfig config) {
        return new ApplicationProtocolNegotiationHandler(ApplicationProtocolNames.HTTP_2) {
            @Override
            protected void configurePipeline(ChannelHandlerContext ctx, String protocol) {
                if (ApplicationProtocolNames.HTTP_2.equals(protocol)) {
                    ctx.pipeline().addLast(
                            Http2FrameCodecBuilder.forServer().build(),
                            new Http2RequestHandler(config));
                    return;
                }
                throw new IllegalStateException("Protocol: " + protocol + " not supported");
            }
        };
    }
}
