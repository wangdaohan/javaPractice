package concurrency.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch��count down�ǵ�������˼��latch�������ŵĺ��塣
 * ���庬��������Ϊ��������˨���ƺ���һ�㡰����һ��֥�鿪�š��ĸо���
 *
 * ����CountDownLatch��ʱ����Ҫ����һ������n���������������������0֮ǰ�����߳���Ҫ�ȴ����ſڣ�
 * ����������������������ɸ���ִ���߳������ģ�ÿ���߳�ִ����һ�����񡰵�����һ�Ρ�
 * �ܽ���˵��CountDownLatch�����þ��ǵȴ��������̶߳�ִ�������񣬱�Ҫʱ���ԶԸ��������ִ�н�����л��ܣ�Ȼ�����̲߳ż�������ִ�С�
 *
 * CountDownLatch��Ҫ������������countDown()��await()��
 *  countDown()��������ʹ��������һ����һ����ִ��������̵߳��ã�
 *  await()������ʹ���ø÷������̴߳��ڵȴ�״̬����һ�������̵߳��á�
 *
 *  ������Ҫע����ǣ�
 *      countDown()������û�й涨һ���߳�ֻ�ܵ���һ�Σ���ͬһ���̵߳��ö��countDown()����ʱ��ÿ�ζ���ʹ��������һ��
 *      await()����Ҳ��û�й涨ֻ����һ���߳�ִ�и÷������������߳�ͬʱִ��await()��������ô�⼸���̶߳������ڵȴ�״̬�������Թ���ģʽ����ͬһ������
 */
public class CountdownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        Service service = new Service(latch);
        Runnable task = () -> service.exec();

        for (int i=0; i < 5; i++) {
            Thread thread = new Thread(task);
            thread.start();
        }

        System.out.println("main thread await");
        latch.await();
        System.out.println("main thread completed.");
    }
}
