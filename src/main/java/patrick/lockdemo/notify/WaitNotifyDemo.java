package patrick.lockdemo.notify;

public class WaitNotifyDemo {
    /**
     * 实现3个线程交替执行 1，2，3 1，2，3
     *
     * 旧式实现： wait/notifyall 缺点不能指定notify某个线程
     * @param args
     */

    private int signal;

    public synchronized void a(){
        while(signal != 0){
            try{
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("a");
        signal ++;
        notifyAll();
    }

    public synchronized void b(){
        while(signal != 1){
            try{
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("b");
        signal ++;
        notifyAll();
    }
    public synchronized  void c(){
        while(signal != 2){
            try{
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("c");
        signal = 0;
        notifyAll();
    }

    public static void main(String[] args) {
        WaitNotifyDemo demo = new WaitNotifyDemo();
        A1 a = new A1(demo);
        B1 b = new B1(demo);
        C1 c = new C1(demo);
        new Thread(a).start();
        new Thread(b).start();
        new Thread(c).start();
    }
}

class A1 implements Runnable{
    private WaitNotifyDemo demo;

    public A1(WaitNotifyDemo demo){
        this.demo = demo;
    }
    @Override
    public void run() {
        while(true){
            demo.a();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class B1 implements Runnable{
    private WaitNotifyDemo demo;

    public B1(WaitNotifyDemo demo){
        this.demo = demo;
    }
    @Override
    public void run() {
        while(true){
            demo.b();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class C1 implements Runnable{
    private WaitNotifyDemo demo;

    public C1(WaitNotifyDemo demo){
        this.demo = demo;
    }
    @Override
    public void run() {
        while(true){
            demo.c();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

