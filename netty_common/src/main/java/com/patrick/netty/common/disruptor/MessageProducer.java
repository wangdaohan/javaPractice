package com.patrick.netty.common.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.patrick.netty.common.entity.TranslatorData;
import com.patrick.netty.common.entity.TranslatorDataWrapper;
import io.netty.channel.ChannelHandlerContext;

public class MessageProducer implements WorkHandler<TranslatorDataWrapper> {
    private String producerId;
    private RingBuffer<TranslatorDataWrapper> ringBuffer;
    public MessageProducer(String producerId, RingBuffer ringBuffer) {
        this.producerId = producerId;
        this.ringBuffer = ringBuffer;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public void onData(TranslatorData data, ChannelHandlerContext ctx) {
        long nextSequence = ringBuffer.next();
        try{
            TranslatorDataWrapper event = ringBuffer.get(nextSequence);
            event.setData(data);
            event.setCtx(ctx);
        } finally {
            ringBuffer.publish(nextSequence);
        }
    }

    @Override
    public void onEvent(TranslatorDataWrapper translatorDataWrapper) throws Exception {

    }
}
