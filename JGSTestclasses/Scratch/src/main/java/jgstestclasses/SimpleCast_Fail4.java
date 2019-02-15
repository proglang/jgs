package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;

/**
 * Created by Nicolas Müller on 16.03.17.
 * Same as SimpleCast_Fail2, but using string instead of ints
 */
public class SimpleCast_Fail4 {
    @Constraints({"LOW <= @0"})
    @Effects("LOW")
    public static void main(String[] args) {
        String v = "42";
        String x = Casts.cast("HIGH ~> ?", v);
        System.out.println(Casts.cast("? ~> LOW", x));
    }
}

/* x has level H due to casting. Hence casting it to low fails */
/* if v is assigned to x without the cast then there is no error */