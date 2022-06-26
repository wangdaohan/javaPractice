package concurrency.arrayblockingqueue.t2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Demo4 implements Callable<Integer> {
    public static void main(String[] args) {
        Demo4 demo4 = new Demo4();
        FutureTask<Integer> task = new FutureTask<>(demo4);
        Thread t = new Thread(task);
        t.start();
        System.out.println("doing others");
        try {
           Integer result= task.get();
           System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("calculating.....");
        Thread.sleep(3000);
        return 12;
    }
}
