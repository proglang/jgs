package demo;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.IOUtils;

/**
 * Simple illustration of how dynamic IFC can be applied.
 */
public class Demo2 {

    @Constraints("LOW ~ @0")
    @Effects("LOW")
    public static void main(String[] args) {
        boolean secretMode = false;
        printDyn(readDyn(secretMode), secretMode);
        secretMode = true;
        printDyn(readDyn(secretMode),
                 // secretMode // <-
                 false   // <- switch comments to get an RT error
        );
    }

    @Constraints({"@0 <= pub", "? <= @ret"})
    @Effects("LOW")
    private static String readDyn(boolean secretMode) {
        String result;
        if (secretMode) {
            result = Casts.cast("HIGH ~> ?", IOUtils.readSecret());
        } else {
            result = Casts.cast("LOW ~> ?", IOUtils.read());
        }
        return result;
    }

    @Constraints({"@0 ~ ?", "@1 <= pub"})
    @Effects({"LOW"})
    private static void printDyn(String value, boolean secretMode) {
        if (secretMode)  {
            IOUtils.printSecret(Casts.cast("? ~> HIGH", value));
        } else {
            System.out.println(Casts.cast("? ~> LOW", value));
        }
    }
}

/* If readDyn is called with false and printDyn is called with true, then it reads like 42 but prints like XX */
/* If both are called with false, then reads and prints like 42 */
/*--> Check with Lu : If both called with true, then reads like 42 but prints like XX */