package asymmetry;

import java.util.Objects;

/**
 * 忽略大小写的字符串类型，重写了equals导致违反对称性。
 *
 * @author LightDance
 */
public class CaseInsensitiveString {

    private final String s;

    CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CaseInsensitiveString) {
            return s.equalsIgnoreCase(
                    ((CaseInsensitiveString) o).s);
        }
        if (o instanceof String) {
            return s.equalsIgnoreCase((String) o);
        }
        return false;
    }

    boolean equals2(Object o) {
        return o instanceof CaseInsensitiveString &&
                ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
    }
}
