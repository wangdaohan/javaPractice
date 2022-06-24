package design_pattern.singleton;

public class EnumSingleton {
    public static void main(String[] args) {
        Singleton instance = Singleton.INSTANCE;
        Singleton instance2 = Singleton.INSTANCE;

        System.out.println(instance.equals(instance2));

        instance.sayOK();
    }
}


/**
 * 1. 枚举方式实现单例模式：
 *    好处：
 *        避免多线程同步问题
 *        防止反序列化重新创建新的对象
 */
enum Singleton{
    INSTANCE;
    public void sayOK(){
        System.out.println("ok~~~");
    }
}
