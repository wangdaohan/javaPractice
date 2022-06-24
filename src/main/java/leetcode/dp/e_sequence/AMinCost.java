package leetcode.dp.e_sequence;

import java.util.ArrayList;
import java.util.List;

public class AMinCost {

        public static void main(String[] args){
            AMinCost aMinCost = new AMinCost();
            String abc = "012345";
            char[] test = abc.toCharArray();

            System.out.println(5/2);

            System.out.println(5%2);
            char[][] costs = new char[][]{{'0','E','0','0'},{'E','0','W','E'},{'0','E','0','0'}};
            aMinCost.isMatch("aa","a*");
        }


    public boolean isMatch(String s, String p) {
        // write your code here
        char[] a = s.toCharArray();
        char[] b = p.toCharArray();
        int m = a.length;
        int n = b.length;
        boolean[][] f = new boolean[m+1][n+1];
        for(int i=0;i<=m;i++){
            for(int j=0;j<=n;j++){
                if(i==0 && j==0){
                    f[i][j] = true;
                    continue;
                }
                if(j==0){
                    f[i][j] = false;
                    continue;
                }
                f[i][j] = false;
                //情况1和情况2
                if(b[j-1]!='*'){
                    if(i>0 && (a[i-1]==b[j-1] || b[j-1]=='.')){
                        f[i][j] = f[i-1][j-1];
                        continue;
                    }
                }else{
                    //情况3
                    //f[i][j] = f[i][j-2] || (f[i][j] = f[i-1][j] && (a[i-1]==b[j-2] || b[j-2]=='.'));

                    //针对
                    //3.1 a[i-1]是0个c的情况
                    //if(a[i-1]!=b[j-2]){
                    if(j>1){
                        f[i][j] = f[i][j-2];
                    }
                    //3.2 a[i-1] >= 1个c的情况
                    if(i>0&& j>1 && (a[i-1]==b[j-2] || b[j-2]=='.')){
                        f[i][j] = f[i][j] || f[i-1][j];
                    }
                }
            }
        }
        return f[m][n];
    }

}
