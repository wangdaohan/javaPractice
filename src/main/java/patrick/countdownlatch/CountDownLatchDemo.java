package patrick.countdownlatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch 一个同步辅助器
 * 作用是： 在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。直到初始传入的countdown为0
 *
 * 特点：countdown不能重置，只能发生一次；如需要重置，应使用CyclicBarrier
 *
 * 实例： 将一个文本中的所有数字求和 (
 *
 *
 *
 * Exchanger:
 *
 */
public class CountDownLatchDemo {

    private static int[] nums;
    private static CountDownLatch count;

    public CountDownLatchDemo(int line){
        nums = new int[line];
        count = new CountDownLatch(line);
    }


    public int calc(String line, int index){

        System.out.println("行计算开始。。。");
        String[] lineNums = line.split(",");
        int total = 0;
        for(String lineNum : lineNums){
            total += Integer.parseInt(lineNum);
        }
        nums[index] = total;
        System.out.println("行计算线束。。。");
        count.countDown();
        return total;
    }

    public void sum(){
        System.out.println("总计算开始。。。");
        int sum = 0;
        for(int num : nums){
            sum +=num;
        }
        System.out.println("总计算="+sum);
    }

    public static void main(String[] args) {
        List<String> contents = readFile();
        CountDownLatchDemo countDownLatchDemo = new CountDownLatchDemo(contents.size());
        for(int i=0; i<contents.size();i++){
            final int j = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    countDownLatchDemo.calc(contents.get(j),j);
                }
            }).start();
        }

        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        countDownLatchDemo.sum();
    }


    private static List<String> readFile(){
        List<String> content = new ArrayList<>();
        String line=null;
        try{
            BufferedReader br = new BufferedReader(new FileReader("/Users/patricklo/Documents/workspace/github/javaPractice/src/main/java/patrick/countdownlatch/nums.txt"));
            while((line = br.readLine()) != null){
                content.add(line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
      return content;

    }



}
