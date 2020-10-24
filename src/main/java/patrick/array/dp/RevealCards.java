package patrick.array.dp;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**MEDIUM
 * https://leetcode.com/problems/reveal-cards-in-increasing-order/
 *
 * 模拟发牌的顺序：
 *    1.发牌是翻开一张牌，然后把下一张牌移到末尾，然后再翻下一张牌，再移动下一张牌到末尾，
 *    2.发牌的的值是有序的建立一个数组，把所有 index 按顺序装入队列，
 *
 *   即 如果：【17,13,11,2,3,5,7】 -》其对应的发牌顺序组合为[2, 13, 3, 11, 5, 17, 7]
 */

public class RevealCards {
    public static void main(String[] args) {
        int[] result = RevealCards.deckRevealedIncreasing(new int[]{17,13,11,2,3,5,7});
        System.out.println(Arrays.toString(result));
    }

    public static int[] deckRevealedIncreasing(int[] deck) {
        int n= deck.length;
        Arrays.sort(deck); //排序
        Queue<Integer> q= new LinkedList<>();
        for (int i=0; i<n; i++) q.add(i);  //放入index值
        int[] res= new int[n];
        for (int i=0; i<n; i++){
            //q.poll(); retrieve and remove head
            res[q.poll()]=deck[i];
            q.add(q.poll());
        }
        return res;
    }
}
