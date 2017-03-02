package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import utils.analyzer.HelperClass;

/**
 * Make integer high, then cast it back to low.
 * Must throw IllegalFlowException (java.lang.Object_$r5)
 */
public class castFromDyn_Fail1 {

   public static void main(String[] args) {
      int z = HelperClass.makeHigh(3);
      z = Casts.cast("? ~> LOW", z);
   }
}
