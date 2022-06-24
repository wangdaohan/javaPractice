package patrick.futureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * future task:
 *
 * 实例
 */
public class FutureTaskDemo {



    public static void main(String[] args) {
        Callable<Integer> call = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("calculating....");
                Thread.sleep(3000);
                return 1;
            }
        };

        FutureTask<Integer> task = new FutureTask<>(call);

        Thread thread = new Thread(task);
        thread.start();


        //do somethinkg
        System.out.println("doing something else...");
        Integer reuslt = null;
        try {
            reuslt = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("getting calculated resutl ="+reuslt);

    }
}
