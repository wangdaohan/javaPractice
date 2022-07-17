package com.client.tcp;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.xml.sax.helpers.AttributesImpl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Log4j2
@RequiredArgsConstructor
public class TcpDirectSender {
    @NonNull
    private String ip;

    @NonNull
    private int port;

    @NonNull
    private Vertx vertx;


    ////////////////////////////

    private volatile NetSocket socket;

    public void startup() {
        vertx.createNetClient().connect(port,ip, new ClientConnHandler());

        new Thread(() -> {
           while (true) {
               try {
                   log.info("checking queue");
                    Buffer msg = sendCache.poll(5, TimeUnit.SECONDS); // poll会阻塞，所以定时5s，等待5s会还拿不到数据就会返回为null
                    if (msg != null
                        && msg.length() > 0
                        && socket != null) {
                        socket.write(msg);
                    }
               } catch (Exception e) {
                   log.error("msg send fail, continue");
               }
           }
        }).start();
    }

    /**
     * 发送消息到网关 - 增加异步处理逻辑
     * 背景：
     *    1. 不推荐每次有消息要发送就直接用socket.write(),
     *       因为有可能会阻塞在网卡中，甚至有可能会被丢弃，但socket并不会告诉上层调用。
     *
     * 推荐解决方案：   - 增加异步处理逻辑
     *     1。 蹭加一个BlockingQueue
     *     2。 让socket自己去缓存中拿
     * 好处：
     *     1. 可以做流量控制，不会造成网卡阻塞等。
     */
    // socket自己去缓存中取数据进行发送
    private final BlockingQueue<Buffer> sendCache = new LinkedBlockingQueue<>();
    public boolean send(Buffer bufferMsg) {
        return sendCache.offer(bufferMsg);
    }

    private class ClientConnHandler implements Handler<AsyncResult<NetSocket>> {


        @Override
        public void handle(AsyncResult<NetSocket> netSocketAsyncResult) {
            if(netSocketAsyncResult.succeeded()) {
                log.info("connect success to remote {} {}", ip, port);
                socket = netSocketAsyncResult.result();
                socket.closeHandler(c -> {
                    log.info("connect to remote {} closed", socket.remoteAddress());
                    reconnect();
                });
                socket.exceptionHandler(ex -> {
                    log.info("error exist", ex.getCause());
                });
            } else {//连接失败
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
