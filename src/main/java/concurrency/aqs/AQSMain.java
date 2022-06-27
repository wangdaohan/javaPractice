package concurrency.aqs;


import java.util.concurrent.locks.LockSupport;

/**
 * AQS锁
 *   1。ReentrantLock重入锁 - Condition / Lock
 *   2。ReentrantReadWriteLock读写锁 -
 *   3. Condition条件判断
 *   4。LockSupport基于线程的锁 -
 *
 *
 *
 * AQS架构：AbstractQueuedSynchronizer
 *      核心
 *          1。 一个共享的资源 state (volatile修饰）
 *          2。一个先进先出的线程等待队列，头部节点代表拿到共享资源，其它节点在队列中等待
 *
 *      AQS定义2种资源共享方法：
 *          1. exclusive(独占）
 *              具体AQS的实现：ReentrantLock
 *          2. share（共享）
 *              具体AQS的实现：Semaphore , CountDownLatch
 *      关键方法
 *          1。tryAcquire / tryRelease 独占的方式尝试获取和释放资源
 *          2。tryAcquireShared / tryReleaseShared 共享方式尝试获取和释放资源
 *
 *      具体AQS的实现 - ReentrantLock代码分析
 *          1。 state初始化为0，代表未锁定状态
 *          2。 A线程lock()的时候，会调用tryAcquire()独占该锁，并将state+1
 *          3. 此后，其它线程再tryAcquire()时就会失败，直到A线程unlock()到state=0为止。
 *             这时，其它线程才有机会获取锁
 *          4。 当然，A线程在释放锁之前，A线程自己是可以重复获取这个锁的，这时state会累加，这就是可重入的概念。
 *          5。 注意，同一个线程获取多少次锁，那么释放的时候也要释放同样多次，这样才能保证state能回到0。
 */
public class AQSMain {
    public static void main(String[] args) throws InterruptedException {

        /**
         * 2.LockSupport的锁
         *    优点： park() / unpark()可以不分先后顺序
         *         传统的wait()/notify()，一定是需要先等wait()执行了，才能用notify()唤醒
         */
        Thread A = new Thread(new Runnable() {
            @Override
            public void run() {
                int sum = 0;
                for(int i=0; i < 10; i++) {
                    sum += i;
                }
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //后于unpark()执行
                LockSupport.park();//挂起阻塞,wait();
                System.out.println("sum:"+sum);
            }
        });

        A.start();

        Thread.sleep(1000);
        System.out.println("notify print sum result");
        //先于unpark()执行
        LockSupport.unpark(A);

        /**
         * 1.传统的object 锁
         */
        Object lock = new Object();

//        Thread A = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int sum = 0;
//                for(int i=0; i < 10; i++) {
//                    sum += i;
//                }
//                synchronized (lock) {
//                    try{
//                        lock.wait(); // wait(); 释放锁
//                    } catch (InterruptedException exception) {
//                        exception.printStackTrace();
//                    }
//                }
//                System.out.println("sum:"+sum);
//            }
//        });
//
//        A.start();
//
//        Thread.sleep(2000);
//        System.out.println("notify print sum result");
//        synchronized (lock) {
//            lock.notify();  //notify();不释放锁
//        }

    }
}
