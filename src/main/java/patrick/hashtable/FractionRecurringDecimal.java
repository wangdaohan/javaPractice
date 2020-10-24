package patrick.hashtable;


import java.util.HashMap;
import java.util.Map;
/**
 * MEDIUM
 * https://leetcode.com/problems/fraction-to-recurring-decimal/
 * 题目大意
 *    计算两个整数的除法，结果可能是小数。如果是循环小数，那么就把循环的部分用括号括起来。
 * Input: numerator = 1, denominator = 2
 * Output: "0.5"

 * Input: numerator = 2, denominator = 1
 * Output: "2"
 *
 * Input: numerator = 2, denominator = 3
 * Output: "0.(6)"
 *
 *分析：这里面需要考虑下面几个问题：
 * 1、如何循环求数？
 * 2、如何判断重复？
 * 3、如何结束循环？
 * 4、会不会溢出？
 */
public class FractionRecurringDecimal {
    public static void main(String[] args) {
        System.out.println(FractionRecurringDecimal.fractionToDecimal(5,3));
    }

    public static String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0) {
            return "0";
        }
        StringBuilder fraction = new StringBuilder();
        // If either one is negative (not both)
        if (numerator < 0 ^ denominator < 0) {
            fraction.append("-");
        }
        // Convert to Long or else abs(-2147483648) overflows
        long dividend = Math.abs(Long.valueOf(numerator));
        long divisor = Math.abs(Long.valueOf(denominator));
        fraction.append(String.valueOf(dividend / divisor));
        long remainder = dividend % divisor; //取余数
        if (remainder == 0) {
            return fraction.toString();
        }
        fraction.append(".");
        Map<Long, Integer> map = new HashMap<>();
        while (remainder != 0) {
            if (map.containsKey(remainder)) {
                fraction.insert(map.get(remainder), "(");
                fraction.append(")");
                break;
            }
            map.put(remainder, fraction.length());
            remainder *= 10;
            fraction.append(String.valueOf(remainder / divisor));
            remainder %= divisor;
        }
        return fraction.toString();
    }
}
