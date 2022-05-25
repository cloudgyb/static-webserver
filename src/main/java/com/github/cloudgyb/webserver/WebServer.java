package com.github.cloudgyb.webserver;

import com.github.cloudgyb.webserver.config.WebServerConfig;
import com.github.cloudgyb.webserver.http.HttpRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        try {
            ChannelFuture cf = serverBootstrap.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, config.getTcpBacklog())
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) {
                            channel.pipeline()
                                    .addLast("httpRequestDecoder", new HttpServerCodec())
                                    .addLast("", new HttpObjectAggregator(512))
                                    .addLast("httpRequestHandler", httpRequestHandler);
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
}
