import java.util.*;

public class GobangAI {
    private final GobangPattern pattern;
    private final Board board;
    private int black;
    private final int[][] expendMap;    // 拓展地图包含了边界

    /**
     * 1 ： AI
     * 2 ：玩家
     * 3 : 边缘
     */
    private static final int NONE = 0;
    private static final int AI = 1;
    private static final int PLAYER = 2;
    private static final int BORDER = 3;

    public GobangAI(GobangPattern pattern, Board board) {
        this.pattern = pattern;
        this.board = board;
        this.black = 1;
        this.expendMap = new int[17][17];
        initExpendMap();
    }

    public Board getBoard() {
        return board;
    }

    public void setBlack(int black) {
        this.black = black;
        board.setAiTurn(black == AI);
    }

    public int getBlack() {
        return black;
    }

    private void initExpendMap() {
        int len = expendMap.length;
        // 设置拓展地图的边界
        for(int i = 0; i < len; i ++) {
            expendMap[i][0] = BORDER;
            expendMap[0][i] = BORDER;
            expendMap[i][len - 1] = BORDER;
            expendMap[len - 1][i] = BORDER;
        }
    }

    private void UpdateExpendMap() {
        int len = board.map.length;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (board.map[i][j] == 0) {
                    continue;
                }
                // 黑棋是AI
                if (black == 1) {
                    expendMap[i + 1][j + 1] = (board.map[i][j] == 1 ? AI : PLAYER);
                }
                // 黑棋是玩家
                else {
                    expendMap[i + 1][j + 1] = (board.map[i][j] == 1 ? PLAYER : AI);
                }
            }
        }
    }

    /*
    * 给当前的局面评分 评分对象为AI的棋子
    * 只选取有棋子的点位进行评分
    * 计算出所有有效点位评分之和
    * */
    public int evaluate(int[][] expendMap) {
        int sum = 0;

        for(int i = 1; i <= 15; i ++) {
            for(int j = 0; j < 12 ; j ++) {
                StringBuilder patStr = new StringBuilder();
                for(int k = 0; k <= 5; k ++) {
                    patStr.append(expendMap[i][j + k]);
                }
                if(pattern.scoreMap.containsKey(patStr.toString())) {
                    sum += pattern.scoreMap.get(patStr.toString());
                }
            }
        }

        for(int j = 1; j <= 15; j ++) {
            for(int i = 0; i < 12 ; i ++) {
                StringBuilder patStr = new StringBuilder();
                for(int k = 0; k <= 5; k ++) {
                    patStr.append(expendMap[i + k][j]);
                }
                if(pattern.scoreMap.containsKey(patStr.toString())) {
                    sum += pattern.scoreMap.get(patStr.toString());
                }
            }
        }

        for(int i = 0; i < 12; i ++) {
            for(int j = 0; j < 12; j ++) {
                StringBuilder patStr = new StringBuilder();
                for(int k = 0; k <= 5; k ++) {
                    patStr.append(expendMap[i + k][j + k]);
                }
                if(pattern.scoreMap.containsKey(patStr.toString())) {
                    sum += pattern.scoreMap.get(patStr.toString());
                }
            }
        }

        for(int i = 0; i < 12; i ++) {
            for(int j = 5; j < 17; j ++) {
                StringBuilder patStr = new StringBuilder();
                for(int k = 0; k <= 5; k ++) {
                    patStr.append(expendMap[i + k][j - k]);
                }
                if(pattern.scoreMap.containsKey(patStr.toString())) {
                    sum += pattern.scoreMap.get(patStr.toString());
                }
            }
        }
        return sum;
    }

    /*
    * 获取排名前CNT的点位
    * 当模拟玩家下棋时,选择最不利于AI的点(score最小)
    * 当模拟AI下棋时,选择最不利于玩家的点(score最大)
    * */
    public Queue<Point> getPossiblePoints(int FLAG, int[][] map) {
        // 模拟玩家和AI下棋改变比较器规则
        PriorityQueue<Point> pointQueue = new PriorityQueue<>
                ((FLAG == AI) ? Point::compareTo : Point::reverseCompareTo);
        Queue<Point> ret = new LinkedList<>();
        // 遍历棋盘
        for(int i = 1; i <= 15; i++) {
            for (int j = 1; j <= 15; j++) {
                // 已有棋子 跳过
                if (map[i][j] != NONE) {
                    continue;
                }
                // 模拟下棋
                map[i][j] = FLAG;
                int score = evaluate(map);
                // 恢复棋盘
                map[i][j] = 0;
                Point point = new Point(i, j, score);
                pointQueue.add(point);
            }
        }
        // 只取前最佳点位
        // 先取得最优解的值
        Queue<Point> bestPointArr = new PriorityQueue<>();
        Point bestPoint = pointQueue.peek();

        // 如果后面仍然是最优解则取出
        while (pointQueue.peek()!= null) {
            // 已经不是最优解则退出
            if (pointQueue.peek().score != bestPoint.score) {
                break;
            }

            // 取出队首
            Point retInfo = pointQueue.poll();
            ret.add(retInfo);
        }
        return ret;
    }

    /*
    * 极大极小搜索
    * DEPTH 搜索深度
    * 返回CNT个最佳落子点位
    * */
    public ArrayList<Point> search(int DEPTH) {
        // Info 类型的 priority queue 存储极大极小层 仅存储最新的两层
        PriorityQueue<PointMapInfo> Max = new PriorityQueue<>(PointMapInfo::compareTo);
        PriorityQueue<PointMapInfo> Min = new PriorityQueue<>(PointMapInfo::reverseCompareTo);
        PointMapInfo initInfo = new PointMapInfo(new Point(-1, -1, 0), expendMap, null);

        /*
        * 先初始化极大AI队列(模拟第0层)
        * */
        // 找到一批最优点
        Queue<Point> initPointQueue = getPossiblePoints(AI, initInfo.map);
        // 将这批最优点转化为新的info 存入MAX极大队列
        PointMapInfo.genInfoQueue(Max, initPointQueue, AI, initInfo);

        /*
        * 从第一层开始循环
        * 第0层上方已经手动模拟
        * */
        for (int depth = 1; depth <= DEPTH; depth++) {
            // 极大搜索 模拟AI
            if (depth % 2 == 0) {
                Max.clear();
                while (!Min.isEmpty()) {
                    // 从极小搜索里拿出一个
                    PointMapInfo info = Min.poll();
                    // 找到一批优点
                    Queue<Point> pointQueue = getPossiblePoints(AI, info.map);
                    // 将这批最优点转化为新的info 存入MAX极大队列
                    PointMapInfo.genInfoQueue(Max, pointQueue, AI, info);
                }
                // 只截取所有最优点
                PointMapInfo.filterQueue(Max, 0);

            }
            // 极小搜索 模拟玩家
            else {
                Min.clear();
                while (!Max.isEmpty()) {
                    // 从极大搜索里拿出一批
                    PointMapInfo info = Max.poll();
                    // 找到一批最差点
                    Queue<Point> pointQueue = getPossiblePoints(PLAYER, info.map);
                    // 将这批最差点转化为新的info 存入MIN极小队列
                    PointMapInfo.genInfoQueue(Min, pointQueue, PLAYER, info);
                }
                // 只截取所有最优点
                PointMapInfo.filterQueue(Min, 1);
            }
        }

        /*
        * 最优点存储在极大队列中 取出所有最优点返回
        * */
        // 先取得最优解的值
        ArrayList<Point> bestPointArr = new ArrayList<>();
        PointMapInfo bestInfo = Max.peek();

        // 如果后面仍然是最优解则取出
        while (Max.peek()!= null) {
            // 已经不是最优解则退出
            if (Max.peek().point.score != bestInfo.point.score) {
                break;
            }

            // 取出队首
            PointMapInfo retInfo = Max.poll();
            // 取出最优解的根父亲作为决策点
            while (retInfo.father != initInfo) {
                retInfo = retInfo.father;
            }

            bestPointArr.add(retInfo.point);
        }
        return bestPointArr;
    }

    /*
    * AI判断的主入口
    * */
    // public Point aiJudge() {
    //     Queue<Point> pointQueue = getPossiblePoints();
    //
    //     // 先取出第一个最优解
    //     ArrayList<Point> bestPoint = new ArrayList<>();
    //     bestPoint.add(pointQueue.poll());
    //     // 如果后面仍然是最优解则取出
    //     while (pointQueue.peek()!= null) {
    //         // 已经不是最优解则退出
    //         if (pointQueue.peek().score != bestPoint.get(0).score) {
    //             break;
    //         }
    //         // 取出
    //         bestPoint.add(pointQueue.poll());
    //     }
    //
    //     // 随机取出一个最优解
    //     Random random = new Random();
    //     return bestPoint.get(random.nextInt(bestPoint.size()));
    // }

    public Point aiJudge() {
        UpdateExpendMap();
        ArrayList<Point> bestPoint = search(8);
        // 随机取出一个最优解
        Random random = new Random();
        // 返回给js的点需要xy坐标分别减去1 因为js的地图不包含边界 不是expendMap
        Point ret = bestPoint.get(random.nextInt(bestPoint.size()));
        ret.x--;
        ret.y--;
        return ret;
    }
}