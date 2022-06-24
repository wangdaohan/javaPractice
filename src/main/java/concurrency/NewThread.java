package concurrency;

public class NewThread implements  Runnable {
    public static void main(String[] args) {
        NewThread n = new NewThread();

        Thread thread = new Thread(n);
        thread.start();

        while(true){
            synchronized (n) {
                System.out.println("main thread running...");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                n.notifyAll();
            }
        }
    }

    @Override
    public synchronized void run() {
        while(true){
            try{
                wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("thread running...");
        }
    }
}
