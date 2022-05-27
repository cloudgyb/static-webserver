package com.github.cloudgyb.webserver.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;

public class HttpMsgEncodeTest {

    static class HttpMsgEncode extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
            if (msg instanceof ByteBuf) {
                ByteBuf m = (ByteBuf) msg;
                int n = m.readableBytes();
                byte[] bytes = new byte[n];
                m.readBytes(bytes);
                System.out.print(new String(bytes));
            } else {
                ctx.write(msg, promise);
            }
            ReferenceCountUtil.release(msg);
        }
    }

    @Test
    public void test() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel();
        embeddedChannel.pipeline()
                .addLast(new HttpMsgEncode())
                .addLast(new HttpServerCodec())
                .addLast(new HttpServerNameEncode())
                .addLast(new HttpObjectAggregator(512));
        DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().add("Server", "gyb");
        ByteBuf body = Unpooled.copiedBuffer("哈哈哈哈", Charset.defaultCharset());
        DefaultHttpContent httpBody = new DefaultHttpContent(body.copy());
        embeddedChannel.writeOutbound(response);
        embeddedChannel.writeOutbound(body);
        embeddedChannel.writeOutbound(httpBody);
        File file = new File("WEBROOT", "index.html");
        embeddedChannel.writeOutbound(new DefaultFileRegion(file, 0, file.length()).retain());
        embeddedChannel.close();
    }
}
