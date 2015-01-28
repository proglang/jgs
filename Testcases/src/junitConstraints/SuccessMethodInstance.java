package junitConstraints;

import static security.Definition.*;

public class SuccessMethodInstance extends stubs.Methods {

    public static void main(String[] args) {
    }

    // Instance methods

    public void successMethod1() {
    }

    @Constraints({})
    public void successMethod2() {
    }

    public int successMethod3() {
        return lowIField;
    }

    @Constraints({ "low <= @return" })
    public int successMethod4() {
        return lowIField;
    }

    @Constraints({ "high <= @return" })
    public int successMethod5() {
        return lowIField;
    }

    @Constraints({ "high <= @return" })
    public int successMethod6() {
        return highIField;
    }

    public int successMethod7(int a1) {
        return lowIField;
    }

    @Constraints({ "@0 <= @return" })
    public int successMethod8(int a0) {
        return a0;
    }

    @Constraints({ "@0 <= @return", "@1 <= @return" })
    public int successMethod9(int a0, int a1) {
        return a0 + a1;
    }

    @Constraints({ "@0 <= @return", "high <= @return" })
    public int successMethod10(int a0) {
        return a0 + highIField;
    }

    @Constraints({ "@0 <= low", "low <= @return" })
    public int successMethod11(int a0) {
        return a0;
    }

    @Constraints({ "@0 <= high", "high <= @return" })
    public int successMethod12(int a0) {
        return a0;
    }

    @Constraints({ "@0 <= high", "@0 <= @return" })
    public int successMethod13(int a0) {
        return a0;
    }

    @Constraints({ "@0 <= low", "@0[ = low", "low <= @return", "low = @return[" })
    public int[] successMethod14(int[] a0) {
        return a0;
    }

    @Constraints({ "@0 <= low", "@0[ = high", "low <= @return",
                  "high = @return[" })
    public int[] successMethod15(int[] a0) {
        return a0;
    }

    @Constraints({ "@0 <= low", "@0[ = high", "high <= @return",
                  "high = @return[" })
    public int[] successMethod16(int[] a0) {
        return a0;
    }

    @Constraints({ "@0 <= high", "@0[ = high", "high <= @return",
                  "high = @return[" })
    public int[] successMethod17(int[] a0) {
        return a0;
    }

    @Constraints("low <= @return")
    public int successMethod18() {
        return _lowI();
    }

    @Constraints("high <= @return")
    public int successMethod19() {
        return _lowI();
    }

    @Constraints("high <= @return")
    public int successMethod20() {
        return _highI();
    }

    @Constraints({ "low <= @return", "@return[ = low" })
    public int[] successMethod21() {
        return _lowLowI();
    }

    @Constraints({ "low <= @return", "@return[ = high" })
    public int[] successMethod22() {
        return _lowHighI();
    }

    @Constraints({ "high <= @return", "@return[ = high" })
    public int[] successMethod23() {
        return _lowHighI();
    }

