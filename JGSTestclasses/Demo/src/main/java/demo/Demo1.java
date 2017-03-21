package demo;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class Demo1 {

    @Sec("?")
    static String dynField;

    @Sec("HIGH")
    static String staticField;

    @Constraints("LOW <= @0")
    @Effects({"LOW", "?"})
    public static void main(String[] args) {
        String secret = IOUtils.readSecret();
        dynField = Casts.cast("HIGH ~> ?", secret);
        IOUtils.printSecret(Casts.cast("? ~> HIGH", dynField));
        // IOUtils.printSecret(Casts.cast("? ~> LOW", dynField));

        /*
        staticField = secret;
        IOUtils.printSecret(staticField);
        */
    }
}
