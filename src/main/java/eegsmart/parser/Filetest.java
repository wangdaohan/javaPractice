package eegsmart.parser;

import org.joda.time.Duration;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

/**
 * Created by patrick on 2016/3/10.
 */
public class Filetest{
    public static void main(String args[]){
//        File f =new File("TileTest.doc");
//        String fileName=f.getName();
//        String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
//        System.out.println(prefix);


        Duration duration = Duration.standardMinutes(new Long("292174"));
        System.out.println(duration.getStandardMinutes());

        DateFormat formatter = new SimpleDateFormat("mm:ss");
        long now = System.currentTimeMillis();
        Calendar calendar = getInstance();
        calendar.setTimeInMillis(292174l);
        //Time
        Date d = new Date(Double.valueOf("180000.58").longValue());
        System.out.println(now + " = " + formatter.format(calendar.getTime()));
        System.out.println(now + " = " + formatter.format(d));
    }
}
