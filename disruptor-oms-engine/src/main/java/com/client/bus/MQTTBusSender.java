package com.client.bus;

import com.client.bean.msg.CommonMsg;
import com.client.codec.IMsgCodec;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.TimeUnit;

@Log4j2
@RequiredArgsConstructor
public class MQTTBusSender implements IBusSender{
    @NonNull
    private String ip;

    @NonNull
    private int port;

    @NonNull
    private IMsgCodec msgCodec;

    @NonNull
    private Vertx vertx;

    private volatile MqttClient sender;

    @Override
    public void startup() {
        //连接总线
        mqttConnect();
    }

    private void mqttConnect() {
        MqttClient mqttClient = MqttClient.create(vertx);
        mqttClient.connect(port, ip, res -> {
           if(res.succeeded()) {
               log.info("connect to mqtt bus[ip:{}, port:{}] succeed", ip, port);
               sender = mqttClient;
           } else {
               log.info("connect to mqtt bus[ip:{}, port:{}] fail, re-try", ip, port);
               mqttConnect();
           }
        });

        //连接异常断开，也需要重连
        mqttClient.closeHandler(h -> {
            try{
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e){
                log.error(e);
            }
            mqttConnect();
        });
    }

    @Override
    public void publish(CommonMsg commonMsg) {
        sender.publish(Short.toString(commonMsg.getMsgDst()), //short value:发往目的ID
                msgCodec.encodeToBuffer(commonMsg),
                MqttQoS.AT_LEAST_ONCE, //至少到达一次
                false,
                false);
    }
}
