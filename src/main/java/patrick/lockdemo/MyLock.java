package patrick.lockdemo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


/**
 * 默认实现为不可重入锁
 */
public class MyLock implements Lock {
    public static void main(String[] args) {

    }

    private AtomicBoolean isLocked =new AtomicBoolean(false);

    Thread lockBy = null;
    int lockCount=0;

    @Override
    public synchronized void lock() {

        /**
         * 以下实现为 不可重入锁
         */
//        while(isLocked.get()){
//            try{
//                wait();
//            }catch (InterruptedException e){
//                e.printStackTrace();
//            }
//            isLocked.compareAndSet(false,true);
//        }

        /**
         * 可重入锁实现：
         * */
        Thread currentThread = Thread.currentThread();
        while(isLocked.get() && currentThread!= lockBy){
            try{
                wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            isLocked.compareAndSet(false,true);
            lockBy = currentThread;
            lockCount++;
        }

    }
    @Override
    public synchronized void unlock() {
        /**
         * 可重入锁实现：
         * */
        if(lockBy == Thread.currentThread()){
            lockCount --;
            if(lockCount==0) {
                isLocked.compareAndSet(true, false);
                notify();
            }
        }

        /**
         * 以下实现为 不可重入锁
         */
//        isLocked.compareAndSet(true,false);
//        notify();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }


    @Override
    public Condition newCondition() {
        return null;
    }
}
