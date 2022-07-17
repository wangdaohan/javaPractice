package com.client.bean;

import com.client.bean.order.OrderCmd;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 本地储存ordercmd的容器 -
 */
public class OrderCmdContainer {
    private static OrderCmdContainer ourInstance = new OrderCmdContainer();

    private OrderCmdContainer(){}

    public static OrderCmdContainer getInstance() {
        return ourInstance;
    }

    ///////////////////////////
    private final BlockingQueue<OrderCmd> queue = new LinkedBlockingQueue<>();

    public int size(){
        return queue.size();
    }

    public boolean cache(OrderCmd cmd) {
        return queue.offer(cmd);
    }
    public List<OrderCmd> getAll(){
        List<OrderCmd> msgList = Lists.newArrayList();
        int count = queue.drainTo(msgList); //drainTo: 1。 以非常快的速度将数据一次性取出，并清空queue 2。 不阻塞（对比pull)
        if (count == 0) {
            return null;
        } else {
            return msgList;
        }
    }

}
