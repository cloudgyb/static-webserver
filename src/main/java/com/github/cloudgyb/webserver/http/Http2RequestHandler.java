package com.github.cloudgyb.webserver.http;

import com.github.cloudgyb.webserver.config.WebServerConfig;
import com.github.cloudgyb.webserver.http.request.Http2RequestWrapper;
import com.github.cloudgyb.webserver.http.response.Http2ResponseWrapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2ChannelDuplexHandler;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2HeadersFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * htt2请求处理器
 *
 * @author geng
 * @since 2022/6/17 11:16
 */
@ChannelHandler.Sharable
public class Http2RequestHandler extends Http2ChannelDuplexHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final HttpStaticResourceHandler httpStaticResourceHandler;

    public Http2RequestHandler(WebServerConfig config) {
        this.httpStaticResourceHandler = new HttpStaticResourceHandler(config.getWebRoot());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Http2HeadersFrame) {
            Http2HeadersFrame http2HeadersFrame = (Http2HeadersFrame) msg;
            Http2Headers headers = http2HeadersFrame.headers();
            headers.path();
            if (http2HeadersFrame.isEndStream()) {
                httpStaticResourceHandler.handle(
                        new Http2RequestWrapper(http2HeadersFrame),
                        new Http2ResponseWrapper(ctx, http2HeadersFrame.stream())
                );
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("", cause);
        ctx.close();
    }
}
