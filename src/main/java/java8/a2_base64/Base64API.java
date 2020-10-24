package java8.a2_base64;

/**
 * 1. 加入Base 64 api：
 *       base64是网络上最常见的用于传输8bit字节码的编码方式，base64就是一种基于64个可打印字符表示二进制数据的方法
 *    java8以前：使用JDK里sun.misc套件下的BASE64Encoder 和BASE64Decoder这2个类 -》 缺点： 编码和解码的效率差，公开信息说以后取消这2个方法
 *              或使用apache commons codec -> 缺点： 需引入 apache commons codec
 *    java8以后：1. 效率比默认的好
 *              2.在java.util包中，新增base64的类，不需要额外引用包
 */
public class Base64API {
    public static void main(String[] args) {

    }
}
