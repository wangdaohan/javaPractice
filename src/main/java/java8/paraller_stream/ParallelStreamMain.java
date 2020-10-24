package java8.paraller_stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

public class ParallelStreamMain {
    public static void main(String[] args) {
        //并行流：使用多线程来加快处理
        //底层使用Fork/join来维护一个线程队列，分割任务，将父任务分割成子任务

        List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        numbers.parallelStream().forEach(System.out::println);

        for(int i =0;i<10;i++){
            //会报错
            List list01 = new ArrayList();
            //解决问题
            List safeList = new CopyOnWriteArrayList();
            IntStream.range(0,100).parallel().forEach(list01::add);
            System.out.println(list01.size());
        }

    }
}
