package concurrency.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Service {
    private CountDownLatch latch;
    public Service(CountDownLatch latch) {
        this.latch = latch;
    }

    public void exec() {
        try{
            System.out.println(Thread.currentThread().getName() + " execute task.");
            sleep(2);
            System.out.println(Thread.currentThread().getName() + " completed task.");
        } finally {
            latch.countDown();
        }
    }

    private void sleep(int sec) {
        try{
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
