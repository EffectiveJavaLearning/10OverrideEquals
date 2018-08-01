package transitivity;

/**
 * 只有坐标的点
 *
 * @author LightDance
 */
public class Point {
    private final int x;
    private final int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) {
            return false;
        }
        Point p = (Point) o;
        return p.x == x && p.y == y;
    }
}
