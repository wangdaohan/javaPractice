package com;

import com.example.disruptoromsgateway.GatewayConfig;
import com.client.checksum.CheckSum;
import com.client.codec.BodyCodec;
import lombok.extern.log4j.Log4j2;


/**
 * 前置网关 - 需要以下功能
 * 1。 服务端 - 对消息的编码/解码
 * 2。 服务端 - 连接处理器
 * 3。 客户端 - 生成报文
 * 4。 客户端 - 发送报文
 */
@Log4j2
public class GatewayStartup {
    public static void main(String[] args) throws Exception{
        String configFileName = "gateway.xml";
        GatewayConfig config = new GatewayConfig();
        config.initConfig(GatewayStartup.class.getResource("/").getPath() + configFileName);
        config.setCheckSum(new CheckSum());
        config.setBodyCodec(new BodyCodec());
        config.startup();
    }
}
