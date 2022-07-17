package com.example.disruptoromsgateway;


import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.client.bean.OrderCmdContainer;
import com.client.checksum.ICheckSum;
import com.client.codec.IBodyCodec;
import com.client.fetch.IFetchService;
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

    //排队机 抓取服务端口
    private int fetchServPort;

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

        //排队机 抓取服务端口
        fetchServPort = Integer.parseInt(root.elementText("fetchservport"));

        log.info("GateWay id:{}, port:{}, fetchServPort:{}", id, recvPort, fetchServPort);
        //TODO 柜台列表 数据库连接
    }

    public void startup() throws Exception {
        //启动gateway 服务端
        //1.启动TCP服务监听
        initRecv();

        //2.排队机交互
       initFetchServ();
    }

    private void initFetchServ() {
        ServerConfig rpcConfig = new ServerConfig()
                .setPort(fetchServPort)
                .setProtocol("bolt");
        ProviderConfig<IFetchService> providerConfig = new ProviderConfig<IFetchService>()
                .setInterfaceId(IFetchService.class.getName())  //对外提供服务的RPC接口
                .setRef(() -> OrderCmdContainer.getInstance().getAll())//对外提供服务的RPC实现
                .setServer(rpcConfig);

        providerConfig.export();

        log.info("gateway startup fetchServ success at port : {}",fetchServPort);
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
