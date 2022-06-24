package patrick.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadPoolExecutor: 线程池的原始类，高级封装类/高级应用：ExecutorService
 *
 * 简单线程池的使用：
 *    ThreadPoolExecutor.CallerRunsPolicy  - 当线程池饱和的时候，由主线程去运行那些多余的线程，避免被忽略的情况
 *    ThreadPoolExecutor.DiscardOldestPolicy  - 以下几种策略，都是当线程池饱和的时候，新的线程会忽略
 *    ThreadPoolExecutor.DiscardPolicy
 *    ThreadPoolExecutor.AbortPolicy
 *
 *    问题： 为什么要有直接忽略/抛弃的策略，这样程序不就不可信了吗？ 这取决于你用哪个方法(executor/submit)提交线程任务
 *       1. ThreadPoolExecutor.execute() - 马上运行线程任务
 *       2. ThreadPoolExecutor.submit() - 只提交线程任务，至于什么时候运行，则取决于什么时候有空闲的线程。进行
 *
 */
public class ThreadPoolExecutorDemo {
    public static void main(String[] args) {
        /**
         * corePoolSize: 线程池的初始线程大小
         * maximumPoolSize: 线程池允许创建的最大线程数
         * keepAliveTime / timeunit: 线程存活时间 ，存活时间的单位
           workQueue: 储存提交过来的待运行的线程，用于保存等待执行的任务的阻塞队列
           RejectedExecutionHandler ： 当线程池饱和的时候，选取的策略。
         */
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3,10,10, TimeUnit.DAYS, new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.CallerRunsPolicy());
        AtomicInteger count = new AtomicInteger(0);
        for(int i =0;i<100;i++){
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                    count.getAndIncrement();
                }
            });
        }

        threadPool.shutdown();

        while(Thread.activeCount()>1){

        }
        System.out.println("ran thread number="+count);
    }
}
