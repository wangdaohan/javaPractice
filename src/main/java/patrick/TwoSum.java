package patrick;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * EASY
 * https://leetcode.com/problems/two-sum/
 *
 * Given an array of integers, return indices of the two numbers such that they add up to a specific target.
 *
 * You may assume that each input would have exactly one solution, and you may not use the same element twice.
 *
 * Example:
 *
 * Given nums = [2, 7, 11, 15], target = 9,
 *
 * Because nums[0] + nums[1] = 2 + 7 = 9,
 * return [0, 1].
 *
 * 寻找数组中某2个数之和等于给定数，并返回对应index
 */
public class TwoSum {
    public static void main(String[] args) {
        TwoSum twoSum = new TwoSum();
        int[] nums = new int[]{2,11,15,7};
        //System.out.println(Arrays.toString(twoSum.twoSum(nums, 9)));
        //System.out.println(Arrays.toString(twoSum.twoSum2(nums, 9)));
        System.out.println(Arrays.deepToString(new TwoSum().calcPalin("aab".toCharArray())));
        System.out.println(Arrays.deepToString(new TwoSum().calcPalin2("aab".toCharArray())));
    }
    boolean[][] calcPalin2(char[] s){
        int n = s.length;
        boolean[][] palin = new boolean[n][n];

        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                palin[i][j] = false;
            }
        }

        //for(int mid=0;mid<n;mid++){
            int i,j,mid;
        mid = n%2;
            if((n%2)!=0) {

                //对于字符串长度是奇数的，那么中心点是一个字符，所以i(向左）j（向右）都可以从中间mid开始
                //如 a,b,c,d,e  -》 mid=2
                i = j = mid;
                while (i >= 0 && j < n && s[i] == s[j]) {
                    palin[i][j] = true;
                    --i;
                    ++j;
                }
            } else {
                //对于字符串长度是偶数的，那么中心点是没有字符，是一条线，所以i(向左）j（向右）分别从mid-1，mid开始
                //如 a,b,c,d  -》 mid-1 = 1  mid=2
                i = mid - 1;
                j = mid;
                while (i >= 0 && j < n && s[i] == s[j]) {
                    palin[i][j] = true;
                    --i;
                    ++j;
                }
            }
            // }
        //}
        return palin;
    }
    boolean[][] calcPalin(char[] s){
        int n = s.length;
        boolean[][] palin = new boolean[n][n];

        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                palin[i][j] = false;
            }
        }
        for(int mid=0;mid<n;mid++){
            int i,j;
            //if((n%2)!=0){

            //对于字符串长度是奇数的，那么中心点是一个字符，所以i(向左）j（向右）都可以从中间mid开始
            //如 a,b,c,d,e  -》 mid=2
            i = j = mid;
            while(i>=0 && j<n && s[i]==s[j]){
                palin[i][j] = true;
                --i;
                ++j;
            }
            // } else{
            //对于字符串长度是偶数的，那么中心点是没有字符，是一条线，所以i(向左）j（向右）分别从mid-1，mid开始
            //如 a,b,c,d  -》 mid-1 = 1  mid=2
            i=mid-1; j=mid;
            while(i>=0 && j<n && s[i]==s[j]){
                palin[i][j] = true;
                --i;
                ++j;
            }
            // }
        }
        return palin;
    }

    /*
    较优解：
    Time complexity : O(n)
    Space complexity : O(n)

    思路：
      1.用一个map存储结果，key=nums[index], value=index
      2.每次target-nums[i]=余数，拿余数在map里找，如果有，返回数组[当前index,另一个在map里的index]
    */
    public int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i]; //余数
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    /*
    次解：
    Time complexity: O(n^2)
    Space complexity: O(1)

    思路：2重循环

     */
    public int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] == target - nums[i]) {
                    return new int[] { i, j };
                }
            }
        }
        throw new IllegalArgumentException("No two sum solution");
    }


}
