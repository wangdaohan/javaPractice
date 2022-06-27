package concurrency.threadpool;

import java.util.concurrent.*;

/**
 * Executors�����ࣺ
 *    1����Ҫʹ��Executors����Ĵ����̳߳صķ��� -> newCachedThreadPool / newFixedThreadPool -> ��Ϊû�н�������(�̳߳��������̶߳��г���������)�������ڰ�ȫ���� -> �磺newCachedThreadPool/newFixedThreadPool���߲������̳߳سű���
 *        newCachedThreadPoolʵ�֣� new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());  -> a. �̳߳ص�maximumPoolSize=Integer.MAX_VALUE-���߲������̳߳����׳ű�
 *                                                                                                                                           b. ����ʹ��new SynchronousQueue<Runnable>()�����������κδ洢��ÿ��һ������ʹ���һ�����߳�
 *        newFixedThreadPoolʵ�֣� new ThreadPoolExecutor(nThreads, nThreads,  0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()); -> a. ȷʵ��������������С�߳������������õ�LinkedBlockingQueue�洢����������ͬʱû���������ޣ�
 *                                                                                                                                                  �����������������ʱ�����װ�LinkedBlockingQueue�ű��ڴ�
 *
 *     2. ����� ��ThreadPoolExecutorȥ�Զ����̳߳� ->
 *
 *     3. ������ú����̳߳ش�С -> ���㣨CPU���ܼ��� �� IO�ܼ���
 *                 ���㣨CPU���ܼ��� �������̳߳ش�С�� CPU + 1
 *                 IO�ܼ���(���ݶ�ȡ���洢�����ݿ�, �ص㣺�ٶȽ����� �̳߳�Ӧ�Դ�һ��) -���̳߳ش�С�� CPU / ��1 - ����ϵ����   ��������ϵ���� 0��8-0��9֮��  �� 8/��1-0��8�� = 40
 *
 *      4�� �����ȷʹ���̳߳� -������3����
 */
public class ThreadPoolMain {
    public static void main(String[] args){
        Executors.newFixedThreadPool(1);
        new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),//int corePoolSize, -> ������ú����̳߳ش�С,
            Runtime.getRuntime().availableProcessors() * 2,//int maximumPoolSize,
            60,//long keepAliveTime,
            TimeUnit.SECONDS,//TimeUnit unit,
            new ArrayBlockingQueue<>(200),//BlockingQueue<Runnable> workQueue,
            new ThreadFactory(){
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("order-thread");
                    return t;
                }
            },//ThreadFactory threadFactory
            new ThreadPoolExecutor.DiscardPolicy()
        );
    }
}
