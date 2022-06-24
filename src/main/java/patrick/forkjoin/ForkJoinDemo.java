package patrick.forkjoin;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Fork/Join: 多线程充分利用多CPU的性能
 *
 * 将重任务：分成几份运行 ，将大任务切成几个小的任务， 再将小的任务切成更小的任务，然后将结果汇总
 *
 * 具体实现子类： RecursiveTask / ForkJoinTask / ForkJoinPool
 * 应用： 如计算1+2+3+...+100
 *c
 * 过程： 将1
 */
public class ForkJoinDemo extends RecursiveTask<Integer> {

    private int begin;
    private int end;

    public ForkJoinDemo(int begin, int end){
        this.begin = begin;
        this.end = end;
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Integer> forkJoinTask = pool.submit(new ForkJoinDemo(1,100000000));
        System.out.println("...");

        try {
            System.out.println("computed value = "+forkJoinTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Integer compute() {
        System.out.println(Thread.currentThread().getName() + "....");  //线程数 应该和CPU数量一致
        //拆分任务

        int sum=0;
        if(end-begin <= 2){
            //可以计算
            for(int i=begin; i<=end; i++){
                sum += i;
            }
        }else{
            //需要拆分
            ForkJoinDemo d1 = new ForkJoinDemo(begin, (begin+end)/2);
            ForkJoinDemo d2 = new ForkJoinDemo((begin+end)/2+1, end);
            d1.fork();
            d2.fork();

            Integer result1 = d1.join();
            Integer result2 = d2.join();

            sum = result1+result2;


        }
        return sum;
    }
}
