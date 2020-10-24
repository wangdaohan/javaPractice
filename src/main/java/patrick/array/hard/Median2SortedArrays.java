package patrick.array.hard;
/**
 * HARD
 * https://leetcode.com/problems/median-of-two-sorted-arrays/
 */

/**
 * There are two sorted arrays nums1 and nums2 of size m and n respectively.
 * Find the median of the two sorted arrays. The overall run time complexity should be O(log (m+n)).
 * You may assume nums1 and nums2 cannot be both empty.
 * Example 1:
 * nums1 = [1, 3]
 * nums2 = [2]
 * The median is 2.0
 * Example 2:
 *
 * nums1 = [1, 2]
 * nums2 = [3, 4]
 *
 * The median is (2 + 3)/2 = 2.5

 题意：
 1. 在给定的2个单独的已排序数组中，求合并后的中位数计算方法：
 *   length是偶数， 中间2位数字相加/2 = median
 *   length是奇数，中间1位数就是median
 *
 * 前提：
 *   2个数组都是有序的
 *
 * 思路：
 *    1. 合并2个数组，并使其保持有序
 *    2. 判断合并后的数组长度是不是偶数，是，则取中间2个数相加/2
 *                                   否，则到中间2个数
 *
 * 改进：
 *    1， 合并时，借用成熟的排序算法，冒泡，快速等
 */
public class Median2SortedArrays {
    public static void main(String[] args){
        System.out.println(Median2SortedArrays.findMedian(new int[]{1, 3, 5},new int[]{2,4,6}));
    }

    /**
     * time complexity: O(log(min(m,n)))
     * space complexity: O(1)
     */
    public static double findMedian(int[] nums1, int[] nums2){
        int[] buf = new int[nums1.length+nums2.length];
        int item1 =0;
        int item2=0;
        //用两个指针 分别代表nums1,nums2
        //因为2个数组都是有序的
        //因此只需要按顺序读，并对比2个数组的当前值，小的放入新数组中，大的进入下次比较
        for(int i=0;i<buf.length;i++){
            if(item1<nums1.length && item2<nums2.length) {
                if (nums1[item1] < nums2[item2]) {
                    buf[i] = nums1[item1];
                    item1++;
                } else {
                    buf[i] = nums2[item2];
                    item2++;
                }
            }else if(item1 == nums1.length && item2<nums2.length){
                buf[i] = nums2[item2];
                item2++;
            }else if(item1 < nums1.length && item2 == nums2.length){
                buf[i] = nums1[item1];
                item1++;
            }
        }

        if(buf.length%2 ==0){
            return (double) (buf[buf.length/2] + buf[buf.length/2-1])/2;
        } else {
            return (double) buf[buf.length/2];
        }
    }
}
