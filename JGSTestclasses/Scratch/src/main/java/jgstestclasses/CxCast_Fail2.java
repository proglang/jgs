package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class CxCast_Fail2 {

    @Sec("HIGH")
    static int highField = 42;

    @Sec("?")
    static int dynField;

    @Sec("LOW")
    static int lowField;

    @Constraints("LOW <= @0")
    @Effects({"?","HIGH"})
    public static void main(String[] args) {

        /*if (highField == 42) {
            // we are in a high-security, dynamic context, so this cxcast should fail.
            Casts.castCx("? ~> HIGH");
            dynField = 5;
            Casts.castCxEnd();
        }*/


        dynField = DynamicLabel.makeHigh(6);
        if (highField == 42) {
            Casts.castCx("HIGH ~> ?");
            //dynField = 10;
            Casts.castCx("? ~> LOW");
            lowField = 5;
            Casts.castCxEnd();
            Casts.castCxEnd();
        }


        Casts.castCx("? ~> HIGH");
        highField = 5;
        Casts.castCxEnd();
    }
}
