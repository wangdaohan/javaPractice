package patrick.string.medium;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * MEDIUM
 * https://leetcode.com/problems/longest-substring-without-repeating-characters/
 *
 * Given a string, find the length of the longest substring without repeating characters.
 *
 * Example 1:
 *
 * Input: "abcabcbb"
 * Output: 3
 * Explanation: The answer is "abc", with the length of 3.
 * Example 2:
 *
 * Input: "bbbbb"
 * Output: 1
 * Explanation: The answer is "b", with the length of 1.
 * Example 3:
 *
 * Input: "pwwkew"
 * Output: 3
 * Explanation: The answer is "wke", with the length of 3.
 *              Note that the answer must be a substring, "pwke" is a subsequence and not a substring.
 */
public class LongestUniSubstring {
    public static void main(String[] args) {
        System.out.println(LongestUniSubstring.lengthOfLongestSubstring02("abcabcbb"));
    }
    /**
     * 优解
     * Time complexity : O(n)
     * 思路：寻找字符串内子字符串最长的一个：要求子字符串内不能有重复字符，返回长度大小
     * 思路： 1. 用Map<Char,Integer> 存放中间数据, key=单个字符 value=字符所在index
     *       2. map主要用于判断字符串是否有重复
     *       3. for ( j =0,start=0;j<length;j++{
     *            如果Map中有s[j]，则说明重复字符，start = max(map.get(s.charAt(j), start)
     *            ansLength = max(ansLength, j-start+1);
     *            map.put(str.charAt(j), j+1);
     *       }
     *       return ansLength;
     */
    public static int lengthOfLongestSubstring02(String s) {
        int n = s.length(), ansLength = 0;
        Map<Character, Integer> map = new HashMap<>(); // current index of character
        // try to extend the range [i, j]
        for (int right = 0, start = 0; right < n; right++) {
            if (map.containsKey(s.charAt(right))) { //说明有重复字符，这时候start在指针要重新计算
                start = Math.max(map.get(s.charAt(right)), start);
            }
            ansLength = Math.max(ansLength, right - start + 1);
            map.put(s.charAt(right), right + 1);
        }
        return ansLength;
    }

  /*
  Solution 1: 次解

  Time complexity : O(n^3)
  space complexity: O(n^2)
   */
    public int lengthOfLongestSubstring(String s) {
        int n = s.length();
        int ans = 0;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j <= n; j++)
                if (allUnique(s, i, j)) ans = Math.max(ans, j - i);
        return ans;
    }
    public boolean allUnique(String s, int start, int end) {
        Set<Character> set = new HashSet<>();
        for (int i = start; i < end; i++) {
            Character ch = s.charAt(i);
            if (set.contains(ch)) return false;
            set.add(ch);
        }
        return true;
    }

    /*
    Solution 2:

     */

}
