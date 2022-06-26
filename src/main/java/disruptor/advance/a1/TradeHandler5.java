package disruptor.advance.a1;

import com.lmax.disruptor.EventHandler;
public class TradeHandler5 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        System.out.println("handler 5: get PRICE:"+trade.getPrice());
    }
}
