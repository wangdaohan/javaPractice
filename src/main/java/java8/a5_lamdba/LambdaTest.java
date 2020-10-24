package java8.a5_lamdba;


import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * lambda 即函数式编程 functional interface @FunctionalInterface
 *    JAVA8之前： java不支持函数式编程的，
 *    java8之后： 所谓的函数编程即可理解为将一个函数作为一个参数进行传递
 *    new Thread(()-> System.out.println("新式创建方法")).start();
 *
 * lambda表达式：
 * 函数式接口
 * 1. 使用场景： 一个接口中只包含一个方法，则可以使用Lambda表达式，这样的接口也称为"函数式接口"
 * 2. 语法： (params) -> expression
 *
 *
 * 好处:
 *   1.Lambda表达式的实现方式在本质是以***匿名内部类的方式***进行实现 Anonymous Inner Class
 *   2.重构臃肿的代码，更高的开发效率
 *   3.
 */
public class LambdaTest {
    public static void main(String[] args) {
        //java8之前
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("老式创建方法");
            }
        }).start();
        List<String> list = Arrays.asList("aaa","ggg","fff","ccc");
        //旧式方法
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });

        //Java8之后
        //使用lambda
        new Thread(()-> System.out.println("新式创建方法")).start();
        //使用lambda
        Collections.sort(list,(a,b)-> b.compareTo(a));

    }
}
