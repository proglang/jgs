package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;

public class SimpleCastFail1 {

   public static void main(String[] args) {

   }

    /**
     * x has Type TOP, y has typ PUBLIC, return value is dynamic
     */
   public static int m(int x, int y) {
      int z = Casts.cast("[top] ~> ?", x);
      z += Casts.cast("pub ~> ?", y);
      if (z >= 0) {
         return 1;
      } else {
         return 0;
      }
   }

   private static int castTopToDyn(int x) {
      return x;
   }

   private static int castPubToDyn(int x) {
      return x;
   }

}
