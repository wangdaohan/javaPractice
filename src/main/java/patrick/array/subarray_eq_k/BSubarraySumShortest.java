package patrick.array.subarray_eq_k;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * HARD
 * https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/
 * Note:
 *  The length of the array is in range [1, 20,000].
 *  The range of numbers in the array is [-1000, 1000] and the range of the integer k is [-1e7, 1e7].
 * 这道题给了我们一个数组，让我们求和为k的连续子数组的个数，
 * 博主最开始看到这道题想着肯定要建立累加和数组啊，然后遍历累加和数组的每个数字，
 * 首先看其是否为k，是的话结果 res 自增1，然后再加个往前的循环，这样可以快速求出所有的子数组之和，看是否为k，参见代码如下：
 */
public class BSubarraySumShortest {
    public static void main(String[] args) {
        System.out.println(BSubarraySumShortest.shortestSubArrayEqK02(new int[]{3,4,7,2,-3,1,4,2}, 7));
    }

    /**
     * 原理：1. sum数组存放累加的结果
     *      2. queue用来存放每个index
     *
     * 思路：
     *    1.首先跟上一题类似做法，将数组内数字累加，将每一个累加结果放进新数组中。[3,4,7,2,-3,1,4,2]  -> [0,3,7,14,16,13,14,18,20] 多一个0
     *    2.迭代新数组，并初始化一个双端队列ArrayDeque,作用：用于存放新数组的index
     *    3. sum[i] - sum[queue.peekFirst()] >= k 即表示前面有符合条件的。
     *       利用这个规则, 循环判断
     *                   min = min, i-queue.peekFirst() -》 可得出最小数组长度大小
     *                   queue往前移一步，即queue.poolFirst();
     */
    public static int shortestSubArrayEqK02(int[] nums, int k){
        if (nums == null || nums.length == 0) return -1;
        int min = nums.length+1;
        int[] sum = new int[nums.length+1];
        for (int i=0; i < nums.length; i++){
            sum[i+1] = sum[i] + nums[i];
        }
        //Deque:双端队列,
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i=0; i<=nums.length;i++){
            //下面这段代码不用，好像也可以。。。
//            while(queue.size() > 0 && sum[i]-sum[queue.peekLast()] <= 0){  //sum[i] 当前值 和 前一个值sum[queue.peekLast()]对比
//                //去掉不符合条件的数字
//                //如果有负数，queue全部清空
//                queue.pollLast();//拿出并删除最后数据
//            }
            while(queue.size() > 0 && sum[i] - sum[queue.peekFirst()] >= k){
                //为什么是>=k 而不是==k
                //sum[i] - sum[queue.peekFirst()] >= k 说明前面有符合条件的
                min = Math.min(min, i-queue.peekFirst());
                queue.pollFirst();//拿出并删除第一个数据，相当于将数组index往前移， 不断比较，直到sum[i] - sum[queue.peekFirst()] < K, 则结束
            }
            queue.offer(i);//插入数据
        }
        return min <= nums.length ? min : -1;
    }

    /**
     * Brute force: NOT RECOMMENDED
     */
    public static int shortestSubArrayEqK01(int[] nums,int k){
        int count = 0;
        for (int start = 0; start < nums.length; start++) {
            for (int end = start + 1; end <= nums.length; end++) {
                int sum = 0;
                for (int i = start; i < end; i++)
                    sum += nums[i];
                if (sum == k)
                    count++;
            }
        }
        return count;
    }


}
