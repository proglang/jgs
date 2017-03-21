package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Casting a high dyn int to static high -> everything ok here.
 */
public class castDyn2Static_Success1 {
    public static void main(String[] args) {
        int z = DynamicLabel.makeHigh(3);
        z = Casts.cast("? ~> HIGH", z);
    }
}
