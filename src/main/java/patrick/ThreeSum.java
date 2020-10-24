package patrick;

import java.util.*;

/**
 * MEDIUM
 * https://leetcode.com/problems/3sum/
 *
 Given an array nums of n integers, are there elements a, b, c in nums such that a + b + c = 0?
 Find all unique triplets in the array which gives the sum of zero.

 Note:

 The solution set must not contain duplicate triplets.

 Example:

 Given array nums = [-1, 0, 1, 2, -1, -4],

 A solution set is:
 [
 [-1, 0, 1],
 [-1, -1, 2]
 ]
 */
public class ThreeSum {
    public static void main(String[] args) {
        ThreeSum twoSum = new ThreeSum();
        int[] nums = new int[]{-1, 0, 1, 2, -1, -4};
        System.out.println(twoSum.threeSum(nums, 0));
    }

/*
 * time complexity: O(n*n)
 * 思路：1. 先给数组排序，无序的后果可能找不到相关解，算法的前提是数组由小到大排序的
 *     2. 用一个 Set<List>存放结果
 *     3. 用3个指针 i用于正常循环迭代的index，j代表i+1(i旁边的数字index），k代表数组最后的index
 *     3. for (i)
 *            while(j<k){
 *              sum = nums[i]+nums[j]+nums[k]
 *                  sum > target k--
 *                  sum < target j++
 *                  sum = target 放入set
 */
    public List<List<Integer>> threeSum(int[] nums, int target){
        Set<List<Integer>> res  = new HashSet<>();
        if(nums.length==0) return new ArrayList<>(res);
        Arrays.sort(nums); //为什么要排序？？因为算法的前提是数组由小到大排序的
        for(int i=0; i < nums.length-2; i++){
            int j = i+1; //下一个元素index
            int k = nums.length - 1;
            while(j < k){
                int sum = nums[i] + nums[j] + nums[k];
                if(sum == target) res.add(Arrays.asList(nums[i],nums[j++],nums[k--]));
                else if ( sum > target) k--;
                else if ( sum < target) j++;
            }
        }
        return new ArrayList<>(res);

    }


}
