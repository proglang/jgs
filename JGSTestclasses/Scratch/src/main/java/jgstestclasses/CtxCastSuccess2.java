package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.Sec;

public class CtxCastSuccess2 {

    @Sec("?")
    static int lowField;

    @Constraints("LOW <= @0")
    @Effects({"?","LOW"})
    public static void main(String[] args){

        // simple context cast : static to dynamic
        Casts.castCx("LOW ~> ?");
        lowField = 5;
        Casts.castCxEnd();
    }
}
