package com.patrick.disruptoromsraftseq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 排队机开发
 * 终端 -> 柜台 -》 委托网关 -》 排队机（对网关的委托信息进行：收集，存储，定序） -》广播 -》 撮合核心
 *
 * 排队机功能点：
 * 1。Key-value store
 * 2. 收集委托
 * 3。定序
 * 4。 UDP-NACK Multi Cast(TODO) - 广播
 *
 */
@SpringBootApplication
public class DisruptorOmsRaftSeqApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisruptorOmsRaftSeqApplication.class, args);
	}

}
