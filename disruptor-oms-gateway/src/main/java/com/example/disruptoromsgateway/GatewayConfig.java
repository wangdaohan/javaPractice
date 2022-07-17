package com.example.disruptoromsgateway;

import com.client.checksum.ICheckSum;
import com.client.codec.IBodyCodec;
import com.example.disruptoromsgateway.handler.ConnHandler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

@Log4j2
@Data
public class GatewayConfig {
    //网关ID
    private short id;

    //端口
    private int recvPort;

    //TODO 柜台列表 数据库连接

    private IBodyCodec bodyCodec;
    private ICheckSum checkSum;

    private Vertx vertx = Vertx.vertx();

    public void initConfig(String fileName) throws Exception {
        //创建dom4j的解析器
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(fileName));
        Element root = document.getRootElement();
        //1. 端口
        recvPort = Integer.parseInt(root.elementText("recvport"));

        //网关ID
        id = Short.parseShort(root.elementText("id"));

        log.info("GateWay id:{}, port:{}", id, recvPort);
        //TODO 柜台列表 数据库连接
    }

    public void startup() throws Exception {
        //启动gateway 服务端
        //1.启动TCP服务监听
        initRecv();

        //TODO 2.排队机交互
    }

    private void initRecv() {
        NetServer server = vertx.createNetServer();
        server.connectHandler(new ConnHandler(this));
        server.listen(recvPort, res -> {
            if (res.succeeded()) {
                log.info("gateway startup success at port : {}", recvPort);
            } else {
                log.error("gateway startup fail");
            }
        });
    }
}
