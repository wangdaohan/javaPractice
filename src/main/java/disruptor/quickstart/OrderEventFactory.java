package disruptor.quickstart;

import com.lmax.disruptor.EventFactory;
public class OrderEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        return new OrderEvent(); //return a new order event object;
    }
}
