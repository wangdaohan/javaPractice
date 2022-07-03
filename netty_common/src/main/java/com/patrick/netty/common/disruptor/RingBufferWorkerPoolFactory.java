package com.patrick.netty.common.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import com.patrick.netty.common.entity.TranslatorDataWrapper;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class RingBufferWorkerPoolFactory {
    private static class SingletonHolder {
        static final RingBufferWorkerPoolFactory instance = new RingBufferWorkerPoolFactory();
    }
    private RingBufferWorkerPoolFactory(){
    }

    public static RingBufferWorkerPoolFactory getInstance(){
        return SingletonHolder.instance;
    }

    private static Map<String, MessageProducer> producers = new ConcurrentHashMap<>();
    private static Map<String, MessageConsumer> consumers = new ConcurrentHashMap<>();

    private RingBuffer<TranslatorDataWrapper> ringBuffer;
    private SequenceBarrier sequenceBarrier;
    private WorkerPool<TranslatorDataWrapper> workerPool;

    public void initAndStart(ProducerType producerType, int bufferSize, WaitStrategy waitStrategy, MessageConsumer[] messageConsumers) {
        System.out.println("init and start ring buffer");
        //1.构建ringbuffer对象
        this.ringBuffer = RingBuffer.create(producerType, new EventFactory<TranslatorDataWrapper>() {
                    @Override
                    public TranslatorDataWrapper newInstance() {
                        return new TranslatorDataWrapper();
                    }
                },
                bufferSize,
                waitStrategy);
        //2。设置序号栅栏SequenceBarrier
        this.sequenceBarrier = this.ringBuffer.newBarrier();
        //3.设置工作池workerpool
        this.workerPool = new WorkerPool<>(this.ringBuffer, this.sequenceBarrier, new EventExceptionHandler(), messageConsumers);
        //4.把所构建的消费都放入池中
        for(MessageConsumer consumer : messageConsumers){
            consumers.put(consumer.getConsumerId(), consumer);
        }
        //5.添加我们的sequences
        this.ringBuffer.addGatingSequences(this.workerPool.getWorkerSequences());

        //6.启动workerpool
        workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
    }

    public MessageProducer getMessageProducer(String producerId) {
        MessageProducer messageProducer = this.producers.get(producerId);
        if (null == messageProducer) {
            messageProducer = new MessageProducer(producerId, this.ringBuffer);
            producers.put(producerId, messageProducer);
        }
        return messageProducer;
    }

    static class EventExceptionHandler<TranslatorDataWrapper> implements ExceptionHandler<TranslatorDataWrapper> {

        @Override
        public void handleEventException(Throwable throwable, long l, TranslatorDataWrapper o) {

        }

        @Override
        public void handleOnStartException(Throwable throwable) {

        }

        @Override
        public void handleOnShutdownException(Throwable throwable) {

        }
    }
}
