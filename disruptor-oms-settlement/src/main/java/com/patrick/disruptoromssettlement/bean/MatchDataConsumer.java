package com.patrick.disruptoromssettlement.bean;


import com.client.bean.order.OrderCmd;
import com.client.bean.order.OrderStatus;
import com.client.hq.MatchData;
import com.patrick.disruptoromssettlement.config.AppConfig;
import com.patrick.disruptoromssettlement.util.DbUtil;
import com.patrick.disruptoromssettlement.util.IDConverter;
import io.netty.util.collection.LongObjectHashMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.client.bean.order.OrderDirection.BUY;
import static com.client.bean.order.OrderDirection.SELL;
import static com.client.bean.order.OrderStatus.CANCEL_ED;
import static com.client.bean.order.OrderStatus.PART_CANCEL;
import static com.patrick.disruptoromssettlement.bean.MqttBusConsumer.*;



@Log4j2
@Component
public class MatchDataConsumer {

    @Autowired
    private AppConfig config;


    //<委托编号，orderCmd>
    private LongObjectHashMap<OrderCmd> oidOrderMap = new LongObjectHashMap<>();

    @PostConstruct
    public void init(){
        EventBus eventBus = config.getVertx().eventBus(); //从vertx总线中订阅刚才在MqttBusConsumer produce转发的行情数据
        //处理核心发过来的行情
        eventBus.consumer(INNER_MATCH_DATA_ADDR).handler(buffer -> {
            Buffer body = (Buffer) buffer.body();
            if(body.length() == 0) {
                return;
            }
            MatchData[] matchData = null;
            try{
                matchData = config.getBodyCodec().deserialize(body.getBytes(), MatchData[].class);
            } catch (Exception e) {
                log.error(e);
            }
            if(ArrayUtils.isEmpty(matchData)) {
                return;
            }
            //按照Oid进行分类
            Map<Long, List<MatchData>> collect = Arrays.asList(matchData).stream().collect(Collectors.groupingBy(t -> t.oid));
            for(Map.Entry<Long, List<MatchData>> entry: collect.entrySet()) {
                if(CollectionUtils.isEmpty(entry.getValue())){
                    continue;
                }

                //拆分获取柜台内部委托编号
                long oid = entry.getKey();
                int counterOid = IDConverter.seperateLong2Int(oid)[1];//高位:柜台 低位：oid
                
                updateAndNotify(counterOid, entry.getValue(), oidOrderMap.get(oid));
                
            }
        });
    }

    private void updateAndNotify(int counterOid, List<MatchData> value, OrderCmd orderCmd) {
        if(CollectionUtils.isEmpty(value)){
            return;
        }

        //成交
        for(MatchData md: value) {
            OrderStatus orderStatus = md.status;
            if (orderStatus == OrderStatus.TRADE_ED || orderStatus == OrderStatus.PART_TRADE) {//部分或全部成交
                //更新成交
                DbUtil.saveTrade(counterOid, md, orderCmd);
                //持仓资金 多退少补
                if(orderCmd.direction == BUY) {
                    if (orderCmd.price > md.price) {
                        DbUtil.addBalance(orderCmd.uid, (orderCmd.price - md.price) * md.volume);
                    }
                    DbUtil.addPosi(orderCmd.uid, orderCmd.code, md.volume, md.price);
                } else if (orderCmd.direction == SELL) {
                    DbUtil.addBalance(orderCmd.uid, md.price * md.volume);
                } else {
                    log.error("unknown direction:{}", orderCmd);
                }

            }
        }
        // 委托变动
        //根据最后一笔Match处理委托
        MatchData finalMatchData = value.get(value.size() - 1);
        OrderStatus finalOrderStatus = finalMatchData.status;
        DbUtil.updateOrder(orderCmd.uid, counterOid, finalOrderStatus);
        if (finalOrderStatus == CANCEL_ED || finalOrderStatus == PART_CANCEL) { // 撤单处理
            oidOrderMap.remove(orderCmd.oid);
            if (orderCmd.direction == BUY) {
                //撤买
                DbUtil.addBalance(orderCmd.uid, -(orderCmd.price * finalMatchData.volume));
            } else if (orderCmd.direction == SELL) {
                //增加持仓  撤卖单
                DbUtil.addPosi(orderCmd.uid, orderCmd.code, -finalMatchData.volume, orderCmd.price);
            } else {
                log.error("wrong direction[{}]", orderCmd.direction);
            }
        }

        //TODO 通知客户端 成交的更新
    }

}
