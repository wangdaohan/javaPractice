package disruptor.advance.a1;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisruptorMain {
    /**
     * һ��disruptor���л��в���
     */
    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(4);
        ExecutorService es2 = Executors.newFixedThreadPool(4);


        int ringBuffersize = 1024 * 1024; // һ����2��n�η��������ڼ���

        //1������disruptor
        Disruptor<Trade> disruptor = new Disruptor<>(
                new EventFactory<Trade>() {
                    @Override
                    public Trade newInstance() {
                        return new Trade();
                    }
                },
                ringBuffersize,
                es2,
                ProducerType.SINGLE,
                new BusySpinWaitStrategy()
        );

        //2��ע�����Ѷ���event handler)
        //2.1 ���������-handler�����в���
        //        disruptor
        //                .handleEventsWith(new TradeHandler1())
        //                .handleEventsWith(new TradeHandler2())
        //                .handleEventsWith(new TradeHandler3());

        //2.2 ���������-handler�����в���
        disruptor.handleEventsWith(new TradeHandler1(), new TradeHandler2(), new TradeHandler3());
        //        disruptor.handleEventsWith(new TradeHandler2());
        //        disruptor.handleEventsWith(new TradeHandler3());

        //3.����disruptor
        RingBuffer<Trade> ringBuffer = disruptor.start();
        CountDownLatch latch = new CountDownLatch(1);
        es.submit(new TradePublisher(latch, disruptor));

        latch.await();

        es.shutdown();
        es2.shutdown();
        disruptor.shutdown();

    }

}
