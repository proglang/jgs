package demo;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class DemoCxCast {

    @Sec("?")
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

        int zLow = Casts.cast("? ~> LOW", dynField);
        if (dynField == 42) {
            Casts.castCx("? ~> LOW");
//            lowField = zLow;
            lowField = 5;
            Casts.castCxEnd();
        }

        IOUtils.printSecret(String.valueOf(lowField));
    }
}
