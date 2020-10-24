package java8.reduce;

import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class ReduceMain {
    public static void main(String[] args) {

        //reduce: 聚合操作，减少-》即计算后返回唯一值
        //参数： BinaryOperator
        //实例：累加
        int value = Stream.of(1,2,3,4,5).reduce((item1,item2)->item1+item2).get();
        System.out.println(value);
        //同上功能
        int value02 = Stream.of(1,2,3,4,5).reduce(new BinaryOperator<Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer+integer2;
            }
        }).get();


       int value03 = Stream.of(1,2,3,4,5).reduce(100, (item1,item2)->item1+item2);
       System.out.println(value03);
        //寻找最大值
        int maxValue = Stream.of(1654,1234,123412,1234,100000).reduce((item1,item2) -> item2>item1?item2:item1).get();
        System.out.println(maxValue);



    }
}
