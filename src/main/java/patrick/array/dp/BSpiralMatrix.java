package patrick.array.dp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * MEDIUM
 * https://leetcode.com/problems/spiral-matrix/
 */
/*
Given a matrix of m x n elements (m rows, n columns), return all elements of the matrix in spiral order.
Input:
[
  [1, 2, 3, 4],
  [5, 6, 7, 8],
  [9,10,11,12]
]
Output: [1,2,3,4,8,12,11,10,9,5,6,7]
5*7 matrix
 [01, 02, 03, 04, 05, 06, 07]
 [20, 21, 22, 23, 24, 25, 08]
 [19, 32, 33, 34, 35, 26, 09]
 [18, 31, 30, 29, 28, 27, 10]
 [17, 16, 15, 14, 13, 12, 11]

螺旋矩阵
   1. spiral matrix 按螺旋方式输出一个矩阵数组
 */
public class BSpiralMatrix {
    public static void main(String[] args) {
        BSpiralMatrix BSpiralMatrix = new BSpiralMatrix();
        int[][] matrix = new int[][]{{1, 2, 3, 4, 5, 6, 7}, {20, 21, 22, 23, 24, 25, 8}, {19, 32, 33, 34, 35, 26, 9}, {18, 31, 30, 29, 28, 27, 10}, {17, 16, 15, 14, 13, 12, 11}};
        System.out.println(BSpiralMatrix.spiralOrder01(matrix));
        System.out.println(BSpiralMatrix.spiralOrder02(matrix));
    }

    public List<Integer> spiralOrder02(int[][] matrix) {
        List result = new ArrayList();
        if (matrix.length == 0) return result;
        int totalRow = matrix.length, totalCol= matrix[0].length;
        //seen数组作用： 不重复加入在转
        boolean[][] visited = new boolean[totalRow][totalCol];
        /*
         定义上下左右 4条边的方向，
         上： row,col  0,1     :代表 row+0, col+1
         右： row,col  1,0   由上到下输出，row+1, col+0保持不变
         下： row,col  0,-1   逆序输出，row+0不变 col-1 每次-1  (0, col-1)
         左： row,col  -1,0   逆序输入，row每次-1 row+0不变
         假设 5*7matrix
         开始由上开始，startRow=0 startCol = 0, matrix[0][0] -> matrix[0]6]  -> startRow=0 startCol=6
         要换边,换到右边: 如何找到右边对应directionOnRow和directionOnCol的下标index呢？
                        算法： directionIndex = (directionIndex+1)%4
                        startRow += directionOnRow[directionIndex]
                        startCol += directionOnRow[directionIndex]
        */
        int[] directionOnRow = {0, 1,  0, -1}; //index =0:上; index=1：右; index=2：下 index=3: 左
        int[] directionOnCol = {1, 0, -1,  0}; //index =0:上; index=1：右; index=2：下 index=3: 左
        int startRow = 0, startCol = 0;
        int directionIndex = 0;  // 0 代表上，从上=0先开始，然后是右， -> 下  ->  左
        //后面如何判断 右 下 下 左呢？
        //首先 directionOnRow，directionOnCol就是按上，右，下，左排列的
        //然后，只需要每次切换方向的时候 根据directionIndex找到对应上，右，下，左所在的Index，即0,1,2,3
        //转换代码就是directionIndex = (directionIndex + 1) % 4;
        for (int i = 0; i < totalRow * totalCol; i++) {
            result.add(matrix[startRow][startCol]);
            visited[startRow][startCol] = true;
            int nextRow = startRow + directionOnRow[directionIndex];
            int nextCol = startCol + directionOnCol[directionIndex];
            //判断是否需要换行了：标准是：nextRow在[0,totalRow] 且 nextCol在[0, totalCol] 且没被 visited
            if (0 <= nextRow && nextRow < totalRow && 0 <= nextCol && nextCol < totalCol && !visited[nextRow][nextCol]) {
                startRow = nextRow;
                startCol = nextCol;
            } else {
                //需要换方向了，
                directionIndex = (directionIndex + 1) % 4;   //关键： 判断是上下左右
                startRow += directionOnRow[directionIndex];
                startCol += directionOnCol[directionIndex];
            }
        }
        return result;
    }

    public List<Integer> spiralOrder01(int[][] matrix) {
        List result = new ArrayList();
        /*
        对于这种螺旋遍历的方法，重要的是要确定上下左右四条边的位置，
        那么初始化的时候，上边 up 就是0，下边 down 就是 m-1，
        左边 left 是0，右边 right 是 n-1。
        然后进行 while 循环，先遍历上边，将所有元素加入结果 res，然后上边下移一位，
        如果此时上边大于下边，说明此时已经遍历完成了，直接 break。
         */
        int rowNumber = matrix.length; //行数
        int colNumber = matrix[0].length; //列数
        //螺旋式输出 一共多少圈,4舍5入
        // 5*7 -> 3圈
        // 3*4 -> 2圈
        int roundNumber = new BigDecimal
                (Math.min(rowNumber, colNumber)).divide(
                        new BigDecimal(2)).setScale(0, BigDecimal.ROUND_HALF_UP)
                .intValue();
        int currentRow = rowNumber;//p
        int currentCol = colNumber;//q

        for (int currentRound = 0; currentRound < roundNumber; ++currentRound, currentRow -= 2, currentCol -= 2) {
            //每一圈都要确定上下左右4条边的位置，
            // 上：从前往后顺序输出，
            // 右：从上到下输出
            // 下：从后往前逆序输出
            // 左：从下到上输入

            // 上
            for (int col = currentRound; col < currentRound + currentCol; ++col) {
                result.add(matrix[currentRound][col]);
            }
            //右
            for (int row = currentRound + 1; row < currentRound + currentRow; ++row) {
                result.add(matrix[row][currentRound + currentCol - 1]);
            }
            if (currentRow == 1 || currentCol == 1) break;

            //下
            for (int col = currentRound + currentCol - 2; col >= currentRound; --col) {
                result.add(matrix[currentRound + currentRow - 1][col]);
            }

            //左
            for (int row = currentRound + currentRow - 1; row > currentRound; --row) {
                result.add(matrix[row][currentRound]);
            }
        }
        return result;
    }



}
