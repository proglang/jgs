package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class NestedContextCast {

    @Sec("HIGH")
    static int highField = 42;

    @Sec("?")
    static int dynField = 5;

    @Sec("LOW")
    static int lowField;

    @Constraints("LOW <= @0")
    @Effects({"?","HIGH"})
    public static void main(String[] args) {
        if (highField == 42) {
            Casts.castCx("HIGH ~> ?");
            //dynField = 10; //-- NSU Failure
            Casts.castCx("? ~> LOW");
            lowField = 5;
            Casts.castCxEnd();
            Casts.castCxEnd();
        }
    }
}
