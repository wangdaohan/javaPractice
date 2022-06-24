package patrick.lockdemo.notify;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo {
    /**
     * 实现3个线程交替执行 1，2，3 1，2，3
     *
     * 新式使用condition实现
     * @param args
     */

    private int signal;
    Lock lock = new ReentrantLock();
    Condition a = lock.newCondition();
    Condition b = lock.newCondition();
    Condition c = lock.newCondition();

    public void a(){
        lock.lock();
        while(signal != 0){
            try{
                a.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("a");
        signal ++;
        b.signal();
        lock.unlock();
    }

    public void b(){
        lock.lock();
        while(signal != 1){
            try{
                b.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("b");
        signal ++;
        c.signal();
        lock.unlock();
    }
    public  void c(){
        lock.lock();
        while(signal != 2){
            try{
                c.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("c");
        signal = 0;
        a.signal();
        lock.unlock();
    }

    public static void main(String[] args) {
        ConditionDemo demo = new ConditionDemo();
        A a = new A(demo);
        B b = new B(demo);
        C c = new C(demo);
        new Thread(a).start();
        new Thread(b).start();
        new Thread(c).start();
    }
}

class A implements Runnable{
    private ConditionDemo demo;

    public A(ConditionDemo demo){
        this.demo = demo;
    }
    @Override
    public void run() {
        while(true){
            demo.a();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class B implements Runnable{
    private ConditionDemo demo;

    public B(ConditionDemo demo){
        this.demo = demo;
    }
    @Override
    public void run() {
        while(true){
            demo.b();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class C implements Runnable{
    private ConditionDemo demo;

    public C(ConditionDemo demo){
        this.demo = demo;
    }
    @Override
    public void run() {
        while(true){
            demo.c();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

