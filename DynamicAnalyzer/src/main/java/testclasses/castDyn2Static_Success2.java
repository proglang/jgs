package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import utils.analyzer.HelperClass;

/**
 * Created by Nicolas Müller on 03.03.17.
 */
public class castDyn2Static_Success2 {
    public static void main(String[] args) {
        int i = 3;
        int j = HelperClass.makeMedium(4);
        int h = HelperClass.makeHigh(5);

        i = Casts.cast("? ~> LOW", i);
        j = Casts.cast("? ~> MEDIUM", j);
        h = Casts.cast("? ~> HIGH", h);

        int z = i + j + h;

    }

}
