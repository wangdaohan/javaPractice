package com.patrick.oms.engine.handler.match;

import com.patrick.oms.engine.bean.RbCmd;
import com.patrick.oms.engine.bean.command.CmdResultCode;
import com.patrick.oms.engine.bean.orderbook.IOrderBook;
import com.patrick.oms.engine.handler.BaseHandler;
import io.netty.util.collection.IntObjectHashMap;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StockMatchHandler extends BaseHandler {
    @NonNull
    private IntObjectHashMap<IOrderBook> orderBookMap;

    @Override
    public void onEvent(RbCmd cmd, long sequence, boolean endOfBatch) throws Exception {
        //对于每笔委托，首先要判断是否通过了前置风控
        if (cmd.resultCode.getCode() < 0) {
            return; //风控不通过
        }

        cmd.resultCode = processCmd(cmd);

    }

    private CmdResultCode processCmd(RbCmd cmd) {
        switch (cmd.command) {
            case NEW_ORDER:
                return orderBookMap.get(cmd.code).newOrder(cmd);
            case CANCEL_ORDER:
                return orderBookMap.get(cmd.code).cancelOrder(cmd);
            case HQ_PUB://行情指令
                orderBookMap.forEach((code, orderBook) -> {
                    cmd.marketDataMap.put(code, orderBook.getL1MarketDataSnapshot());
                });
            default:
                return CmdResultCode.SUCCESS;
        }
    }
}
