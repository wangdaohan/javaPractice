package patrick.array.dp;

/**
 * EASY
 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
 */

/**
 * 和上题一样，但允许多次买卖
 */
public class IBestTimeBuyStockII {
    public static void main(String[] args) {
        int[] prices = new int[]{7,1,5,3,4,6};
        System.out.println(IBestTimeBuyStockII.maxProfit02(prices));
    }

    /**
     优解
     */
    public static int maxProfit02(int[] prices) {
        int maxprofit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1])
                maxprofit += prices[i] - prices[i - 1];
        }
        return maxprofit;
    }
}
