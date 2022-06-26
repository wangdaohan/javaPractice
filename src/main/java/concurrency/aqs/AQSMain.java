package concurrency.aqs;


import java.util.concurrent.locks.LockSupport;

/**
 * AQS锁
 *   1。ReentrantLock重入锁 - Condition / Lock
 *   2。ReentrantReadWriteLock读写锁 -
 *   3. Condition条件判断
 *   4。LockSupport基于线程的锁 -
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
