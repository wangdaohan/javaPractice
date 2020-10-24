package practice.java.outofmemory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 2016/4/11.
 */
public class Hello {

    private final static int BYTE_SIZE = 4 * 1024 * 1024;

    public static void main(String []args) {
        List<Object> List = new ArrayList<Object>();
        for(int i = 0 ; i < 10 ; i ++) {
            try {
                Thread.sleep(10000);
            }catch(Exception e){
                e.printStackTrace();
            }
            List.add(new byte[BYTE_SIZE]);
            System.out.println(i);
        }
    }
}
