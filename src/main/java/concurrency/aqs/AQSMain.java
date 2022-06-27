package concurrency.aqs;


import java.util.concurrent.locks.LockSupport;

/**
 * AQS��
 *   1��ReentrantLock������ - Condition / Lock
 *   2��ReentrantReadWriteLock��д�� -
 *   3. Condition�����ж�
 *   4��LockSupport�����̵߳��� -
 *
 *
 *
 * AQS�ܹ���AbstractQueuedSynchronizer
 *      ����
 *          1�� һ���������Դ state (volatile���Σ�
 *          2��һ���Ƚ��ȳ����̵߳ȴ����У�ͷ���ڵ�����õ�������Դ�������ڵ��ڶ����еȴ�
 *
 *      AQS����2����Դ��������
 *          1. exclusive(��ռ��
 *              ����AQS��ʵ�֣�ReentrantLock
 *          2. share������
 *              ����AQS��ʵ�֣�Semaphore , CountDownLatch
 *      �ؼ�����
 *          1��tryAcquire / tryRelease ��ռ�ķ�ʽ���Ի�ȡ���ͷ���Դ
 *          2��tryAcquireShared / tryReleaseShared ����ʽ���Ի�ȡ���ͷ���Դ
 *
 *      ����AQS��ʵ�� - ReentrantLock�������
 *          1�� state��ʼ��Ϊ0������δ����״̬
 *          2�� A�߳�lock()��ʱ�򣬻����tryAcquire()��ռ����������state+1
 *          3. �˺������߳���tryAcquire()ʱ�ͻ�ʧ�ܣ�ֱ��A�߳�unlock()��state=0Ϊֹ��
 *             ��ʱ�������̲߳��л����ȡ��
 *          4�� ��Ȼ��A�߳����ͷ���֮ǰ��A�߳��Լ��ǿ����ظ���ȡ������ģ���ʱstate���ۼӣ�����ǿ�����ĸ��
 *          5�� ע�⣬ͬһ���̻߳�ȡ���ٴ�������ô�ͷŵ�ʱ��ҲҪ�ͷ�ͬ����Σ��������ܱ�֤state�ܻص�0��
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
