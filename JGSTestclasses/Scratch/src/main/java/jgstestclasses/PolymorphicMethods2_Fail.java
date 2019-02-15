package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.*;

/**
 * Testing the correct handling of the polymorphic method "max".

 * Created by fennell on 26.09.17.
 */
public class PolymorphicMethods2_Fail {

    @Sec("HIGH")
    static int secret = 42;

    @Sec("pub")
    static int zero = 0;

    @Constraints({"@0 <= @ret", "@1 <= @ret"})
    public static int max(int x, int y) {
        if (x < y) {
            return y;
        } else {
            return x;
        }
    }

    @Effects({"LOW"})
    public static void main(String ... args) {
        int i = max(Casts.cast("pub ~> ?", 1), 2);
        int dynSecret = Casts.cast("HIGH ~> ?", secret);
        if (dynSecret == 42) {
            i = zero; // should trigger NSU
        }

        IOUtils.printSecret(String.valueOf(i));
    }
}

/* NSU failure at line 32 as dynSecret has level H and
   Even if the cast at line 30 is removed, dynSecret still has level H as secret has level H */
