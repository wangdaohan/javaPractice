package com.patrick.netty;

import com.lmax.disruptor.BlockingWaitStrategy;
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
		RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.SINGLE, 1024, new BlockingWaitStrategy(), consumers);
		new NettyServer();

	}

}
