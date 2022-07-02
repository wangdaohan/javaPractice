package disruptor.advance.a5;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.dsl.Disruptor;
import jdk.internal.vm.annotation.Contended;

/**
 * Disruptor底层源码分析
 *  1.Disruptor为何底层性能如此强大
 *      1.1 高性能之道 - 数据结构 - 内存预加载机制    ->    使用环形结构、数组、内存预加载
 *      1.2 高性能之道 - 内核 - 使用单线程写         ->    使用单线程方式、内存屏障
 *      1.3 高性能之道 - 系统内存优化 - 内存屏障
 *      1.4 高性能之道 - 系统缓存优化 - 消除伪共享    ->    消除伪共享（填充缓存行）
 *      1.5 高性能之道 - 算法优化 - 序号栅栏机制      ->    序号栅栏和序号配合使用来消除锁和CAS
 *      1.6 获取下一个可用序列号
 *
 *
 *    1.1 高性能之道 - 数据结构 - 内存预加载机制    ->    使用环形结构、数组、内存预加载
 *        -- RingBuffer使用数组Object[] entries作为存储元素
 *        -- 内存预加载： RingBuffer初始化时，会将所有event类实例化（初始化）并放在数组中
 *        --
 *
 */
public class DisruptorMain {
    /**1.1 高性能之道 - 数据结构 - 内存预加载机制    ->    使用环形结构、数组、内存预加载
     *     RingBuffer源码分析
     */
    RingBuffer<Object> ringBuffer;
    //RingBufferFields ringBufferFields;

    /**1.2 高性能之道 - 内核 - 使用单线程写         ->
     *    a. 使用单线程方式
     *
     *    Disruptor的ringbuffer，之所以可以做到完全无锁，也是因为“单线程”写，这是所有前提的前提
     *
     *    Redis, Netty等高性能技术框架的设计核心都是同理，因为单线程，才能做到无锁，从而实现高性能，高并发
     *         好比： 一个幼儿园老师管理一个孩子（单线程）
     *               一个幼儿园老师管理多个孩子（多线程）
     */

    /**
     *1.3 高性能之道 - 系统内存优化 - 内存屏障
     *      系统内存优化  - 内存屏障(valotile / happens before语义）
     */

    /**
     *1.4 高性能之道 - 系统缓存优化 - 消除伪共享    ->    消除伪共享(false sharing)（填充缓存行）
     *    背景：缓存系统中是以缓存行(cache line) 为单位存储的
     *         缓存行是2的整数幂个连续字节，一般为32-256个字节，
     *         最常见的缓存行大小是64个字节
     *
     *  当有多线程修改互相独立的变量时，如果这些变量共享同一个缓存行，就会无意中影响彼此的性能， 这就是伪共享 （无声的性能杀手）
     *
     *  Disruptor如何消除伪共享(false sharing)呢？
     *     1。比如Disruptor中的Sequence就实现的伪共享的消除
     *         Sequence的value（long 占8个字节）继承自RhsPadding 代表缓存行中右(Right)边数据， RhsPadding中的剩下7个Long类型(p9, p10, p11, p12, p13, p14, p15)
     *                                        继承自LhsPadding 代表缓存行中左(Left)边数据， LhsPadding中的剩下7个Long类型(p1, p2, p3, p4, p5, p6, p7)
     *         - 换句话说我们每个sequence的值 一定是占用一整个缓存行的（ 1 +7） （ 8 * 8 = 64字节）
     *              缓存行示意图：假设存储Long数据 类型（8个字节， 64/8=8刚好放8个Long值）
     *              |p1|p2|p3|p4|p5|p6|p7|p8|
     *          - RhsPadding + LhsPadding是为了保证变量确保不跟前面或后面的变量在一个相同的缓存行上。
     *          - 这样多个线程在访问不同变量时，因为在不同的缓存行，因此不会造成竞争和性能损失
     *          - 这本质是一种空间换时间的做法。
     *
     *     2。 JAVA原生代码中也有类似的实现  - @Contended （添加后默认不生效，必须jvm启动十加 -XX:-RestrictContended）
     *                                   -并发包下很多也用到了类似技术 CounterCell
     */
    //@Contended
      Sequence sequence;

    /**
     *     1.5 高性能之道 - 算法优化 - 序号栅栏机制      ->    序号栅栏和序号配合使用来消除锁和CAS
     *          我们在生产者进行投递Event的时候，总是会使用：ringBuffer.next()获取下一个可用序列号；
     *
     *      在Disruptor3.0中，序号栅栏SequenceBarrier和序号Sequence搭配使用，主要用于协调和管理消费者与生产者的工作节奏，避免了去使用锁和CAS
     *                       各个消费者和生产者都持有自己的序号，这些序号的变化必须满足以下基本条件：
     *                           a. 消费者持有的序号必须小于生产者的序号
     *                           b. 消费者序号的数值必须小于其前置（依赖关系）消费者的序号数值
     *                           c. 生产者序号数值不能大于消费者中最小的序号数值  - 以避免生产者速度过快，将还未来得及消费的消息覆盖
     *
     */
    RingBuffer<Object> testRingBuffer2;
    //testRingBuffer2.next();  -》SingleProducerSequencer


}
