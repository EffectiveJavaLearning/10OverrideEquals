import asymmetry.AsymmetryExample;

/**
 * 覆盖equals需要十分谨慎，因为很可能导致严重后果。最容易的避免方式就是不要去覆盖它，
 * 这种默认情况下每个类只和它本身相等。如果满足了以下条件之一，那就直接继承而不要去重写它：
 *      (1).类的每个实例本质上唯一。比如{@link Thread}这种，所代表的不是值而是一个活动实体，
 *      应当使equals()方法保证能够区分它们。
 *      (2).无需为该类提供“逻辑相等”测试（简称“重写无意义”）。比如，虽然{@link java.util.regex.Pattern}
 *      可以重写equals来比较两个Patten匹配形式表达式是否相同，但感觉完全不会有什么地方会要求比对这个，
 *      所以这时候也完全无需重写这个方法
 *      (3).父类已经重写过这个方法，而这对子类仍然适用。比如大多数Set继承了父类
 *      {@link java.util.AbstractSet}重写后的equals方法，Map , List同理。
 *      (4).类的访问权限是private或者缺省(包私有)的，而且确定它的equals不会被调用（简称“没用”）。
 *      当然如果还是不放心，也可以重写一下比如这样:
 *          public boolean equals(Object o) {throw new AssertionError();}
 *
 * 那么什么时候需要覆盖它？那就是该类(可以称为“值类”value class)有独特的逻辑相等判断方式(不同于对象等同)，
 * 而父类的equals又没办法满足这一需求的时候。这种情况通常发生在当类仅仅表示一个值的时候，比如Integer或者
 * Date的比较，无需了解两者是否同一对象，而要知道其值是否等同。这时为满足需求，不仅需要覆盖equals()方法，
 * 还要保证该类的实例被用到map映射作“键”，或者在set集合中作为其中元素时符合逻辑，满足预期行为。
 *
 * *注意，有一种“值类”(value class)不需要覆盖equals方法，称为“实例受控类”(单例?)。这种类型已确保创建时，
 * 每个实例绝对唯一。比如枚举类型Enum就属于这种类型。对其而言，逻辑相等与对象相等实际上是等价的，
 * 于是也便无需重写equals()了。
 *
 * 当确认需要覆盖它的时候，也必须遵守一定的通用规约，即在x,y不为null的前提下满足：
 *      (1).自反性。x.equals(x) == true;
 *      (2).对称性。x.equals(y) == y.equals(x);
 *      (3).传递性。x.equals(y) == true && y.equals(z) == true ,则 x.equals(z)必须为true;
 *      (4).一致性。x.equals(y)在x与y的值没有改变的情况下返回结果不会变;
 *      (5).x.equals(null) == false 总是成立。
 *
 * 似乎这些公式看起来很头大，但是想传达的意思非常容易理解，学过一点离散数学更容易理解。不过要警惕，
 * 如果不小心违反了其中规定，那么你的麻烦就大了，因为实例之间有时会频繁通信、传递，
 * 而且包括{@link java.util.Collections}在内的所有集合类以及其他很多类，都需要依靠equals方法进行判断，
 * 而出现问题之后却不太容易联想到是equals的锅。
 *
 * 下面逐一解释上面5条通用规约。
 *  1.自反性(reflexivity)。不是很容易违反的一条，调试时可以添加到collection中然后查看contains结果，
 *  以确定是否包含刚刚添加进去的类。
 *  2.对称性(symmetry)。举一个大小写匹配是否相同导致违反对称性的例子{@link AsymmetryExample}
 *  3.传递性(transitivity)。无意识地违反这一条一般发生在子类继承中，因子类增加的信息会影响equals结果，
 *  比如{@link transitivity.NonTransitivityExample}。我们无法在扩展可实例化的类的同时，
 *  既增加新的成员变量，又保留原有的equals约定，除非愿意放弃面向对象的抽象所带来的优势。
 *      *似乎用getClass代替instanceof能够通过防止子类“钻空子”而解决这些冲突，对此专门做一些解释，见：
 *      {@link transitivity.liskovsubstitution.Point}
 *      虽然没有办法能既扩展可实例化的类，在增加成员变量的同时保留原equals约定，但有一种比较好的权宜之计：
 *      根据复合优先于继承，我们不再让ColorPoint继承Point，而是在新的ColorPoint类中加入私有的point域，
 *      以及一个公有的view方法{@link transitivity.ColorPointC}
 *
 *      另外，Java的类库中有一些类扩展了可实例化的类，加入了新的成员变量。比如{@link java.sql.Timestamp}
 *      就扩展了{@link java.util.Date}，甚至还违反了对称性。对此，Timestamp整了个免责声明，
 *      告诫程序员不要把Timestamp和Date混合使用，否则会引起不正确的行为，但除了建议以外，
 *      没有任何阻止该行为的措施，而且一旦出错将会很难调试，小朋友们千万不要学习它哦
 *
 *      **另外，在抽象类的子类中对父类进行扩展是不会违反equals通用规约的。对于
 *      “Prefer class hierarchies to tagged classes”这一条来说挺重要。比如先创建啥都没有的抽象Shape类，
 *      再用Circle去继承它并加入新的成员变量和成员方法，只要别直接创建超类的实例，上述问题就不会发生。
 *
 *  4.一致性(consistency)。想保证一致性，就别让equals方法依赖不可靠的资源。
 *  比如{@link java.net.URL#equals(Object)}依赖于对URL中主机IP地址的比较，而将主机名转化成IP地址，
 *  可能需要访问网络。这随着时间推移，不能确保永远产生相同的结果，所以违反了equals规约。但遗憾的是，
 *  因为兼容性的要求，这一行为无法更改。为了避免这种问题，所有equals方法应仅仅对内存中的对象执行确定性计算
 *
 *  5.非空性(not-nullity)。所有对象与null比较都应该返回false，因为如果返回true，那么很可能在之后的运行过程中抛出
 *  {@link NullPointerException}.因此，很多类的equals方法中都会对null进行显式的if判断去识别它：
 *  if(o == null)return false;但实际上并没有这个必要，因为使用instanceof的时候已经可以检查是否为空了。
 *  如果漏掉这一步类型检查，而传入的参数类型又有问题，就会抛{@link ClassCastException};
 *  如果instance的第一个操作数为null，则无论第二个操作数是什么，都会返回false，就不需要单独对null的if判断了。
 *
 * 综上，总结写出高质量equals方法的秘籍：
 *  1.==操作符检查“是否为这个对象的引用”。是则返回true.这只是一种性能优化，如果比较的代价很高可以考虑这么做。
 *  2.instanceof检查“参数类型是否正确”。正确指的是equals方法所在的那个类，或者该类所实现的某个接口。
 *  如果接口需要类实现经过改进的equals方法以允许跨类比较，那么就让接口作为第二个操作数。比如Set，Map，
 *  List这些，它们都实现了Collection接口，所以都应该这么干。
 *  3.将参数强转为正确类型。由于之前的instanceof判断，所以这一步骤不会出现什么问题
 *  4.对该类中的每个“关键”域，检查它们是否相等（比如之前{@link transitivity.Point}中对坐标值的比较）。
 *  如果步骤2中的第二操作数是接口，就必须通过接口方法访问参数的字段；如果是类，就可以根据类中这些参数的访问权限，
 *  直接或间接地访问这些字段。
 *      *.对于float或者double类型，需要使用{@link Float#compare(float, float)}或者
 *      {@link Double#compare(double, double)}进行比较，否则因浮点型精度问题，可能会有“相等值比较返回fasle”
 *      这样的bug出现；而且对float和double的特殊值特殊处理是有必要的，因为存在NaN,0.0f这种常量。
 *      float与double之间的比较可以用{@link Float#equals(Object)}或者{@link Double#equals(Object)},
 *      但是由于涉及到自动装箱，效率会相对较低。对于数组来说，需要确认对应的每个元素都相同，
 *      这时如果能保证每个元素都是有效的，那么可以考虑使用{@link java.lang.reflect.Array#equals(Object)}
 *
 *      **.有些情况下，null是合法的，为了防止抛出空指针异常，可以用{@link Object#equals(Object)}检测是否相同，
 *      有另一些情况下，对象之间的比较或许非常复杂繁琐耗时，那么可以考虑把一些“范式”置为常量存储，
 *      到时候直接跟这些范式比较，会省时很多。这对于一些不可变类非常管用。因为一旦类发生改变，范式也要做出相应变化。
 *
 *      ***.equals方法性能受字段比较的顺序影响，应将最有可能不一样的、比较起来容易的字段，将其比较的顺序提前。
 *      并且，不应该比较不属于对象逻辑状态域之外的字段，比如用于同步操作的Lock域；
 *      也不要比较可以由关键字段计算得出的冗余数据，因为计算方式不变时，特定参数应该对应相同的结果，
 *      不过如果这种结果能体现该类的整体特征，换句话讲就是比较完这一个参数就能确定其他十几个参数是否相等时，
 *      当然可以先比较这个参数。比如对两个多边形类进行比较时，如果面积area不同，那么两个多边形必然不相同
 *  5.当编辑完equals方法之后，需要检查它是否满足自反、对称、一致三个特性(另外两个特性一般会自动满足)，
 *  并且稍稍花些时间验证。
 *  6.覆盖equals方法的同时也要覆盖hashCode()，说明见(Always override hashCode when you override equals).
 *  7.不要企图将equals方法搞得多么智能，并且不要把任何有关数据分析的语句放在其中，
 *  比如帮文件的符号链接自动找到对应文件然后进行比较
 *  8.不要将形参类型Object换成其他什么类型。换成其他类型后，其他程序员使用时或许会花很多时间去搞清它为什么写成这样，
 *  然后为什么出现异常。原因在于，改变成了其他类型之后，并没有覆盖掉父类的equals方法，而是重载了它，
 *  在原有equals 方法的基础上又提供了一个“强类型(strongly typed)”的equals方法，这可能会导致子类重写该方法时，
 *  不注意就重写了错误的方法，然后再不注意又调用了错误的方法，严重影响系统正常运行，拖慢开发速度。
 *      为了防止这种问题，强烈建议不要手动输入方法声明，而是使用@Override的方式重写(在IDEA 里面快捷键是ctrl+O),
 *      可以有效避免这种失误。例如{@link #equals(Integer)}，如果将@Override注解前的双斜杠注释符号去掉，
 *      这个方法编译时会报错然后编译失败。(Error:(118, 5) java: 方法不会覆盖或实现超类型的方法)
 *
 * 写这些equals方法可能比较枯燥，好在现在已经有很多像Google.AutoValue这样的开源框架自动生成equals方法
 * (以及hashCode)，而且IDE(Integrated Development Environment集成开发环境)也有很多类似的插件，
 * 但使用起来还是Auto Value最方便，简洁、自动追踪并修改代码。所以，尽量使用IDE或者AutoValue
 * 自动生成这两种方法，这样可以避免很多粗心造成的错误。
 *
 *
 *
 *
 *      昨夜寒蛩不住鸣。惊回千里梦，已三更。起来独自绕阶行。人悄悄，帘外月胧明。
 *      白首为功名。旧山松竹老，阻归程。欲将心事付瑶琴。知音少，弦断有谁听。
 *
 * @author LightDance
 */
public class OverrideEquals {

    //@Override
    public boolean equals(Integer obj) {
        return super.equals(obj);
    }

}
