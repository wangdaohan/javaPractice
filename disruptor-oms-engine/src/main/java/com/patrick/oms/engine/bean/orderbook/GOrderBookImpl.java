package com.patrick.oms.engine.bean.orderbook;

import com.alipay.sofa.jraft.rhea.util.Lists;
import com.client.bean.order.OrderDirection;
import com.client.bean.order.OrderStatus;
import com.client.hq.L1MarketData;
import com.patrick.oms.engine.bean.RbCmd;
import com.patrick.oms.engine.bean.command.CmdResultCode;
import io.netty.util.collection.LongObjectHashMap;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.*;

@Log4j2
@RequiredArgsConstructor
public class GOrderBookImpl implements IOrderBook{
    //每一个股票都有一个OrderBook
    @NonNull
    private int code;

    //why treemap?
    //key: price
    //<price, orderBucket>
    private final NavigableMap<Long, IOrderBucket> sellBuckets = new TreeMap<>(); //price从小到大排序 / 默认
    private final NavigableMap<Long, IOrderBucket> buyBuckets = new TreeMap<>(Collections.reverseOrder());//price需要从大到小排序

    //存放所有委托的缓存 cache,用于快速查找使用
    private final LongObjectHashMap<Order> oidMapCache = new LongObjectHashMap<>();

    @Override
    public CmdResultCode newOrder(RbCmd cmd) {
        //1.判断重复
        if(oidMapCache.containsKey(cmd.oid)){
            return CmdResultCode.DUPLICATE_ORDER_ID;
        }
        //2.生成新order
        //2.1 预撮合 - 很有可能这个OrderBook过来的时候已经带有撮合订单，并已经撮合完，所以就不用生成这个Order对象
        //如果我是卖单(sell) Px:50 Qty: 100, 那么就需要去买单(Buy)里面找Buckets >= 50
        //如果我是买单(buy) Px:40 Qty: 100, 那么就需要去卖单(sell)里面找Buckets <= 40
        final NavigableMap<Long, IOrderBucket> subMatchBuckets = (cmd.direction == OrderDirection.BUY ? sellBuckets : buyBuckets).headMap(cmd.price, true);
        long tVolume = preMatch(cmd, subMatchBuckets);//预撮合方法：参数1：这笔委托 参数2：能匹配上的bucket  -> 返回最终能成效的委托量
        if (tVolume == cmd.volume) {
            return CmdResultCode.SUCCESS;

        }
        final Order order = Order.builder()
                .mid(cmd.mid)
                .uid(cmd.uid)
                .code(cmd.code)
                .direction(cmd.direction)
                .price(cmd.price)
                .volume(cmd.volume)
                .tvolume(tVolume)
                .oid(cmd.oid)
                .timestamp(cmd.timestamp)
                .build();

        if (tVolume == 0) {
            genMatchEvent(cmd, OrderStatus.ORDER_ED);
        } else {
            genMatchEvent(cmd, OrderStatus.PART_TRADE);
        }

        //3.加入OrderBucket
        final IOrderBucket bucket = ((cmd.direction == OrderDirection.SELL) ? sellBuckets : buyBuckets).computeIfAbsent(cmd.price , p -> {
            final IOrderBucket b = IOrderBucket.create(IOrderBucket.OrderBucketImplType.GUDY);
            b.setPrice(p);
            return b;
        });

        bucket.put(order);
        oidMapCache.put(cmd.oid, order);


        return null;
    }

    private void genMatchEvent(RbCmd cmd, OrderStatus status) {
        long now = System.currentTimeMillis();
        MatchEvent event = new MatchEvent();
        event.timestamp = now;
        event.mid = cmd.mid;
        event.oid = cmd.oid;
        event.status = status;
        event.volume = 0;
        cmd.getMathEventList().add(event);

    }

    private long preMatch(RbCmd cmd, SortedMap<Long, IOrderBucket> matchingBuckets) {
        int tVol = 0;
        if (matchingBuckets.size() == 0) {
            return tVol;
        }

        List<Long> emptyBuckets = Lists.newArrayList();
        for (IOrderBucket bucket : matchingBuckets.values()) {
            tVol += bucket.match(cmd.volume - tVol, cmd, order -> oidMapCache.remove(order.getOid()));

            if(bucket.getTotalVolume() == 0) {
                emptyBuckets.add(bucket.getPrice());
            }

            if (tVol == cmd.volume) {
                break;
            }
        }

        emptyBuckets.forEach(matchingBuckets::remove);

        return tVol;
    }

    @Override
    public CmdResultCode cancelOrder(RbCmd cmd) {
        //1. 从缓存中删除
        Order order = oidMapCache.get(cmd.oid);
        if(order == null) {
            return CmdResultCode.INVALID_ORDER_ID;
        }
        oidMapCache.remove(order.getOid());

        //2.从orderBucket中移除委托
        final NavigableMap<Long, IOrderBucket> buckets = order.getDirection() == OrderDirection.BUY ? buyBuckets : sellBuckets;
        IOrderBucket orderBucket = buckets.get(order.getPrice());
        orderBucket.remove(order.getOid());
        if(orderBucket.getTotalVolume() == 0) {
            buckets.remove(order.getPrice());
        }

        //3。发送撤单MatchEvent
        MatchEvent cancelEvent = new MatchEvent();
        cancelEvent.timestamp = System.currentTimeMillis();
        cancelEvent.mid = order.getMid();
        cancelEvent.oid = order.getOid();
        cancelEvent.status = order.getTvolume() == 0 ? OrderStatus.CANCEL_ED : OrderStatus.PART_CANCEL;
        cancelEvent.volume = order.getTvolume() - order.getVolume(); //负数
        cancelEvent.price = order.getPrice();
        cmd.getMathEventList().add(cancelEvent);
        return CmdResultCode.SUCCESS;
    }

    @Override
    public void fillCode(L1MarketData data) {
        data.code = code;
    }

    @Override
    public void fillSell(int sellSize, L1MarketData data) {
        if(sellSize == 0) {
            data.sellSize = 0;
            return;
        }

        int i = 0;
        for (IOrderBucket bucket : sellBuckets.values()) {
            data.sellPrices[i] = bucket.getPrice();
            data.sellVolumes[i] = bucket.getTotalVolume();
            if (++i == sellSize) {
                break;
            }
        }
        data.sellSize = i;
    }

    @Override
    public void fillBuys(int buySize, L1MarketData data) {
        if(buySize == 0) {
            data.buySize = 0;
            return;
        }
        int i = 0;
        for (IOrderBucket bucket : buyBuckets.values()) {
            data.buyPrices[i] = bucket.getPrice();
            data.buyVolumes[i] = bucket.getTotalVolume();
            if (++i == buySize) {
                break;
            }
        }
        data.buySize = i;
    }

    @Override
    public int limitSellBucketSize(int maxSize) {
        return Math.min(maxSize, sellBuckets.size());
    }

    @Override
    public int limitBuyBucketSize(int maxSize) {

        return Math.min(maxSize, buyBuckets.size());
    }
}
