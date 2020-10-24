package patrick.array.twopointers;

/**
 * MEDIUM
 * https://leetcode.com/problems/container-with-most-water/
 */

/**
 * Given n non-negative integers a1, a2, ..., an , where each represents a point at coordinate (i, ai). n vertical lines are drawn such that the two endpoints of line i is at (i, ai) and (i, 0). Find two lines, which together with x-axis forms a container, such that the container contains the most water.
 * <p>
 * Note: You may not slant the container and n is at least 2.
 * <p>
 * 能容纳多少水
 * <p>
 * 这道求装最多水的容器的题和那道 Trapping Rain Water 很类似，
 * 但又有些不同，那道题让求整个能收集雨水的量，
 * 这道只是让求最大的一个的装水量，而且还有一点不同的是，那道题容器边缘不能算在里面，
 * 而这道题却可以算，相比较来说还是这道题容易一些，
 * 这里需要定义i和j两个指针分别指向数组的左右两端，
 * 然后两个指针向中间搜索，每移动一次算一个值和结果比较取较大的，
 * <p>
 * 容器装水量的算法是找出左右两个边缘中较小的那个乘以两边缘的距离，代码如下：
 */
public class AMostWaterInContainer {
    public static void main(String[] args) {
        AMostWaterInContainer AMostWaterInContainer = new AMostWaterInContainer();
        System.out.println(AMostWaterInContainer.mostWaterKeepInContain2(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}));
        System.out.println(AMostWaterInContainer.mostWaterKeepInContain(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}));
    }

    /*
    solution 2
    O(n)
    思路： 也是2个指针思路
         start=0  end = length-1
         size 取 size 和start-end之间的容量 最大值
         当start的值<end的值，start向后移 ++start
           end的值<start的值，end向前移  --end
     */
    public int mostWaterKeepInContain2(int[] heights) {
        int start = 0;
        int length = heights.length;
        int end = length - 1;
        int size = 0;
        while (start < end) {
            size = Math.max(size, Math.min(heights[start], heights[end]) * (end - start));
            if (heights[start] < heights[end]) ++start;
            else --end;
        }
        return size;
    }

    /*
    solution 1
      思路：
        最慢方便，2个循环嵌套，找到最大的容量值
        O(N^2)
     */
    public int mostWaterKeepInContain(int[] heights) {
        int length = heights.length;
        int size = 0;
        for (int start = 0; start < length; start++) {
            for (int end = start + 1; end < length; end++) {
                int maxHeight = Math.min(heights[start], heights[end]);
                size = Math.max(size, maxHeight * (end - start));
            }
        }
        return size;
    }

}
