package com.github.cloudgyb.webserver.http.response;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http2.Http2FrameStream;

import java.io.File;

/**
 * @author geng
 * @since 2022/6/17 15:45
 */
public class Http2ResponseWrapper implements HttpResponse {
    public Http2ResponseWrapper(ChannelHandlerContext ctx, Http2FrameStream stream) {

    }

    @Override
    public void setStatus(HttpResponseStatus status) {

    }

    @Override
    public void write(byte[] content) {

    }

    @Override
    public void write(File file, long start, long length) {

    }

    @Override
    public void addHeader(String header, Object value) {

    }

    @Override
    public void setCookie(Cookie cookie) {

    }

    @Override
    public void end() {

    }
}
