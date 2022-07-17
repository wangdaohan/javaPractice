package com.patrick.disruptoromssettlement.config;

import com.client.checksum.ICheckSum;
import com.client.codec.IBodyCodec;
import com.client.codec.IMsgCodec;
import io.vertx.core.Vertx;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Data
@Log4j2
public class AppConfig {

    /////////////////////UUID 相关配置////////////////////////////////
    @Value("${counter.dataCenterId}")
    private long dataCenterId;

    @Value("${counter.workerId}")
    private long workerId;
    /////////////////////////////////////////////////////

    /////////////////////交易所会员号////////////////////////////////
    @Value("${counter.memberid}")
    private short id;

    /////////////网关配置//////////
    @Value("${gateway.senderIp}")
    private String sendIp;

    @Value("${gateway.senderPort}")
    private int port;

    @Value("${gateway.gatewayId}")
    private short gatewayId;

    /////////////////

    private Vertx vertx = Vertx.vertx();

    /////////////编码相关配置//////////

    //private IBodyCodec bodyCodec = new BodyCodec();
    //private ICheckSum checkSum = new CheckSum();
    @Value("${counter.checksum}")
    private String checkSumClass;

    @Value("${counter.bodycodec}")
    private String bodyCodecClass;

    @Value("${counter.msgcodec}")
    private String msgCodecClass;


    private IMsgCodec msgCodec;
    private IBodyCodec bodyCodec;
    private ICheckSum checkSum;
    @PostConstruct
    private void init(){
        Class<?> clz;
        try {
            clz = Class.forName(checkSumClass);
            checkSum = (ICheckSum) clz.getDeclaredConstructor().newInstance();

            clz = Class.forName(bodyCodecClass);
            bodyCodec = (IBodyCodec) clz.getDeclaredConstructor().newInstance();

            clz = Class.forName(msgCodecClass);
            msgCodec = (IMsgCodec) clz.getDeclaredConstructor().newInstance();

        } catch (Exception e) {
            log.error("init config error", e);
        }

    }

}
