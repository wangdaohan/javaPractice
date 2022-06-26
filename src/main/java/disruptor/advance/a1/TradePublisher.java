package disruptor.advance.a1;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class TradePublisher implements Runnable{
    private CountDownLatch latch;
    private Disruptor<Trade> disruptor;

    public TradePublisher(CountDownLatch latch, Disruptor<Trade> disruptor) {
        this.latch = latch;
        this.disruptor = disruptor;
    }

    @Override
    public void run() {
        try{
            //新的提交任务方式，
            //之前都是通过ringbuffer去publish

            //提交多次
            for(int i=0; i<1; i++) {
                disruptor.publishEvent(new TradeEventTranslator());
            }
        }finally {
            latch.countDown();
        }


    }
}

class TradeEventTranslator implements EventTranslator<Trade> {

    @Override
    public void translateTo(Trade trade, long l) {
        this.generateTrade(trade);
    }

    private void generateTrade(Trade event) {
        //生成订单，编写业务逻辑
        event.setPrice(new Random().nextDouble() * 9999);
    }
}
