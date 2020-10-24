package patrick.array;
import java.util.Arrays;

/**
 * 不明白此题的意义
 * EASY
 * https://leetcode.com/problems/di-string-match/
 * https://blog.csdn.net/iwts_24/article/details/84436773
 *
 给出一个字符串S，S全部由‘D’和‘I’组成，
 然后现在需要解出一个数组，而这个数组需要和S相匹配。
 假设S的长度为N，那么这个数组的长度是N+1，并且值是集合{0...N}的任意排列。
 给出一个例子，假设有字符串S1 = "DIDID"，那么S1的长度为5，
 那么答案就必须是集合{0,1,2,3,4,5}的一个排序，可能是{5,4,3,2,1,0}，也可能是{0,1,3,5,2,4}等等。
 而这个排序必须是与字符串S相匹配的。
 *
 * 要求：
 *  如果S[i] =='I'，那么A[i] < A[i + 1]。
 *  如果S[i] =='D'，那么A[i] > A[i + 1]。
 *  那么我们可以设定两个值，初始的话：low = 0，high = N。
 *  这样，从左开始遍历字符串，碰见一个字符，
 *      如果是‘I’，那么就直接赋值low，同时low++。这样，‘I’右边所有的数，一定是都比这个位置大的。因为此时low>a[i]，同时high > low。
 *      反而言之，碰见‘D’，直接赋值hight，同时high--。这样所有的数就一定比这个小了。大概就是这样，在O(n)的时间复杂度下就能构造出答案数组。
 * 例如：
 * 输入：“IDID”
 * 输出：[0,4,1,3,2]
 *
 * 注意：
 * 1 <= S.length <= 10000
 * S仅包含字符I或D。
 */
public class DIString {
    public static void main(String[] args) {
        Arrays.stream(DIString.diStringMatch("IDID")).forEach(System.out::println);
    }

    public static int[] diStringMatch(String S) {
        int max = S.length();
        int len = S.length();
        int min = 0;
        int[] ans = new int[max + 1];
        for (int i = 0; i < len; i++) {
            char c = S.charAt(i);
            if (c == 'D') {
                ans[i] = max--;
            } else {
                ans[i] = min++;
            }
        }
        ans[len] = min;
        return ans;
    }
}