    @Constraints({ "high <= @return", "@return[ = high" })
    public int[] successMethod24() {
        return _highHighI();
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = low" })
    public int[][] successMethod25() {
        return _lowLowLowI();
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = high" })
    public int[][] successMethod26() {
        return _lowLowHighI();
    }

    @Constraints({ "low <= @return", "@return[ = high", "@return[[ = high" })
    public int[][] successMethod27() {
        return _lowHighHighI();
    }

    @Constraints({ "high <= @return", "@return[ = high", "@return[[ = high" })
    public int[][] successMethod28() {
        return _lowHighHighI();
    }

    @Constraints({ "high <= @return", "@return[ = high", "@return[[ = high" })
    public int[][] successMethod29() {
        return _highHighHighI();
    }

    public void successMethod30() {
        low_I(lowIField);
    }

    public void successMethod31() {
        high_I(lowIField);
    }

    public void successMethod32() {
        high_I(highIField);
    }

    public void successMethod33() {
        lowLow_I(lowLowIField);
    }

    public void successMethod34() {
        lowHigh_I(lowHighIField);
    }

    public void successMethod35() {
        highHigh_I(lowHighIField);
    }

    public void successMethod36() {
        highHigh_I(highHighIField);
    }

    public void successMethod37() {
        lowLowLow_I(lowLowLowIField);
    }

    public void successMethod38() {
        lowLowHigh_I(lowLowHighIField);
    }

    public void successMethod39() {
        lowHighHigh_I(lowHighHighIField);
    }

    public void successMethod40() {
        highHighHigh_I(lowHighHighIField);
    }

    public void successMethod41() {
        highHighHigh_I(highHighHighIField);
    }

    public void successMethod42() {
        low$low_I(lowIField, lowIField);
    }

    public void successMethod43() {
        low$high_I(lowIField, lowIField);
    }

    public void successMethod44() {
        low$high_I(lowIField, highIField);
    }

    public void successMethod45() {
        high$low_I(lowIField, lowIField);
    }

    public void successMethod46() {
        high$low_I(highIField, lowIField);
    }

    public void successMethod47() {
        high$high_I(lowIField, highIField);
    }

    public void successMethod48() {
        high$high_I(highIField, highIField);
    }

    public void successMethod49() {
        high$high_I(highIField, lowIField);
    }

    public void successMethod50() {
        low$lowLow_I(lowIField, lowLowIField);
    }

    public void successMethod51() {
        lowLow$low_I(lowLowIField, lowIField);
    }

    public void successMethod52() {
        lowLow$lowLow_I(lowLowIField, lowLowIField);
    }

    public void successMethod53() {
        lowLow$lowHigh_I(lowLowIField, lowHighIField);
    }

    public void successMethod54() {
        lowHigh$lowHigh_I(lowHighIField, lowHighIField);
    }

    public void successMethod55() {
        lowHigh$highHigh_I(lowHighIField, lowHighIField);
    }

    public void successMethod56() {
        lowHigh$highHigh_I(lowHighIField, highHighIField);
    }

    public void successMethod57() {
        highHigh$lowHigh_I(lowHighIField, lowHighIField);
    }

    public void successMethod58() {
        highHigh$lowHigh_I(highHighIField, lowHighIField);
    }

    public void successMethod59() {
        highHigh$highHigh_I(lowHighIField, lowHighIField);
    }

    public void successMethod60() {
        highHigh$highHigh_I(highHighIField, lowHighIField);
    }

    public void successMethod61() {
        highHigh$highHigh_I(lowHighIField, highHighIField);
    }

    public void successMethod62() {
        highHigh$highHigh_I(highHighIField, highHighIField);
    }

    @Constraints("low <= @return")
    public int successMethod63() {
        return sl0_sl0I(lowIField);
    }

    @Constraints("high <= @return")
    public int successMethod64() {
        return sl0_sl0I(highIField);
    }

    @Constraints("low <= @return")
    public int successMethod65() {
        return sl0$sl1_sl0Xsl1I(lowIField, lowIField);
    }

    @Constraints("high <= @return")
    public int successMethod66() {
        return sl0$sl1_sl0Xsl1I(lowIField, lowIField);
    }

    @Constraints("high <= @return")
    public int successMethod67() {
        return sl0$sl1_sl0Xsl1I(lowIField, highIField);
    }

    @Constraints("high <= @return")
    public int successMethod68() {
        return sl0$sl1_sl0Xsl1I(highIField, lowIField);
    }

    @Constraints("high <= @return")
    public int successMethod69() {
        return sl0$sl1_sl0Xsl1I(highIField, highIField);
    }

    @SuppressWarnings("unused")
    @Constraints({ "@0 <= high", "high <= @return" })
    public int successMethod70(int a0) {

        int i = successMethod70(a0);
        return a0;
    }

}
