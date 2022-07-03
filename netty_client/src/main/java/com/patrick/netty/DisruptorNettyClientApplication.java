package com.patrick.netty;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
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

		//new YieldingWaitStrategy() 性能最高，但CPU使用率很高
		//new BlockingWaitStrategy  性能最低，但CPU占用最低
		RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI, 1024 * 1024, new YieldingWaitStrategy(), consumers);

		NettyClient client = new NettyClient();
		client.sendData();
	}

}
