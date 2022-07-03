package com.patrick.netty;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.patrick.netty.client.MessageConsumerImplForClient;
import com.patrick.netty.client.NettyClient;
import com.patrick.netty.common.disruptor.MessageConsumer;
import com.patrick.netty.common.disruptor.RingBufferWorkerPoolFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DisruptorNettyClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisruptorNettyClientApplication.class, args);

		MessageConsumer[] consumers = new MessageConsumer[4];
		for(int i=0;i<consumers.length;i++) {
			consumers[i] = new MessageConsumerImplForClient("code:client:consumer:"+i);
		}
		RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.SINGLE, 1024, new BlockingWaitStrategy(), consumers);

		NettyClient client = new NettyClient();
		client.sendData();
	}

}
