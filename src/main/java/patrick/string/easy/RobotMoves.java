package patrick.string.easy;

/**
 * EASY
 * https://leetcode.com/problems/robot-return-to-origin/
 * 判断机器人是否回到原点
 *
 *
 *
 */
public class RobotMoves {
    public static void main(String[] args) {
        System.out.println(RobotMoves.robotMoves("UD"));
        System.out.println(RobotMoves.robotMoves("LL"));
    }

    public static boolean robotMoves(String moves) {
        int l = 0, r = 0, u = 0, d = 0;
        char[] movsArr = moves.toCharArray();
        for(int i = 0; i < movsArr.length; i ++) {
            if(movsArr[i] == 'U') u ++;
            if(movsArr[i] == 'D') d ++;
            if(movsArr[i] == 'L') l ++;
            if(movsArr[i] == 'R') r ++;
        }

        if(u == d && l == r) return true;
        return false;
    }
}
