package patrick.array.medium;

import java.util.*;

/**
 * MEDIUM
 * https://leetcode.com/problems/group-anagrams/
 */
/*
Given an array of strings, group anagrams together.
Input: ["eat", "tea", "tan", "ate", "nat", "bat"],
Output:
[["ate","eat","tea"],["nat","tan"],["bat"]
]

Anagram:相同字母异序词;
题意：
 寻找数组内，同一串字符的所有错位字

关键：
  如何判断2个字符是同一个字符串，仅仅是字母位置不一样呢？

整体思路：
  每一串字符都先排序，这样就能判断2个字符串是不是同一串字符
 */
public class AGroupAnagrams {
    public static void main(String[] args) {
        AGroupAnagrams AGroupAnagrams = new AGroupAnagrams();
        System.out.println(AGroupAnagrams.groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}));
        System.out.println(AGroupAnagrams.groupAnagrams2(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}));

    }
    /*
    solution2:
    Time Complexity: O(NK)
    Space Complexity: O(NK)

    具体解法思路：
       因为是字符串，因此根据英文字母只有26个的原则，我们初始化一个26大小的count数组
       1，使用count计数思路，初始化26大小的数组，代表26个英文字母
       2. 每一个string.toCharArray ，然后count[charValue-'a']++ 即 count[]数字+1
       3. 由count数组，生成一个key,如组合出key:  #2#1#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0 代表有1个a,1个b
       4，所以符合key的字符串都属于同一个字符串的错位字符串
     */
    public List<List<String>> groupAnagrams2(String[] strs) {
        if (strs.length == 0) return new ArrayList();
        Map<String, List> ans = new HashMap<String, List>();
        int[] count = new int[26];
        for (String s : strs) {
            Arrays.fill(count, 0);
            //涉及acsii码 与 英文字母对应问题  count[0] 对应为 字符'a'小写的位置 count[charValue-'a']
            for (char charValue : s.toCharArray()) count[charValue - 'a']++;
            //组合出key:  #2#1#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0 代表有1个a,1个b
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < 26; i++) {
                sb.append('#');
                sb.append(count[i]);
            }
            String key = sb.toString();
            if (!ans.containsKey(key)) ans.put(key, new ArrayList());
            ans.get(key).add(s);
        }
        return new ArrayList(ans.values());
    }
    /*
    solution1:
    具体解法思路：
        1，迭代str数组
        2，每一个str都toCharArray,以便使用Arrays.sort方法
        3，用一个Map来保存结果，key是排序后的字符串，value是所有符合条件的字符串List
        4， map contains key -> add 原始字符串

    Time Complexity: O(NKlogK),
    Space Complexity: O(NK)
     */
    public List<List<String>> groupAnagrams(String[] strs){
        if(strs.length==0) return new ArrayList<>();
        Map<String,List> result = new HashMap<String, List>();
        for(String str: strs){
            char[] strChar = str.toCharArray();
            Arrays.sort(strChar);
            String key = String.valueOf(strChar);
            if(!result.containsKey(key)) result.put(key,new ArrayList());
            result.get(key).add(str);
        }
        return new ArrayList(result.values());
    }

}
