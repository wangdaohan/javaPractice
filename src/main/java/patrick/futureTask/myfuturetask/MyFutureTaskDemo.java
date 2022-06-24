package patrick.futureTask.myfuturetask;

import java.util.concurrent.Future;

public class MyFutureTaskDemo {
    public static void main(String[] args) {
        ProductFactory pf = new ProductFactory();

        //order
        MyFutureTask f = pf.createProduct("Cake");

        System.out.println("I'm going to work, will take the cake");
        // get product
        System.out.println("I take my Cake backing home"+f.getProduct());

    }
}
