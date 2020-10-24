package patrick.array.hard;
/**
 * HARD
 * https://leetcode.com/problems/first-missing-positive/
 * */
/**
 * Given an unsorted integer array, find the smallest missing positive integer.
 * Input: [3,4,-1,1]
 * Output: 2
 * Your algorithm should run in O(n) time and uses constant extra space.
 *
 * 题意：在一个未排序的数组中，找到缺失的最小正整数

 前提：
 1. time complexity : 0(n) and space complexity 0(n)
 2. 只能迭代一次

 思路：
 1.
 *
 */
public class FirstMissingPositive {
    public static void main(String[] args) {
        FirstMissingPositive firstMissingPositive = new FirstMissingPositive();
        System.out.println(firstMissingPositive.firstMissingPositive(new int[]{3,4,-1,7}));
        System.out.println(firstMissingPositive.firstMissingPositive(new int[]{3,4,3,7}));
    }
    /*
    思路：根据数组下标，第i位放数值i+1 (第0位数值是1）->
        交换数组元素，使得数组中第i位存放数值(i+1)。- 但：负数和大于数组长度的数字 不作交换
        最后遍历数组，寻找第一个不符合此要求的元素，返回其下标。
        整个过程需要遍历两次数组，复杂度为O(n)。
     */
    public int firstMissingPositive(int nums[]){
        int length = nums.length;
        for (int i = 0; i < length; i++) {
            /*
            * 1. >0 负数不交换
            * 2. 大于数组长度的不交换
            * 3. 符合条件的 i+1 = nums[i] 不用交换
            * 4. 两个待交换的数值 即有重复数字时 如果相等 也不交换
            */
            if (nums[i] > 0 &&
                nums[i] <= length &&
                i + 1 != nums[i] &&
                nums[nums[i]-1] != nums[i])
            {
                swap(nums, i, nums[i] - 1);
            }
        }
        for (int i = 0; i < length; i++) {
            if (nums[i] != i+1) {
                return i+1;
            }
        }
        return length + 1;
    }
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
