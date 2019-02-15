package jgstestclasses;

/**
 * Created by Nicolas Müller on 13.03.17.
 */

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.Sec;

public class ScratchMonomorphic_Success {

    @Constraints({"LOW <= @0"})
    public static void main(String[] args) {

        // Added by gaurr
        /* System.out.println(Casts.cast("LOW ~> ?", aStaticMethod(10)));
        System.out.println(Casts.cast("LOW ~> ?", methodWithImplicitFlows(true, false)));
        */ // -- Both cause error
    }

    int answer = 42;

    @Sec("HIGH")
    int high = 123;

    @Sec("HIGH")
    Object highObject = new Object();


    static int aStatic = 0;

    @Constraints({"LOW <= @0 ", "@0 <= @ret"})
    @Effects({})
    static int aStaticMethod(int i) {
        if (i == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Constraints({"pub <= @0", "? <= @1", "@0 <= @ret", "@1 <= @ret"})
    public static int methodWithImplicitFlows(boolean i, boolean j) {
        int z = 0;
        if (i) {
            z += 10;
        }
        if (j) {
            z += 1;
        }
        return z;
    }

}

/* Ask Lu - incomplete ? */