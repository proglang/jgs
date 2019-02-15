package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.IOUtils;
import de.unifreiburg.cs.proglang.jgs.support.Sec;

/**
 * Testing the correct handling of the polymorphic method "max".

 * Created by fennell on 26.09.17.
 */
public class PolymorphicMethods2_Success {

    @Sec("HIGH")
    static int secret = 42;

    @Sec("LOW")
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
        int i = max(1, 2);
        if (secret == 42) {
            i = zero; // should *not* trigger NSU
        }

        IOUtils.printSecret(String.valueOf(i));
    }
}

/* Sysout instead of printSecret causes an error as i has level H due to the condition on line 33. But it does not fail there due to NSU
    because i was not cast */