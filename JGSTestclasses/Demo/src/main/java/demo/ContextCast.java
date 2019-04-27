package demo;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class ContextCast {

    @Sec("HIGH")
    static int highField = 45;

    @Sec("?")
    static int dynField;

    @Sec("LOW")
    static int lowField;

    @Constraints("LOW <= @0")
    @Effects({"?","LOW"})
    public static void main(String[] args) {
        if (highField == 42) {
            Casts.castCx("HIGH ~> ?");
            //dynField = 10; -- NSU Failure
            Casts.castCx("? ~> LOW");
            //dynField = 5;
            lowField = 5;
            Casts.castCxEnd();
            Casts.castCxEnd();
       }
    }
}
