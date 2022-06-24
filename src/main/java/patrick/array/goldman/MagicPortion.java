package patrick.array.goldman;

public class MagicPortion {
    public static void main(String[] args) {
        System.out.println(minimalSteps("ABCDABCE"));
        //System.out.println("final Result = "+ingrediants(6,6));

        //System.out.println("final Result = "+test2("ABCABCE"));



    }


    /**
     *
     */


    private static String test2(String str){
        StringBuilder result = new StringBuilder();
        int left = 0;
        for(int right=0; right<str.length();right++){
            if(right%2==1 && isReaptSubString(str, left,right)){
                System.out.print("Right:"+result);
                System.out.print("Right:"+(right+1));
                if(result.substring(right,right+1).equals("*")){
                    result.append("*");
                }

            }else{
                result.append(str.substring(right,right+1));
                left++;
            }
        }

        return result.toString();
    }


    private static Boolean isReaptSubString(String str, int left, int right){
        System.out.println("left:"+left+" , right:"+right);
        System.out.println("str:"+strArr.length);
        return str.substring(0,left).equals(str.substring(left,right+1));
    }

    static String[] strArr = {"A","B","C","A","B","C","E"};
    static String str = "ABCABCE";
    private static String ingrediants(int left, int right){
        System.out.println("left:"+left+" , right:"+right);
        System.out.println("strArr:"+strArr.length);
        if(left==0 || right-left>strArr.length/2) return str.substring(left,right);
        else if(str.substring(0,left).equals(str.substring(left,right+1))) {
            return "*";
        }
        else{
            String a = ingrediants(left-1, right) + strArr[right]; // from right -> left
            System.out.println("A="+a);
            String b =  strArr[1] + ingrediants(1, right--);  // from left -> right
            System.out.println("b="+b);
            return a.length()>b.length()?b:a;
        }
    }

    private static int minimalSteps(String ingredients) {
        int n = ingredients.length();
        int[] dp = new int[n];
        dp[0] = 1;
        for (int i = 1; i < n; i++) {
            // Check repeat only for even number of characters (odd index)
            if (i % 2 == 1 && (repeatString(ingredients, 0, i / 2, i))) {
                // If repeated use the length of the first block + 1 (for *)
                dp[i] = dp[i / 2] + 1;
            } else {
                // If not just increment
                dp[i] = dp[i-1] + 1;
            }
        }
        return dp[n-1];
    }

    /**
     * Checks if string is repeated in i..j and j+1..k
     */
    private static boolean repeatString(String s, int i, int j, int k) {
        return s.substring(i, j+1).equals(s.substring(j+1, k+1));
    }
}
