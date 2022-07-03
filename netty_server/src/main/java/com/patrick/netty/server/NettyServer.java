package com.patrick.netty.server;

import com.patrick.netty.common.marshalling.MarshallingcodeCFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Netty的基本功能就是用于 发送数据 和接收数据
 */
public class NettyServer {
    public NettyServer() {
        //创建netty server流程
        /**
         * 1。创建2个工作线程组：
         *      一个用于接受网络请求的线程组 -> 对应的是Linux底层TCP连接(长连接）中的SYNC数组（SYNC数组存储未握手（3次握手）成功的连接（包括正第1次或第2次的连接）
         *      另一个用于实际处理业务的线程组 -> 对应的是linux底层TCP连接中的Accept数组 （Accept数组存储已连接成功的连接）
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            /**
             * 1。 ChannelOption.SO_BACKLOG ->
             *          linux中SYNC数组+Accept数组统称为BACKLOG数组，
             *          底层backlog数组的大小，则决定高并发情况下，接收TCP请求的速度和时间
             *
             * 2. 发送数据 和 接收数据所使用的buffer大小  -》自适用buffer大小 ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT
             *     buffer大小 ->需要根据实现情况下每一条数据的大小来设置，
             *                  buffer太小 -> 一条数据需要分多次传输，浪费性能
             *                  buffer太大 -> 浪费资源
             *
             * 3. 缓冲池. 池化操作 ChannelOption.ALLOCATOR
             */
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //表示 缓存区的自适应动态调配
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    //缓存区，池化操作
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    //日志
                                .handler(new LoggingHandler(LogLevel.INFO))
                        //数据接收回调 （异步） - 不要将业务逻辑写到这里，不然执行时间过长，会影响消费workGroup的速度
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                                sc.pipeline().addLast(MarshallingcodeCFactory.buildMarshallingDecoder());
                                sc.pipeline().addLast(MarshallingcodeCFactory.buildMarshallingEncoder());
                                sc.pipeline().addLast(new ServerHandler());
                        }
                    });

            //绑定端口，以及同步等待 接收请求
            ChannelFuture channelFuture = serverBootstrap.bind(8765).sync();
            System.out.println("server started");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅停机
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            System.out.println("server shutdown.");
        }
    }
}
