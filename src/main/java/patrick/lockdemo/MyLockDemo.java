package patrick.lockdemo;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyLockDemo {
    private int value;

    Lock lock = new ReentrantLock();

    public int getNext(){
        lock.lock();
        int a = value ++;
        lock.unlock();
        return a;
    }

    public static void main(String[] args) {
        MyLockDemo lockDemo = new MyLockDemo();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println(Thread.currentThread().getName()+" " + lockDemo.getNext());
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println(Thread.currentThread().getName()+" " + lockDemo.getNext());
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }
}
