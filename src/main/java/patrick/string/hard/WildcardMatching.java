package patrick.string.hard;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 通配符 匹配
 * Given an input string (s) and a pattern (p), implement wildcard pattern matching with support for '?' and '*'.
 *
 * '?' Matches any single character.
 * '*' Matches any sequence of characters (including the empty sequence).
 * The matching should cover the entire input string (not partial).
 *
 * Note:
 *
 * s could be empty and contains only lowercase letters a-z.
 * p could be empty and contains only lowercase letters a-z, and characters like ? or *.
 * Example 1:
 *
 * Input:
 * s = "aa"
 * p = "a"
 * Output: false
 * Explanation: "a" does not match the entire string "aa".
 * Example 2:
 *
 * Input:
 * s = "aa"
 * p = "*"
 * Output: true
 * Explanation: '*' matches any sequence.
 * Example 3:
 *
 * Input:
 * s = "cb"
 * p = "?a"
 * Output: false
 * Explanation: '?' matches 'c', but the second letter is 'a', which does not match 'b'.
 * Example 4:
 *
 * Input:
 * s = "adceb"
 * p = "*a*b"
 * Output: true
 * Explanation: The first '*' matches the empty sequence, while the second '*' matches the substring "dce".
 */
public class WildcardMatching {
    public static void main(String[] args) {
        WildcardMatching wildcardMatching = new WildcardMatching();
        System.out.println(wildcardMatching.isMatch("adceb","*a*b"));
        //System.out.println(wildcardMatching.isMatch01("adceb","*a*b"));
    }
    /*
    solution2: 较优解
    Time complexity : O(SP) where S and P are lengths of the input string and the pattern correspondingly.
    Space complexity : O(SP) to keep the matrix.
    思路： DP法，动态规划法。
          待匹配字符串：S:  和pattern P
          初始化一个： dp[s+1][p+1} 矩阵  s+1 * p+1 行列大小
                     （0行0列为放初始值，并不使用）
          初始： dp[0][0] = true; 以及 处理s="" and p="*"的特殊情况
                其余全为false
          处理：
              for(i=1; i<行数;i++)  //迭代S
                 for(j=i; j<列数;j++) //迭代P
                    如果 s.charAt(i-1) == p.charAt(j-1) ： 则  dp[i][j] =   dp[i-1][j-1] 矩阵中的斜对角
                    否则如果 p.charAt(j-1)=='?' ， 则  dp[i][j] = dp[i-1][j-1] 矩阵中的斜对角
                        即：如s="adcbdk" p="*a*b?k" 当到 s=c p=? 或 c 匹配时，只需要看前面 ab是否与*a匹配即（dp[i-1][j-1]）
                    否则如果 p.charAt(j-1)=='*'，要看 dp[i][j] = dp[i][j-1] || dp[i-1][j] 取矩阵中上方或下方的值
                    否则如果 s.charAt(i-1) != p.charAt(j-1), dp[i][j] = false
     */
    public boolean isMatch(String s, String p) {
        if(s.length() == 0 && p.length() == 0) return true;
        /* To handle s = ""  p = "*" case*/
        if(s.length() == 0 && p.length() == 1 && p.charAt(0) == '*') return true;

        boolean[][] dp = new boolean[s.length()+1][p.length()+1];
        /* Empty s and Empty p*/
        dp[0][0] = true;
        //初始化，全部为false
        for(int i = 1; i < dp.length; i++){
            dp[i][0]  = false;
        }

        System.out.println("=========BEFORE===========");
        for(int i=0;i<dp.length;i++)
            System.out.println(Arrays.toString(dp[i]));
        for(int i = 1; i < dp[0].length; i++){
            /* Special case, when s = "" and p = "*" */
            /**
             * 1. 针对特别情形：(a) P第一个就是*
             *                (b) when s = "" and p = "*"
             * 2. 初始化第0行的数据，第0行的数据决定了后面的计算结果
             */
            if(p.charAt(i-1) == '*') {
                dp[0][i] = dp[0][i-1];
            }
                /* Empty s and some p */
            else dp[0][i] = false;
        }
        int totalRowLen = dp.length;  // row number 总行数
        int totalColumnLen = dp[0].length;//第0行 col number 总列数
        for(int i = 1; i < totalRowLen; i++){
            for(int j = 1; j < totalColumnLen; j++){
                if(s.charAt(i-1) == p.charAt(j-1) || p.charAt(j-1) == '?'){
                    dp[i][j] = dp[i-1][j-1];
                }
                else if(p.charAt(j-1) == '*'){
                    dp[i][j] = dp[i][j-1] || dp[i-1][j];
                }
                else if(s.charAt(i-1) != p.charAt(j-1)){
                    dp[i][j] = false;
                }
            }
        }

        System.out.println("=========AFTER===========");
        for(int i=0;i<dp.length;i++)
            System.out.println(Arrays.toString(dp[i]));


        return dp[dp.length-1][dp[0].length-1]; //结果为：矩阵最后一个元素
    }

    /*
    solution1: 差解
            recusively 递归法

            Time complexity: \mathcal{O}(\min(S, P))O(min(S,P))
            本代码有bug，仅供参考
     */
    Map<String,Boolean> dp = new HashMap<>();
    public boolean isMatch01(String s, String pattern){
        return helper(s, pattern);
    }

    public boolean helper(String s, String pattern){
        String key = s+"-"+pattern;
        if(dp.containsKey(key)){
            return dp.get(key);
        }
        if(pattern.equals(s) ||  pattern.equals("*")){
            dp.put(key,true);
        }else if(StringUtils.isEmpty(pattern) || StringUtils.isEmpty(s)){
            dp.put(key,false);
        }else if(s.charAt(0)== pattern.charAt(0) || pattern.charAt(0)=='?'){
            dp.put(key,helper(s.substring(1,s.length()-1),pattern.substring(1,pattern.length()-1)));
        }else if(pattern.charAt(0)=='*'){
            dp.put(key, helper(s,pattern.length()>1?pattern.substring(1,pattern.length()-1):"") || helper(s.length()>1?s.substring(1,s.length()-1):"",pattern));
        }
        else
            dp.put(key,false);

        return dp.get(key);
    }

}
