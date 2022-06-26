package disruptor.advance.a1;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

public class TradeHandler1 implements EventHandler<Trade>, WorkHandler<Trade> {
    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        this.onEvent(trade);
    }

    @Override
    public void onEvent(Trade trade) throws Exception {
        System.out.println("handler 1: SET NAME");
        trade.setName("H1");
        Thread.sleep(1000);
    }
}
