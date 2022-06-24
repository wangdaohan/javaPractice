package practice.java;

public class MinimumSizeSumarraySum {
    public static void main(String[] args) {

        MinimumSizeSumarraySum mini = new MinimumSizeSumarraySum();
        System.out.println(mini.minSubArrayLen(7, new int[] {2,3,1,2,4,3}));
    }

    public int minSubArrayLen(int target , int [] numbers){
        int left =0 ;
        int minLen = Integer.MAX_VALUE;
        for(int right =0;right<numbers.length;right++){
            target = target - numbers[right];
            while(target<=0){
                minLen = Math.min(minLen, right-left+1);
                target = target+numbers[left++];
            }
        }
        if(minLen == Integer.MAX_VALUE){
            return 0;
        }
        return minLen;
    }
}
