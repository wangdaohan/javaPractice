package concurrency.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch中count down是倒数的意思，latch则是门闩的含义。 作为AQS的共享锁的实现之一。
 * 整体含义可以理解为倒数的门栓，似乎有一点“三二一，芝麻开门”的感觉。
 *
 * 构造CountDownLatch的时候需要传入一个整数n，在这个整数“倒数”到0之前，主线程需要等待在门口，
 * 而这个“倒数”过程则是由各个执行线程驱动的，每个线程执行完一个任务“倒数”一次。
 * 总结来说，CountDownLatch的作用就是等待其他的线程都执行完任务，必要时可以对各个任务的执行结果进行汇总，然后主线程才继续往下执行。
 *
 * CountDownLatch主要有两个方法：countDown()和await()。
 *  countDown()方法用于使计数器减一，其一般是执行任务的线程调用，
 *  await()方法则使调用该方法的线程处于等待状态，其一般是主线程调用。
 *
 *  这里需要注意的是，
 *      countDown()方法并没有规定一个线程只能调用一次，当同一个线程调用多次countDown()方法时，每次都会使计数器减一；
 *      await()方法也并没有规定只能有一个线程执行该方法，如果多个线程同时执行await()方法，那么这几个线程都将处于等待状态，并且以共享模式享有同一个锁。
 *
 *  CountDownLatch的底层实现：
 *      任务分为N个子线程去执行，state也初始化为N。
 *      这N个子线程是并行执行的，每个子线程执行完后countDown() - 1 , state会CAS减1
 *      等到所有子线程执行完毕后（即state=0）,会unpark()调用线程
 *      然后主调用线程就会从await()函数返回，继续后面的操作
 *
 *
 *
 *
 *
 */
public class CountdownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        Service service = new Service(latch);
        Runnable task = () -> service.exec();

        for (int i=0; i < 5; i++) {
            Thread thread = new Thread(task);
            thread.start();
        }

        System.out.println("main thread await");
        latch.await();
        System.out.println("main thread completed.");
    }
}
