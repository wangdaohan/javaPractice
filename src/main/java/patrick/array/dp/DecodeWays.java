package patrick.array.dp;
/**
 * EASY
 * https://leetcode.com/problems/decode-ways/
 */
/**
 * 相似题目：
 *    ClimbStairs, EUniPaths
 * 了解题目意思：
 *    题中假设数字1-26分别代表字母A-Z， 求给定一个数字字符串（如，226，23423），返回给组合出多少种字母组合
 *  暗含规律为： 字符串数组中，前一个数字如果是1-9之间，字母组合数+1
 *                      如果前二个数字是在10-26之间，字母组合数再+1
 *  代码实现为：string s ,  dp[s.length+1] dp[0]=1 dp[1]=1
 *    for(i=2;i<n;i++){
 *        int first = Integer.valueof(s.subString(i-1,i))
 *        int sec = Integer.valueOf(s.subString(i-2,i))
 *        if(first>1 && first <=9{
 *            dp[i] += dp[i-1];
 *        }
 *        if(sec>=10 && sec<=26){
 *            dp[i] += dp[i-2]
 *        }
 *        return dp[n]
 *    }
 */
public class DecodeWays {
    public static void main(String[] args) {
        DecodeWays decodeWays = new DecodeWays();
        System.out.println(decodeWays.decodeWays01("3265"));
    }
    /*
    较优解：
      dp: 动态路径法
     */

    public int decodeWays01(String s){
        if(s == null || s.length() == 0) {
            return 0;
        }
        int n = s.length();
        int[] result = new int[n+1];
        result[0] = 1;
        result[1] = s.charAt(0) != '0'?1:0;
        for(int i =2;i<=n;i++){
            //因为数字1-26分别代表字母A-Z -> 最多只有2位数
            int first = Integer.valueOf(s.substring(i-1,i));
            int sec = Integer.valueOf(s.substring(i-2,i));
            if(first>=1 && first<=9){
                result[i] += result[i-1];
            }
            if(sec>=10 && sec<=26){
                result[i] += result[i-2];
            }
        }
        return result[n];
    }
}
