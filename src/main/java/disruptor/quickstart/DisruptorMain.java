package disruptor.quickstart;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class DisruptorMain {
    public static void main(String[] args) {
        OrderEventFactory orderEventFactory = new OrderEventFactory();
        int ringBuffersize = 1024 * 1024;
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                orderEventFactory,
                ringBuffersize,
                executorService,
                ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );

        disruptor.handleEventsWith(new OrderEventHandler());
        disruptor.start();

        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();

        OrderEventProducer orderEventProducer = new OrderEventProducer(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        for(long i = 0; i < 100; i++){
            System.out.println("producing");
            bb.putLong(0, i);
            orderEventProducer.sendData(bb);
        }

        System.out.println("produced");
        disruptor.shutdown();
        executorService.shutdown();
    }
}