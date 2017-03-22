package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;

public class SimpleCast_Fail3 {

    @Constraints({"LOW <= @0"})
    @Effects("LOW")
    public static void main(String[] args) {
        int x = Casts.cast("HIGH ~> ?", 42);
        System.out.println(Casts.cast("? ~> LOW", x));
    }
}
