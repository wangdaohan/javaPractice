package patrick.futureTask.myfuturetask;

/**
 * FutureTask的简单实现
 */
public class MyFutureTask {
    private Product product;
    private Boolean done = false;

    public synchronized Product getProduct(){
        while(!done){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.product;
    }

    public synchronized void setProduct(Product product){
        if(done){
            return;
        }
        this.product = product;
        this.done = true;
        notifyAll();
    }
}
