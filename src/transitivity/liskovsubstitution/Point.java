package transitivity.liskovsubstitution;

import java.util.HashSet;
import java.util.Set;

/**
 * 这个Point类与{@link transitivity.Point}的不同在于，它的equals方法中把instanceof换成了getClass.
 * 这个类只有在对象具有相同实现(类相同)时才能使对象等同，也就避免了子类“钻空子”导致违反传递性的情况，
 * 虽然似乎不是太糟糕，但请看这个例子:
 *
 * 我们希望添加一个能够检测某整值点是否处在单位圆上的方法如下{@link #onUnitCircle(Point)},
 * 然后又要通过继承为其添加一些新的成员变量，比如记录创建了多少个实例的计数器{@link CounterPoint}
 * 假如把CounterPoint的实例传给{@link #onUnitCircle(Point)}方法，
 * 那么即使该实例满足在单位圆上的条件，结果也仍然会返回false，这违反了传说中的<strong>里氏置换原则</strong>。
 *
 * 里氏置换原则：一个类的任何重要属性也应适用于它的子类。
 *
 * 由于Point使用了基于getClass的equals方法，而{@link #onUnitCircle(Point)}中的contains()检验是基于
 * equals方法的，因此它总是返回false.父类Point的实例在该方法上完美运行，而继承它的子类CounterPoint
 * 的实例却不行，这显然和面向对象的继承关系有矛盾。
 *
 * @author LightDance
 */
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    private static final Set<Point> unitCircle = new HashSet<Point>();
    static {
        unitCircle.add(new Point(1,0));
        unitCircle.add(new Point(-1,0));
        unitCircle.add(new Point(0,1));
        unitCircle.add(new Point(0,-1));
    }

    public static boolean onUnitCircle(Point p) {
        return unitCircle.contains(p);
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        Point p = (Point) o;
        return p.x == x && p.y == y;
    }

    public static void main(String[] args) {
        Point p1 = new Point(1, 1);
        Point p2 = new Point(   1,1);
        System.out.println(p1.equals(p2));
        System.out.println(p2.equals(p1));
    }
}
