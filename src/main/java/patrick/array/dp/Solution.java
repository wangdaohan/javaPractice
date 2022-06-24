package patrick.array.dp;

class Solution {
    public static void main(String[] args) {
        Solution s = new Solution();
        System.out.println(s.findNumberOfLIS(new int[]{1, 3, 5, 4, 7}));
    }

    public int findNumberOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        //state
        int[] count = new int[nums.length]; //用来记录第i个位置的最长的个数
        int[] f = new int[nums.length]; //用来记录第i个位置的最长长度
        //init
        f[0] = 1;
        count[0] = 1;
        //func
        int max_len = 1;
        for (int i = 1; i < nums.length; ++i) {
            //init
            f[i] = 1;
            count[i] = 1;
            for (int j = 0; j < i; ++j) {
                if (nums[j] < nums[i] && f[i] - f[j] < 1) {
                    //一个新的子序列
                    f[i] = f[j] + 1;
                    count[i] = count[j];
                } else if (nums[j] < nums[i] && f[i] - f[j] == 1) {
                    count[i] += count[j];
                }
            }
            max_len = Math.max(f[i], max_len);
        }
        for (int i = 0; i < nums.length; i++) {
            System.out.print(count[i]);
        }
        System.out.println("");
        //ans
        int res = 0;
        for (int i = 0; i < nums.length; ++i) {
            if (max_len == f[i]) {
                res += count[i];
            }
        }
        return res;
    }
}