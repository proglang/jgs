package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Created by Nicolas Müller on 20.02.17.
 * Purpose of this is to check that
 *
 * res =    i      +     j
 *          |            |
 *        DYN(LOW)     PUBLIC
 *
 * returns DYN(LOW). See {@link util.staticResults.FakeTypingMaps}
 */
public class LowPlusPublic {
    public static void main(String[] args) {
        int i = Casts.cast("? ~> LOW", 9);
        //int i = DynamicLabel.makeLow(9);              // b0: this is going to be DYNAMIC(LOW), so fails
        int j = 10;             // b1: this is going to be PUBLIC
        int res = i + j;        // i2: the join of b0 and b1
        System.out.println(res);
    }
}
