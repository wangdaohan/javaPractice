package disruptor.quickstart;

import com.lmax.disruptor.EventHandler;

public class OrderEventHandler implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent orderEvent, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("consumer processing:"+orderEvent.getValue());
       // Thread.sleep(10000);
        System.out.println("consumer processed:"+orderEvent.getValue());
    }
}
