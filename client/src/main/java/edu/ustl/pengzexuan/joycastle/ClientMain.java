package edu.ustl.pengzexuan.joycastle;

import edu.ustl.pengzexuan.joycastle.handler.CalculateClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientMain {
    public static void main(String[] args) {
        var eventLoopGroup = new NioEventLoopGroup();
        try {
            var bootStrap = new Bootstrap();
            bootStrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new CalculateClientHandler());
                        }
                    });
            var channelFuture = bootStrap.connect("127.0.0.1", 6666).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}