package concurrency.arrayblockingqueue;

import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ArrayBlockingQueueTesting {
    /**
     * ArrayBlockingQueue，由数组结构组成的有界阻塞队列；
     */
//    private final static ArrayBlockingQueue<Apple> queue = new ArrayBlockingQueue<>(1);
    //公平阻塞队列
    private final static ArrayBlockingQueue<Apple> queue = new ArrayBlockingQueue<>(2,true);

    public static void main(String[] args) {

        new Thread(new Producer(queue)).start();
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
        LinkedBlockingQueue test ;
        ArrayBlockingQueue test2;
        LinkedList l;
    }
}


class Producer implements Runnable {
    private final ArrayBlockingQueue<Apple> mAbq;
    Producer(ArrayBlockingQueue<Apple> arrayBlockingQueue){
        this.mAbq = arrayBlockingQueue;
    }
    @Override
    public void run() {
        while (true){
            produce();

        }
    }
    public void produce(){
        try {
            Apple apple = new Apple();
            mAbq.put(apple);//队列满则阻塞
            System.out.println(Thread.currentThread().getName()+"生产:"+apple);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private ArrayBlockingQueue<Apple> mAbq;
    Consumer(ArrayBlockingQueue<Apple> arrayBlockingQueue){
        this.mAbq = arrayBlockingQueue;
    }
    @Override
    public void run() {
        while (true){
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void consume()throws InterruptedException{
        Apple apple = mAbq.take();//队列空则阻塞
        System.out.println("消费Apple="+apple);
    }
}

class Apple {
    public Apple() {

    }
}