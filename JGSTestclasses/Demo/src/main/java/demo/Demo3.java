package demo;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.Sec;

/**
 * Show off NSU failures.
 *
 * Also shows of a strange bug; NSU errors non-deterministically do not happen. Maybe the checkPC... methods get moved around by the compiler?
 */
public class Demo3 {

    @Sec("HIGH")
    static int secret = 42;

    @Sec("?")
    static String dynField = "";

    @Sec("HIGH")
    static String secretString = "42";

    @Constraints("@0 ~ LOW")
    @Effects({"LOW", "?"})
    public static void main(String[] args) {
        System.out.println(Casts.cast("? ~> LOW", attemptNSUAndReturn()));
    }


    @Constraints({"? <= @ret"})
    static String attemptNSUAndReturn() {
        String x = Casts.cast("HIGH ~> ?", secretString);
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

    @Constraints("? <= @ret")
    @Effects({"LOW", "?"})
    public static String m() {
        String msg = "Don't know the secret";
        int dynSecret = Casts.cast("HIGH ~> ?", secret);
        if (dynSecret == 42) {
            msg = "I guess it's 42";
        }
        dynField = msg;
        return msg;
    }
}
