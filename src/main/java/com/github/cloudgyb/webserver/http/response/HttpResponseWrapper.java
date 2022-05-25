package com.github.cloudgyb.webserver.http.response;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.FileRegion;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;

/**
 * HttpResponse 包装器
 *
 * @author cloudgyb
 */
public class HttpResponseWrapper implements HttpResponse {
    private final ChannelHandlerContext context;
    private final ServerCookieEncoder cookieEncoder;
    private final HttpHeaders headers;
    private HttpResponseStatus status = HttpResponseStatus.OK;
    private ByteBuf body;

    public HttpResponseWrapper(ChannelHandlerContext context) {
        this.context = context;
        this.headers = new DefaultHttpHeaders();
        this.cookieEncoder = ServerCookieEncoder.STRICT;
        this.body = Unpooled.buffer(0);
    }

    @Override
    public void setStatus(HttpResponseStatus status) {
        this.status = status;
    }

    @Override
    public void addHeader(String header, Object value) {
        this.headers.add(header, value);
    }

    @Override
    public void setCookie(Cookie cookie) {
        String encode = cookieEncoder.encode(cookie);
        addHeader(HttpHeaderNames.SET_COOKIE.toString(), encode);
    }

    @Override
    public void write(byte[] content) {
        this.body = Unpooled.copiedBuffer(body, Unpooled.copiedBuffer(content));
    }

    @Override
    public void write(FileRegion file) {
        this.context.write(file);
    }

    public void end() {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                this.status, this.body,
                this.headers, new DefaultHttpHeaders());
        this.context.writeAndFlush(response);
    }

}
