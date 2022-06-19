package com.github.cloudgyb.webserver.http.response;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.codec.http2.*;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

/**
 * http2响应实现
 *
 * @author geng
 * @since 2022/6/17 15:45
 */
public class Http2ResponseWrapper implements HttpResponse {
    private final ServerCookieEncoder cookieEncoder;
    private final ChannelHandlerContext ctx;
    private final Http2FrameStream stream;
    private final Http2Headers headers;
    private final ByteBuf body;
    private File file = null;
    private long fileStart = 0;
    private long fileLength;

    public Http2ResponseWrapper(ChannelHandlerContext ctx, Http2FrameStream stream) {
        this.cookieEncoder = ServerCookieEncoder.STRICT;
        this.ctx = ctx;
        this.stream = stream;
        this.headers = new DefaultHttp2Headers().status(HttpResponseStatus.OK.codeAsText());
        this.body = Unpooled.buffer();
    }

    @Override
    public void setStatus(HttpResponseStatus status) {
        this.headers.status(status.codeAsText());
    }

    @Override
    public void write(byte[] content) {
        this.body.writeBytes(content);
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

    @Override
    public void addHeader(String header, Object value) {
        this.headers.add(header, value.toString());
    }

    @Override
    public void setCookie(Cookie cookie) {
        String encode = cookieEncoder.encode(cookie);
        addHeader(HttpHeaderNames.SET_COOKIE.toString(), encode);
    }

    @Override
    public void end() {
        final DefaultHttp2HeadersFrame headersFrame = new DefaultHttp2HeadersFrame(this.headers)
                .stream(this.stream);
        this.ctx.write(headersFrame);
        if (body.readableBytes() > 0) {
            this.ctx.write(new DefaultHttp2DataFrame(body));
        }

        if (this.file != null) {
            try {
                ChunkedNioFile chunkedNioFile = new ChunkedNioFile(
                        FileChannel.open(file.toPath(), StandardOpenOption.READ),
                        fileStart, fileLength, 8192);
                final Http2DataChunkedInput http2DataChunkedInput = new Http2DataChunkedInput(chunkedNioFile, this.stream);
                this.ctx.write(http2DataChunkedInput);
            } catch (IOException ignore) {
            }
        }
        this.ctx.writeAndFlush(new DefaultHttp2DataFrame(true).stream(this.stream));
    }
}
