package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.*;

/**
 * Testing the correct handling of the polymorphic method "max".

 * Created by fennell on 26.09.17.
 */
public class PolymorphicMethods1_Fail {

    @Sec("HIGH")
    static int secret = 42;

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
        System.out.println(max(1, 2));
        String result = Casts.cast("HIGH ~> ?", String.valueOf(max(1, secret)));
        System.out.println(Casts.cast("? ~> LOW", result));
    }
}

/* result has level H due to the cast on line 27. Hence casting it to LOW on line 28 fails */

/* If the cast is taken out on line 27, result still has level H as secret is defined with level H and it still fails */

/* Direct Sysout on line 27 (with or without the cast) does not work */