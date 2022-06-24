package patrick.semaphore;


import java.util.concurrent.Semaphore;

/**
 * Semaphore: 信号 / 信号标
 *
 * 作用： 可用于流量控制等作用/ 红绿灯控制流量等每次可以过多少人
 *
 * public Semaphore(int permits)
 * 创建具有给定的许可数和非公平的公平设置的 Semaphore。
 * 参数：
 * permits - 初始的可用许可数目。此值可能为负数，在这种情况下，必须在授予任何获取前进行释放。
 *
 */
public class SemaphoreDemo {

    public void method(Semaphore semaphore){
        try {
            System.out.println(Thread.currentThread().getName()+" trying to get permit....");
            //获取Permit许可, 无法获取会一直等待，直到获取到许可
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName()+" got permit....");
        System.out.println(Thread.currentThread().getName()+" is running....");

        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        semaphore.release();
    }


    public static void main(String[] args) {
        SemaphoreDemo semaphoreDemo = new SemaphoreDemo();
        Semaphore semaphore = new Semaphore(2);
        for(int i=0;i<50;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    semaphoreDemo.method(semaphore);
                }
            }).start();
        }
    }
}
