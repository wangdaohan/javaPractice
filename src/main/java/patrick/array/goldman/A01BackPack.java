package patrick.array.goldman;

import java.util.Arrays;

public class A01BackPack {
    /**
     * 0/1背包问题： 数组内的每个元素只存在拿/不拿的2种情况
     */

    static int max_weight = 10;
    int[] w_arr = new int[] {2,3,4,7};
    public static void main(String[] args) {

        int[] arr = new int[] {1,3,5,9};
        A01BackPack a01BackPack = new A01BackPack();
        System.out.println(a01BackPack.backpack_opt(arr,arr.length-1, max_weight));
        System.out.println(a01BackPack.backpack_opt_no_recur(arr, max_weight));
        System.out.println(a01BackPack.backpack_opt_no_recur2(arr, max_weight));
    }

    public int backpack_opt(int[] arr, int index, int weight){
        if(index==0 && weight>0) {
            return arr[index];
        }
        else if(weight<=0) return 0;
        else if(w_arr[index]>weight) return backpack_opt(arr,index-1, weight);
        else {
            int a = backpack_opt(arr,index-1, weight-w_arr[index]) + arr[index];
            int b = backpack_opt(arr,index-1, weight);
            return Math.max(a,b);
        }
    }

    public int backpack_opt_no_recur(int[] arr, int weight){
        int[][] opt = new int[arr.length][weight+1];

        for (int i = 1; i < opt.length; i++) {
            for (int j = 0; j < opt[i].length; j++) {
                if(i==0 & j>=w_arr[i]) opt[i][j]=arr[i];
                else if(w_arr[i]>j) {
                    opt[i][j] = opt[i-1][j];
                }
                else {
                    opt[i][j] = Math.max(opt[i-1][j-w_arr[i]]+arr[i], opt[i-1][j]);
                }
            }
        }

        return opt[arr.length-1][weight];
    }
    public int backpack_opt_no_recur2(int[] arr, int weight){
        int[] opt = new int[weight+1];
        for(int i=1;i<arr.length;i++)
            for (int j = weight; j >1; j--) {  //将DP从二维数组，压缩成1维，递推时需要从后往前推
                if(j>=w_arr[i]) {
                    opt[j] = Math.max(opt[j-w_arr[i]]+arr[i], opt[j]);
                }

            }
        return opt[weight];
    }

}
