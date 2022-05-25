package com.github.cloudgyb.webserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

public class ChannelOutBoundPipelineTest {

    public static class ChannelInBoundHandlerA implements ChannelInboundHandler {

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) {

        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) {

        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {

        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {

        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            System.out.println(getClass().getSimpleName() + ":" + msg);
            ctx.fireChannelRead(msg + getClass().getSimpleName());
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {

        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {

        }

        @Override
        public void channelWritabilityChanged(ChannelHandlerContext ctx) {

        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {

        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) {

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        }
    }

    public static class ChannelInBoundHandlerB implements ChannelInboundHandler {

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) {

        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) {

        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {

        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {

        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            System.out.println(getClass().getSimpleName() + ":" + msg);
            ctx.fireChannelRead(msg + getClass().getSimpleName());
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {

        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {

        }

        @Override
        public void channelWritabilityChanged(ChannelHandlerContext ctx) {

        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {

        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) {

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        }
    }

    public static class ChannelInBoundHandlerC implements ChannelInboundHandler {

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) {

        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) {

        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {

        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {

        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            System.out.println(getClass().getSimpleName() + ":" + msg);
            ctx.fireChannelRead(msg + getClass().getSimpleName());
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {

        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {

        }

        @Override
        public void channelWritabilityChanged(ChannelHandlerContext ctx) {

        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {

        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) {

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        }
    }

    public static class ChannelOutBoundHandlerA extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println(getClass().getSimpleName() + ":" + msg);
            super.write(ctx, msg, promise);
        }
    }

    public static class ChannelOutBoundHandlerB extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
            System.out.println(getClass().getSimpleName() + ":" + msg);
            //super.write(ctx, msg, promise);
        }
    }

    public static class ChannelOutBoundHandlerC extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println(getClass().getSimpleName() + ":" + msg);
            super.write(ctx, msg, promise);
        }
    }

    @Test
    public void testPipeline() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel();
        embeddedChannel.pipeline()
                .addLast(new ChannelInBoundHandlerA())
                .addLast(new ChannelInBoundHandlerB())
                .addLast(new ChannelInBoundHandlerC())
                .addLast(new ChannelOutBoundHandlerA())
                .addLast(new ChannelOutBoundHandlerB())
                .addLast(new ChannelOutBoundHandlerC());
        embeddedChannel.writeInbound("test");
        embeddedChannel.writeOutbound("testOutbound");
        embeddedChannel.close();
    }
}
