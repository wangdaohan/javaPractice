package test;

import java.util.Arrays;

public class ListMerge {
    public static void main(String[] args) {
        ListMerge listMerge = new ListMerge();
        listMerge.generateOutput(new String[]{"1","2","3","4","5"},new String[]{"a","b","c"});
        listMerge.maxProfit(new int[]{2,5,6,4,8});
    }

    public void generateOutput(String[] arr1, String[] arr2){
        int maxLength = arr1.length>arr2.length?arr1.length:arr2.length;
        String[] mergeArr = new String[arr1.length+arr2.length];
        int j=0;
        for(int i=0;i<maxLength;i++){
            if(i<arr1.length) {
                mergeArr[j++] = arr1[i];
            }
            if(i<arr2.length){
                mergeArr[j++] = arr2[i];
            }
        }

        Arrays.asList(mergeArr).forEach(a ->{
            //System.out.print(a+",");
        });
    }

    public void maxProfit(int[] px){
        int max= 0;
        for(int i=0;i<px.length;i++){
            for(int j=px.length-1;j>=i;j--) {
                int profit = px[j] - px[i];
                max = max > profit ? max : profit;
            }
        }
        System.out.println(max);
    }




}
