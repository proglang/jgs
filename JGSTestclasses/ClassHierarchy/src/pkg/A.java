package pkg;

import de.unifreiburg.cs.proglang.jgs.support.*;

/**
 * A base class
 */
public class A {

    @Sec("HIGH")
    public int publicField;

    @Constraints({"@0 <= @ret", "@1 <= @ret"})
    public int m1(int x, int y) {
        return x + y;
    }

    @Constraints({"@0 <= @ret", "@1 <= HIGH"})
    @Effects({"HIGH"})
    public int m2(int x, int y) {
        publicField = y;
        return x;
    }
}
