package com.patrick.oms.engine.bean.orderbook;

import com.client.bean.order.OrderStatus;
import com.patrick.oms.engine.bean.RbCmd;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

@Log4j2
@ToString
public class GOrderBucketImpl implements IOrderBucket{

    //1. 价格
    @Getter
    @Setter
    private long price;
    //2. 量
    @Getter
    private long totalVolume = 0;

    /** 3. 委托列表 list
     *
     *  同时满足可以快速加入委托 / 快速删除委托 -> list排除 / -> 链表和Map结合 LinkedHashMap
     * */
    private final LinkedHashMap<Long, Order> entries = new LinkedHashMap<>();

    @Override
    public void put(Order order) {
        entries.put(order.getOid(), order);
        totalVolume += order.getVolume() - order.getTvolume(); //总委托 - 已成交委托
    }

    @Override
    public Order remove(long oid) {
        Order order = entries.get(oid);
        if (order == null) {
            return null;
        }
        return entries.remove(oid);
    }

    //撮合实现
    @Override
    public long match(long volumeLeft, RbCmd triggerCmd, Consumer<Order> removeOrderCallback) {
        // 卖单
        // S 46 -> 5 10 24
        // S 45 -> 11 20 10 20

        // 买单
        // B 45 100? 卖单中找最有优势的价格 -> S 45 -> 11 20 10 20  ->剩39，挂在买单中 B 45 39

        Iterator<Map.Entry<Long, Order>> iterator = entries.entrySet().iterator();
        long volumeMatch = 0;
        while (iterator.hasNext() && volumeLeft > 0) {
            Map.Entry<Long, Order> next = iterator.next();
            Order order = next.getValue();
            //计算order可以吃多少委托量
            long traded = Math.min(volumeLeft, order.getVolume() - order.getTvolume());
            volumeMatch += traded;

            //修改对应数据
            //1.order自身的量 2.volumeLeft 3.Bucket总委托量
            order.setTvolume(order.getTvolume() + traded);
            volumeLeft -= traded;
            totalVolume -= traded;

            //生成事件
            boolean fullMatch = order.getVolume() == order.getTvolume();
            genMatchEvent(order, triggerCmd, fullMatch, volumeLeft == 0, traded);
            if(fullMatch) {
                removeOrderCallback.accept(order);
                iterator.remove();
            }
        }
        return volumeMatch;
    }

    private void genMatchEvent(Order order, RbCmd triggerCmd, boolean fullMatch, boolean cmdFullMatch, long traded) {
        long now = System.currentTimeMillis();
        long tid = IOrderBucket.tidGen.getAndIncrement();
        //2个MatchEvent
        MatchEvent bidEvent = new MatchEvent();
        bidEvent.timestamp = now;
        bidEvent.mid = triggerCmd.mid;
        bidEvent.oid = triggerCmd.oid;
        bidEvent.status = cmdFullMatch ? OrderStatus.TRADE_ED : OrderStatus.PART_TRADE;
        bidEvent.tid = tid;
        bidEvent.volume = traded;
        bidEvent.price = order.getPrice();
        triggerCmd.getMathEventList().add(bidEvent);

        MatchEvent askEvent = new MatchEvent();
        askEvent.timestamp = now;
        askEvent.mid = order.getMid();
        askEvent.oid = order.getOid();
        askEvent.status = cmdFullMatch ? OrderStatus.TRADE_ED : OrderStatus.PART_TRADE;
        askEvent.tid = tid;
        askEvent.volume = traded;
        askEvent.price = order.getPrice();

        triggerCmd.getMathEventList().add(askEvent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GOrderBucketImpl that = (GOrderBucketImpl) o;

        return new EqualsBuilder().append(price, that.price).append(totalVolume, that.totalVolume).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(price).append(entries).toHashCode();
    }
}



























