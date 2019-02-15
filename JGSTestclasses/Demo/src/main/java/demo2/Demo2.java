package demo2;

import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.Sec;

import static de.unifreiburg.cs.proglang.jgs.support.DynamicLabel.makeHigh;
import static de.unifreiburg.cs.proglang.jgs.support.StringUtil.bits;

/**
 * This is Demo0 with *static enforcement*
 */
public class Demo2 {

    @Sec("HIGH") // <-- This tells the type system that "secret" is a secret
    public static String secret = "The secret is: 42";

    /* The system knows that "println" is not suitable for high-typed
    values. All these leaks can be detected now.  */

    @Effects({"LOW"})
    //         ^-- these annotation are required for checking implicit flows
    // during typing (cf. "throws" clauses in Java)
    public static void example1() {
        System.out.println(secret); // a pretty obvious leak. Even "grepable"
    }

    @Effects({"LOW"})
    public static void example2() {
        String h = secret;
        // still kind of obvious, but not grepable (in general)
        System.out.println(h);
    }

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

    @Effects({"LOW"})
    public static void example4() {
        for (boolean b : bits(secret)) {
            // this code leaks the string *implicitly*
            if (b) {
                System.out.print("1");
            } else {
                System.out.print("0");
            }
        }
        System.out.println();
    }

    @Effects({"LOW"})
    public static void main(String[] args) {
//        example1();
//        example2();
//        example3(true);
        example3(false); // cannot run!
//        example4();
    }

}

/* missing Constraints(example3()) */
/* Add Constraints({"0 <=LOW", "LOW ~ @0"}) */
/* secret has level HIGH, so all Sysouts fail */