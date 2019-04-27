package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.Sec;

public class CtxCastSuccess1 {

    @Sec("HIGH")
    static int highField = 42;

    @Constraints("LOW <= @0")
    @Effects({"?","HIGH"})
    public static void main(String[] args){

        // simple context cast : dynamic to static
        Casts.castCx("? ~> HIGH");
        highField = 5;
        Casts.castCxEnd();
    }

}
