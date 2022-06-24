package patrick.futureTask.myfuturetask;

public class Product {
    private String name;

    public Product(String name){
        System.out.println("start making "+name);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Made a "+name);
    }

}
