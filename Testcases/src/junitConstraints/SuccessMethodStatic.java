package junitConstraints;

import security.Definition.Constraints;

public class SuccessMethodStatic extends stubs.Methods {

    public static void main(String[] args) {
    }

    // Static methods

    public static void successMethod1() {
    }

    @Constraints({})
    public static void successMethod2() {
    }

    public static int successMethod3() {
        return lowSField;
    }

    @Constraints({ "low <= @return" })
    public static int successMethod4() {
        return lowSField;
    }

    @Constraints({ "high <= @return" })
    public static int successMethod5() {
        return lowSField;
    }

    @Constraints({ "high <= @return" })
    public static int successMethod6() {
        return highSField;
    }

    public static int successMethod7(int a1) {
        return lowSField;
    }

    @Constraints({ "@0 <= @return" })
    public static int successMethod8(int a0) {
        return a0;
    }

    @Constraints({ "@0 <= @return", "@1 <= @return" })
    public static int successMethod9(int a0, int a1) {
        return a0 + a1;
    }

    @Constraints({ "@0 <= @return", "high <= @return" })
    public static int successMethod10(int a0) {
        return a0 + highSField;
    }

    @Constraints({ "@0 <= low", "low <= @return" })
    public static int successMethod11(int a0) {
        return a0;
    }

    @Constraints({ "@0 <= high", "high <= @return" })
    public static int successMethod12(int a0) {
        return a0;
    }

    @Constraints({ "@0 <= high", "@0 <= @return" })
    public static int successMethod13(int a0) {
        return a0;
    }

    @Constraints({ "@0 <= low", "@0[ = low", "low <= @return", "low = @return[" })
    public static int[] successMethod14(int[] a0) {
        return a0;
    }

    @Constraints({ "@0 <= low", "@0[ = high", "low <= @return",
                  "high = @return[" })
    public static int[] successMethod15(int[] a0) {
        return a0;
    }

    @Constraints({ "@0 <= low", "@0[ = high", "high <= @return",
                  "high = @return[" })
    public static int[] successMethod16(int[] a0) {
        return a0;
    }

    @Constraints({ "@0 <= high", "@0[ = high", "high <= @return",
                  "high = @return[" })
    public static int[] successMethod17(int[] a0) {
        return a0;
    }

    @Constraints("low <= @return")
    public static int successMethod18() {
        return _lowS();
    }

    @Constraints("high <= @return")
    public static int successMethod19() {
        return _lowS();
    }

    @Constraints("high <= @return")
    public static int successMethod20() {
        return _highS();
    }

    @Constraints({ "low <= @return", "@return[ = low" })
    public static int[] successMethod21() {
        return _lowLowS();
    }

    @Constraints({ "low <= @return", "@return[ = high" })
    public static int[] successMethod22() {
        return _lowHighS();
    }

    @Constraints({ "high <= @return", "@return[ = high" })
    public static int[] successMethod23() {
        return _lowHighS();
    }

