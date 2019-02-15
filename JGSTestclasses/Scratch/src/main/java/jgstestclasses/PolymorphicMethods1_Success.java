package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.IOUtils;
import de.unifreiburg.cs.proglang.jgs.support.Sec;

/**
 * Testing the correct handling of the polymorphic method "max".

 * Created by fennell on 26.09.17.
 */
public class PolymorphicMethods1_Success {

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
        IOUtils.printSecret(String.valueOf(max(1, secret)));
    }
}

/* No cast, no errors */
/* Sysout on line 30 does not work */