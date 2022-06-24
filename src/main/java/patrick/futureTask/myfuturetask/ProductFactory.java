package patrick.futureTask.myfuturetask;

/**
 * 做蛋糕工厂：相当于蛋糕店
 */
public class ProductFactory  {

    public MyFutureTask createProduct(String name){
        MyFutureTask myFutureTask = new MyFutureTask();

        System.out.println("order successfuly");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Product product = new Product(name);
                myFutureTask.setProduct(product);
            }
        }).start();
        return myFutureTask;
    }

}
