package java8.a3_date;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 新加入LocalDate/LocalTime/LocalDateTime等核心类
 *    java8以前： 使用SimpleDateFormat/Calendar/Date等类， 缺点：SimpleDateFormat/Date是非线程安全的，API设计差（对于时间/日期的比较，加减，格式化等不友好）
 *    java8后：  添加常用API： getYear/getMonth/Duration        加入：线程安全的DateTimeFormatter
 *
 */
public class LocalTimeTest {
    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        System.out.println(localDate.getYear());
        System.out.println(localDate.getMonth());
        System.out.println(localDate.getDayOfMonth());
        System.out.println(localDate.plusMonths(2));
        System.out.println(localDate.minusMonths(3));
        LocalDate changedDate = localDate.minusMonths(3);
        System.out.println(localDate.isAfter(changedDate));


        LocalDateTime ldt = LocalDateTime.now();
        System.out.println(ldt);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String ldtStr = dtf.format(ldt);
        System.out.println(ldtStr);

        //Duration
        LocalDateTime ldt2 = LocalDateTime.of(2010, 1,1,0,0,0);
        Duration dur = Duration.between(ldt,ldt2);
        System.out.println(dur.toDays());
        System.out.println(dur.toHours());

    }
}
