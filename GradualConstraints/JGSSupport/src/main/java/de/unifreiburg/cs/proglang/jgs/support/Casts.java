package de.unifreiburg.cs.proglang.jgs.support;

public class Casts {

    /**
     * The generic value cast. Pass the conversion as a String constant to the first argument.
     *
     * Only string constants will work.
     * The conversion syntax is: <type> ~> <type>
     *     where <type> is a type repr recognized by the security domain
     */
    public static <T> T cast(String conversion, T x){
        return x;
    }

    /**
     * The generic context casts.
     */
    public static <T> T cxCast(String conversion, T x){
        return x;
    }

    /**
     * The end of a context cast
     */
    public static void castCxEnd(){}

    /**
     * Casts whose conversion is manually specified in an external json file (option --cast-methods of JGSCheck).
     *
     * They have to be defined and listed in the --cast-methods file for every domain.
     * (As this is a lot of work, it is recommended to use the generic casts above)
     */
    public static <T> T castHighToDyn(T x) {
        return x;
    }

    public static int castHighToDyn(int x) {
        return x;
    }

    public static <T> T castLowToDyn(T x) {
        return x;
    }

    public static int castLowToDyn(int x) {
        return x;
    }

    public static <T> T castDynToLow(T x) {
        return x;
    }

    public static int castDynToLow(int x) {
        return x;
    }

    public static <T> T castDynToHigh(T x) {
        return x;
    }

    public static int castDynToHigh(int x) {
        return x;
    }

    public static <T> T castDynToPub(T x) {
        return x;
    }

    public static int castDynToPub(int x) {
        return x;
    }

    public static <T> T castLowToPub(T x) {
        return x;
    }

    public static int castLowToPub(int x) {
        return x;
    }

    public static void castCxHighToDyn() {}
    public static void castCxLowToDyn() {}
    public static void castCxLowToPub() {}
    public static void castCxDynToLow() {}
    public static void castCxDynToHigh() {}
    public static void castCxDynToPub() {}

}
