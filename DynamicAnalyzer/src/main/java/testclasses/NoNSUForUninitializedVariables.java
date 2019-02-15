package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;
import de.unifreiburg.cs.proglang.jgs.support.IOUtils;

/**
 * In a HIGH context, the NSU check has to distinguish whether a variable is
 * *initialized* or *set* (has been initialized before).
 *
 * Created by fennell on 21.09.17.
 */
public class NoNSUForUninitializedVariables {

    public static void main(String... args) {
        int i;
        boolean secret = DynamicLabel.makeHigh(true);
        if (secret) {
            i = 1; // NO NSU FAILURE
        } else {
            i = 0; // NO NSU FAILURE
        }
        IOUtils.printSecret(String.valueOf(i));
    }

}

/* FAILS with NSU Error*/