package demo2;

import static de.unifreiburg.cs.proglang.jgs.support.StringUtil.bits;

/**
 * This example illustrates the basic information flows and leaks.
 * <p>
 * Here the field "secret" contains a secret, and System.out.println is a public
 * sink.
 */
public class Demo0 {

    /* Here is the "secret". In reality, this is a password, some sensible,
    personal information, etc. */
    public static String secret = "The secret is: 42";

    /* Let's look at some degenerate examples where leak */

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
        example1();
        example2();
        example3(true);
        example4();
    }

}

/* missing Effects(all methods) and Constraints(example3()) */