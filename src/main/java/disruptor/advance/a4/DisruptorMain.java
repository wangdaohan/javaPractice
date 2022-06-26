package disruptor.advance.a4;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * 多生产者模式
 * 多消费者模式
 */
public class DisruptorMain {
    public static void main(String[] args) throws InterruptedException {
        RingBuffer<Order> ringBuffer = RingBuffer.create(ProducerType.MULTI,
                new EventFactory<Order>() {
                    @Override
                    public Order newInstance() {
                        return new Order();
                    }
                },
                1024 * 1024,
                new YieldingWaitStrategy());
        //通过ringbuffer创建屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //构建多消费者
        Consumer[] consumers = new Consumer[10];
        for(int i=0; i < consumers.length; i++) {
            consumers[i] = new Consumer("C"+i);
        }
        //多消费者工作池
        WorkerPool<Order> workerPool = new WorkerPool<Order>(ringBuffer,sequenceBarrier,new EventExceptionHandler(), consumers);

        //设置多个消费者的sequence序列号，用于单独统计消费进度
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

        //start workerpool
        workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

        CountDownLatch latch = new CountDownLatch(1);
        for(int i=0; i < 100; i++) {
             Producer producer = new Producer(ringBuffer);
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     try {
                         latch.await();
                     } catch (Exception e){
                         e.printStackTrace();
                     }
                     for(int j = 0; j < 100; j++) {
                         producer.sendData(UUID.randomUUID().toString());
                     }
                 }
             }).start();
        }

        Thread.sleep(2000);
        System.out.println("Thread created, start producing data");
        latch.countDown();

        Thread.sleep(5000);
        System.out.println("total task count:"+consumers[2].getCount());
    }

    static class EventExceptionHandler<Order> implements ExceptionHandler {

        @Override
        public void handleEventException(Throwable throwable, long l, Object o) {

        }

        @Override
        public void handleOnStartException(Throwable throwable) {

        }

        @Override
        public void handleOnShutdownException(Throwable throwable) {

        }
    }

}
