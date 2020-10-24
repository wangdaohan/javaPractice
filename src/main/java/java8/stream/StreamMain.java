package java8.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StreamMain {
    public static void main(String[] args) {
        //1. Sorted
        List<String> list = Arrays.asList("springboot","springcloud","reids","jetty","java","docker");
        List<String> sortedList = list.stream().sorted(Comparator.comparing(obj->obj.length(),Comparator.reverseOrder())).collect(Collectors.toList()); //按长度
        sortedList = list.stream().sorted(Comparator.comparing(String::length).reversed()).collect(Collectors.toList());
        //2. Limit 截断流
        sortedList = list.stream().sorted(Comparator.comparing(String::length).reversed()).limit(3).collect(Collectors.toList());
        //3. allMatch:match所有 / anyMatch：match一个就行
        boolean flag = list.stream().allMatch(obj->obj.length()>6); //要所有元素长度都大于6才会是true
        flag = list.stream().anyMatch(obj->obj.length()>5);

    }
}
