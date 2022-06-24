package concurrency.t2;

public class Demo2 implements Runnable {
    public static void main(String[] args) {
        
    }

    @Override
    public void run() {
        while(true){
            System.out.println("threading running...");
        }
    }
}
