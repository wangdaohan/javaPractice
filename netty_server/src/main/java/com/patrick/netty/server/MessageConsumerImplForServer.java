package com.patrick.netty.server;

import com.patrick.netty.common.disruptor.MessageConsumer;
import com.patrick.netty.common.entity.TranslatorData;
import com.patrick.netty.common.entity.TranslatorDataWrapper;

public class MessageConsumerImplForServer extends MessageConsumer {

    public MessageConsumerImplForServer(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWrapper translatorDataWrapper) throws Exception {
        TranslatorData request = translatorDataWrapper.getData();
        System.out.println("server receive:" + request.getId() + ":" + request.getName() + ":" + request.getMessage());
        TranslatorData response = TranslatorData.builder().id("resp:" + request.getId()).name("resp:" + request.getName()).message("resp:" + request.getMessage()).build();
        translatorDataWrapper.getCtx().writeAndFlush(response);
    }
}
