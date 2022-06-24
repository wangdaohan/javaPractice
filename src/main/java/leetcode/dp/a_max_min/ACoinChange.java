package leetcode.dp.a_max_min;

/**
 * DP类型：
 *    1. 最值型的动态规划： 最大/最小/最长/longest/shortest. coin change
 *
 */

public class ACoinChange {


    public static void main(String[] args) {
        ACoinChange wCoinChange = new ACoinChange();
        int [] ints = new int[]{1,2,5};
        System.out.print(wCoinChange.coinChange(ints,11));
    }

    public int coinChange(int[] coins, int amount) {
        int[] f = new int[amount + 1]; //数组应该开多大，基本都要开到amount下标大小

        int n = coins.length; //number of kinds of coins

        //初始化
        f[0] = 0;

        for(int i=1;i<=amount; i++){
            f[i] = Integer.MAX_VALUE; //初始值
            // last coin coins[j]
            // f[i] = min { f[i-coins[0]]+1, ... , f[i-coins[n-1]]+1 }
            for(int j=0; j<n; ++j){
                //检查边界 1. i >= coins[j] -> f[i - coins[j]]能取到值
                //        2.  f[i - coins[j]] 不是正无穷，不然正无穷+1会报错
                if( i >= coins[j] && f[i - coins[j]]!=Integer.MAX_VALUE) {
                    f[i] = Math.min(f[i - coins[j]] + 1, f[i]);
                }
            }
        }

        return f[amount];

    }


}
