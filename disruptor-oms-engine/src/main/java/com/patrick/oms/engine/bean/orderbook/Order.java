package com.patrick.oms.engine.bean.orderbook;

//只在OrderBook OrderBucket?内部使用

import com.client.bean.order.OrderDirection;
import lombok.*;
import org.apache.commons.lang.builder.HashCodeBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public final class Order {
    private short mid;
    private long uid;
    private int code;
    private OrderDirection direction;
    private long price;
    private long volume; // 订单总量
    private long tvolume; //已成效量
    private long oid;
    private long timestamp;
    private long innerOid; //内部排序顺序

    @Override
    public int hashCode(){
        return new HashCodeBuilder(17, 37).append(mid).append(uid).append(code).append(direction).append(price).append(volume).append(tvolume).append(oid).append(innerOid).hashCode();
    }

}
