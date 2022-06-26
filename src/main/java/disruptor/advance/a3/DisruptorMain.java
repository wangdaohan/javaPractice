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
    /**  六边形操作
         *         TradeHandler1(串行）  ---  > TradeHandler2(串行）
         *       /                                                \
         *      /                                                  \
     *   P1-----                                                       ---  > TradeHandler3(串行）
         *      \                                                   /
         *       \                                                /
     *            TradeHandler4(串行） ---  > TradeHandler5(串行）
     *  二、 disruptor同时实现并行 和 串行操作
     */
    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(4);
        ExecutorService es2 = Executors.newFixedThreadPool(5); // 有多少个handler，就应该有多少个线程池 -> 单消费者模式弊端 ->解决：用多消费者模式
        int ringBuffersize = 1024 * 1024; // 一定的2的n次方，有利于计算

        //1。构建disruptor
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

        //2。注册消费都（event handler) - 六边形操作
        TradeHandler1 h1 = new TradeHandler1();
        TradeHandler2 h2 = new TradeHandler2();
        TradeHandler3 h3 = new TradeHandler3();
        TradeHandler4 h4 = new TradeHandler4();
        TradeHandler5 h5 = new TradeHandler5();
        disruptor.handleEventsWith(h1,h4); // h1, h4并行
        disruptor.after(h1).handleEventsWith(h2);
        disruptor.after(h4).handleEventsWith(h5);
        disruptor.after(h2, h5).handleEventsWith(h3);
        //3.启动disruptor
        RingBuffer<Trade> ringBuffer = disruptor.start();
        CountDownLatch latch = new CountDownLatch(1);
        es.submit(new TradePublisher(latch, disruptor));

        latch.await();

        es.shutdown();
        es2.shutdown();
        disruptor.shutdown();

    }

}
