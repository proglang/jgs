package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import utils.analyzer.HelperClass;

/**
 * Created by Nicolas Müller on 02.03.17.
 */
public class castDyn2Static_Fail2 {
    public static void main(String[] args) {

    }






    /**
     *   TODO put in different test files
     */
    public static int m(int x, int y) {
        //Casts.cast("[top] ~> ?", x);
        //int z = Casts.cast("[top] ~> ?", x);
        //int z = Casts.cast("HIGH ~> ?", x);    // init
        // z += Casts.cast("pub ~> ?", y);        // init
        int z = HelperClass.makeHigh(x);
        z = Casts.cast("? ~> LOW", z);       // check that z is high
        //z = Casts.cast("? ~> HIGH", z);       // check that z is high
        if (z >= 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
