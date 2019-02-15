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

/* x has level H due to the cast, hence it cannot be cast to LOW */
/* If the cast in line 13 is removed, then it causes an error which can be fixed by printSecret() */