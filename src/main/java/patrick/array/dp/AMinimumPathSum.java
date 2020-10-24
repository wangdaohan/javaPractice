package patrick.array.dp;

/**
 * MEDIUM
 * https://leetcode.com/problems/minimum-path-sum/
 */
/*
Given a m x n grid filled with non-negative numbers,
find a path from top left to bottom right which minimizes the sum of all numbers along its path.
Note: You can only move either down or right at any point in time.

Example:

Input:
[
  [1,3,1],
  [1,5,1],
  [4,2,1]
]
Output: 7
Explanation: Because the path 1 → 3 → 1 → 1 → 1 minimizes the sum.
题本意：
  1.从最开始top left [0][0] 到 最底部bottom right [2][2] ，求出所有路径中，总值最小的路线，并返回总值sum
  2.只能往右或者往下方向走，也是就不能往左边或往上走

思路：1. backtracking 也就是recursion方法
     2. dp 动态路径规划 较优

 */
public class AMinimumPathSum {
    public static void main(String[] args) {
        AMinimumPathSum AMinimumPathSum = new AMinimumPathSum();
        int[][] matrix = new int[][]{{1, 3, 1},{1,5,1},{4,2,1}};
        System.out.println(AMinimumPathSum.minPathSum01(matrix));
        System.out.println(AMinimumPathSum.minPathSum02(matrix));
    }
    /*
    solution2: DP 动态规划法
       Time complexity : O(mn)
       Space complexity : O(mn)

       In fact, there is nothing fancy about DP.
       It is simply that we store or cache the results of every single calculation
       so that we don't need to calculate the same thing again and again.
       The whole idea is almost the same.

       使用原有的矩阵来存储每一个结果
     */
    public int minPathSum02(int[][] matrix){
        int rowLength = matrix.length;
        int colLength = matrix[0].length;
        for(int row=0;row<rowLength;row++){ //行
            for(int col=0; col<colLength; col++){ //列
                if(row==0 && col==0) matrix[row][col] = matrix[row][col];
                else if(row==0 && col!=0) matrix[row][col] = matrix[row][col] + matrix[row][col-1];
                else if(row!=0 && col==0) matrix[row][col] = matrix[row][col] + matrix[row-1][col];
                else matrix[row][col] = matrix[row][col] + Math.min(matrix[row-1][col],matrix[row][col-1]);
            }
        }
        return matrix[rowLength-1][colLength-1];
    }

    /*
    solution1:
       Time complexity : O(2^(m+n))
       Space complexity : O(m+n)

       recursion 递归方法
       解题思路：
          1. 累计加上矩阵
       详细：
          1. 传入由总行数rowLength，总列数colLength，进入递归方法findMin
          2. 如果 row==0 && col == 0 返回matrix[row][col] - 递归方法的出口
             如果 row==0 && col != 0, 返回matrix[row][col] + matrix[row][col-1]
             如果 row!=0 && col == 0, 返回matrix[row][col] + matrix[row-1][col]
             如果 以上都不是， 返回matrix[row][col] + Math.min(findMin(matrix, row-1, col), findMin(matrix, row, col-1)
     */
    public int minPathSum01(int[][] matrix){
        int rowLength = matrix.length;
        int colLength = matrix[0].length;
        return findMin(matrix, rowLength-1,colLength-1);
    }

    public int findMin(int[][] matrix, int rowLength, int colLength){
        if(rowLength==0 && colLength==0) return matrix[0][0];
        if(rowLength==0) return matrix[rowLength][colLength] + findMin(matrix, 0, colLength-1);
        if(colLength==0) return matrix[rowLength][colLength] + findMin(matrix, rowLength-1, 0);
        return matrix[rowLength][colLength] + Math.min(findMin(matrix,rowLength-1, colLength), findMin(matrix,rowLength, colLength-1));
    }



}
