package patrick.exchanger;


import java.util.concurrent.Exchanger;

/**
 * Exchanger: 共享/交换不同线程之间的值，可用于不同线程之间的结果对比 / 数据交互等用途
 *
 */
public class ExchangerDemo {
    public void a(Exchanger<String> exch){
        System.out.println("a running...");
        try{

            System.out.println("a getting data....");
            Thread.sleep(4000);
            System.out.println("a got data....");
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        String res="12345";
        try{
            exch.exchange(res);
            System.out.println("waiting comparing....");
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    public void b(Exchanger<String> exch){
        System.out.println("b running...");
        try{

            System.out.println("b getting data....");
            Thread.sleep(4000);
            System.out.println("b got data....");
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        String res="123";
        try{
            String value = exch.exchange(res);
            System.out.println("start comparing....");
            System.out.println("result: "+value.equals(res));
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExchangerDemo exchangerDemo = new ExchangerDemo();
        Exchanger<String> exchanger = new Exchanger<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                exchangerDemo.a(exchanger);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                exchangerDemo.b(exchanger);
            }
        }).start();
    }
}
