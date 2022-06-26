package concurrency.aqs;


import java.util.concurrent.locks.LockSupport;

/**
 * AQS��
 *   1��ReentrantLock������ - Condition / Lock
 *   2��ReentrantReadWriteLock��д�� -
 *   3. Condition�����ж�
 *   4��LockSupport�����̵߳��� -
 */
public class AQSMain {
    public static void main(String[] args) throws InterruptedException {

        /**
         * 2.LockSupport����
         *    �ŵ㣺 park() / unpark()���Բ����Ⱥ�˳��
         *         ��ͳ��wait()/notify()��һ������Ҫ�ȵ�wait()ִ���ˣ�������notify()����
         */
        Thread A = new Thread(new Runnable() {
            @Override
            public void run() {
                int sum = 0;
                for(int i=0; i < 10; i++) {
                    sum += i;
                }
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //����unpark()ִ��
                LockSupport.park();//��������,wait();
                System.out.println("sum:"+sum);
            }
        });

        A.start();

        Thread.sleep(1000);
        System.out.println("notify print sum result");
        //����unpark()ִ��
        LockSupport.unpark(A);

        /**
         * 1.��ͳ��object ��
         */
        Object lock = new Object();

//        Thread A = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int sum = 0;
//                for(int i=0; i < 10; i++) {
//                    sum += i;
//                }
//                synchronized (lock) {
//                    try{
//                        lock.wait(); // wait(); �ͷ���
//                    } catch (InterruptedException exception) {
//                        exception.printStackTrace();
//                    }
//                }
//                System.out.println("sum:"+sum);
//            }
//        });
//
//        A.start();
//
//        Thread.sleep(2000);
//        System.out.println("notify print sum result");
//        synchronized (lock) {
//            lock.notify();  //notify();���ͷ���
//        }

    }
}
