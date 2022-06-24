package patrick.array.dp;

/**
 *
 * can Frog jump?
 */
public class FrogJump {
    public static void main(String[] args) {
        FrogJump frogJump = new FrogJump();
        System.out.println(frogJump.canJum(new int[]{2,3,1,1,4})); //True
        System.out.println(frogJump.canJum(new int[]{3,2,1,0,4})); //false
    }

    public boolean canJum(int[] a){
        if(a==null || a.length==0){
            return false;
        }
        int n = a.length;
        boolean[] dp = new boolean[n];
        dp[0] = true;
        for(int j=1;j<n;j++){
            dp[j] = false;
            for(int i=0; i<j; ++i){
                if(dp[i] && a[i] >= j-i){
                    dp[j] = true;
                    break;
                }
            }
        }

        return dp[n-1];
    }


}
