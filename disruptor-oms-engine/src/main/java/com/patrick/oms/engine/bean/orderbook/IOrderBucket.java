package com.patrick.oms.engine.bean.orderbook;

import com.patrick.oms.engine.bean.RbCmd;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public interface IOrderBucket extends Comparable<IOrderBucket>{

    AtomicLong tidGen = new AtomicLong(0);

    //1.新增订单
    void put(Order order);
    //2.移除订单
    Order remove(long oid);

    //3. match
    //volumeLeft需要匹配数量
    //triggerCmd委托订单
    //回调函数
    long match(long volumeLeft, RbCmd triggerCmd, Consumer<Order> removeOrderCallback);

    //4.行情发布
    long getPrice();
    void setPrice(long price);
    long getTotalVolume();

    //5.初始化选项
    static IOrderBucket create(OrderBucketImplType type) {
        switch (type) {
            case GUDY:
                return new GOrderBucketImpl();
            default:
                throw new IllegalArgumentException();
        }
    }

    @Getter
    enum OrderBucketImplType {
        GUDY(0);

        private byte code;

        OrderBucketImplType(int code) {
            this.code = (byte) code;
        }
    }

    // 6.comparable 排序功能
    default int compareTo(IOrderBucket other) {
        return Long.compare(this.getPrice(), other.getPrice());
    }
}
