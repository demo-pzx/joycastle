package edu.ustl.pengzexuan.joycastle.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
public class CalculatorServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        var channel = ctx.channel();
        log.debug("The message from server {} is : {}", channel.remoteAddress().toString().substring(1), msg);
        var byteBuffer = (ByteBuf) msg;
        var message = byteBuffer.toString(CharsetUtil.UTF_8);
        if (message.equalsIgnoreCase("ping")) {
            ctx.writeAndFlush(Unpooled.copiedBuffer("pong", StandardCharsets.UTF_8));
            return;
        } else {
            var dataList = message.split(" ");
            int ans;
            int leftNumber;
            int rightNumber;
            // 两个数字的加减，长度一定等于三，index(0)是第一个数字，index(1)是符号位，index(2)是第二个数字
            if (dataList.length == 3) {
                switch (dataList[1]) {
                    case "+":
                        leftNumber = Integer.parseInt(dataList[0]);
                        rightNumber = Integer.parseInt(dataList[2]);
                        ans = leftNumber + rightNumber;
                        log.debug("The {} + {} is {}", leftNumber, rightNumber, ans);
                        ctx.writeAndFlush(Unpooled.copiedBuffer("The " + leftNumber + " + " + rightNumber + " is " + ans, CharsetUtil.UTF_8));
                        break;
                    case "-":
                        leftNumber = Integer.parseInt(dataList[0]);
                        rightNumber = Integer.parseInt(dataList[2]);
                        ans = leftNumber - rightNumber;
                        log.debug("The {} - {} is {}", leftNumber, rightNumber, ans);
                        ctx.writeAndFlush(Unpooled.copiedBuffer("The " + leftNumber + " - " + rightNumber + " is " + ans, CharsetUtil.UTF_8));
                        break;
                    default:
                        log.error("Illegal operator: {}", dataList[1]);
                        ctx.writeAndFlush(Unpooled.copiedBuffer("Illegal operator: " + dataList[1], CharsetUtil.UTF_8));
                }
            } else {
                log.error("The algorithm passed in is illegal: {}", msg);
                ctx.writeAndFlush(Unpooled.copiedBuffer("The algorithm passed in is illegal" + Arrays.toString(dataList), CharsetUtil.UTF_8));
            }
        }
        super.channelRead(ctx, msg);
    }
}
