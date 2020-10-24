package patrick.array.dp;

import java.util.ArrayList;
import java.util.List;
/**
 * EASY
 * https://leetcode.com/problems/pascals-triangle/
 */
/**
 * 杨辉三角 Pascal triangle
 *      [1],
 *     [1,1],
 *    [1,2,1],
 *   [1,3,3,1],
 *  [1,4,6,4,1]
 *
 * 规律：
 * 1.每行开始和结尾的都是1.
 * 2.每个数等于它上方两数之和。
 * 3.每行数字左右对称，由1开始逐渐变大。
 * 4.第n行的数字有n项。
 * 5.前n行共[(1+n)n]/2 个数。
 * 6.第n行的m个数可表示为 C(n-1，m-1)，即为从n-1个不同元素中取m-1个元素的组合数。
 * 7.第n行的第m个数和第n-m+1个数相等 ，为组合数性质之一。
 *
 * 题意：根据给定数字N,生成前n行的杨辉三角
 * 生成逻辑：每一行的首个和结尾一个数字都是1，从第三行开始，中间的每个数字都是上一行的左右两个数字之和
 */
public class FPascalTriangle {
    public static void main(String[] args) {
        System.out.println(FPascalTriangle.generate01(5));
    }
    /*
    * Time complexity : O(numRows^2)
    * Space complexity: O(numRows^2)
    * */
    public static List<List<Integer>> generate01(int rows){
        List<List<Integer>> result = new ArrayList<>();
        if(rows==0){
            return result;
        }
        //第一行是1, row=0
        result.add(new ArrayList<>());
        result.get(0).add(1);

        for(int i=1; i<rows;i++){
            List<Integer> rowResult = new ArrayList<>();
            List<Integer> prevRow = result.get(i-1);
            rowResult.add(1);  //每一行的开始数字都是1
            //第n行的数字都有n个，也是是N列
            //跳过第一个和最后一个数字，因为都是固定值1
            for(int colNum=1;colNum<i;colNum++){
                rowResult.add(prevRow.get(colNum-1)+prevRow.get(colNum));
            }
            rowResult.add(1);  //第一行的结尾都是1

            result.add(rowResult);
        }

        return result;
    }
}
