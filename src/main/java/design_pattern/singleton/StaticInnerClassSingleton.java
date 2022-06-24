package design_pattern.singleton;


/**
 * 静态内部类：
 *    1. 当主类被装载时，静态内部类不会被加载
 *    2. 当然使用到静态内部类时，如下面：SingletonInsatnce.INSTANCE时，才会被装载，而且只装载一次，装载过程是线程安全的。
 *
 * 2. 用静态内部类来实现单例模式
 *
 *
 *    好处：
 */
public class  StaticInnerClassSingleton{
    public static void main(String[] args) {

    }

    private StaticInnerClassSingleton(){}

    private static class SingletoneInstance{
        private static final StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }
}
