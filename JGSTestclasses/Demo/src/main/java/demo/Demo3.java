package demo;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.Sec;

/**
 * Show off NSU failures
 */
public class Demo3 {

    @Sec("HIGH")
    static int secret = 42;

    @Constraints("@0 ~ LOW")
    @Effects("LOW")
    public static void main(String[] args) {
        String msg = "Don't know the secret";
        int dynSecret = Casts.cast("HIGH ~> ?", secret);
        if (dynSecret == 42) {
            msg = "I guess it's 42";
        }
        System.out.println(Casts.cast("? ~> LOW", msg));
    }
}
