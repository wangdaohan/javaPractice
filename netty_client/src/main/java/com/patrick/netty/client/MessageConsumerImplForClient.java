package com.patrick.netty.client;

import com.patrick.netty.common.disruptor.MessageConsumer;
import com.patrick.netty.common.entity.TranslatorData;
import com.patrick.netty.common.entity.TranslatorDataWrapper;
import io.netty.util.ReferenceCountUtil;

public class MessageConsumerImplForClient  extends MessageConsumer {
    public MessageConsumerImplForClient(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWrapper translatorDataWrapper) throws Exception {
        try {
            TranslatorData response = translatorDataWrapper.getData();
            System.out.println("Client received response: "+response.getId()+":"+response.getName()+":"+response.getMessage());
        } finally {
            //一定要注意，用完了一定要释放缓存
            ReferenceCountUtil.release(translatorDataWrapper.getData());
        }
    }
}
