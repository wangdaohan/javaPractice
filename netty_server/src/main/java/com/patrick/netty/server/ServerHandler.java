package com.patrick.netty.server;

import com.patrick.netty.common.disruptor.MessageProducer;
import com.patrick.netty.common.disruptor.RingBufferWorkerPoolFactory;
import com.patrick.netty.common.entity.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ServerHandler
     * 如果ServerHandler里面写的业务逻辑很复杂，耗时很长的话，会非常影响workgroup消费速度，即影响netty性能
 *   * 解决方法：1。尽量不要在接收数据的时候，去编写业务逻辑
 *   *         2。利用异步机制，比如a. 使用线程池异步处理，即使用阻塞队列
 *   *                          b. 使用disruptor提高性能
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        /**
         * 由于netty特性, 因为msg是要缓存里的，所以msg用完，必须要释放缓存
         * ReferenceCountUtil.release(msg);
         *
         * 因为读完request 还有写操作（发送response) 发送response的writeAndFlush已经帮忙release了缓存
         *
         * 如果只有读request操作，则需要去手动release一下
         */
//        TranslatorData request = (TranslatorData) msg;
//        System.out.println("server receive:" + request.getId() + ":"+request.getName()+":"+request.getMessage());
        /**
         * 收到request data后，在实际应用中，这里应该还有业务逻辑处理代码，这些都是耗时操作
         *      如：１．数据持久化（有IO读写）
         * 解决：1. 利用异步机制，使用线程池异步处理，即使用阻塞队列
         */
//        TranslatorData response = TranslatorData.builder().id("resp:"+request.getId()).name("resp:"+request.getName()).message("resp:"+request.getMessage()).build();
//        ctx.writeAndFlush(response);

        /**
         * 使用disruptor优化handler数据处理逻辑，提升性能
         *     **** disruptor提升的不是2个服务之间的生产消费， 而是一个服务内部自身的生产消费
         */
        TranslatorData request = (TranslatorData) msg;
        //producer id自动生成
        String producerId = "server-sessionId:001";
        MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
        messageProducer.onData(request, ctx);
    }
}
