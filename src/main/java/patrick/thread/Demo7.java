package patrick.thread;

import java.util.Arrays;
import java.util.List;

public class Demo7 {
    public static void main(String[] args) {
        List<Integer> values= Arrays.asList(2,3,4,5,5);
        int res = new Demo7().add(values);
        System.out.println(res);
    }

    public int add(List<Integer> values){
        return values.parallelStream().mapToInt(a -> a).sum();
    }
}
