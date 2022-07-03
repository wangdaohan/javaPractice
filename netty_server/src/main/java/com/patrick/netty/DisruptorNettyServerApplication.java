package com.patrick.netty;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.patrick.netty.common.disruptor.MessageConsumer;
import com.patrick.netty.common.disruptor.RingBufferWorkerPoolFactory;
import com.patrick.netty.server.MessageConsumerImplForServer;
import com.patrick.netty.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DisruptorNettyServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisruptorNettyServerApplication.class, args);
		MessageConsumer[] consumers = new MessageConsumer[4];
		for(int i=0;i<consumers.length;i++) {
			consumers[i] = new MessageConsumerImplForServer("code:server:consumer:"+i);
		}
		//new YieldingWaitStrategy() 性能最高，但CPU使用率很高
		//new BlockingWaitStrategy  性能最低，但CPU占用最低
		RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI, 1024 * 1024, new BlockingWaitStrategy(), consumers);
		new NettyServer();

	}

}
