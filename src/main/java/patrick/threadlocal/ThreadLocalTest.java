package patrick.threadlocal;


/**
 * ThreadLocal: 作用定义一个ThreadLocal变量，可以给不同的线程使用，即单个线程的局部变量。
 *
 * 比如：thread 1 - 对同一个ThreadLocal<Integer> count +1
 *      thread 2 - 访问count时，还是初始值（0）
 *
 *  作用：
 *
 *
 *
 */
public class ThreadLocalTest {
    private ThreadLocal<Integer> count = new ThreadLocal<Integer>(){
        protected Integer initialValue(){
            return new Integer(0);
        }
    };

    public int getCurrent(){
        return count.get();
    }
    public int getNext(){
        Integer value = count.get();
        value++;
        count.set(value);
        return value;
    }



    public static void main(String[] args) {
        ThreadLocalTest  d = new ThreadLocalTest();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println(Thread.currentThread().getName() +" "+ d.getNext());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println(Thread.currentThread().getName() +" "+ d.getCurrent());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println(Thread.currentThread().getName() +" "+ d.getNext());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }
}
