package transitivity;

import java.awt.*;
import java.util.Objects;

/**
 * 折中方法，不再让ColorPoint继承Point，而是在新的ColorPoint类中加入私有的point域，
 * 以及一个公有view方法{@link #asPoint()}。这样，既能用view方法得到{@link Point}，
 * 并通过Point类的equals方法与Point类的实例比较，又能够直接用equals与本类型比较。
 *
 * @author LightDance
 */
public class ColorPointC {
    private final Point point;
    private final Color color;

    public ColorPointC(int x, int y, Color color) {
        point = new Point(x, y);
        this.color = Objects.requireNonNull(color);
    }

    /**
     * Returns the point-view of this color point.
     */
    public Point asPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPointC)) {
            return false;
        }
        ColorPointC cp = (ColorPointC) o;
        return cp.point.equals(point) && cp.color.equals(color);
    }
}
