package edu.ustl.pengzexuan.joycastle;

import edu.ustl.pengzexuan.joycastle.handler.CalculatorServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) {
        var bossEventLoopGroup = new NioEventLoopGroup(1);
        var workerEventLoopGroup = new NioEventLoopGroup();
        var serverBootStrap = new ServerBootstrap();
        System.err.print("Whether to use Nagle(true / false) > ");
        var scanner = new Scanner(System.in);
        var nagle = scanner.nextBoolean();
        scanner.close();
        try {
            serverBootStrap.group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.TCP_NODELAY, nagle)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new CalculatorServerHandler());
                }
            });
            var channelFuture = serverBootStrap.bind(6666).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerEventLoopGroup.shutdownGracefully();
            bossEventLoopGroup.shutdownGracefully();
        }
    }
}