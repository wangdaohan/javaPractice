package patrick.array.duplicates;
/** MEDIUM
 * https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/solution/*/
/*
Given a sorted array nums, remove the duplicates in-place
such that duplicates appeared at most twice and return the new length.

Do not allocate extra space for another array,
you must do this by modifying the input array in-place with O(1) extra memory.

Example 1:

Given nums = [1,1,1,2,2,3],

Your function should return length = 5, with the first five elements of nums being 1, 1, 2, 2 and 3 respectively.

It doesn't matter what you leave beyond the returned length.
Example 2:

Given nums = [0,0,1,1,1,1,2,3,3],

Your function should return length = 7, with the first seven elements of nums being modified to 0, 0, 1, 1, 2, 3 and 3 respectively.

It doesn't matter what values are set beyond the returned length.


区别:
  1. 前一题是要求同一个值最多只能存在1个
  2. 本题要求是相同值，可以存在最多2个

改进：
  1。此题算法也可以适用上题

题意：
  1. 统计给定有序数组的唯一2次值的长度，相同值可以最多存在2个

前提：
1. 给定数组是有顺序的
2. 不能使用额外的空间，也就是不能借助新的数组
3. 只能迭代一次
2. 统计不重复的数组length，相同值可以最多存在2个

思路：
1. 初始化2个指针 i=1 j = 1 和一个计数器count=1
2. i 用来迭代数组，当数组前后值 i, i-1相等时，count+1
   当 count<=2(可重复个数)时， j++  (j用于统计符合条件的值长度）

 */
public class BRemoveDuplicate2 {
    public static void main(String[] args) {
        BRemoveDuplicate2 removeDuplicate = new BRemoveDuplicate2();
        System.out.println(removeDuplicate.removeDuplicates(new int[]{1, 2, 3, 5, 5, 5, 6, 7},3));
    }

    public int removeDuplicates(int[] nums, int allowDups) {
        int j = 1, count = 1;
//        for (int i = 1; i < nums.length; i++) {
//            // If the current element is a duplicate, increment the count.
//            if (nums[i] == nums[i - 1]) {
//                count++;
//            } else {
//                // Reset the count since we encountered a different element
//                // than the previous one.
//                count = 1;
//            }
//            // For a count <= 2, we copy the element over thus
//            // overwriting the element at index "j" in the array
//            if (count <= allowDups) {
//                j++;
//                //nums[j++] = nums[i];
//            }
//        }
        return j;
    }
}
