package disruptor.advance.a4;

import com.lmax.disruptor.WorkHandler;
import disruptor.advance.a1.Trade;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements WorkHandler<Order> {
    private String consumerId;
    private static AtomicInteger count = new AtomicInteger(0);
    private Random random = new Random();

    public Consumer(String consumerId) {
        this.consumerId = consumerId;
    }

    @Override
    public void onEvent(Order order) throws Exception {
        Thread.sleep(1 * random.nextInt(10));
        System.out.println("current consumer:"+this.consumerId+", message ID:"+order.getId());
        count.incrementAndGet();
    }

    public int getCount(){
        return count.get();
    }

}
