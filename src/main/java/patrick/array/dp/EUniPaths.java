package patrick.array.dp;

import java.util.Arrays;
/**
 * MEDIUM
 * https://leetcode.com/problems/unique-paths/
 *
 * 和AMinimumPathSum重复？
 */

/**
 * A robot is located at the top-left corner of a m x n grid (marked 'Start' in the diagram below).
 *
 * The robot can only move either down or right at any point in time. The robot is trying to reach the bottom-right corner of the grid (marked 'Finish' in the diagram below).
 *
 * How many possible unique paths are there?
 * Above is a 7 x 3 grid. How many possible unique paths are there?
 *
 * Note: m and n will be at most 100.
 *
 * Example 1:
 *
 * Input: m = 3, n = 2
 * Output: 3
 * Explanation:
 * From the top-left corner, there are a total of 3 ways to reach the bottom-right corner:
 * 1. Right -> Right -> Down
 * 2. Right -> Down -> Right
 * 3. Down -> Right -> Right
 * Example 2:
 *
 * Input: m = 7, n = 3
 * Output: 28
 *
 *
 * 这个问题关键是看懂题目意思，并且能快速的反应到代码上来。
 *  此题的意思是 求一个m*n的二维数组(矩阵）从开始点（0,0)到 (m,n) 一共有多少条路径？？
 *
 *        前提是： 寻找路径时，只能往下或往右边。 也就是直来直去，不能随意绕弯，不然无解了。无数条路径
 前提映射到代码就是： 初始一个m*n的数组，全部初始值为1，
                    除了i==0 或 j==0 （因为是起始位置，不会有其它路径交叉，所以都是==1）
                    所有经过某个点的路径总数为： matrix[i][j] = matrix[i-1][j] + matrix[i][j-1]

 */
public class EUniPaths {
    public static void main(String[] args) {
        EUniPaths uniPaths = new EUniPaths();
        System.out.println(uniPaths.uniPath(7,3));
        System.out.println(uniPaths.uniPath02(7,3));
    }

    /*
    较优解法

    DP 动态路劲规划法
    Time complexity: O(N×M).
    Space complexity: O(N×M).
     */
    public int uniPath(int row, int col){
        int[][] matrix = new int[row][col];
        for(int[] arr:matrix){
            Arrays.fill(arr,1);
        }
        //从1,1开始，跳过（0,0)(0,1)(1,0) 因为默认是1
        for(int i = 1 ;i < row;i++){
            for(int j = 1; j< col;j++){
                matrix[i][j] = matrix[i-1][j] + matrix[i][j-1];
            }
        }
        return matrix[row-1][col-1];

    }

    /*
    较次解法

    recursion 递归的方法解决
    缺点：速度没有上面的解法快

     */
    public int uniPath02(int m, int n){
        if(m==1 || n==1){
            return 1;
        }
        return uniPath02(m-1,n)+uniPath02(m,n-1);
    }


}
