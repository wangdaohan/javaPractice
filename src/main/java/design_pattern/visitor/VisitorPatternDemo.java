package design_pattern.visitor;


/**
 * 访问者模式(visitor pattern): 封装一些作用于某种数据结构的各元素的操作，它可以在不改变数据结构的前提下定义作用于这些元素的新的操作。
 *   目的： （1）将数据结构与数据操作分离，解决数据结构和操作的耦合性问题
 *   工作原理：（1）在被访问的类里面加一个对外提供接待访问者的接口
 *
 *   应用场景： 需要对一个对象结构中的对象进行很多不同的操作，（而这些操作间没有关联），同时需要避免让这些操作污染（耦合）这些对象的类，可以选用访问者解决。
 *
 *
 *   具体需求：给歌手打分
 */
public class VisitorPatternDemo {
    public static void main(String[] args) {
        //创建ObjectStructure
        ObjectStructure objectStructure = new ObjectStructure();
        objectStructure.attach(new Man());
        objectStructure.attach(new Woman());
        objectStructure.attach(new Man());

        //正评
        SuccessAction successAction = new SuccessAction();
        objectStructure.display(successAction);

        //负评
        FailAction failAction = new FailAction();
        objectStructure.display(failAction);
    }
}
