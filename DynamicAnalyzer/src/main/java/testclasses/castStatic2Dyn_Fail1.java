package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;

/**
 * Created by Nicolas Müller on 04.03.17.
 */
public class castStatic2Dyn_Fail1 {
    public static void main(String[] args) {
        String k = "I'm high sec";
        String s = Casts.cast("HIGH ~> ?", k);
        System.out.println(s);
    }
}