    @Constraints({ "high <= @return", "@return[ = high" })
    public static int[] successMethod24() {
        return _highHighS();
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = low" })
    public static int[][] successMethod25() {
        return _lowLowLowS();
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = high" })
    public static int[][] successMethod26() {
        return _lowLowHighS();
    }

    @Constraints({ "low <= @return", "@return[ = high", "@return[[ = high" })
    public static int[][] successMethod27() {
        return _lowHighHighS();
    }

    @Constraints({ "high <= @return", "@return[ = high", "@return[[ = high" })
    public static int[][] successMethod28() {
        return _lowHighHighS();
    }

    @Constraints({ "high <= @return", "@return[ = high", "@return[[ = high" })
    public static int[][] successMethod29() {
        return _highHighHighS();
    }

    public static void successMethod30() {
        low_S(lowSField);
    }

    public static void successMethod31() {
        high_S(lowSField);
    }

    public static void successMethod32() {
        high_S(highSField);
    }

    public static void successMethod33() {
        lowLow_S(lowLowSField);
    }

    public static void successMethod34() {
        lowHigh_S(lowHighSField);
    }

    public static void successMethod35() {
        highHigh_S(lowHighSField);
    }

    public static void successMethod36() {
        highHigh_S(highHighSField);
    }

    public static void successMethod37() {
        lowLowLow_S(lowLowLowSField);
    }

    public static void successMethod38() {
        lowLowHigh_S(lowLowHighSField);
    }

    public static void successMethod39() {
        lowHighHigh_S(lowHighHighSField);
    }

    public static void successMethod40() {
        highHighHigh_S(lowHighHighSField);
    }

    public static void successMethod41() {
        highHighHigh_S(highHighHighSField);
    }

    public static void successMethod42() {
        low$low_S(lowSField, lowSField);
    }

    public static void successMethod43() {
        low$high_S(lowSField, lowSField);
    }

    public static void successMethod44() {
        low$high_S(lowSField, highSField);
    }

    public static void successMethod45() {
        high$low_S(lowSField, lowSField);
    }

    public static void successMethod46() {
        high$low_S(highSField, lowSField);
    }

    public static void successMethod47() {
        high$high_S(lowSField, highSField);
    }

    public static void successMethod48() {
        high$high_S(highSField, highSField);
    }

    public static void successMethod49() {
        high$high_S(highSField, lowSField);
    }

    public static void successMethod50() {
        low$lowLow_S(lowSField, lowLowSField);
    }

    public static void successMethod51() {
        lowLow$low_S(lowLowSField, lowSField);
    }

    public static void successMethod52() {
        lowLow$lowLow_S(lowLowSField, lowLowSField);
    }

    public static void successMethod53() {
        lowLow$lowHigh_S(lowLowSField, lowHighSField);
    }

    public static void successMethod54() {
        lowHigh$lowHigh_S(lowHighSField, lowHighSField);
    }

    public static void successMethod55() {
        lowHigh$highHigh_S(lowHighSField, lowHighSField);
    }

    public static void successMethod56() {
        lowHigh$highHigh_S(lowHighSField, highHighSField);
    }

    public static void successMethod57() {
        highHigh$lowHigh_S(lowHighSField, lowHighSField);
    }

    public static void successMethod58() {
        highHigh$lowHigh_S(highHighSField, lowHighSField);
    }

    public static void successMethod59() {
        highHigh$highHigh_S(lowHighSField, lowHighSField);
    }

    public static void successMethod60() {
        highHigh$highHigh_S(highHighSField, lowHighSField);
    }

    public static void successMethod61() {
        highHigh$highHigh_S(lowHighSField, highHighSField);
    }

    public static void successMethod62() {
        highHigh$highHigh_S(highHighSField, highHighSField);
    }

    @Constraints("low <= @return")
    public static int successMethod63() {
        return sl0_sl0S(lowSField);
    }

    @Constraints("high <= @return")
    public static int successMethod64() {
        return sl0_sl0S(highSField);
    }

    @Constraints("low <= @return")
    public static int successMethod65() {
        return sl0$sl1_sl0Xsl1S(lowSField, lowSField);
    }

    @Constraints("high <= @return")
    public static int successMethod66() {
        return sl0$sl1_sl0Xsl1S(lowSField, lowSField);
    }

    @Constraints("high <= @return")
    public static int successMethod67() {
        return sl0$sl1_sl0Xsl1S(lowSField, highSField);
    }

    @Constraints("high <= @return")
    public static int successMethod68() {
        return sl0$sl1_sl0Xsl1S(highSField, lowSField);
    }

    @Constraints("high <= @return")
    public static int successMethod69() {
        return sl0$sl1_sl0Xsl1S(highSField, highSField);
    }

    @SuppressWarnings("unused")
    @Constraints({ "@0 <= high", "high <= @return" })
    public static int successMethod70(int a0) {
        int i = successMethod70(a0);
        return a0;
    }

}
