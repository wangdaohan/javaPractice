package com.example.disruptoromsgateway.handler;

import com.client.bean.msg.CommonMsg;
import io.vertx.core.net.NetSocket;

public interface IMsgHandler {
    default void onConnect(NetSocket netSocket) {};
    default void onDisconnect(NetSocket netSocket) {};
    default void onException(NetSocket netSocket, Throwable e) {};

    void onData(CommonMsg msg);
}
