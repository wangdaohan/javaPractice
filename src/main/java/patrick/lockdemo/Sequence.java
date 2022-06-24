package patrick.lockdemo;

public class Sequence {
    private MyLock lock = new MyLock();
    private  int value;

    public int getNext(){
        lock.lock();
        int a = value ++;
        lock.unlock();
        return a;

    }
    public static void main(String[] args) {
        Sequence s = new Sequence();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println(Thread.currentThread().getName()+" " + s.getNext());
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println(Thread.currentThread().getName()+" " + s.getNext());
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
}
