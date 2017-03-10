package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;

/**
 * Created by Nicolas Müller on 04.03.17.
 */
public class castStatic2Dyn_Success1 {
    public static void main(String[] args) {
        String s = Casts.cast("LOW ~> ?", "I'm low security");
        System.out.println(s);
    }
}
