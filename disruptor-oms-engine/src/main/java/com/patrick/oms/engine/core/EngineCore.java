package com.patrick.oms.engine.core;

import com.client.bean.order.CmdType;
import com.client.bean.order.OrderCmd;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.patrick.oms.engine.bean.RbCmd;
import com.patrick.oms.engine.bean.RbCmdFactory;
import com.patrick.oms.engine.handler.BaseHandler;
import com.patrick.oms.engine.handler.exception.DisruptorExceptionHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityThreadFactory;

import java.util.Timer;
import java.util.TimerTask;

import static com.patrick.oms.engine.handler.pub.L1PubHandler.HQ_PUB_RATE;

@Log4j2
public class EngineCore {
    private static final int RING_BUFFER_SIZE = 1024;
    private final Disruptor<RbCmd> disruptor;

    @Getter
    private final EngineApi api;

    public EngineCore(
            @NonNull final BaseHandler riskHandler,
            @NonNull final BaseHandler matchHandler,
            @NonNull final BaseHandler pubHandler
    ) {
        this.disruptor = new Disruptor<RbCmd>(
                 new RbCmdFactory(),
                RING_BUFFER_SIZE,
                new AffinityThreadFactory("aft_engine_core", AffinityStrategies.ANY),
                ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );

        this.api = new EngineApi(disruptor.getRingBuffer());

        //1. 设置全局异常处理器
        final DisruptorExceptionHandler<RbCmd> exceptionHandler = new DisruptorExceptionHandler<>(
                "main",
                (ex, seq) -> {
                    log.error("exception thrown on seq={}, ",seq,ex);
                }
        );
        disruptor.setDefaultExceptionHandler(exceptionHandler);

        //2. 前置风控handler -> 撮合handler -> 发布数据 handler
        disruptor.handleEventsWith(riskHandler).then(matchHandler).then(pubHandler);

        //3。启动disruptor
        disruptor.start();
        log.info(("match engine start"));

        //4. producer - 1. 定时发布行情数据  2.排队机过来的委托订单数据
        //4.1 定时发布行情数据
        new Timer().schedule(new HqPubTask(), 1000, HQ_PUB_RATE);

    }

    private class HqPubTask extends TimerTask {

        @Override
        public void run() {
            api.submitCommand(OrderCmd.builder().type(CmdType.HQ_PUB).build());
        }
    }
}
