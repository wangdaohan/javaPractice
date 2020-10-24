package patrick.array.duplicates;

/**
 * EASY
 * https://leetcode.com/problems/rotate-array/
 *
 * 类似题型：MEDIUM ASearchRotatedSortedArray
 *
 * */

/*
Given an array, rotate the array to the right by k steps, where k is non-negative.
Example 1:

Input: [1,2,3,4,5,6,7] and k = 3
Output: [5,6,7,1,2,3,4]
Explanation:
rotate 1 steps to the right: [7,1,2,3,4,5,6]
rotate 2 steps to the right: [6,7,1,2,3,4,5]
rotate 3 steps to the right: [5,6,7,1,2,3,4]

Note:
Try to come up as many solutions as you can, there are at least 3 different ways to solve this problem.
Could you do it in-place with O(1) extra space?

题意：
  1. 给定的数组，按照给定的index K划分左和右，旋转数组，左边数组变右边数组，右边数组变左边数组

前提：
1. 尽可能不使用额外的空间，也就是space complexity = O(1)

总体思路：
  思路1： 对于旋转数组，有一个关键的算法必须清楚，           取余 (i+k) % length = 旋转后所在的位置 k：为给定的第几个index开始旋转
   如指定在从第3个位置rotate， 当前位置是1：rotate之后的index = (1+3) % 5 = 4,即旋转后的位置是实际index=4
                                                           (0+3) % 5 = 3,即旋转后的位置是实际index=3
      缺点：需使用额外O(n)的空间复杂度
  思路2：

 */
public class DRotateArray {
    public static void main(String[] args) {
        System.out.println((1+3)%5);
        DRotateArray rotateArray = new DRotateArray();
        rotateArray.rotate01(new int[] {1,2,3,4,5,6,7}, 3);

        rotateArray.rotate02(new int[] {1,2,3,4,5,6,7}, 3);
    }

    /*
    Time complexity : O(n)
    Space complexity : O(n)
    思路1：
           1. 借助新数组
           2. 了解 (i+k)%length = 旋转后所在的位置
       缺点：
           空间复杂度增加
     */
    public void rotate01(int[] nums, int k) {
        int[] a = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            a[(i + k) % nums.length] = nums[i];
        }
        for (int i = 0; i < nums.length; i++) {
            nums[i] = a[i];
        }
    }

    /*
    Time complexity : O(n)
    Space complexity : O(1)
    思路2：1. dp动态编程 [1,2,3,4,5,6,7]
          2，先倒置整个数组 [7,6,5,4,3,2,1]
          3. 把倒置的数据前k个按原来 [5,6,7,4,3,2,1]
          4. 再把k后面的数组按原来顺序调整 [5,6,7,1,2,3,4]
     */
    public void rotate02(int[] nums, int k){
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    public void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }
}
