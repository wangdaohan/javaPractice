package com.example.gatewaydemo;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.extern.log4j.Log4j2;


/**
 * TCP编程的2大核心是：
 * 1。 客户端的重连
 * 2。 服务端的编码
 */

//客户端要用断线重连功能
//按照规定的格式发送TCP数据


@Log4j2
public class TcpClient {

    public static void main(String[] args) {
        new TcpClient().startConn();
    }

    public Vertx vertx;

    public void startConn() {
        vertx = Vertx.vertx();
        vertx.createNetClient().connect(8091, "127.0.0.1", new ClientConnHandler());  // new ClientConnHandler()是一个异步handler，会异步循环执行handle方法

    }

    public class ClientConnHandler implements Handler<AsyncResult<NetSocket>> {

        @Override
        public void handle(AsyncResult<NetSocket> netSocketAsyncResult) {
            if (netSocketAsyncResult.succeeded()) { //连接成功
                log.info("connected to remote server");
                NetSocket socket = netSocketAsyncResult.result();

                //关闭连接处理器
                socket.closeHandler(c -> {
                    log.info("connect to {} closed", socket.remoteAddress());
                    reconnect();
                });

                //异常处理器
                socket.exceptionHandler(e -> {
                    log.error("error exist", e);

                });

                //给服务器不停的发送消息,编辑报文数据
                byte[] req = "hello i am client".getBytes();
                int bodylen = req.length;
                Buffer buffer =  Buffer.buffer().appendInt(bodylen).appendBytes(req);  //组成一条报文消息 报头+数据
                socket.write(buffer);

            } else { //连接失败
                //重连
                log.error("connect remote server failed");
                reconnect();
            }
        }

        private void reconnect(){
            //每5秒重试连接
            vertx.setTimer(1000 * 5, res -> {
                log.info("try reconnect to server");
                vertx.createNetClient().connect(8091, "127.0.0.1", new ClientConnHandler());
            });
        }

    }


}
