package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;

import static de.unifreiburg.cs.proglang.jgs.support.DynamicLabel.makeHigh;

public class SimpleCast_Fail {

    @Constraints({"LOW <= @0"})
    @Effects("LOW")
    public static void main(String[] args) {
        int v = 42;
        int x = makeHigh(v);
        System.out.println(Casts.cast("? ~> LOW", x));
    }
}

/* x has level H due to makeHigh(). Hence casting it to low fails */
/* if v is assigned to x without makeHigh, then there is no error */