package com.github.cloudgyb.webserver.http;

import com.github.cloudgyb.webserver.config.WebServerConfig;
import com.github.cloudgyb.webserver.http.request.HttpRequest;
import com.github.cloudgyb.webserver.http.request.HttpRequestWrapper;
import com.github.cloudgyb.webserver.http.response.HttpResponse;
import com.github.cloudgyb.webserver.http.response.HttpResponseWrapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求处理器,委派给{@link HttpStaticResourceHandler}处理请求
 *
 * @author cloudgyb
 * @see HttpStaticResourceHandler#handle(HttpRequest, HttpResponse)
 */
@ChannelHandler.Sharable
public class HttpRequestHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final HttpStaticResourceHandler httpStaticResourceHandler;

    public HttpRequestHandler(WebServerConfig config) {
        this.httpStaticResourceHandler = new HttpStaticResourceHandler(config.getWebRoot());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            httpStaticResourceHandler.handle(
                    new HttpRequestWrapper(request),
                    new HttpResponseWrapper(ctx)
            );
            ((FullHttpRequest) msg).release();
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
