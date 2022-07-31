package com.client.hq;

import lombok.ToString;

import java.io.Serializable;

//一档行情
@ToString
public class L1MarketData implements Serializable {
    @ToString.Exclude
    public static final int L1_SIZE = 5;

    public int code;
    public long newPrice;

    //买卖实际档位数量
    @ToString.Exclude
    public transient int buySize;
    @ToString.Exclude
    public transient int sellSize;

    public long[] buyPrices;
    public long[] buyVolumes;
    public long[] sellPrices;
    public long[] sellVolumes;

    public long timestamp;

    public L1MarketData(int buySize, int sellSize) {
        this.buySize = buySize;
        this.sellSize = sellSize;
    }
}
