package patrick.array.backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MEDIUM
 * https://leetcode.com/problems/combination-sum/
 */

/*
Given a set of candidate numbers (candidates) (without duplicates)
and a target number (target), find all unique combinations in candidates
where the candidate numbers sums to target.
The same repeated number may be chosen from candidates unlimited number of times.
Example 2:
Input: candidates = [2,3,5], target = 8,
A solution set is:
[
  [2,2,2,2],
  [2,3,3],
  [3,5]
]
题意：
  1. 给定一个无重复的正整数数字的数组内和一个目标的正整数数字
  2. 拿出数组内所有和等于目标的组合， 注：数组内数字可以重复。

思路：
1. 动态规划和递归方法，与subset 方法一致
 */
public class CombinationSum {
    public static void main(String[] args) {
        CombinationSum combinationSum = new CombinationSum();
        System.out.println(combinationSum.combinationSum(new int[]{2,3,6,7},8));
    }
    public List<List<Integer>> combinationSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> tempSubResult = new ArrayList<>();
        Arrays.sort(nums);
        backtrack(result, tempSubResult, nums, 0, target);
        return result;
    }
    private void backtrack(List<List<Integer>> result, List<Integer> tempSubResult, int [] nums, int start,int remain){
        if(remain < 0) return;
        else if(remain == 0) {result.add(new ArrayList<>(tempSubResult));}
        else{
            for(int i = start; i < nums.length; i++){
                tempSubResult.add(nums[i]);
                // 不是i+1,因为允许使用重复数字 not i + 1 because we can reuse same elements
                backtrack(result, tempSubResult, nums,  i,remain - nums[i]);
                tempSubResult.remove(tempSubResult.size() - 1);
            }
        }
    }
}
