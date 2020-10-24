package patrick.array.backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MEDIUM
 * https://leetcode.com/problems/subsets/
 */

/*
Given a set of distinct integers, nums, return all possible subsets (the power set).
Example:
Input: nums = [1,2,3]
Output:
[[3],[1],[2],[1,2,3],[1,3],[2,3],[1,2],[]]
题意：寻找数组的全部子组合
  1. 寻找给定数组的所有子数组（包括空的数组）

前提：
  1. 数组中可能包含重复的数字

思路：
  1. 使用递归（即动态规划DP）backtracking方法 -递归的另一个名词
  2. 针对重复数字，先对数组进行排序 Arrays.sort(nums);
  3.
 */
public class ASubSetsTest {
    public static void main(String[] args) {
        ASubSetsTest ASubSetsTest = new ASubSetsTest();
        System.out.println(ASubSetsTest.subsets(new int[]{1,2,3}));
    }

    private void backtrack(List<List<Integer>> result , List<Integer> tempSubResult, int [] nums, int start){
        result.add(new ArrayList<>(tempSubResult));  //1. 首先加上空的子数组 2. 每次递归回来都把子结果tempSubResult先加上
        for(int i = start; i < nums.length; i++){
            if(i > start && nums[i] == nums[i-1]) continue; // skip duplicates,如果数组中有重复数字，则事先需要Arrays.sort(nums)
            tempSubResult.add(nums[i]);
            backtrack(result, tempSubResult, nums, i + 1);
            tempSubResult.remove(tempSubResult.size() - 1);
        }
    }

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> tempSubResult = new ArrayList<>();
        Arrays.sort(nums); //不必要，除非数组中有重复数字, 以便下一个逻辑判断
        backtrack(result, tempSubResult, nums, 0);
        return result;
    }


}