package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Created by Nicolas Müller on 07.02.17.
 */
public class NoNSU1 {
    public static void main(String[] args) {
        int x = 3;
        int y = DynamicLabel.makeHigh(7);
        int z = x + y;
        System.out.println(z);
    }
}
