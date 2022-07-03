package com.patrick.netty.client;

import com.patrick.netty.common.entity.TranslatorData;
import com.patrick.netty.common.marshalling.MarshallingcodeCFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyClient {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8765;
    private Channel channel;
    private  EventLoopGroup workGroup;
    private ChannelFuture cf;

    public NettyClient() {
        this.connect(HOST, PORT);
    }

    public void connect(String host, int port) {
        //创建netty client流程
        /**
         * 1。创建1个工作线程组, 用于处理实际业务的线程组
         */
        workGroup = new NioEventLoopGroup();
        try{
            /**
             * 2。 辅助类，（注意client和server不一样）
             *      Bootstrap:
             *          Server: ServerBootstrap
             *          Client: Bootstrap
             *      Channel:
             *          Server: NioSctpServerChannel
             *          Client: NioSocketChannel
             *      Handler:
             *          Server: childHandler
             *          Client: handler
             */
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    //表示 缓存区的自适应动态调配
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    //缓存区，池化操作
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    //日志
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //数据接收回调 （异步） - 不要将业务逻辑写到这里，不然执行时间过长，会影响消费workGroup的速度
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(MarshallingcodeCFactory.buildMarshallingDecoder());
                            sc.pipeline().addLast(MarshallingcodeCFactory.buildMarshallingEncoder());
                            sc.pipeline().addLast(new ClientHandler());
                        }
                    });

            //启动连接
            this.cf = bootstrap.connect(host, port).sync();
            System.out.println("client connected");

            //接下来就进行数据 的发送
            //首先获取channel 通道，然后在另外sendData方法里发送数据
            this.channel = this.cf.channel();
            //cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        finally {
//            //优雅停机
//            workGroup.shutdownGracefully();
//            System.out.println("client shutdown.");
//        }
    }

    public void sendData() {
        for (int i = 0; i < 10; i++) {
            this.channel.writeAndFlush(TranslatorData.builder().id(""+i).name("Name-"+i).message("Message-"+i).build());
        }
    }

    public void close() throws Exception{
        //优雅停机
        cf.channel().closeFuture().sync();
        workGroup.shutdownGracefully();
        System.out.println("client shutdown.");
    }
}
