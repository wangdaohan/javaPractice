package patrick.array.duplicates;
/** EASY */

/*
Given a sorted array nums,
remove the duplicates in-place such that each element appear only once and return the new length.

Do not allocate extra space for another array,
you must do this by modifying the input array in-place with O(1) extra memory.
Example 1:

Given nums = [1,1,2],

Your function should return length = 2, with the first two elements of nums being 1 and 2 respectively.

It doesn't matter what you leave beyond the returned length.

题意：
  1. 统计给定有序数组的唯一值的长度，相同值只能存在一个

前提：
1. 给定数组是有顺序的
2. 不能使用额外的空间，也就是不能借助新的数组
3. 只能迭代一次
2. 统计不重复的数组length

思路：
1. 因为数组是有序的
2. 2个指针思路 ->  指针j用来迭代数组，从1开始
              指针i用来记录前一个数组位置，从0开始  (也用来记录值不重复的数组长度）
3. 迭代开始
          当j val != i val， i++, 同时nums[i]=nums[j]

                                     -> 目的：当i和j中间差>=2时，下一轮j+1不需要和原来的i比较，应与前面一个值（也就是j）比较
改进：
  1。下题算法也可以适用本题

 */

public class ARemoveDuplicate {
    public static void main(String[] args) {
        ARemoveDuplicate removeDuplicate = new ARemoveDuplicate();
        System.out.println("new length="+removeDuplicate.removeDuplicates(new int[]{1, 1, 1, 2, 3, 5, 5, 5}));
        System.out.println("new length="+removeDuplicate.removeDupliates02(new int[]{1, 1, 1, 2, 3, 5,5},1));
    }
    //就是下一题（BRemoveDuplicate2.java) 的解法
    public int removeDupliates02(int[] nums, int allow_dup){
        int j=1; int count=1;
        for(int i =1;i<nums.length;i++){
            if(nums[i]==nums[i-1]){
                count++;
            }else{
                count = 1;
            }
            if(count<=allow_dup){ //1；可设置，指可允许相同数字在数组中出现的次数
                j++;
            }
        }
        return j;
    }

    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int i = 0; //用于统计个数
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                //下面这行代码，用于解决当前面N个数字都是一样的情况，即如：1，1，1, 2，3，5，5
                //如果没有下面这行，结果是5，而不是4
                nums[i] = nums[j]; //目的：当i和j中间差>=2时，下一轮j+1不需要和原来的i比较，应与前面一个值（也就是j）比较
            }
        }
        //Arrays.stream(nums).forEach(System.out::println);
        return i + 1;
    }

}
