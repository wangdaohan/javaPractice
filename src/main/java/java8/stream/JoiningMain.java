package java8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JoiningMain {
    public static void main(String[] args) {
        //拼接函数 Collectors.joining
        List<String> list = Arrays.asList(" 234","sdf","asfalsf");

        String result = list.stream().collect(Collectors.joining());
        System.out.println(result);

        String result2 = list.stream().collect(Collectors.joining("||||"));
        System.out.println(result2);


        String result3 = list.stream().collect(Collectors.joining("||||","{","}"));
        System.out.println(result3);

        System.out.println(Stream.of("springboot","mysql","htms5","css5").collect(Collectors.joining("::::")));
    }
}
