package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Created by Nicolas Müller on 14.02.17.
 */
public class HighArgument {
    public static void main(String[] args) {
        int i = 42;
        i = DynamicLabel.makeHigh(i);
        callPrint(i);
    }

    static void callPrint(int arg) {
        System.out.println(arg);
    }
}
