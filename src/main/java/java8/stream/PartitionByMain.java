package java8.stream;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PartitionByMain {
    public static void main(String[] args) {
        //需求: 根据list里面进行分组，字符串长度大于4的为一组， 其它为另外一组
        Map<Boolean, List<String>> result = Stream.of("java","sql","css4","HTML5","springboot").collect(Collectors.partitioningBy(obj->obj.length()>4));

        System.out.println(result);

    }
}
