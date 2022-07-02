package disruptor_netty;

/**
 * 1. 实战应用业务场景 - disruptor与Netty实现百万级场景 链接 接入
 *    与netty网络通信框架整合提升性能
 *    1。 在使用netty进行接收处理数据的时候，尽量都不要在工作线程上全编写自己或业务的代码逻辑
 *    2。 我们需要利用异步的机制，比如使用线程池异步处理，如果使用线程池就意味者使用阻塞队列，这里线程池可以替换为disruptor来提高性能
 */
public class NettyClientMain {
}
