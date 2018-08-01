package transitivity;

import java.awt.*;

/**
 * 一个演示子类影响equals结果的测试类
 *
 * {@link Point}仅有坐标属性，而当继承它的子类{@link ColorPointA}和{@link ColorPointB}添加颜色属性时，
 * 设计者企图使其equals方法能够与父类相比较，但是在子类A中，由于继承关系，mColorPointA.equals(mPoint)与
 * mPoint.equals(mColorPointA)的结果是不一样的，即违反了对称性；
 * 于是对其改进，有了子类B.子类B通过在equals方法中加入对父类的处理逻辑，满足了对称性，但由于不得不忽略颜色，
 * 导致违反了传递性
 *
 * 那么如何解决？实际上这是面向对象语言中等价关系的一个基本问题，我们无法在扩展可实例化的类的同时，
 * 既增加新的变量，又保留原有的equals约定，除非愿意放弃面向对象的抽象所带来的优势。
 *
 * @author LightDance
 */
public class NonTransitivityExample {

    public static void main(String[] args) {
        Point mPoint = new Point(1, 1);
        ColorPointA mColorPointA = new ColorPointA(1, 1, Color.RED);
        ColorPointB mColorPointB1 = new ColorPointB(1, 1, Color.RED);
        ColorPointB mColorPointB2 = new ColorPointB(1, 1, Color.BLUE);

        System.out.println("asymmetry:");
        System.out.println("mPoint:mColorPointA is:" + mPoint.equals(mColorPointA));
        System.out.println("mColorPointA:mPoint is:" + mColorPointA.equals(mPoint));

        System.out.println("symmetry but non-transitivity:");
        System.out.println("mPoint:mColorPointB1 is:" + mPoint.equals(mColorPointB1));
        System.out.println("mColorPointB1:mPoint is:" + mColorPointB1.equals(mPoint));
        System.out.println("mColorPointB2:mPoint is:" + mColorPointB2.equals(mPoint));
        System.out.println("mColorPointB1:mColorPointB2 is:" + mColorPointB1.equals(mColorPointB2));

    }
}
