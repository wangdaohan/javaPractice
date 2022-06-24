package patrick.lockdemo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyAQSLockNonReentrant implements Lock {

    private Helper helper = new Helper();

    @Override
    public void lock() {
        helper.acquire(1);

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        helper.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return helper.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return helper.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        helper.release(1);
    }

    @Override
    public Condition newCondition() {
        return helper.newCondition();
    }

    /***
     *
     * AbstractQueuedSynchronizer: 独占
     */
    private class Helper extends AbstractQueuedSynchronizer{
        @Override
        protected boolean tryAcquire(int arg){
            /**
             * 如果第一个线程出来，可以拿到锁，因此我们可以返回 true
             * 如果第二个线程出来，拿不到锁，返回false
             *
             * 如何判断第一个、第二个线程 -> state
             */
            int state = getState();
            if(state==0){
                if(compareAndSetState(0, arg)){
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg){
            /**
             * 锁的获取和释放肯定是一一对应的，那么调用此方法的线程一定要是当前线程
             */
            if(Thread.currentThread() != getExclusiveOwnerThread()){
                throw new RuntimeException();
            }
            int state = getState() - arg;
            boolean flag = false;

            if(state == 0){
                setExclusiveOwnerThread(null);
                flag = true;
            }

            setState(state);


            return flag;
        }

        Condition newCondition(){
            return new ConditionObject();
        }
    }
}
