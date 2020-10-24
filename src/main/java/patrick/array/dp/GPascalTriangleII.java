package patrick.array.dp;
import java.util.ArrayList;
import java.util.List;
/**
 * EASY
 * https://leetcode.com/problems/pascals-triangle-ii/
 * 杨辉三角 Pascal triangle,上一题的扩展FPascalTriangle
 *
 * 题意：根据给定数字N,只返回第N行的的杨辉三角，
 *      并且控制space complexity在o(n)（意思就是不能生成所有行，然后返回第n行）
 * 生成逻辑：
 * 利用其性质：除了第1行，其它行的值公式 = (当前行 - 当前列+1)/当前列  * 前一个数字
 */
public class GPascalTriangleII {
    public static void main(String[] args) {
        System.out.println(GPascalTriangleII.getRow(4));
    }
    /*
    * Time complexity : O(numRows^2)
    * Space complexity: O(numRows^2)
    * */
    public static List<Integer> getRow(int rowNum) {
        List<Integer> row =  new ArrayList<Integer>();
        row.add(1);  //每行第一个数都是1

        for (int colNum = 1; colNum <= rowNum; colNum++) {
            //利用其性质：除了第1行，其它行的值公式 = (当前行 - 当前列+1)/当前列  * 前一个数字
            row.add((int) ((row.get(row.size() - 1) * (long) (rowNum - colNum + 1)) / colNum));
        }

        return row;
    }


}
