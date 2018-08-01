package transitivity.liskovsubstitution;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 添加了新的成员变量的Point,用于说明里氏置换原则
 *
 *
 * @author LightDance
 */
public class CounterPoint extends Point {
    public static final AtomicInteger counter = new AtomicInteger();

    public CounterPoint(int x, int y) {
        super(x, y);
        counter.incrementAndGet();
    }

    public int numberCreated(){return counter.get();}
}
