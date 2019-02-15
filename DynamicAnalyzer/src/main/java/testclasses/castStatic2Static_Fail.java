package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;

/**
 * Created by Nicolas Müller on 03.03.17.
 */
public class castStatic2Static_Fail {
    public static void main(String[] args) {
        String s1 = "foo";
        int i = 1337;
        i = Casts.cast("LOW ~> HIGH", i);     // should be fine --> is not fine, fails

        // can't use that here, cause it will cause all unit tests to fail
        //s1 = Casts.cast("HIGH ~> LOW", s1);   // not okay! Security Exception
    }
}
