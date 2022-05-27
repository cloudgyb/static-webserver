package com.github.cloudgyb.webserver.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpResponse;

public class HttpServerNameEncode extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse resp = (HttpResponse) msg;
            resp.headers().add("Server", "gyb/1.0");
        }
        super.write(ctx, msg, promise);
    }
}
