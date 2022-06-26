package disruptor.advance.a1;

import com.lmax.disruptor.EventHandler;
import java.util.UUID;
public class TradeHandler2 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        System.out.println("handler 2: SET ID");
        trade.setId(UUID.randomUUID().toString());
        Thread.sleep(1000);
    }
}