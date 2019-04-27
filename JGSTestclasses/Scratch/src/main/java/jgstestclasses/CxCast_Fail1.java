package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class CxCast_Fail1 {

    @Sec("HIGH")
    int a;

    @Constraints("LOW <= @0")
    @Effects({"LOW", "HIGH", "?"})
    public static void main(String[] args) {
       /* String s = "ffgdg";
        s = Casts.cast("LOW ~> ?", s);
        s = s.substring(2);
        IOUtils.printSecret(s);*/


        int x = Casts.cast("? ~> HIGH", 7);

        while (x < 8) {
            x++;
        }

        IOUtils.printSecret(x);


    }



    /*@Sec("?")
    static int dynField;

    @Sec("HIGH")
    static int staticField;

    @Sec("LOW")
    static int lowField;

    @Constraints("LOW <= @0")
    @Effects({"LOW", "?"})
    public static void main(String[] args) {
        staticField = 42;
        dynField = Casts.cast("HIGH ~> ?", staticField);

        if (dynField == 42) {
            // we are in a high-security, dynamic context, so this cxcast should fail.
            Casts.castCx("? ~> LOW");
            lowField = 5;
            Casts.castCxEnd();
        }

        IOUtils.printSecret(String.valueOf(lowField));
    }*/
}
