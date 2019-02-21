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
    public static void main(String... args) {
        int i;
        boolean secret = DynamicLabel.makeHigh(true);
        if (secret) {
            i = 1; // NO NSU FAILURE
        } else {
            i = 0; // NO NSU FAILURE
        }
        IOUtils.printSecret(String.valueOf(i));
    }

}
