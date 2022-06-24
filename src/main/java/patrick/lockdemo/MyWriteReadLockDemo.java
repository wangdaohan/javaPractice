package patrick.lockdemo;


public class MyWriteReadLockDemo {
    public static void main(String[] args){
        MyWriteReadLock myWriteReadLock = new MyWriteReadLock();
        new Thread(new Runnable() {
            @Override
            public void run() {
                myWriteReadLock.put("b",10);
                myWriteReadLock.put("a",20);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                myWriteReadLock.put("c",10);
                myWriteReadLock.put("d",20);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                myWriteReadLock.put("e",10);
                myWriteReadLock.put("f",20);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                myWriteReadLock.put("g",10);
                myWriteReadLock.put("h",20);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                myWriteReadLock.get("b");
            }
        }).start();
    }
}
