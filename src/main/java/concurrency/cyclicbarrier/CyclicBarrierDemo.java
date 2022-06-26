package concurrency.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    static class TaskThread extends Thread {
        CyclicBarrier barrier;
        public TaskThread(CyclicBarrier cyclicBarrier) {
            this.barrier = cyclicBarrier;
        }
        @Override
        public void run(){
            try {
                Thread.sleep(1000);
                System.out.println(getName() + " arrived barrier A");
                barrier.await();
                System.out.println(getName() + " breach barrier A");

//                Thread.sleep(2000);
//                System.out.println(getName() + " arrived barrier B");
//                barrier.await();
//                System.out.println(getName() + " breach barrier B");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        int threadNum = 5;
        CyclicBarrier barrier = new CyclicBarrier(threadNum, new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "  finish last task");
            }
        });
        for(int i=0; i<threadNum;i++) {
            new TaskThread(barrier).start();
        }
    }
}
