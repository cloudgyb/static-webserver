package com.github.cloudgyb.webserver.http.response;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

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
    private File file = null;
    private long fileStart = 0;
    private long fileLength;

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
    public void write(File file, long start, long length) {
        if (file == null)
            throw new IllegalArgumentException("file");
        if (start < 0 || start > file.length() - 1)
            throw new IllegalArgumentException("start");
        if (length <= 0 || length > file.length())
            throw new IllegalArgumentException("length");
        this.file = file;
        this.fileStart = start;
        this.fileLength = length;
    }

    public void end() {
        DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
                this.status, this.headers);
        this.context.write(response);
        if (body.readableBytes() > 0) {
            this.context.write(body);
        }
        if (this.file != null) {
            if (this.context.pipeline().get(SslHandler.class) != null) {
                try {
                    ChunkedNioFile chunkedNioFile = new ChunkedNioFile(
                            FileChannel.open(file.toPath(), StandardOpenOption.READ),
                            fileStart, fileLength, 8192);
                    this.context.write(chunkedNioFile);
                } catch (IOException ignore) {
                }
            } else {
                DefaultFileRegion fileRegion = new DefaultFileRegion(file, fileStart, fileLength);
                this.context.write(fileRegion);
            }
        }
        //DefaultLastHttpContent defaultLastHttpContent = new DefaultLastHttpContent();
        this.context.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
    }

}
