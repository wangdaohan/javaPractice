package practice.java.datastructure.reverse;

/**
 * Created by patrick on 4/29/15.
 */
public class ReverseStr {

    public static void main(String [] args){
        String reverseStr=reverseRecursively("abcd");
        System.out.println(reverseStr);

        System.out.println(reverseStr.substring(1));


        System.out.println(palindromTestByIteration("abcd"));

        System.out.println(""+ReverseStr.reverseInt(123));
    }


    /**
     * Reverse Integer
     * @param number
     * @return
     */
    public static  int reverseInt(int number){
        int result = 0;
        while(number!=0){
            result *= 10;
            result += number%10;
            number = number/10;
        }
        return result;
    }
    public static String reverseRecursively(String str) {
        if(str == null){
            System.out.println("the string is empty");
            return null;
        }
        //base case to handle one char string and empty string
        if (str.length() < 2) {
            return str;
        }

        return reverseRecursively(str.substring(1)) + str.charAt(0);

    }

    //Use Iteration
    public static String palindromTestByIteration(String testStr){
        StringBuilder sb = new StringBuilder();
        char[] testStrArray = testStr.toCharArray();
        for(int i= testStrArray.length-1;i >= 0 ; i--){
            sb.append(testStrArray[i]);
        }
        return sb.toString();
    }

    public static void reverse2(String input){
        //String input = "GeeksforGeeks";

        // getBytes() method to convert string
        // into bytes[].
        byte[] strAsByteArray = input.getBytes();

        byte[] result = new byte[strAsByteArray.length];

        // Store result in reverse order into the
        // result byte[]
        for (int i = 0; i < strAsByteArray.length; i++)
            result[i] = strAsByteArray[strAsByteArray.length - i - 1];

        System.out.println(new String(result));
    }

    public static void reverse3(String input){

        StringBuilder input1 = new StringBuilder();

        // append a string into StringBuilder input1
        input1.append(input);

        // reverse StringBuilder input1
        input1.reverse();

        // print reversed String
        System.out.println(input1);
    }

    public static void reverse4(){
        String str = "Geeks";

        // conversion from String object to StringBuffer
        StringBuffer sbr = new StringBuffer(str);
        // To reverse the string
        sbr.reverse();
        System.out.println(sbr);
    }

}
