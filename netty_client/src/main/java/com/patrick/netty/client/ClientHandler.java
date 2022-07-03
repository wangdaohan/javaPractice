package com.patrick.netty.client;

import com.patrick.netty.common.disruptor.MessageProducer;
import com.patrick.netty.common.disruptor.RingBufferWorkerPoolFactory;
import com.patrick.netty.common.entity.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.lang.ref.Reference;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        //为什么一开始要try finally? 由于netty特性, 因为msg是要缓存里的，所以msg用完，必须要释放缓存

//        try {
//            TranslatorData response = (TranslatorData) msg;
//            System.out.println("Client received response: "+response.getId()+":"+response.getName()+":"+response.getMessage());
//        } finally {
//            //一定要注意，用完了一定要释放缓存
//            ReferenceCountUtil.release(msg);
//        }

        /**
         * 使用disruptor优化handler数据处理逻辑，提升性能
         *     **** disruptor提升的不是2个服务之间的生产消费， 而是一个服务内部自身的生产消费
         */
        String producerId = "client-session:001";
        MessageProducer producer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
        producer.onData((TranslatorData) msg, ctx);
    }
}
