package disruptor.advance.a2;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import disruptor.advance.a1.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisruptorMain {
    /**  ���β���
     *         TradeHandler1(���У�
     *       /              \
     *      /                \
     *   P1                    TradeHandler3(���У�
     *      \                 /
     *       \              /
     *        TradeHandler2(���У�
     *  ���� disruptorͬʱʵ�ֲ��� �� ���в���
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

        //2��ע�����Ѷ���event handler) - ���β���
        //2.1 �Ȳ���ִ�� handler1 handler2 ���������-handler�����в���
        //2.2 �ٴ���handler3���������-handler�����в���
        //����1
//        disruptor
//                .handleEventsWith(new TradeHandler1(), new TradeHandler2())
//                .handleEventsWith(new TradeHandler3());

        //����2
        EventHandlerGroup<Trade> eventHandlerGroup = disruptor
                .handleEventsWith(new TradeHandler1(), new TradeHandler2());
        eventHandlerGroup.then(new TradeHandler3());
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
