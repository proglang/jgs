package demo;

import de.unifreiburg.cs.proglang.jgs.support.*;

/**
 * Simple JGS demo.
 *
 * Security lattice: LOW < MIDDLE < HIGH
 */
public class Demo1 {

    @Sec("?")
    static String dynField;

    @Sec("HIGH")
    static String staticField;

    @Constraints("LOW <= @0")
    @Effects({"LOW", "?"})
    public static void main(String[] args) {
        String secret = IOUtils.readSecret(); // <- library method

        IOUtils.printSecret(secret);          // <- no leak
        // System.out.println(secret);        // <- static leak
        // dynField = Casts.cast("HIGH ~> ?", secret);             // <- journey through dynamic code
        // IOUtils.printSecret(Casts.cast("? ~> HIGH", dynField)); // <-
        // IOUtils.printSecret(Casts.cast("? ~> LOW", dynField)); // <- dynamically detected leak


        /*
        staticField = secret;
        IOUtils.printSecret(staticField);
        */
    }

    @Constraints({"@0 ~ ?", "@1 <= pub"})
    private static void printDyn(String value, boolean isSecret) {
        if (isSecret)  {
            IOUtils.printSecret(Casts.cast("? ~> HIGH", value));
        } else {
            System.out.println(Casts.cast("? ~> LOW", value));
        }
    }
}
