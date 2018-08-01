package asymmetry;

import java.util.ArrayList;
import java.util.List;

/**
 * 不小心违反了对称性的例子{@link CaseInsensitiveString}，它的equals方法设计初衷很好，
 * 希望可以跟String类型相互操作，但问题是{@link String#equals(Object)}是区分大小写的，
 * 所以下面两个equals的结果是不同的。
 *
 * 而且，如果把这种违反自反性的对象放到List里面，那么调用contains方法传入String类型的参数，
 * 返回结果也可能在不同环境下出现变化，在当前版本的JDK中，它恰好返回的是false，但在其他的实现中，
 * true或者抛出异常都是有可能发生的。也就是说：
 * <strong>一旦违反了通用规约，就不能确定其他对象在面对你的对象时将做出什么反应</strong>
 *
 * 若要遵守规约，修改也不麻烦，只要放弃与String类型的相互操作就好了，比如：
 * {@link CaseInsensitiveString#equals2(Object)} (由于想放在一起作对比，
 * 就没办法同名&加override标记了)
 *
 * @author LightDance
 */
public class AsymmetryExample {

    public static void main(String[] args) {
        String str = "String";
        CaseInsensitiveString cis = new CaseInsensitiveString("string");
        //return false
        System.out.println(str.equals(cis));
        //return true
        System.out.println(cis.equals(str));

        List<CaseInsensitiveString> list = new ArrayList<>();
        list.add(cis);
        //return false
        System.out.println(list.contains(str));
    }
}
