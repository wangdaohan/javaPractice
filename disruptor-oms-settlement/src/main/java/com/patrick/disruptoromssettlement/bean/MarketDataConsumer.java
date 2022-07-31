package com.patrick.disruptoromssettlement.bean;


import com.client.hq.L1MarketData;
import com.patrick.disruptoromssettlement.config.AppConfig;
import io.netty.handler.codec.CodecException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


import static com.patrick.disruptoromssettlement.bean.MqttBusConsumer.INNER_MARKET_DATA_CACHE_ADDR;

@Log4j2
@Component
public class MarketDataConsumer {

    @Autowired
    private AppConfig config;

    // key: 股票代码code , value:最新的五档行情数据
    private IntObjectHashMap<L1MarketData> l1Cache = new IntObjectHashMap<>();

    @PostConstruct
    public void init(){
        EventBus eventBus = config.getVertx().eventBus(); //从vertx总线中订阅刚才在MqttBusConsumer produce转发的行情数据
        //处理核心发过来的行情
        eventBus.consumer(INNER_MARKET_DATA_CACHE_ADDR).handler(buffer -> {
            Buffer body = (Buffer) buffer.body();
            if(body.length() == 0) {
                return;
            }
            L1MarketData[] marketData = null;
            try{
                marketData = config.getBodyCodec().deserialize(body.getBytes(), L1MarketData[].class);
            } catch (Exception e) {
                log.error(e);
            }
            if(ArrayUtils.isEmpty(marketData)) {
                return;
            }

            for(L1MarketData md: marketData) {
                L1MarketData data = l1Cache.get(md.code);
                if (data == null || data.timestamp < md.timestamp){
                    l1Cache.put(md.code, md);

                } else {
                    log.warn("l1MarketData is null or l1MarketData.timestamp < md.timestamp ");
                }
            }
        });
    }
}
