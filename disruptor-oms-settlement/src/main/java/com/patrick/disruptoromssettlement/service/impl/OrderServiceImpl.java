package com.patrick.disruptoromssettlement.service.impl;

import com.client.bean.order.CmdType;
import com.client.bean.order.OrderCmd;
import com.client.bean.order.OrderDirection;
import com.client.bean.order.OrderType;
import com.patrick.disruptoromssettlement.bean.res.OrderInfo;
import com.patrick.disruptoromssettlement.bean.res.PosiInfo;
import com.patrick.disruptoromssettlement.bean.res.TradeInfo;
import com.patrick.disruptoromssettlement.config.AppConfig;
import com.patrick.disruptoromssettlement.config.GatewayConn;
import com.patrick.disruptoromssettlement.service.IOrderService;
import com.patrick.disruptoromssettlement.util.DbUtil;
import com.patrick.disruptoromssettlement.util.IDConverter;
import io.vertx.core.buffer.Buffer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.patrick.disruptoromssettlement.bean.MatchDataConsumer.ORDER_DATA_CACHE_ADDR;

@Log4j2
@Component
public class OrderServiceImpl implements IOrderService {
    @Override
    public Long getBalance(long uid) {
        return DbUtil.getBalance(uid);
    }

    @Override
    public List<PosiInfo> getPostList(long uid) {
        return DbUtil.getPosiList(uid);
    }

    @Override
    public List<OrderInfo> getOrderList(long uid) {
        return DbUtil.getOrderList(uid);
    }

    @Override
    public List<TradeInfo> getTradeList(long uid) {
        return DbUtil.getTradeList(uid);
    }

    @Autowired
    private AppConfig config;

    @Autowired
    private GatewayConn gatewayConn;

    @Override
    public boolean sendOrder(long uid, short type, long timestamp, int code,
                             byte direction, long price, long volume, byte ordertype) {
        final OrderCmd orderCmd = OrderCmd.builder()
                .type(CmdType.of(type))
                .timestamp(timestamp)
                .mid(config.getId())
                .uid(uid)
                .code(code)
                .direction(OrderDirection.of(direction))
                .price(price)
                .volume(volume)
                .orderType(OrderType.of(ordertype))
                .build();

        //1.入库
        int oid = DbUtil.saveOrder(orderCmd);
        if(oid < 0){
            return false;
        }else {
            //TODO 发送网关
            //1.调整资金持仓数据
            if (orderCmd.direction == OrderDirection.BUY) {
                DbUtil.minusBalance(orderCmd.uid, orderCmd.volume * orderCmd.volume);
            } else if(orderCmd.direction == OrderDirection.SELL) {
                DbUtil.minusPosi(orderCmd.uid, orderCmd.code, orderCmd.volume, orderCmd.price);
            } else {
                log.error("wrong direction[{}], ordercmd:{}", orderCmd.direction, orderCmd);
            }
            //2.生成全局ID  组装ID long[int(柜台ID）,int（委托ID，数据库中的主键）]
            orderCmd.oid = IDConverter.combineInt2Long(config.getId(), oid);


            //存入缓存
            byte[] serialize = null;
            try {
                serialize = config.getBodyCodec().serialize(orderCmd);
            } catch (Exception e) {
                log.error(e);
            }
            if (serialize == null) {
                return false;
            }
            config.getVertx().eventBus().send(ORDER_DATA_CACHE_ADDR, Buffer.buffer(serialize));


            //3。打包委托，发送数据(OrderCmd -> CommonMsg -> TCP数据流)
            //4. 发送数据
            gatewayConn.sendOrder(orderCmd);


            log.info(orderCmd);

            return true;
        }
    }

    @Override
    public boolean cancelOrder(int uid, int counteroid, int code) {

        final OrderCmd orderCmd = OrderCmd.builder()
                .uid(uid)
                .code(code)
                .type(CmdType.CANCEL_ORDER)
                .oid(IDConverter.combineInt2Long(config.getId(), counteroid))
                .build();

        log.info("recv cancel order :{}", orderCmd);
        gatewayConn.sendOrder(orderCmd);
        return true;
    }
}
