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
        System.out.println(attemptFailingCast());
        System.out.println(attemptFailingCastUsingMkHigh());
        attemptNSU();
        String hello = (aStaticMethodWithCasts(hello1)); // fails at run-time due to an bad cast in aStaticMethodWithCasts
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
    @Effects({})
    static String aStaticMethodWithCasts(String i) {
        String x = Casts.cast("HIGH ~> ?", i);
        String result;
        if (i == null) {
            System.out.println("Attempting that illegal cast!");
            result =  Casts.cast("? ~> LOW", x);
        } else {
            result = null;
        }
        return result;
    }

}
