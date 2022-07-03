package com.patrick.netty.common.disruptor;

import com.lmax.disruptor.WorkHandler;
import com.patrick.netty.common.entity.TranslatorDataWrapper;

public abstract class MessageConsumer implements WorkHandler<TranslatorDataWrapper> {
    private String consumerId;

    public MessageConsumer(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }
}
