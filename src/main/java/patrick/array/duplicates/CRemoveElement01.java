package patrick.array.duplicates;
/** EASY */

/*
Given an array nums and a value val, remove all instances of that value in-place and return the new length.

Do not allocate extra space for another array, you must do this by modifying the input array in-place with O(1) extra memory.

The order of elements can be changed. It doesn't matter what you leave beyond the new length.

Example 1:

Given nums = [3,2,2,3], val = 3,

Your function should return length = 2, with the first two elements of nums being 2.

It doesn't matter what you leave beyond the returned length.

10 easy
5 medium
1 hard

题意：
  1. 在给定的数组内，删除给定的一个值，同时返回新的数组的长度

前提：
1. 不能使用额外的空间，也就是不能借助新的数组
2. 只能迭代一次

思路：
1. 迭代数组，一个个对比， 不相等则length+1;

 */
public class CRemoveElement01 {
    public static void main(String[] args) {
        CRemoveElement01 removeElement01 = new CRemoveElement01();
        System.out.println(removeElement01.removeElement01(new int[]{3,2,2,3}, 3));
    }
    /*
    solution 1
     Time complexity : O(n)
     Space complexity : O(1)
     */
    public int removeElement01(int[] nums, int val) {
        int i = 0;
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != val) {
                nums[i] = nums[j];
                i++;
            }
        }
        return i;
    }
}
