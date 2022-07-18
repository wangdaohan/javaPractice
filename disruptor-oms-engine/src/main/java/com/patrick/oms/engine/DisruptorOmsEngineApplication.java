package com.patrick.oms.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 撮合引擎核心：
 * 1。 接收委托广播并校验  - 来自排队机
 * 2。 委托撮合  - 最重要
 * 3。 发布委托/行情广播
 *
 * 1。 排队机和撮合核心的交互协议选择：UDP
 *    优点：UDP轻量/无连接开销（不用3次接手 不用发送心跳请求来保持连接）
 *         UDP比较灵活 - 对于多撮合核心，不用频繁配置排队机
 *         对于丢包率 - 内网极低的丢包率  - 就算有丢包，也会能校验机制，去重新获取被丢的包
 *
 *
 *   2。 订单簿 OrderBook 设计
 *
 *   3。 委托变动/行情的发布  - MQ sub/pub模式
 */
@SpringBootApplication
public class DisruptorOmsEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisruptorOmsEngineApplication.class, args);
    }

}
