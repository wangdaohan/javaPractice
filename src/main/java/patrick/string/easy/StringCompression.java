package patrick.string.easy;

/**
 * EASY
 * https://leetcode.com/problems/string-compression/
 * 字符串压缩 - 将给定字符串 替换/压缩 重复字符后面跟重复次数  即 aaabbbcc -> a3 a b3 b c2
 * 要求： 1. in-place进行 => 即不使用额外空间
 *
 * 思路： 3个指针
 *       1. read 顺序读取数组
 *       2. start 浮标 - 记录每组相同字符的开始位置 （计算相同字符个数时用到： read-anchor+1)
 *       3. wirte (1)用于将同字符个数 写到第一个字符后面 即 ['a','a','b','b','c','c','c'] -> ['a','2','b','2','c','3','c']
 *                (2)同时记录新数组的大小
 */
public class StringCompression {
    public static void main(String[] args) {
        char[] testChar01 =new char[]{'a','a','b','b','b','c','c','c'};
        char[] testChar02 =new char[]{'a','b','b','b','b','b','b','b','b','b','b','b','b'};
       System.out.println(StringCompression.stringCompress(testChar01));
        System.out.println(StringCompression.stringCompress(testChar02));

    }

    /**
     * 思路：1. 3个指针：
     *         i=0 用于顺序读取数组
     *         start=0 浮标 - 用于记录相同字符的开始位置 ，当chars[i]!= chars[i+1]时,start往前移 -》 start = read+1
     *         write: 记录应该在哪里写统计的个数，write = start+1的位置
     */
    public static int stringCompress(char[] chars){
        int start=0,write=0;
        for(int i=0; i<chars.length; i++){
            if(i+1==chars.length || chars[i+1] != chars[i]) {
                write = start+1;
                if(i > start){
                    //将统计个数转成char数组，以方便放进原数组中，如12，转成['1','2']
                    char[] countChar = ("" + (i - start +1)).toCharArray();
                    for(char c : countChar) {
                        chars[write++] = c;
                    }
                }
                start = i + 1;
            }
        }
        System.out.println(chars);
        return write;
    }
}
