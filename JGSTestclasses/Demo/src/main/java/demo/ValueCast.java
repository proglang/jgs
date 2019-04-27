package demo;

import de.unifreiburg.cs.proglang.jgs.support.*;
import fj.data.IO;

public class ValueCast {

    @Sec("?")
    static int dynField;

    @Constraints("LOW <= @0")
    @Effects({"LOW", "?"})
    public static void main(String[] args) {

        double x = Casts.cast("HIGH ~> ?", 25.0);
        IOUtils.printSecret(x);
    }
}
