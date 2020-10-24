package patrick.array.subarray_eq_k;

/**
 * MEDIUM
 * https://leetcode.com/problems/subarray-sum-equals-k/
 */

import java.util.HashMap;
import java.util.Map;

/**
 * Note:
 *  The length of the array is in range [1, 20,000].
 *  The range of numbers in the array is [-1000, 1000] and the range of the integer k is [-1e7, 1e7].
 * 这道题给了我们一个数组，让我们求和为k的连续子数组的个数，
 * 博主最开始看到这道题想着肯定要建立累加和数组啊，然后遍历累加和数组的每个数字，
 * 首先看其是否为k，是的话结果 res 自增1，然后再加个往前的循环，这样可以快速求出所有的子数组之和，看是否为k，参见代码如下：
 */
public class ASubarraySum {
    public static void main(String[] args) {
        System.out.println(ASubarraySum.subarraySum02(new int[]{3,4,7,2,-3,1,4,2}, 7));
        //System.out.println(ASubarraySum.subarraySum01(new int[]{3,4,7,2,-3,1,4,2}, 7));
    }

    /**
     * 原理：map用于放sum-k的key，可以检测是否有连续子数组的和==sum-k，如果有，那么即表明和为K的连续子数组也一定存在。  -》 why???
     * 即判断sum(t) = sum(t-1) + k -> 当 sum(t) - k = sum(t-1) 即sum(t) - sum(t-1) = k 代表中间存在着和为k的子数组
     */
    public static int subarraySum02(int[] nums, int k){
        int count = 0, sum = 0;
        Map< Integer, Integer > map = new HashMap < > ();//存放key=sum,value=次数
        map.put(0, 1); //需要初始一个，不然计数会计少一个，即第一个K时应计为1
        for (int i = 0; i <  nums.length; i++) {
            sum += nums[i];
            if (map.containsKey(sum - k))
                count += map.get(sum - k);
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return count;
    }

    /**
     * Brute force: NOT RECOMMENDED
     */
//    public static int subarraySum01(int[] nums,int k){
//        int count = 0;
//        for (int start = 0; start < nums.length; start++) {
//            for (int end = start + 1; end <= nums.length; end++) {
//                int sum = 0;
//                for (int i = start; i < end; i++)
//                    sum += nums[i];
//                if (sum == k)
//                    count++;
//            }
//        }
//        return count;
//    }
}
