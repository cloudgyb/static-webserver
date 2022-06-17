package com.github.cloudgyb.webserver.http;

import com.github.cloudgyb.webserver.config.WebServerConfig;
import com.github.cloudgyb.webserver.http.request.Http2RequestWrapper;
import com.github.cloudgyb.webserver.http.response.Http2ResponseWrapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

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
                String respBody = "Hello H2!";
                DefaultHttp2Headers resHeaders = new DefaultHttp2Headers();
                resHeaders.status(HttpResponseStatus.OK.codeAsText());
                resHeaders.add(HttpHeaderNames.CONTENT_TYPE, "text/html");
                resHeaders.add(HttpHeaderNames.CONTENT_LENGTH, respBody.length() + "");
                ctx.write(new DefaultHttp2HeadersFrame(resHeaders).stream(http2HeadersFrame.stream()));
                ctx.writeAndFlush(
                        new DefaultHttp2DataFrame(
                                Unpooled.copiedBuffer(respBody.getBytes(StandardCharsets.UTF_8))
                        ).stream(http2HeadersFrame.stream())
                );
                ctx.writeAndFlush(
                        new DefaultHttp2DataFrame(
                                true
                        ).stream(http2HeadersFrame.stream())
                );
                httpStaticResourceHandler.handle(
                        new Http2RequestWrapper(http2HeadersFrame),
                        new Http2ResponseWrapper(ctx, http2HeadersFrame.stream())
                );
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
