package stubs;

import static security.Definition.*;

public class Methods extends ProtectedFields {

    @Constraints("low <= @return")
    public int _lowI() {
        return lowIField;
    }

    @Constraints({ "low <= @return", "@return[ = low" })
    public int[] _lowLowI() {
        return lowLowIField;
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = low" })
    public int[][] _lowLowLowI() {
        return lowLowLowIField;
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = high" })
    public int[][] _lowLowHighI() {
        return lowLowHighIField;
    }

    @Constraints({ "low <= @return", "@return[ = high" })
    public int[] _lowHighI() {
        return lowHighIField;
    }

    @Constraints({ "low <= @return", "@return[ = high", "@return[[ = high" })
    public int[][] _lowHighHighI() {
        return lowHighHighIField;
    }

    @Constraints("high <= @return")
    public int _highI() {
        return highIField;
    }

    @Constraints({ "high <= @return", "@return[ = high" })
    public int[] _highHighI() {
        return highHighIField;
    }

    @Constraints({ "high <= @return", "@return[ = high", "@return[[ = high" })
    public int[][] _highHighHighI() {
        return highHighHighIField;
    }

    @Constraints("@0 <= low")
    public void low_I(int a0) {
    }

    @Constraints({ "@0 <= low", "@0[ = low" })
    public void lowLow_I(int[] a0) {
    }

    @Constraints({ "@0 <= low", "@0[ = low", "@0[[ = low" })
    public void lowLowLow_I(int[][] a0) {
    }

    @Constraints({ "@0 <= low", "@0[ = low", "@0[[ = high" })
    public void lowLowHigh_I(int[][] a0) {
    }

    @Constraints({ "@0 <= low", "@0[ = high" })
    public void lowHigh_I(int[] a0) {
    }

    @Constraints({ "@0 <= low", "@0[ = high", "@0[[ = high" })
    public void lowHighHigh_I(int[][] a0) {
    }

    @Constraints("high <= @0")
    public void high_I(int a0) {
    }

    @Constraints({ "@0 <= high", "@0[ = high" })
    public void highHigh_I(int[] a0) {
    }

    @Constraints({ "@0 <= high", "@0[ = high", "@0[[ = high" })
    public void highHighHigh_I(int[][] a0) {
    }

    @Constraints({ "@0 <= low", "@1 <= low" })
    public void low$low_I(int a0, int a1) {
    }

    @Constraints({ "@0 <= low", "@1 <= high" })
    public void low$high_I(int a0, int a1) {
    }

    @Constraints({ "@0 <= high", "@1 <= high" })
    public void high$high_I(int a0, int a1) {
    }

    @Constraints({ "@0 <= high", "@1 <= low" })
    public void high$low_I(int a0, int a1) {
    }

    @Constraints({ "@0 <= low", "@1 <= low", "@1[ = low" })
    public void low$lowLow_I(int a0, int[] a1) {
    }

    @Constraints({ "@0 <= low", "@0[ = low", "@1 <= low" })
    public void lowLow$low_I(int[] a0, int a1) {
    }

    @Constraints({ "@0 <= low", "@0[ = low", "@1 <= low", "@1[ = low" })
    public void lowLow$lowLow_I(int[] a0, int[] a1) {
    }

    @Constraints({ "@0 <= low", "@0[ = low", "@1 <= low", "@1[ = high" })
    public void lowLow$lowHigh_I(int[] a0, int[] a1) {
    }

    @Constraints({ "@0 <= low", "@0[ = high", "@1 <= low", "@1[ = low" })
    public void lowHigh$lowLow_I(int[] a0, int[] a1) {
    }

    @Constraints({ "@0 <= low", "@0[ = high", "@1 <= low", "@1[ = high" })
    public void lowHigh$lowHigh_I(int[] a0, int[] a1) {
    }

    @Constraints({ "@0 <= low", "@0[ = high", "@1 <= high", "@1[ = high" })
    public void lowHigh$highHigh_I(int[] a0, int[] a1) {
    }

    @Constraints({ "@0 <= high", "@0[ = high", "@1 <= low", "@1[ = high" })
    public void highHigh$lowHigh_I(int[] a0, int[] a1) {
    }

    @Constraints({ "@0 <= high", "@0[ = high", "@1 <= high", "@1[ = high" })
    public void highHigh$highHigh_I(int[] a0, int[] a1) {
    }

    @Constraints({ "low <= @0", "low <= @return" })
    public int low_lowI(int a0) {
        return lowIField;
    }

    @Constraints({ "low <= @0", "high <= @return" })
    public int low_highI(int a0) {
        return highIField;
    }

    @Constraints({ "high <= @0", "high <= @return" })
    public int high_highI(int a0) {
        return highIField;
    }

    @Constraints("@0 <= @return")
    public int sl0_sl0I(int a0) {
        return a0;
    }

    @Constraints({ "@0 <= @return", "@1 <= @return" })
    public int sl0$sl1_sl0Xsl1I(int a0, int a1) {
        return a0 + a1;
    }

    @Constraints("low <= @return")
    public static int _lowS() {
        return lowSField;
    }

    @Constraints({ "low <= @return", "@return[ = low" })
    public static int[] _lowLowS() {
        return lowLowSField;
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = low" })
    public static int[][] _lowLowLowS() {
        return lowLowLowSField;
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = high" })
    public static int[][] _lowLowHighS() {
        return lowLowHighSField;
    }

    @Constraints({ "low <= @return", "@return[ = high" })
    public static int[] _lowHighS() {
        return lowHighSField;
    }

    @Constraints({ "low <= @return", "@return[ = high", "@return[[ = high" })
    public static int[][] _lowHighHighS() {
        return lowHighHighSField;
    }

    @Constraints("high <= @return")
    public static int _highS() {
        return highSField;
    }

    @Constraints({ "high <= @return", "@return[ = high" })
    public static int[] _highHighS() {
        return highHighSField;
    }

    @Constraints({ "high <= @return", "@return[ = high", "@return[[ = high" })
    public static int[][] _highHighHighS() {
        return highHighHighSField;
    }

    @Constraints("@0 <= low")
    public static void low_S(int a0) {
    }

    @Constraints({ "@0 <= low", "@0[ = low" })
    public static void lowLow_S(int[] a0) {
    }

    @Constraints({ "@0 <= low", "@0[ = low", "@0[[ = low" })
    public static void lowLowLow_S(int[][] a0) {
    }

    @Constraints({ "@0 <= low", "@0[ = low", "@0[[ = high" })
    public static void lowLowHigh_S(int[][] a0) {
    }

    @Constraints({ "@0 <= low", "@0[ = high" })
    public static void lowHigh_S(int[] a0) {
    }

    @Constraints({ "@0 <= low", "@0[ = high", "@0[[ = high" })
    public static void lowHighHigh_S(int[][] a0) {
    }

    @Constraints("high <= @0")
    public static void high_S(int a0) {
    }

    @Constraints({ "@0 <= high", "@0[ = high" })
    public static void highHigh_S(int[] a0) {
    }

    @Constraints({ "@0 <= high", "@0[ = high", "@0[[ = high" })
    public static void highHighHigh_S(int[][] a0) {
    }

    @Constraints({ "@0 <= low", "@1 <= low" })
    public static void low$low_S(int a0, int a1) {
    }

    @Constraints({ "@0 <= low", "@1 <= high" })
    public static void low$high_S(int a0, int a1) {
    }

    @Constraints({ "@0 <= high", "@1 <= high" })
    public static void high$high_S(int a0, int a1) {
    }

    @Constraints({ "@0 <= high", "@1 <= low" })
    public static void high$low_S(int a0, int a1) {
    }

    @Constraints({ "@0 <= low", "@1 <= low", "@1[ = low" })
    public static void low$lowLow_S(int a0, int[] a1) {
    }

    @Constraints({ "@0 <= low", "@0[ = low", "@1 <= low" })
    public static void lowLow$low_S(int[] a0, int a1) {
    }

    @Constraints({ "@0 <= low", "@0[ = low", "@1 <= low", "@1[ = low" })
    public static void lowLow$lowLow_S(int[] a0, int[] a1) {
    }

    @Constraints({ "@0 <= low", "@0[ = low", "@1 <= low", "@1[ = high" })
    public static void lowLow$lowHigh_S(int[] a0, int[] a1) {
    }

    @Constraints({ "@0 <= low", "@0[ = high", "@1 <= low", "@1[ = low" })
    public static void lowHigh$lowLow_S(int[] a0, int[] a1) {
    }

    @Constraints({ "@0 <= low", "@0[ = high", "@1 <= low", "@1[ = high" })
    public static void lowHigh$lowHigh_S(int[] a0, int[] a1) {
    }

    @Constraints({ "@0 <= low", "@0[ = high", "@1 <= high", "@1[ = high" })
    public static void lowHigh$highHigh_S(int[] a0, int[] a1) {
    }

    @Constraints({ "@0 <= high", "@0[ = high", "@1 <= low", "@1[ = high" })
    public static void highHigh$lowHigh_S(int[] a0, int[] a1) {
    }

    @Constraints({ "@0 <= high", "@0[ = high", "@1 <= high", "@1[ = high" })
    public static void highHigh$highHigh_S(int[] a0, int[] a1) {
    }

    @Constraints({ "low <= @0", "low <= @return" })
    public static int low_lowS(int a0) {
        return lowSField;
    }

    @Constraints({ "low <= @0", "high <= @return" })
    public static int low_highS(int a0) {
        return highSField;
    }

    @Constraints({ "high <= @0", "high <= @return" })
    public static int high_highS(int a0) {
        return highSField;
    }

    @Constraints("@0 <= @return")
    public static int sl0_sl0S(int a0) {
        return a0;
    }

    @Constraints({ "@0 <= @return", "@1 <= @return" })
    public static int sl0$sl1_sl0Xsl1S(int a0, int a1) {
        return a0 + a1;
    }

}
