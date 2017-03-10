package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import utils.analyzer.HelperClass;

/**
 * Created by Nicolas Müller on 03.03.17.
 * Cast from Dyn ~> Dyn are handled like assign stmts.
 */
public class castDyn2DynSuccess {
    public static void main (String[] args) {
        int i = 3;
        int j = 0;
        // the following cast must act like an assign stmt y = 3
        j = Casts.cast("? ~> ?", 3);
        if (i - j != 0) {
            String except = HelperClass.makeHigh("This is just a dirty way to cause an exception, because i cannot use" +
                    "assert stmts here (will cause soot ref error).");
            System.out.println(except);
        }
    }
}
