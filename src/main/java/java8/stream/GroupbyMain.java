package java8.stream;

import java8.a4_optional.Student;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupbyMain {
    public static void main(String[] args) {
        List<Student> studentList = Arrays.asList(new Student(21,"广东"),new Student(21,"海南"),new Student(21,"北京"),
                new Student(21,"海南"),new Student(21,"北京"),new Student(21,"广东"),
                new Student(21,"广东"),new Student(21,"广东"),new Student(21,"海南"),
                new Student(21,"北京"),new Student(21,"海南"),new Student(21,"广东"));
        Map<String,Long> listMap = studentList.stream().collect(Collectors.groupingBy(obj->obj.getName(),Collectors.counting()));
    }
}
