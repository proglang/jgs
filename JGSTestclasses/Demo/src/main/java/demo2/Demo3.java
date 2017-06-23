package demo2;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.Sec;

import static de.unifreiburg.cs.proglang.jgs.support.StringUtil.bits;

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
