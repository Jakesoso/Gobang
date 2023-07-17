public class Board {
    int[][] map;
    boolean isBlackTurn;
    boolean isAiTurn;
    int steps;
    int cur_x, cur_y;
    boolean isWin;

    public Board() {
        map = new int[15][15];
        isBlackTurn = true;
        isAiTurn = false;
        isWin = false;
        steps = 0;
    }

    public boolean isBlackTurn() {
        return isBlackTurn;
    }

    public boolean isAiTurn() {
        return isAiTurn;
    }

    public int getSteps() {
        return steps;
    }

    public boolean getWin() {
        return isWin;
    }

    public void setBlackTurn(boolean isBlackTurn) {
        this.isBlackTurn = isBlackTurn;
    }

    public void setAiTurn(boolean isAiTurn) {
        this.isAiTurn = isAiTurn;
    }

    public void update(int row, int col) {
        cur_x = row;
        cur_y = col;
        /*
        * 0：该位置为空，没有落子
        * 1：该位置被黑方占据
        * -1：该位置被白方占据
        * */
        map[cur_x][cur_y] = isBlackTurn ? 1 : -1;
        isBlackTurn = !isBlackTurn;
        steps++;
    }

    public void printBoard() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }

    public int checkFive(boolean flag, int cnt, int k, int player) {
        int[][] dirs = {{1, 0}, {0, 1}, {-1, 1}, {1, 1},};
        int x = cur_x;
        int y = cur_y;

        while(cnt < 5) {
            if (flag) {
                x += dirs[k][0];
                y += dirs[k][1];
            }
            else {
                x -= dirs[k][0];
                y -= dirs[k][1];
            }

            if (x < 0 || x >= map.length || y < 0
                    || y >= map[0].length) {
                break;
            }
            if(map[x][y] != player) {
                break;
            }
            cnt++;
        }
        return cnt;
    }

    public void setWin() {
        if(steps < 9) {
            return;
        }
        int player = isBlackTurn ? -1 : 1;

        for(int k = 0; k < 4; k++) {
            int cnt = 1;
            cnt = checkFive(true, cnt, k, player);
            cnt = checkFive(false, cnt, k, player);
            if(cnt == 5) {
                this.isWin = true;
                return;
            }
        }
    }
}
