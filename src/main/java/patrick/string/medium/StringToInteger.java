package patrick.string.medium;
/**
 * MEDIUM
 * https://leetcode.com/problems/string-to-integer-atoi/

 Implement atoi which converts a string to an integer.

ascii to integer
字符 转 数字

Input: "42"
Output: 42

Input: "   -42"
Output: -42

Input: "4193 with words"
Output: 4193

Input: "-91283472332"
Output: -2147483648
Explanation: The number "-91283472332" is out of the range of a 32-bit signed integer.
             Thefore INT_MIN (−231) is returned.
 \
Input: "words and 987"
Output: 0
 */
public class StringToInteger {
    public static void main(String[] args) {
        StringToInteger stringToInteger = new StringToInteger();
        System.out.println(stringToInteger.myATOI("-450011"));
    }

    /**
     * Complexity analysis:
     *    Time: O(n);
     *    Space: O(1)
     注意点：
     1. 取出的char是ascii字符码，如 数字0的ascii是48，因此取出后在 - '0' （48）才能得出实际数字
     2. 要判断Int值不超过MAX_VALUE 和 MIN_VALUE
     3. 每一次循环累计值都 * 10 这样就可以加上下一个数字

     思路 ：
     1. 很简单，按每个char取出字符中的数字 （注意： 取出的char是ascii字符码，如 数字0的ascii是48，因此取出后在 - '0' （48）才能得出实际数字
     2. 每一次循环累计值都 * 10 这样就可以加上下一个数字
     3. 注意：要判断Int值不超过MAX_VALUE 和 MIN_VALUE
     */

    public int myATOI(String str){
        int intValue= 0;
        str = str.trim();  //del space from leading or tailing
        boolean isNegative = false;
        for(int i=0; i<str.length();i++){
            char currChar = str.charAt(i);   // 返回的currChar 是ascii 码，如'4' 的ascii是52
            if(i==0 && (currChar=='-' || currChar== '+')){
                isNegative = (currChar == '-');
            }else if(Character.isDigit(currChar)){ //判断是否是数字
                //Check digit
                //0~9数字对应十进制48－57
                //a~z字母对应的十进制97－122
                //A~Z字母对应的十进制65－90
                int currDigit = currChar - '0';  // ascii to number ,如'4' 的ascii是52， 52-'0'（48） = 4
                intValue *= 10; //向前进一位，以便加下一个数字
                if(!isNegative && intValue > (Integer.MAX_VALUE - currDigit)){
                    return Integer.MAX_VALUE;
                }
                else if(isNegative && -intValue < (Integer.MIN_VALUE + currDigit)){
                    return Integer.MIN_VALUE;
                }
                else{
                    intValue += currDigit;
                }

            }else{
                break;
            }
        }
        return isNegative?-intValue:intValue;
    }
}
