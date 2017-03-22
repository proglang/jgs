package demo;

import de.unifreiburg.cs.proglang.jgs.support.*;

/**
 * Simple JGS demo.
 *
 * Security lattice: LOW < MIDDLE < HIGH
 */
public class Demo11 {

    @Constraints("LOW <= @0")
    @Effects({"LOW", "?"})
    public static void main(String[] args) {
        String secret = IOUtils.readSecret(); // <- library method

         String dynSecret = Casts.cast("HIGH ~> ?", secret);             // <- journey through dynamic code
         String result;
         if (dynSecret.equals("42")) {
             result = "It's 42";
         } else {
             result = "It's not 42";
         }
         IOUtils.printSecret(Casts.cast("? ~> HIGH", result));
    }

}
