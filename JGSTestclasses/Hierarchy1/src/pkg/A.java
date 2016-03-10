package pkg;

import de.unifreiburg.cs.proglang.jgs.support.Constraints;

/**
 * A base class
 */
public class A {

    private final int x;
    private final int y;

    @Constraints({"@0 <= pub", "@1 <= pub"})
    public A(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
