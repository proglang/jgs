package demo;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.Sec;

/**
 * Show of an NSU failure
 */
public class Demo4 {
    @Sec("HIGH")
    static int secret = 42;


    @Sec("?")
    static String dynField = "";

    @Constraints("@0 ~ LOW")
    @Effects({"LOW", "?"})
    public static void main(String[] args) {
        // m();
        dynField = "Don't know the secret";
        int dynSecret = Casts.cast("HIGH ~> ?", secret);
        if (dynSecret == 42) {
            dynField = "I guess it's 42";
        }
    }


}

/* dynSecret has security level H, making level of dynField H too. But the method should produce LOW Effect and hence fails */