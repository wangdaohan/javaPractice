package com.patrick.oms.engine.bean.orderbook;

import com.client.hq.L1MarketData;
import com.patrick.oms.engine.bean.RbCmd;
import com.patrick.oms.engine.bean.command.CmdResultCode;

import static com.client.hq.L1MarketData.L1_SIZE;

public interface IOrderBook {

    //1.新增委托
    CmdResultCode newOrder(RbCmd cmd);

    //2.撤单
    CmdResultCode cancelOrder(RbCmd cmd);

    //3.查询行情快照
    default L1MarketData getL1MarketDataSnapshot() {
        final int buySize = limitBuyBucketSize(L1_SIZE);
        final int sellSize = limitSellBucketSize(L1_SIZE);

        final L1MarketData data = new L1MarketData(buySize, sellSize);
        fillBuys(buySize, data);
        fillSell(sellSize, data);
        fillCode(data);

        data.timestamp = System.currentTimeMillis();
        return data;
    }

    void fillCode(L1MarketData data);

    void fillSell(int sellSize, L1MarketData data);

    void fillBuys(int buySize, L1MarketData data);

    int limitSellBucketSize(int maxSize);

    int limitBuyBucketSize(int maxSize);

    //4.TODO 初始化枚举


}
