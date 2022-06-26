package concurrency;

/**
 * 并发容器类review
 * 1. ConcurrentMap
 * 2. CopyOnWrite 写的时候复制一份供读取 ->
 *         缺点：不适合写特别多的场景，以及数据量大的场景
 *             适合读多写少的以及数据量小的场景
 *
 * 3. ArrayBlockingQueue有界 / LinkedBlockingQueue无界 - SynchronousQueue, PriorityBlockingQueue, DelayQueue,
 */
public class Review {
}
