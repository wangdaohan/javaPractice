package patrick.binarysearch.medium;
/**
 * MEDIUM
 * 题1：https://leetcode.com/problems/search-in-rotated-sorted-array/
 *
 * 题2：https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/
 *     解法2 满足需求
 */

/**
 * Suppose an array sorted in ascending order is rotated at some pivot unknown to you beforehand.
 * Your algorithm's runtime complexity must be in the order of O(log n).
 * Example 1:
 *
 * Input: nums = [4,5,6,7,0,1,2], target = 0
 * Output: 4
 * 题目：给定一个有序数组，并且经过某个index旋转过后的数组。找出某个数字在数组中的位置，如无则返回-1
 * 题目前提： 给定数组是有序的，但被rotate了，即随机选了一个点对数组进行旋转操作
 *     如： [0,1,2,4,5,6,7] ->  [4,5,6,7,0,1,2]
 *
 * 解法 ：
 */
public class ASearchRotatedSortedArray {
    public static void main(String[] args) {
        ASearchRotatedSortedArray searchRotatedSortedArray = new ASearchRotatedSortedArray();
        System.out.println(searchRotatedSortedArray.searchInUnsortedArraySolution1(new int[] {4,5,6,7,0,1,2},0));
        System.out.println(searchRotatedSortedArray.search_solution2(new int[] {4,5,6,7,0,1,2},0));
        System.out.println(searchRotatedSortedArray.search_solution3(new int[] {5,6,7,0,1,2,3,4},2));
    }
    /*
       Solution3:   one pass
         思路： 利用二分查找(binary search in BinarySearchTest.java)基础，再加上针对本题逻辑的判断
           Time complexity:   O(log(N))
           Space complexity:  O(1)
           */
    public int search_solution3(int[] nums, int target) {
        int start = 0, end = nums.length - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;  //why??? 不是(start+end)/2??
            if (nums[mid] == target) return mid;
            else if (nums[mid] >= nums[start]) {
                if (target >= nums[start] && target < nums[mid]) end = mid - 1;
                else start = mid + 1;
            }
            else {
                if (target <= nums[end] && target > nums[mid]) start = mid + 1;
                else end = mid - 1;
            }
        }
        return -1;
    }
    /*
      Solution2：
      time complexity:   O(log(N))
      Space complexity:  O(1)
      题目前提： 给定数组是有序的，但被rotate了，即随机选了一个点对数组进行旋转操作
           如： [0,1,2,4,5,6,7] ->  [4,5,6,7,0,1,2]
      思路： 利用二分查找算法 (Binary search algorithm)
          但是由于数组不是完全有序：分为第一段有序 和第二段有序

          1. 找出rotate的点    -  满足 题1 需求
          2.根据Roate点，将数组分为第一段和第二段
          3. 分别将第一段和第二段传入binarySearch去查找
         */
    int [] nums;
    int target;
    public int search_solution2(int[] nums, int target) {
        this.nums = nums;
        this.target = target;

        int n = nums.length;
        if (n == 0)
            return -1;
        if (n == 1)
            return this.nums[0] == target ? 0 : -1;
        int rotate_index = find_rotate_index(0, n - 1);
        // if target is the smallest element
        if (nums[rotate_index] == target)
            return rotate_index;
        // if array is not rotated, search in the entire array
        if (rotate_index == 0)
            return binarySearch(0, n - 1);
        if (target < nums[0])
            // search in the right side
            return binarySearch(rotate_index, n - 1);
        // search in the left side
        return binarySearch(0, rotate_index);
    }

    public int find_rotate_index(int left, int right) {
        if (nums[left] < nums[right])
            return 0;
        while (left <= right) {
            int pivot = (left + right) / 2;
            //因为有序的数组都最nums[pivot]<nums[pivot+1]
            //所以当 pivot>pivot+1时，就能找到rotate的index
            if (nums[pivot] > nums[pivot + 1]) //说明找到了rotate点
                return pivot + 1;
            else {
                if (nums[pivot] < nums[left])
                    right = pivot - 1;
                else
                    left = pivot + 1;
            }
        }
        return 0;
    }

    public int binarySearch(int left, int right) {
    /*
    Binary search
    */
        while (left <= right) {
            int pivot = (left + right) / 2;
            if (nums[pivot] == target)
                return pivot;
            else {
                if (target < nums[pivot])
                    right = pivot - 1;
                else
                    left = pivot + 1;
            }
        }
        return -1;
    }
    /*
    solution1:
    思路： 最差解 遍历整个数组，遇到则返回
    time complexity: O(n) 达不到题目要求
     */
    public int searchInUnsortedArraySolution1(int[] nums, int target){
        for(int i=0;i<nums.length;i++){
            if(nums[i]==target){
                return i;
            }
        }
        return -1;
    }

}
