package patrick.lockdemo;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁：既有排他锁(写锁）,也有共享锁（读锁）
 *
 * AQS,Lock都是排他锁
 *
 */
public class MyWriteReadLock {
    private Map<String, Object> map = new HashMap<>();
    private ReadWriteLock rwl = new ReentrantReadWriteLock();
    private Lock readLock = rwl.readLock();
    private Lock writeLock = rwl.writeLock();
    public Object get(String key){
        readLock.lock();
        System.out.println(Thread.currentThread().getName() + " reading ");
        try{
            Thread.sleep(1000);
            return map.get(key);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        } finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + " reading done");
        }
    }

    public void put(String key, Object v){
        writeLock.lock();

        System.out.println(Thread.currentThread().getName() + " writing ");
        try{
            Thread.sleep(1000);
            map.put(key,v);
        } catch (InterruptedException e) {

            throw new RuntimeException();
        } finally {
            writeLock.unlock();

            System.out.println(Thread.currentThread().getName() + " writing done");
        }
    }
}
