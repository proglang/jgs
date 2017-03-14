package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.Sec;

/**
 * Created by Nicolas Müller on 14.03.17.
 */
public class SimpleFail {

    @Constraints({"LOW <= @0 "})
    public static void main(String[] args) {
        String hello1 = "Hello World";
        String hello = (aStaticMethodWithCasts(hello1)); // fails at run-time due to an bad cast in aStaticMethodWithCasts
    }

    @Constraints({"HIGH <= @0 ", "@0 <= @ret"})
    @Effects({})
    static String aStaticMethodWithCasts(String i) {
        String x = Casts.cast("HIGH ~> ?", i);
        if (i == null) {
            return Casts.cast("? ~> LOW", x);
        } else {
            return null;
        }
    }

}
