package practice.java;


/**
 * the power of 10
 */
public class PowersOfTen {


        public static boolean isPowerOfTen(long input) {

            if (input % 10 != 0 || input == 0) {
                return false;
            }

            if (input == 10) {
                return true;
            }

            return isPowerOfTen(input/10);
        }

        public static void main(String[] args) {

            System.out.println("1000: " + isPowerOfTen(1000));
            System.out.println("4: " + isPowerOfTen(4));
            System.out.println("0: " + isPowerOfTen(0));
            System.out.println("10: " + isPowerOfTen(10));
            System.out.println("100: " + isPowerOfTen(100));
        }
}
