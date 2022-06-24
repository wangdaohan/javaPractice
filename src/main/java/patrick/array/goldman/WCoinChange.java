package patrick.array.goldman;

public class WCoinChange {
    /**
     * 完全背包问题： 数组内的元素存在拿1,2,3,4...n次/不拿的情况
     * @param args
     */

    public static void main(String[] args) {
        WCoinChange wCoinChange = new WCoinChange();
        int [] ints = new int[]{4,5,11};
        System.out.println(wCoinChange.coinChange(ints,15));
        System.out.println(wCoinChange.coinChange2(new int[]{4,5,11},15));
        System.out.println(wCoinChange.coinChangeRecusive(new int[]{1,2,5},2, 11));
    }


    public int coinChangeRecusive(int[] coins, int index, int w){
        if( w==0){
            return 1;
        }else if(w<0 || index>=coins.length){
            return 0;
        }
        else if (w%coins[index]==0){
            return w/coins[index];
        }else if(index == 0){
            return Integer.MAX_VALUE-1;
        }else if(w-coins[index]<0){
            return coinChangeRecusive(coins, index-1, w);
        }else{
            int a = coinChangeRecusive(coins, index, w-coins[index]) + 1;
            int b = coinChangeRecusive(coins, index-1, w);
            return Math.min(a,b);
        }
    }

    public int coinChange2(int[] coins, int amount){
        int[] f = new int[amount+1];
        for(int i=1; i<=amount;i++) f[i] = Integer.MAX_VALUE-1;
        for(int i=4;i<=amount;i++) {
            int cost = Integer.MAX_VALUE - 1;
            if (i - 4 >= 0) cost = Math.min(cost, f[i - 4] + 1);
            if (i - 5 >= 0) cost = Math.min(cost, f[i - 5] + 1);
            if (i - 11 >= 0) cost = Math.min(cost, f[i - 11] + 1);
            f[i]=cost;
            //System.out.println("i="+i+" = "+f[i]);
        }
        return f[amount];
    }

    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        for (int i = 1; i <= amount; i++) dp[i] = 0x7fff_fffe;//Integer.MAX_VALUE-1
        for (int coin : coins)
            for (int i = coin; i <= amount; i++)
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
        return dp[amount] == 0x7fff_fffe ? -1 : dp[amount];
    }
}
