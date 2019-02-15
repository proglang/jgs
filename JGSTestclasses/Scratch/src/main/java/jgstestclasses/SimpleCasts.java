package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class SimpleCasts {

    @Constraints({"LOW <= @0 "})
    @Effects("LOW")
    public static void main(String[] args) {
        String hello1 = null;
        System.out.println(attemptFailingCast()); // Fails as the name suggests
        System.out.println(attemptFailingCastUsingMkHigh()); // Fails as the name suggests
        attemptNSU(); // Fails as the name suggests
        String hello = (aStaticMethodWithCasts(hello1)); // fails at run-time due to a bad cast in aStaticMethodWithCasts
    }

    @Constraints({"LOW <= @ret"})
    static String attemptFailingCast() {
        String v = "secret: 42";
        String x = Casts.cast("HIGH ~> ?", v);
        return Casts.cast("? ~> LOW", x);
    }

    @Constraints({"LOW <= @ret"})
    static String attemptFailingCastUsingMkHigh() {
        String x = DynamicLabel.makeHigh("secret: 42");
        return Casts.cast("? ~> LOW", x);
    }

    @Effects("LOW")
    static void attemptNSU() {
        String x = DynamicLabel.makeHigh("secret: 42");
        String low = DynamicLabel.makeLow("null");
        System.out.println("attempting NSU");
        if (x != null) {
            low = "not null";
        }
    }

    @Constraints({"? <= @ret"})
    static String attemptNSUAndReturn() {
        String x = DynamicLabel.makeHigh("secret: 42");
        String low = "I guess the secret is `42'";
        String tmp = null;
        if (x != null) {
            tmp = "secret is null";
        }
        if (tmp == null) {
            low = "I guess the secret is `null'";
        }
        return low;
    }

    @Constraints({"HIGH <= @0 ", "@0 <= @ret"})
    @Effects({"LOW"})
    static String aStaticMethodWithCasts(String i) {
        String x = Casts.cast("HIGH ~> ?", i);
        /* x has level H due to the cast, and if i is null, then converting it to LOW fails */
        /* if i is not null, then there is no error */
        String result;
        if (i == null) {
            result =  Casts.cast("? ~> LOW", x);
        } else {
            result = null;
        }
        return result;
    }

}
