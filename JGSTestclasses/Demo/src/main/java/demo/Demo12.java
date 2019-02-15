package demo;

import de.unifreiburg.cs.proglang.jgs.support.*;

/**
 * Simple JGS demo.
 *
 * Security lattice: LOW < MIDDLE < HIGH
 */
public class Demo12 {

    @Constraints("LOW <= @0")
    @Effects({"LOW", "?"})
    public static void main(String[] args) {
        String secret = IOUtils.readSecret(); // <- library method
        // No cast, hence statSecret level not set to HIGH
        String statSecret = secret;
        String result;
        if (statSecret.equals("42")) {
            result = "It's 42";
        } else {
            result = "It's not 42";
        }
        IOUtils.printSecret(result);
    }

}

/* prints(in xxx format) Its's 42 if secret is 42 else prints It's not 42. No errors due to casting issues or NSU */