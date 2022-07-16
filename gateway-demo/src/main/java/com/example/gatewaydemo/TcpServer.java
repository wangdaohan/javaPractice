package com.example.gatewaydemo;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TcpServer {
    public static void main(String[] args) {
        new TcpServer().startServer();
    }

    public void startServer() {
        Vertx vertx = Vertx.vertx();
        NetServer netServer = vertx.createNetServer();
        netServer.connectHandler(new ConnHandler());  // new ConnHandler()是一个异步handler，会异步循环执行handle方法
        netServer.listen(8091, res -> {
            if (res.succeeded()) {
                log.info("gateway startup success at port 8091");
            } else {
                log.error("gatewy statrtup fail, ", res.cause());
            }
        });
    }

    public class ConnHandler implements Handler<NetSocket> {
        //报文：报头（INT, 报文长度）+包体（BYTE[]数据）
        //报头（INT, 报文长度） = 4个字节，int = 4个字节
        private static final int PACKET_HEADER_LENGTH = 4;
        @Override
        public void handle(NetSocket netSocket) {
            //自定义解析器 -> 解析HTTP报文，服务端解析客户端的消息
            //报文：报头（报文长度）+包体（数据）
            final RecordParser parser = RecordParser.newFixed(PACKET_HEADER_LENGTH);
            parser.setOutput(new Handler<Buffer>() {
                int bodyLength = -1;
                @Override
                public void handle(Buffer buffer) { //开始解析原始报文数据
                    if (bodyLength == -1) { //读取报头
                        bodyLength = buffer.getInt(0); //获取报头数据即报文长度，因为一个Int就是4个字节，因为getInt(0)拿到的就是报头信息
                        parser.fixedSizeMode(bodyLength); //TCP编程原理之一： 会有一个异步线程不断读取报文数据，当达到bodylength时，会再次调用handle方法
                    } else {
                        //读取数据
                        byte[] bodyBytes = buffer.getBytes();
                        log.info("get msg from client:{}", new String(bodyBytes));
                        //一条数据读取完毕后，恢复现场
                        //恢复现场
                        parser.fixedSizeMode(PACKET_HEADER_LENGTH);
                        bodyLength = -1;
                    }
                }
            });
            netSocket.handler(parser);
        }
    }
}
