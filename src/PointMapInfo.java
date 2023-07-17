import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

/*
* 该类仅用于搜索算法模拟落子时的棋局信息的记录
* 该类有两个成员变量
* Point point: 目前模拟落子的位置
* int[][] map: 落子后的新地图
*
* genInfoQueue方法用于将Point队列中的所有Point元素转化成PointMapInfo的队列
* 用于将搜索时找到一系列的最优点，转化为这些最优点模拟落子后形成的PointMapInfo的队列
* */

public class PointMapInfo {
    final Point point;
    final int[][] map;
    PointMapInfo father;

    public PointMapInfo(Point point, int[][] map, PointMapInfo father) {
        this.point = point;
        this.map = mapCopy(map);
        this.father = father;
    }

    /*
    * 使用 System.arraycopy() 方法进行数组拷贝具有高效性能
    * */
    public static int[][] mapCopy(int[][] source) {
        int[][] destination = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            int[] row = source[i];
            int[] newRow = new int[row.length];
            System.arraycopy(row, 0, newRow, 0, row.length);
            destination[i] = newRow;
        }
        return destination;
    }

    public static void filterQueue(PriorityQueue<PointMapInfo> queue, int cmp) {
        if (queue.isEmpty()) {
            return;
        }

        PointMapInfo firstMember = queue.peek();
        ArrayList<PointMapInfo> filteredList = new ArrayList<>();

        for (PointMapInfo member : queue) {
            if (cmp == 0 && member.compareTo(firstMember) != 0) {
                filteredList.add(member);
            }
            else if (cmp == 1 && member.reverseCompareTo(firstMember) != 0) {
                filteredList.add(member);
            }
        }

        queue.removeAll(filteredList);
    }

    /*
     * 将pointQueue中的每一个成员都构造成新的Info
     *
     * pointQueue : 旧Info生成的Point队列
     * father     : 旧Info
     * return     : 新Info
     *
     * */

    public static void genInfoQueue(PriorityQueue<PointMapInfo> InfoQueue, Queue<Point> pointQueue, int FLAG, PointMapInfo father) {
        // 构造Info
        while (!pointQueue.isEmpty()) {
            // 从最优点队列中取出作为新的Info的Point成员
            Point newInfoPoint = pointQueue.poll();
            // 生成Point队列的旧Info的地图作为新Info的新地图
            int[][] newInfoMap = mapCopy(father.map);
            // 在新地图上落下Point
            newInfoMap[newInfoPoint.x][newInfoPoint.y] = FLAG;

            PointMapInfo info = new PointMapInfo(newInfoPoint, newInfoMap, father);
            InfoQueue.add(info);
        }
    }

    public int compareTo(PointMapInfo o) {
        return Integer.compare(o.point.score, this.point.score);

    }
    public int reverseCompareTo(PointMapInfo o) {
        return Integer.compare(this.point.score, o.point.score);
    }
}