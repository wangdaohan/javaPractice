package java8.stream;

import java8.a4_optional.Student;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class SummarizeMain {
    public static void main(String[] args) {
        List<Student> studentList = Arrays.asList(new Student(21,"广东"),new Student(18,"海南"),new Student(19,"北京"),
                new Student(23,"海南"),new Student(21,"北京"),new Student(55,"广东"),
                new Student(32,"广东"),new Student(39,"广东"),new Student(23,"海南"),
                new Student(40,"北京"),new Student(45,"海南"),new Student(67,"广东"));
        IntSummaryStatistics listMap = studentList.stream().collect(Collectors.summarizingInt(Student::getAge));
        System.out.println(listMap.getAverage());
        System.out.println(listMap.getSum());
        System.out.println(listMap.getCount());
        System.out.println(listMap.getMax());

    }
}
