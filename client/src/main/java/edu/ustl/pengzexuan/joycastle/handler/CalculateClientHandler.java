package edu.ustl.pengzexuan.joycastle.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class CalculateClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("ping", CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        var byteBuffer = (ByteBuf) msg;
        var message = byteBuffer.toString(CharsetUtil.UTF_8);
        if (message.equalsIgnoreCase("pong")) {
            log.info("pong");
            log.info("connect...");
        } else {
            log.error(message);
        }
        var scanner = new Scanner(System.in);
        System.out.print("send > ");
        var string = scanner.nextLine();
        ctx.writeAndFlush(Unpooled.copiedBuffer(string, CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
