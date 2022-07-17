package com.patrick.disruptoromssettlement.util;

public class IDConverter {

    public static long combineInt2Long(int high, int low) {
        //java long占64位,8个字节，因为一个字节等于8位
        //java int占32位,4个字节
        // high + low 拼接成Long
        // high 左移32移 ， low填到低位的32位数上。
        // 按位与： 0xFFFFFFFF00000000L ， 确保低位上32位数全为0
        return ((long) high << 32 & 0xFFFFFFFF00000000L) | ((long) low & 0xFFFFFFFFL);
    }

    public static int[] seperateLong2Int(long val) { //将一个long值 拆分为2个Int值
        int[] res = new int[2];
        res[1] = (int) (0xFFFFFFFFL & val);//低位
        res[0] = (int) ((0xFFFFFFFF00000000L & val) >> 32);//高位
        return res;
    }

    public static void main(String[] args){
        int high = 1001;
        int low = 200;
        long l = IDConverter.combineInt2Long(high,low);
        int[] ints = IDConverter.seperateLong2Int(l);
        System.out.println(ints[0]);
        System.out.println(ints[1]);

        //System.out.println((long)high << 32 & 0xFFFFFFFF00000000L);
    }

}
