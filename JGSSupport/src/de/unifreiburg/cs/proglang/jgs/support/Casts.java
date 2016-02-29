package de.unifreiburg.cs.proglang.jgs.support;

/**
 * Created by fennell on 2/24/16.
 */
public class Casts {

    public static <T> T castHighToDyn(T x) {
        return x;
    }

    public static <T> T castLowToDyn(T x) {
        return x;
    }

    public static void castCxHighToDyn() {}
    public static void castCxLowToDyn() {}
    public static void castCxEnd(){}

}
