package com.patrick.oms.engine.bean.orderbook;

import com.client.bean.order.OrderStatus;
import com.client.hq.MatchData;
import lombok.NoArgsConstructor;
import lombok.ToString;

//不能命名为TradeEvent，因为除了成交事件，还有撤单等其它event

// MatchEvent vs MatchData
//  MatchEvent 是服务内部使用
// MatchData是作为外部通信使用格式, 对所有服务公开,用于发送到总线上供上/下家消费
@NoArgsConstructor
@ToString
public final class MatchEvent {
    public long timestamp;
    public short mid;
    public long oid;
    public OrderStatus status = OrderStatus.NOT_SET;
    public long tid;

    //撤单数量 成交数量
    public long volume;
    public long price;


    public MatchData copy() {
        return MatchData.builder()
                .timestamp(timestamp)
                .mid(mid)
                .oid(oid)
                .status(status)
                .tid(tid)
                .volume(volume)
                .price(price)
                .build();
    }

}
