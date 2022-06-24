package patrick.lockdemo;

public class MyAQSLockReentrantDemo {
    private int value;
    private MyAQSLockReentrant lock = new MyAQSLockReentrant();

    public int next() {
        lock.lock();
        try{
            Thread.sleep(100);
            return value++;
        }catch (InterruptedException e){
            throw new RuntimeException();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 测试是否为可重入锁
     */
    public void a(){
        lock.lock();
        System.out.println("a");
        b();
        lock.unlock();
    }
    public void b(){
        lock.lock();
        System.out.println("b");
        lock.unlock();
    }

    public static void main(String[] args) {
        MyAQSLockReentrantDemo m = new MyAQSLockReentrantDemo();

        /**
         * 当使用非可重入锁时，调用a()；会造成饥饿状态，类似死锁
         *
         * 解决：实现或使用一个可重入锁
         */
        m.a();


        /**
         * 测试锁的使用
         */

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println(Thread.currentThread().getId()+" - "+m.next());
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println(Thread.currentThread().getId()+" - "+m.next());
                }
            }
        }).start();
    }
}
