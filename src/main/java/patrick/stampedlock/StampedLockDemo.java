package patrick.stampedlock;

import java.util.concurrent.locks.StampedLock;

/**
 *   StampedLock  用于增强ReadAndWriteLock（读写锁，在高并发环境下，如果写线程多，读线程少，会导致写线程饥饿问题），
 *
 *   导致读写锁问题原因：
 *      1.读-写锁是互斥的，写-写锁也是互斥的
 *      2.
 *
 *   StampedLock
 *      1. 读锁 - 写锁 不互斥  -》 新问题：要保证读写一致性
 */
public class StampedLockDemo {
    private int balance;
    private StampedLock stampedLock = new StampedLock();

    public void optimisticRead(){
        long stamp = stampedLock.tryOptimisticRead();

        int c = balance;

        //这里可能会出现写操作，因些要进行判断
        if(!stampedLock.validate(stamp)){
            //要重新 读锁
            long readStamp = stampedLock.readLock();
            c = balance;
            stamp = readStamp;
        }

        stampedLock.unlockRead(stamp);
    }

    public void read(){
        long stamp = stampedLock.readLock();
        int c = balance;
        stampedLock.unlockRead(stamp);
    }

    public void write(int value){
        long stamp = stampedLock.writeLock();
        balance += value;
        stampedLock.unlockWrite(stamp);
    }

    public static void main(String[] args) {

    }
}
