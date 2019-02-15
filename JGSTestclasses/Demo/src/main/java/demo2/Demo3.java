package demo2;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.Sec;

/**
 * This is example3 of Demo0 with *gradual enforcement*
 */
public class Demo3 {

    @Sec("HIGH")
    public static String secret = "The secret is: 42";

    /*
    @Effects({"LOW"})
    public static void example3(boolean b) {
        String h = secret;
        String x;
        if (b) {
            x = h;
        } else {
            x = "XXX";
        }
        System.out.println(x); // a leak iff b == true
    }
    */

    @Effects({"LOW"})
    public static void example3_gradual(boolean b) {
        String h = secret;
        String x;
        if (b) {
            x = Casts.cast("HIGH ~> ?", h); //<-- the conversion is resolved at compile time
        } else {
            x = "XXX";
        }

        /* Illegal cast of LOW to x which has level HIGH */
        /* Change LOW to HIGH below, but HIGH cannot be passed with Sysout. So pass with IOUtils.printSecret() */
        System.out.println(Casts.cast("? ~> LOW", x)); // a leak iff b == true
    }

    @Effects({"LOW"})
    public static void main(String[] args) {
//        example1();
//        example2();
//        example3(true);
        example3_gradual(false); // runs fine
        example3_gradual(true); // security error
//        example4();
    }

}


/* missing Constraints(example3_gradual()) */
/* Add Constraints({"0 <=?", "? ~ @0"}) */