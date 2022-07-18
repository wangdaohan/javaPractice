package com.demo.disruptordemo;

import com.demo.disruptordemo.event.DisruptorExceptionHandler;
import com.demo.disruptordemo.event.RbCmd;
import com.demo.disruptordemo.event.RbCmdFactory;
import com.demo.disruptordemo.event.RbData;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.log4j.Log4j2;
import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityThreadFactory;

import java.util.Timer;
import java.util.TimerTask;

@Log4j2
public class DisruptorTestMain {
    private Disruptor disruptor;

    public void initDisruptor(){
        disruptor = new Disruptor(
                new RbCmdFactory(),
                1024,  //ringbuffer size power of 2 (2^N) , 跟RingBuffer的寻址原理有关： index索引与处理序列号关系 index = 13544980(序列号）& ( 1024 -1 ) -> index  //按位于操作要求是2^N,可快速定位
                new AffinityThreadFactory("aft_core", AffinityStrategies.ANY), //线程池，用于产生消费和生产线程
                ProducerType.SINGLE,
                new BlockingWaitStrategy() //等待策略： 最快到最慢  BusySpinWaitStrategy(吃CPU） -> YieldWait(轮询完成使用后yield让出CPU，等待重新唤醒) -> blockWaiting -> TimeoutBlockingWait
        );
        //指定异常处理器，设置全局异常处理器
        //因为在生产线程 或消费线程的内部加try catch是非常不合适的，应该把异常抛出
        final DisruptorExceptionHandler<RbCmd> exceptionHandler = new DisruptorExceptionHandler<>("disruptor-1", (ex, sequence) ->{
            log.error("Exception thrown on seq={}", sequence, ex);
        });
        disruptor.setDefaultExceptionHandler(exceptionHandler);
        //加入消费线程
        ConsumerA consumerA = new ConsumerA();
        ConsumerB consumerB = new ConsumerB();
        disruptor.handleEventsWith(consumerA).then(consumerB);

        disruptor.start();


        //加入生产线程
        new Timer().schedule(new ProducerTaskA(), 2000, 1000);

    }

    private class ConsumerA implements EventHandler<RbCmd> {

        @Override
        public void onEvent(RbCmd rbCmd, long l, boolean b) throws Exception {
            log.info("ConsumerA recv:{}", rbCmd);
        }
    }

    private class ConsumerB implements EventHandler<RbCmd> {

        @Override
        public void onEvent(RbCmd rbCmd, long l, boolean b) throws Exception {
            log.info("ConsumerB recv:{}", rbCmd);
        }
    }

    private static final EventTranslatorOneArg<RbCmd, RbData> PUB_TRANSLATOR =
            (rbCmd, sequence, rbData) -> {
                    rbCmd.code = rbData.code;
                    rbCmd.msg = rbData.msg;
            };

    private int index = 0;

    private class ProducerTaskA extends TimerTask{

        @Override
        public void run() {
            disruptor.getRingBuffer().publishEvent(PUB_TRANSLATOR, RbData.builder().code(index++).msg("hello world").build());
        }
    }

    public static void main(String[] args) {
        new DisruptorTestMain().initDisruptor();
    }
}
