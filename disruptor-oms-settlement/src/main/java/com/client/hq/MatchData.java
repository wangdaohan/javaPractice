package com.client.hq;

import com.client.bean.order.OrderStatus;
import lombok.Builder;

import java.io.Serializable;


// MatchEvent vs MatchData
//  MatchEvent 是服务内部使用
// MatchData是作为外部通信使用格式，用于发送到总线上供上/下家消费
@Builder
public class MatchData implements Serializable {
    public long timestamp;
    public short mid;
    public long oid;
    public OrderStatus status;
    public long tid;
    public long volume;
    public long price;
}
