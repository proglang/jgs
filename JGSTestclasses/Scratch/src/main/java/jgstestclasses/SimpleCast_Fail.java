package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;

import static utils.analyzer.HelperClass.makeHigh;

public class SimpleCast_Fail {

    @Constraints({"LOW <= @0"})
    public static void main(String[] args) {
        int v = 42;
        int x = makeHigh(v);
        System.out.println(Casts.cast("? ~> LOW", x));
    }
}
