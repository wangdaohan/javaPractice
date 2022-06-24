package practice.java;

public class KnightProb {
    public static void main(String[] args) {
        KnightProb knightProb = new KnightProb();
        System.out.println(knightProb.prob_sum(3,2,0,0));
    }

    public double prob_sum(int boardSize, int steps, int start_x, int start_y){
        // direction vector for the knight (8 directions)
        int dx[] = {1, 2,  3,  1, -1, -2, -2,-1};
        int dy[] = {2, 1, -1, -2, -2, -1,  1, 2};

        double dp1[][][] =new double[boardSize][boardSize][steps+1];
        //for 0 number of steps , each postion will have probability 1
        for(int i=0;i<boardSize;++i)
            for(int j=0;j<boardSize;++j)
                dp1[i][j][0] = 1;
        //for each step
        for(int step=1; step<= steps; ++step){
            //for each position from (0,0) in chess board
            for(int x=0;x<boardSize;++x){
                for(int y=0;y<boardSize;++y){
                    double prob = 0.0;
                    for(int i=0;i<8;++i){
                        int nx = x+dx[i];
                        int ny = y+dy[i];
                        //if new (x,y) still in the board
                        if(stillInBoard(nx,ny,boardSize))
                            prob += dp1[nx][ny][step-1] / 8;
                    }
                    dp1[x][y][step] = prob;
                }
            }
        }

        return dp1[start_x][start_y][steps];
    }

    public boolean stillInBoard(int x, int y , int boardSize){
        return x>=0 && y>=0 && x<boardSize && y<boardSize;
    }
}
