package com.patrick.id;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 分布式统一ID生成策略抗压：
 * 	1. 对于ID的生成，在我们日常开发里应该是一个最基本问题，我们先来说说最基础的ID的生成策略
 * 	2。 最简单的是利用java.util.UUID工具类进行生成， ID没有排序策略
 * 	    这种方式的问题就是比如我要查询一批数据，进行入库时间做数据排序的时候，
 * 	    只能够自己在表里设置一个create_time，给这个字段添加索引然后进行排序
 * 	3。 ID生成算法：
 * 			- 单点ID生成服务算法
 * 				   - 雪花算法 / 数据库sequence序列 / 自增ID等
 * 	               - 时间顺序ID生成方式：fasterxml里有一个的TimeBasedGenerator生成的ID是有时间先后顺序的，我们可以使用ID天然进行排序
 *				   - 带业务规则的ID生成方式 - 最好使用带业务含义的ID生成策略，带一些业务意义的前缀
 *			- 高并发下的统一ID生成服务
 *				- 问题：
	 *				- 如何解决ID生成在并发下的重复生成问题
	 *				- 如何承载高并发ID生成的性能瓶颈问题
	 *			- 针对以上2个问题，如何进行落地：
	 *				- 使用zookeeper的分布式锁实现 - 有性能瓶颈，zookeeper对于高QPS超过1000，会有性能瓶颈（特别对于写操作）
	 *				- 使用redis缓存，利用redis分布式锁实现 - 也有性能瓶颈，会有延时（集群特性）
 *				- 正确主流的分布式高并发下的统一ID生成服务
 *					- 实现一：提前加载，也就是预加载机制
 *							核心： 1。 提前加载，也就是预先加载的机制
 *						          2。并发的获取，采用disruptor框架去提升性能
 *					- 实现二：单点生成方式
 *							核心： 1。固定的机器生成唯一ID，好处能做到全局唯一  - NTP问题？
 *								  2。需要相应的业务规则拼接  - 为什么要进行拼接自增序列？ 因为在高并发的场景下，我们的生成ID请求量非常巨大，就会暴露NTP（Network Time Protocol) 的问题
 *								  				比如机器码是：ABK007 当前时间戳：15280189092517 ID生成为：ABK00715280189092517
 *								  				当NTP发生（时间校准）时，机器 的时间会进行更新校准，所以时间戳有可能会回到之前的时间：比如	15280189092517，生成ID重复。
 *								  	解决NTP：后面+自增序列
 *
 *		分布式统一ID生成策略 - 架构设计：
 *				1。定时任务 -> 定时提前预加载和生成ID到guava缓存
 *			    2。	并发的获取，采用disruptor框架去提升性能
 *
 */
@SpringBootApplication
public class IdGenerationApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdGenerationApplication.class, args);
	}

}
