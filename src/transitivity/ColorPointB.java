package transitivity;

import java.awt.*;

/**
 * 颜色+坐标的点，第二种重写equals的方式。
 * 改进后并不违反自反性，但是由于非要跟父类互相操作，颜色的忽略与不忽略导致违反了传递性
 *
 * @author LightDance
 */
public class ColorPointB extends Point{
    private final Color color;

    public ColorPointB(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    /**违反了自反性*/
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) {
            return false;
        }

        //如果o是无颜色参数的点，比较时忽略颜色
        if (!(o instanceof ColorPointB)){
            return o.equals(this);
        }else {
            return super.equals(o) && ((ColorPointB) o).color == color;
        }

    }
}
