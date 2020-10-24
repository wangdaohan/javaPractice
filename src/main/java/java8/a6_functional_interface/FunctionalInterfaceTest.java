package java8.a6_functional_interface;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * java8最大的特性就是函数式接口 @FunctionalInterface
 *
 * 但是我们使用时，都需要先定义好函数式接口，然后才能在Lambda中使用。
 * java8中自带有以下4大核心函数式接口functional interface，避免用户去新定义接口
 *    1. Consumer<T>: 消费型接口： 有入参，无返回值
 *       场景： 适用于打印，发送短信，等消费动作
 *       典型应用： foreach
 *       void accept(T t);
 *    2. Supplier<T>: 供给型接口： 无入参，有返回值  场景： 无参的工厂方法，即工厂模式创建对象，简单来说就是提供者
 *       T get();
 *    3. Function<T>: 函数型型接口： 有入参，有返回值
 *       - 传入一个值 经过函数的计算返回另一个值  (BiFunction支持传入2个参数）
 *       - 作用: 将转换逻辑提取出来，解耦合
 *       R apply(T t);
 *    4. Predicate<T>: 断言型接口： 有入参，有Boolean类型返回值    - 一般是在需要使用测试t/f时使用
 *       boolean test(T t);
 */
public class FunctionalInterfaceTest {
    public static void main(String[] args) {
        //1. 使用Function<T>
        //用法1：
        FunctionalInterfaceTest.testFunction("acb", a -> "concat_"+a);
        //用法 2：
        FunctionalInterfaceTest.testFunction2("acb", new FunctionImpl());
        //用法 3：
        //第一个String表传入的参数类型， 第2个参数String代表return的值类型
        Function<String,String> testFunction3 = input -> "concat_"+input;
        System.out.println(testFunction3.apply("abd"));

        //2. 使用Predicate<T>
        System.out.println(FunctionalInterfaceTest.testPredicate(18, (a) -> a>30));


        //3. 使用BiFunction
        BiFunction<Integer,Integer,Integer> testBiFunction = (a,b) -> a+b;
        System.out.println(testBiFunction.apply(10,20));


        //4. Consumer 消费者模型，只输入，不返回任何内容
        //场景： 适用于打印，发送短信，等消费动作
        Consumer<String> testConsumer = msg -> System.out.println("send msg"+msg);
        FunctionalInterfaceTest.sendMsg("this is the message",testConsumer);
        FunctionalInterfaceTest.sendMsg("this is the message",msg -> System.out.println("sending msg"+msg));


        //5. Supplier 生产者模型，无输入，只有返回值
        //用途： 泛型类型一定和方法的返回值类型是一种类型，
        //场景： 无参的工厂方法，即工厂模式创建对象，简单来说就是提供者

    }


    public static void sendMsg(String msg, Consumer<String> consumer){
        consumer.accept(msg);
    }

    public static void testFunction(String input, Function fuc){
        System.out.println(fuc.apply(input));
    }

    public static void testFunction2(String input, FunctionImpl func){
        System.out.println(func.apply(input));
    }

    public static Boolean testPredicate(int age, Predicate<Integer> predicate){
        return predicate.test(age);
    }
}

class FunctionImpl implements Function{

    @Override
    public Object apply(Object o) {
        return "concat_"+o;
    }
}
