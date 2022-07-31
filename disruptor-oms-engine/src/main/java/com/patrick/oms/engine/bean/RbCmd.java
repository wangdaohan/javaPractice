package com.patrick.oms.engine.bean;

import com.client.bean.order.CmdType;
import com.client.bean.order.OrderDirection;
import com.client.bean.order.OrderType;
import com.patrick.oms.engine.bean.command.CmdResultCode;
import com.client.hq.L1MarketData;
import com.patrick.oms.engine.bean.orderbook.MatchEvent;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import java.util.List;

@Builder
@ToString
@Data
public class RbCmd {
    public long timestamp;
    public short mid;
    public long uid;
    public CmdType command;
    public int code;
    public OrderDirection direction;
    public long price;
    public long volume;
    public long oid;
    public OrderType orderType;

    //trade event chain
    @ToString.Exclude
    public List<MatchEvent> mathEventList;

    //存储消息的当前处理状态，到了哪一步
    //前置风控 -> 撮合 -> 发布
    @ToString.Exclude
    public CmdResultCode resultCode;

    //保存行情
    @ToString.Exclude
    public IntObjectHashMap<L1MarketData> marketDataMap;
}
