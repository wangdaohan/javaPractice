package patrick.array.dp;
/**
 * EASY
 *https://leetcode.com/problems/climbing-stairs/
 */

/**
 * You are climbing a stair case. It takes n steps to reach to the top.
 * Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
 * Note: Given n will be a positive integer.
 * Input: 3
 * Output: 3
 * Explanation: There are three ways to climb to the top.
 * 1. 1 step + 1 step + 1 step
 * 2. 1 step + 2 steps
 * 3. 2 steps + 1 step
 * 题目意思：
 *   爬梯子，爬的方法有两种方法 ：一种一次爬1个台阶，另一种一次爬2个台阶。问：给定梯子台阶数，返回爬梯子的方法数
 *
 *   隐藏意思： 到达第n台阶的方法数 = 到达n-1台阶的方法数 + 到达 n-2台阶的方法数  （除了台阶1，和台阶2以外）
 *
 * 换成代码的意思是：假设 n个台阶，求爬N个台阶的方法数
 *          int[] dp = new int[n];
 *         dp[0] = 1;  //代表1个台阶，爬台阶方法为一共1个
 *         dp[1] = 2;  //代表2个台阶，爬台阶方法为一共2个
 *         for (int i = 2; i <= n; i++) {
 *             dp[i] = dp[i - 1] + dp[i - 2];
 *         }
 *
 *
 */
public class ClimbStairs {
    public static void main(String[] args) {
        ClimbStairs climbStairs = new ClimbStairs();
        System.out.println(climbStairs.climbStaris01(9));

        System.out.println(climbStairs.climbStair02(9));
    }
 /*
 较优解：
Time complexity : O(n)
Space complexity : O(n)
  */
    public int climbStaris01(int n){
        int[] dp = new int[n];
          dp[0] = 1;  //代表一个台阶 爬台阶的方法为：1
          dp[1] = 2;  //代表二个台阶 爬台阶的方法为：2
          for (int i = 2; i < n; i++) {
              // 到达第n台阶的方法数 = 到达n-1台阶的方法数 + 到达 n-2台阶的方法数  （除了台阶1，和台阶2以外）
              dp[i] = dp[i - 1] + dp[i - 2];
          }

          return dp[n-1];
    }

    /*
    次优解: back tracking

    递归方法 recursion
    Time complexity : O(n)
    Space complexity : O(n)
     */

    public int climbStair02(int n){
        if(n==1){
            return 1;
        }
        if(n==2){
            return 2;
        }
        return climbStair02(n-1)+climbStair02(n-2);
    }
}
