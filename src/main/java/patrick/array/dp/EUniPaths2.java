package patrick.array.dp;


import java.util.Arrays;

/**
 * Unique Path II
 */
public class EUniPaths2 {
    public static void main(String[] args) {
        EUniPaths2 uniPaths2 = new EUniPaths2();
        System.out.println(uniPaths2.uniquePaths (new int[][] {{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0}}));
        System.out.println(uniPaths2.uniquePaths (new int[][] {{0,0},{0,0},{0,0},{1,0},{0,0}}));
    }

    public int uniquePaths(int[][] a){
        int m = a.length;
        int n = a[0].length;
        int[][] dp = new int[m][n];
        dp[0][0] = 1;
        for(int i=0;i<m; i++){
            for(int j=0; j<n; j++){
                if(a[i][j]==1){  //有障碍
                    dp[i][j] = 0;
                }else{

                        if(i >=1){
                            dp[i][j] += dp[i-1][j];
                        }
                        if(j >=1){
                            dp[i][j] += dp[i][j-1];
                        }
                }
            }
        }

        System.out.println(Arrays.deepToString(dp));
        return dp[m-1][n-1];
    }
}
