package patrick;

public class StaticDemo{
    public static int booksum=0;//静态变量

    static{//这是静态初始化块
        print();
        System.out.println("this is static block");
    }

    {//实例初始化块
        System.out.println("初始化块："+booksum);
    }

    public StaticDemo(){//构造方法
        System.out.println("this is Book's constructor~");
        booksum+=1;
    }

    public static void print(){//静态方法
        System.out.println("this is static method~");
    }

    public static void main(String []args){
        StaticDemo book=new StaticDemo();
    }
}