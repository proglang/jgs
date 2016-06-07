package pkg;

import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;

public class B extends A {

    int publicField;

    // OK: we produce less flows than our parent
    @Override
    public int m1(int x, int y) {
        return 0;
    }

    // ERROR: we have more effects than our parent and also more flows to the return value
    @Override
    @Constraints({"@0 <= @ret", "@1 <= @ret"})
    @Effects ({"pub"})
    public int m2(int x, int y) {
        this.publicField = 0;
        return x + y;

    }
}
