package java8.a1_default1;


/**
 * 1. Default/静态方法 关键字：
 *    1.1 why/benefit? -> 1.8以前的接口只能有抽象方法，不能有方法的实现； 在接口方法中使用default关键字，则能定义具体实现
 *    1.2 好处？：与抽象类有什么区别？ 其实没多大区别
 *         可以将一些能用的功能方法，放在接口中实现，比较方便
 */
public interface A1DefaultFeature {
    default void breath(){
        System.out.println("Default Breath");
    }

    static void testStatic(){
        System.out.println("this is static method");
    }
}
