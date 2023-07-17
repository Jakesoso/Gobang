public class Point {
    int x, y;
    int score;

    public Point() {
    }

    public Point(int x, int y, int score) {
        this.x = x;
        this.y = y;
        this.score = score;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int compareTo(Point o) {
        return Integer.compare(o.score, this.score);

    }
    public int reverseCompareTo(Point o) {
        return Integer.compare(this.score, o.score);
    }

}
