package patrick.array.backtracking;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/**
 * MEDIUM
 * https://leetcode.com/problems/permutations/ 排列组合
Given a collection of distinct integers, return all possible permutations.
Example:
Input: [1,2,3]
Output:
[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
题意：寻找给定数组内数字的所有排列组合
思路： 与SubSet 思路一样，都是使用递归DP方法
 */
public class BPremutaions {
    public static void main(String[] args) {
        BPremutaions BPremutaions = new BPremutaions();
        System.out.println(BPremutaions.permute(new int[]{1,2,3}));
    }
    /*
     solution1: 对于此类非查找类的题，基本没有最优解，都需要迭代出来。
     思路：backtracking 也叫回溯 也叫递归法
     解法：
        1.由tempList保存每一个组合的结果
        2. 当templist.length = nums.length时，加入总结果result
             否则：
                  由i=0，i<nums.length开始，如果templist不存在这个数字，则加入；
                  然后，递归调用这个方法
                  最后，tempList删除最后一个数字，为下一个新组合准备
     Time complexity : O(∑(1-n)P(N,k))
     Space complexity : O(N!)  一个正整数的阶乘（factorial）
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        // Arrays.sort(nums); // not necessary
        backtrack(result, new ArrayList<>(), nums);
        return result;
    }

    private void backtrack(List<List<Integer>> result, List<Integer> tempList, int [] nums){
        if(tempList.size() == nums.length){
            result.add(new ArrayList<>(tempList));
        } else{
            for(int i = 0; i < nums.length; i++){
                //假设原数组中不包含重复数字
                if(tempList.contains(nums[i])) continue; // element already exists, skip
                tempList.add(nums[i]);
                backtrack(result, tempList, nums);
                tempList.remove(tempList.size() - 1);
            }
        }
    }
    /*
    solution2:
     是solution1的优化，但比较难理解，仅作为参考
     */

    public List<List<Integer>> permute02(int[] nums) {
        // init output list
        List<List<Integer>> output = new LinkedList();
        // convert nums into list since the output is a list of lists
        ArrayList<Integer> nums_lst = new ArrayList<Integer>();
        for (int num : nums)
            nums_lst.add(num);
        int length = nums.length;
        backtrack02(length, nums_lst, output, 0);
        return output;
    }

    public void backtrack02(int n,
                          ArrayList<Integer> nums,
                          List<List<Integer>> output,
                          int first) {
        // if all integers are used up
        if (first == n)
            output.add(new ArrayList<Integer>(nums));
        for (int i = first; i < n; i++) {
            // place i-th integer first
            // in the current permutation
            Collections.swap(nums, first, i);
            // use next integers to complete the permutations
            backtrack02(n, nums, output, first + 1);
            // backtrack
            Collections.swap(nums, first, i);
        }
    }
}
