package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;

/**
 * Created by Nicolas Müller on 02.03.17.
 */
public class castDyn2Static_Fail2 {
    public static void main(String[] args) {

        String f = Casts.cast("HIGH ~> ?", "I'm cast to HIGH security!");
        f = Casts.cast("? ~> LOW", f);         // cast from Dyn(HIGH) ~> Static(LOW) is a security leak
    }
}
