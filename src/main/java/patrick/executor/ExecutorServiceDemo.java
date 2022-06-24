package patrick.executor;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Executor 框架： ExecutorService ,
 */
public class ExecutorServiceDemo {
    public static void main(String[] args) {
        //创建10个线程来处理大量的任务
        //ExecutorService executor = Executors.newFixedThreadPool(10);
        //ExecutorService executor = Executors.newCachedThreadPool(); //Cached 即给线程60秒的存活时间，在这时间内是可重复用的，除些之外，以newFixedThreadPool是一样的。
//        AtomicInteger count = new AtomicInteger(0);
//        for(int i =0;i<100;i++){
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println(Thread.currentThread().getName());
//                    count.getAndIncrement();
//                }
//            });
//        }
//
//        executor.shutdown();
//
//        while(Thread.activeCount()>1){
//
//        }
//        System.out.println("ran thread number="+count);


//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10); //Scheduled: 即延时或定时、计划任务
//        while (true) {
//            executor.schedule(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println(Thread.currentThread().getName());
//                }
//            }, 5, TimeUnit.SECONDS);
//        }

        ExecutorService executor = Executors.newWorkStealingPool();  //JDK8:新增，为forkjoinpool




    }
}
