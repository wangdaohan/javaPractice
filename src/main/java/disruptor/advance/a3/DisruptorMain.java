package disruptor.advance.a3;

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
    /**  �����β���
         *         TradeHandler1(���У�  ---  > TradeHandler2(���У�
         *       /                                                \
         *      /                                                  \
     *   P1-----                                                       ---  > TradeHandler3(���У�
         *      \                                                   /
         *       \                                                /
     *            TradeHandler4(���У� ---  > TradeHandler5(���У�
     *  ���� disruptorͬʱʵ�ֲ��� �� ���в���
     */
    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(4);
        ExecutorService es2 = Executors.newFixedThreadPool(5); // �ж��ٸ�handler����Ӧ���ж��ٸ��̳߳� -> ��������ģʽ�׶� ->������ö�������ģʽ
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

        //2��ע�����Ѷ���event handler) - �����β���
        TradeHandler1 h1 = new TradeHandler1();
        TradeHandler2 h2 = new TradeHandler2();
        TradeHandler3 h3 = new TradeHandler3();
        TradeHandler4 h4 = new TradeHandler4();
        TradeHandler5 h5 = new TradeHandler5();
        disruptor.handleEventsWith(h1,h4); // h1, h4����
        disruptor.after(h1).handleEventsWith(h2);
        disruptor.after(h4).handleEventsWith(h5);
        disruptor.after(h2, h5).handleEventsWith(h3);
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
