package java8.a4_optional;

import java.util.Optional;


/**
 * Optional:
 *   为解决NullPointException问题
 *   java8之前： 使用try/catch
 *   java8后： Optional<Student> opt = Optional.of(stu); 当传入的stu是Null是会抛异常
 *            用isPresent检查是否为空，用get来获取值
 */
public class OptinalUsage {
    public static void main(String[] args) {
        Student t = null;
        Student t1 = new Student();
        //不能为空
        Optional<Student> notNullable = Optional.of(t1);
        //可能为空
        Optional<Student>  nullable = Optional.ofNullable(t);
        //可能为空时，orElse设置兜底的值
        Student student = nullable.orElse(new Student(1,"default name"));
        System.out.println(student.getName());
        if(notNullable.isPresent()){
            notNullable.get();
        }else{

        }
        if(nullable.isPresent()){
            nullable.get();
        }

        Student student1 = null;
        Student nullAge = new Student();
        nullAge.setAge(7);
        int age = Optional.ofNullable(student1).map(obj -> obj.getAge()).orElse(7);
        String abcAge = Optional.ofNullable(nullAge.getName()).orElse("defaultName");
        System.out.println(age);
        System.out.println(abcAge);

    }
}
