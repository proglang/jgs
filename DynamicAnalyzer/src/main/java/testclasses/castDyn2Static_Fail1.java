package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Make integer high, then cast it back to low.
 * Must throw IFCError (java.lang.Object_$r5)
 */
public class castDyn2Static_Fail1 {

   public static void main(String[] args) {
      int z = DynamicLabel.makeHigh(3);
      z = Casts.cast("? ~> MEDIUM", z);
   }
}
