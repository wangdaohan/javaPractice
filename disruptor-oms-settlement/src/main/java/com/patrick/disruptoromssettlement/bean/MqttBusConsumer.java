package com.patrick.disruptoromssettlement.bean;

import com.client.bean.msg.CommonMsg;
import com.client.checksum.ICheckSum;
import com.client.codec.IMsgCodec;
import com.client.codec.MsgCodec;
import com.google.common.collect.Maps;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.client.bean.msg.MsgConstants.MATCH_HQ_DATA;
import static com.client.bean.msg.MsgConstants.MATCH_ORDER_DATA;


/**
 * MatchDataConsumer 和 MarketDataConsumer都用了@component，
 * MqttBusConsumer 不用@Component,
 *    因为对MqttBusConsumer的初始化，是与AppConfig有顺序依赖的关系，需要等AppConfig初始化完成后才能执行初始化
 *      方案1： 可以使用加载依赖关系的注解
 *      方案2： 简单方法：在AppConfig初始化的方法内调用MqttBusConsumer的初始化方法
 */

@RequiredArgsConstructor
@Log4j2
public class MqttBusConsumer {
    @NonNull
    private String busIp;

    @NonNull
    private int busPort;

    //从总线mqtt subcribe的topic： recvAddr(柜台ID）
    //从总线mqtt subcribe的topic： HQ_ADDR(行情的ID， -1）
    @NonNull
    private String recvAddr;
    private final static String HQ_ADDR = "-1";

    @NonNull
    private IMsgCodec msgCodec;

    @NonNull
    private ICheckSum cs;

    @NonNull
    private Vertx vertx;


    //收到行情或订单数据后，转发到vertx 总线上的相关地址
    public final static String INNER_MARKET_DATA_CACHE_ADDR = "l1_market_data_cache_addr"; // 处理5档行情数据
    public final static String INNER_MATCH_DATA_ADDR = "match_data_addr"; //处理match的订单数据


    public void startup() {
        mqttConnect(vertx, busIp, busPort);
    }

    private void mqttConnect(Vertx vertx, String busIp, int busPort) {
        MqttClient mqttClient = MqttClient.create(vertx);
        mqttClient.connect(busPort, busIp, res -> {
            if(res.succeeded()){
                log.info("connect mqtt bus succeed");
                Map<String, Integer> topic = Maps.newHashMap();
                topic.put(recvAddr, MqttQoS.AT_LEAST_ONCE.value());
                topic.put(HQ_ADDR, MqttQoS.AT_LEAST_ONCE.value());

                mqttClient.subscribe(topic).publishHandler(h -> {
                    CommonMsg msg = msgCodec.decodeFromBuffer(h.payload());
                    if(msg.getChecksum() != (cs.getChecksum(msg.getBody()))){
                        return;
                    }
                    byte[] body = msg.getBody();
                    if(ArrayUtils.isNotEmpty(body)) {
                        short msgType = msg.getMsgType();
                        if(msgType == MATCH_HQ_DATA){
                            vertx.eventBus().send(INNER_MARKET_DATA_CACHE_ADDR, Buffer.buffer(body));
                        } else if (msgType == MATCH_ORDER_DATA) {
                            vertx.eventBus().send(INNER_MATCH_DATA_ADDR, Buffer.buffer(body));
                        } else {
                            log.error("recv unknown msgtype:{}", msg);
                        }
                    }
                });

            } else {
                log.error("connection fail,retrying in closeHandler");
            }
        });

        mqttClient.closeHandler(h -> {
            try{
                TimeUnit.SECONDS.sleep(5);

            } catch (Exception e) {
                log.error(e);
            }
            mqttConnect(vertx,busIp,busPort);
        });
    }
}
