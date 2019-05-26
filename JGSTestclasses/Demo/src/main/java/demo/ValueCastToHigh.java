package demo;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class ValueCastToHigh {

    @Sec("?")
    static int dynField;

    @Constraints("LOW <= @0")
    @Effects({"LOW", "?"})
    public static void main(String[] args) {
        double x = Casts.cast("HIGH ~> ?", 25.0);

        double xH = Casts.cast("? ~> HIGH", x);

        double x2;
        if (xH < 15) {
            x2 = xH;
        } else {
            x2 = 42.0;
        }
        IOUtils.printSecret(x2);
    }
}
