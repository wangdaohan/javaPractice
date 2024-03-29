package com.demo.disruptordemo.event;

import com.lmax.disruptor.ExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.function.BiConsumer;

@Log4j2
@AllArgsConstructor
public class DisruptorExceptionHandler<T> implements ExceptionHandler<T> {
    public final String name;
    public final BiConsumer<Throwable, Long> onException;

    @Override
    public void handleEventException(Throwable ex, long sequence, T event) {
        log.debug("Disruptor '{}‘ seq={} exist exception", name, sequence);
//        if(log.isDebugEnabled()) {
//            log.debug("Disruptor '{}‘ seq={} exist exception", name, sequence);
//        }
        onException.accept(ex,sequence);
    }

    @Override
    public void handleOnStartException(Throwable throwable) {
        log.info("Disruptor '{}‘ start exception", name);

    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {
        log.info("Disruptor '{}‘ shutdown exception", name);

    }
}
