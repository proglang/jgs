package demo2;

import static de.unifreiburg.cs.proglang.jgs.support.DynamicLabel.makeHigh;
import static de.unifreiburg.cs.proglang.jgs.support.StringUtil.bits;

/**
 * This is Demo0 with *dynamic enforcement*
 */
public class Demo1 {

    /* "makeHigh" is a special method, that adds a high-security label to the
     string */
    public static String secret = makeHigh("The secret is: 42");

    /* The system knows that "println" is not suitable for high-labelled
    values. All these leaks can be detected now.  */

    public static void example1() {
        System.out.println(secret); // a pretty obvious leak. Even "grepable"
    }


    public static void example2() {
        String h = secret;
        // still kind of obvious, but not grepable (in general)
        System.out.println(h);
    }

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

    public static void main(String[] args) {
//        example1();
//        example2();
//        example3(true);
        example3(false);
        example4();
    }

}


/* missing Effects(all methods) and Constraints(example3()) */
