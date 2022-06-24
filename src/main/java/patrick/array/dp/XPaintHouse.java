package patrick.array.dp;

public class XPaintHouse {




    public int minCost(int[][] costs) {
        // write your code here
        int n = costs.length;
        if(n==0){
            return 0;
        }
        //初始化N+1,f[0]代表前0栋房子 = 0
        //f[1]代表前1栋房子
        int[][] f = new int[n+1][3];
        int result;
        for(int i=0; i<=n; i++){
            //前i-1栋房子的颜色
            for(int j=0; j<3; j++){
                if(i==0) f[i][j] = 0;
                else{
                    f[i][j] = Integer.MAX_VALUE;
                    //前i-2栋房子的颜色
                    for(int k=0; k<3; ++k){
                        if(j==k){continue;} //撞色了
                        f[i][j] = Math.min(f[i][j], f[i-1][k] + costs[i-1][j]);
                    }

                }
            }
        }

        result = Math.min(Math.min(f[n][0],f[n][1]),f[n][2]);
        return result;
    }
}