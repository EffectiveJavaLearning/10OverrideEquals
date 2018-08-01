package transitivity;

import java.awt.*;

/**
 * 颜色+坐标的点，第一种重写equals的方式。
 * 由于继承关系，违反了自反性
 *
 * @author LightDance
 */
public class ColorPointA extends Point{
    private final Color color;

    public ColorPointA(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    /**违反了自反性*/
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPointA)) {
            return false;
        }
        return super.equals(o) && ((ColorPointA) o).color == color;
    }
}
