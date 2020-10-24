package patrick.string.hard;

import java.util.HashMap;
import java.util.Map;
/**
 * HARD
 *https://www.cnblogs.com/grandyang/p/4340948.html
 * 下面来考虑用代码来实现，先来回答一下前面埋下的伏笔，为啥要用 HashMap，而不是 HashSet，现在应该很显而易见了吧，
 * 因为要统计T串中字母的个数，而不是仅仅看某个字母是否在T串中出现。
 * 统计好T串中字母的个数了之后，开始遍历S串，对于S中的每个遍历到的字母，都在 HashMap 中的映射值减1，如果减1后的映射值仍大于等于0，
 * 说明当前遍历到的字母是T串中的字母，使用一个计数器 cnt，使其自增1。当 cnt 和T串字母个数相等时，
 * 说明此时的窗口已经包含了T串中的所有字母，此时更新一个 minLen 和结果 res，这里的 minLen 是一个全局变量，
 * 用来记录出现过的包含T串所有字母的最短的子串的长度，结果 res 就是这个最短的子串。
 * 然后开始收缩左边界，由于遍历的时候，对映射值减了1，所以此时去除字母的时候，就要把减去的1加回来，此时如果加1后的值大于0了，
 * 说明此时少了一个T中的字母，那么 cnt 值就要减1了，然后移动左边界 left。
 * 你可能会疑问，对于不在T串中的字母的映射值也这么加呀减呀的，真的大丈夫（带胶布）吗？
 * 其实没啥事，因为对于不在T串中的字母，减1后，变-1，cnt 不会增加，之后收缩左边界的时候，映射值加1后为0，cnt 也不会减少，
 * 所以并没有什么影响啦，下面是具体的步骤啦：
 *
 * - 先扫描一遍T，把对应的字符及其出现的次数存到 HashMap 中。
 *
 * - 然后开始遍历S，就把遍历到的字母对应的 HashMap 中的 value 减一，如果减1后仍大于等于0，cnt 自增1。
 *
 * - 如果 cnt 等于T串长度时，开始循环，纪录一个字串并更新最小字串值。然后将子窗口的左边界向右移，如果某个移除掉的字母是T串中不可缺少的字母，那么 cnt 自减1，表示此时T串并没有完全匹配。
 */
public class MinimumWindowSubstring {
    public static void main(String[] args) {
        System.out.println(MinimumWindowSubstring.minWindow("AADOBBECCODEBANC", "ABC"));
    }

    /**
     * 思路：1. 用一个长为255（ascii码）的int数组来统计target字符串的次数  index代表字符在ascii码中的位置 value=出现次数
     *      2. i 用于迭代source字符串
     *         begin = -1 end =source.len  //记录符合条件子字符串的位置
     *         start = 0 //记录每一轮最小子字符串的开始位置
     */
    public static String minWindow(String source, String target) {
        //ascii表 a-z 97-122 A-Z 65090
        int[] targetCount = new int[255];
        // 记录目标字符串每个字母出现次数
        for(int i = 0; i < target.length(); i++){
            targetCount[target.charAt(i)]++;
        }
        int start = 0,i= 0;
        // 用于记录窗口内每个字母出现次数
        int[] sourceCount = new int[255];
        int found = 0;
        int begin = -1, end = source.length();
        int minLength = source.length();
        for(start = i = 0; i < source.length(); i++){
            // 每来一个字符给它的出现次数加1
            sourceCount[source.charAt(i)]++;
            // 如果加1后这个字符的数量不超过目标串中该字符的数量，则找到了一个匹配字符
            if(targetCount[source.charAt(i)] >= sourceCount[source.charAt(i)]) found++;
            // 如果找到的匹配字符数等于目标串长度，说明找到了一个符合要求的子串
            if(found == target.length()){
                // 将没用的字符都跳过，没用是指（1） 该字符出现次数超过了target字符串中出现的次数，并把它们出现次数都减1
                //                          (2)  该字符没有在target字符串中出现
                while(start < i && sourceCount[source.charAt(start)] > targetCount[source.charAt(start)]){
                    sourceCount[source.charAt(start)]--;
                    start++;
                }
                // 这时候start指向该子串开头的字母，判断该子串长度
                if(i - start < minLength){
                    minLength = i - start;
                    begin = start;
                    end = i;
                }
                // 把开头的这个匹配字符跳过，并将匹配字符数减1
                sourceCount[source.charAt(start)]--;
                found--;
                // 子串起始位置加1，我们开始看下一个子串了
                start++;
            }
        }
        // 如果begin没有修改过，返回空
        return begin == -1 ? "" : source.substring(begin,end + 1);
    }
}
