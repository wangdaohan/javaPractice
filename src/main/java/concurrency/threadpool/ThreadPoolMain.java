package concurrency.threadpool;

import java.util.concurrent.*;

/**
 * Executors工厂类：
 *    1。不要使用Executors里面的创建线程池的方法 -> newCachedThreadPool / newFixedThreadPool -> 因为没有界限限制(线程池数量，线程队列长度无限制)，都存在安全隐患 -> 如：newCachedThreadPool/newFixedThreadPool，高并发是线程池撑爆了
 *        newCachedThreadPool实现： new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());  -> a. 线程池的maximumPoolSize=Integer.MAX_VALUE-》高并发是线程池容易撑爆
 *                                                                                                                                           b. 而且使用new SynchronousQueue<Runnable>()对新任务无任何存储，每来一个任务就创建一个新线程
 *        newFixedThreadPool实现： new ThreadPoolExecutor(nThreads, nThreads,  0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()); -> a. 确实是限制了最大和最小线程数量，但其用的LinkedBlockingQueue存储新任务请求，同时没有设置上限，
 *                                                                                                                                                  当海量的新任务过来时，容易把LinkedBlockingQueue撑爆内存
 *
 *     2. 解决： 用ThreadPoolExecutor去自定义线程池 ->
 *
 *     3. 如何设置合理线程池大小 -> 计算（CPU）密集型 与 IO密集型
 *                 计算（CPU）密集型 ——》线程池大小： CPU + 1
 *                 IO密集型(数据读取，存储，数据库, 特点：速度较慢， 线程池应稍大一点) -》线程池大小： CPU / （1 - 阻塞系数）   ——阻塞系数： 0。8-0。9之间  （ 8/（1-0。8） = 40
 *
 *      4。 如何正确使用线程池 -》以上3个点
 */
public class ThreadPoolMain {
    public static void main(String[] args){
        Executors.newFixedThreadPool(1);
        new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),//int corePoolSize, -> 如何设置合理线程池大小,
            Runtime.getRuntime().availableProcessors() * 2,//int maximumPoolSize,
            60,//long keepAliveTime,
            TimeUnit.SECONDS,//TimeUnit unit,
            new ArrayBlockingQueue<>(200),//BlockingQueue<Runnable> workQueue,
            new ThreadFactory(){
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("order-thread");
                    return t;
                }
            },//ThreadFactory threadFactory
            new ThreadPoolExecutor.DiscardPolicy()
        );
    }
}
