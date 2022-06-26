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
            //�µ��ύ����ʽ��
            //֮ǰ����ͨ��ringbufferȥpublish

            //�ύ���
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
        //���ɶ�������дҵ���߼�
        event.setPrice(new Random().nextDouble() * 9999);
    }
}
