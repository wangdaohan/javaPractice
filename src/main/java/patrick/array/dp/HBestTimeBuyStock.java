package patrick.array.dp;

/**
 * EASY
 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
 */

/**
 * 题意：
 *    给定一个数组，index代表日期，value是该日期的价格
 *    设计算法计算最大的获利
 *    只允许一次买一次卖，不允许多次买和卖
 * 前提：顺序只能是先买后卖
 *
 * 解读：即迭代每一个值(买入价）
 *          然后寻找该值以后后面的数字，寻找(后面数字-买入价)>0 （获利）
 *      在以上获利中寻找最大值
 */
public class HBestTimeBuyStock {
    public static void main(String[] args) {

    }
    /**
    优解
    */
    public int maxProfit02(int prices[]) {
        int minprice = Integer.MAX_VALUE;
        int maxprofit = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < minprice)
                minprice = prices[i];
            else if (prices[i] - minprice > maxprofit)
                maxprofit = prices[i] - minprice;
        }
        return maxprofit;
    }

    /**
     * 最差解
     */
    public int maxProfit(int prices[]) {
        int maxprofit = 0;
        for (int i = 0; i < prices.length - 1; i++) {
            for (int j = i + 1; j < prices.length; j++) {
                int profit = prices[j] - prices[i];
                if (profit > maxprofit)
                    maxprofit = profit;
            }
        }
        return maxprofit;
    }

}
