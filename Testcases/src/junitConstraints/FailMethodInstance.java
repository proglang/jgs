package junitConstraints;

import static security.Definition.*;

public class FailMethodInstance extends stubs.Methods {

    public static void main(String[] args) {
    }

    // Instance methods

    @Constraints("low <= @return")
    public int failMethod1() {
        // @security("Return high value - expected was low")
        return _highI();
    }

    @Constraints({ "low <= @return", "@return[ = low" })
    public int[] failMethod2() {
        // @security("Return high value - expected was low")
        return _lowHighI();
    }

    @Constraints({ "low <= @return", "@return[ = low" })
    public int[] failMethod3() {
        // @security("Return high value - expected was low")
        // @security("Flow from high to low")
        return _highHighI();
    }

    @Constraints({ "low <= @return", "@return[ = high" })
    public int[] failMethod4() {
        // @security("Return high value - expected was low")
        return _highHighI();
    }

    @Constraints({ "high <= @return", "@return[ = low" })
    public int[] failMethod5() {
        // @security("Return high value - expected was low")
        return _highHighI();
    }

    @Constraints({ "low <= @return", "@return[ = high", "@return[[ = high" })
    public int[][] failMethod6() {
        // @security("Return high value - expected was low")
        // @security("Flow from high to low")
        return _lowLowLowI();
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = high" })
    public int[][] failMethod7() {
        // @security("Return high value - expected was low")
        // @security("Flow from high to low")
        return _lowLowLowI();
    }

    @Constraints({ "low <= @return", "@return[ = high", "@return[[ = low" })
    public int[][] failMethod8() {
        // @security("Return high value - expected was low")
        // @security("Flow from high to low")
        return _lowLowLowI();
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = low" })
    public int[][] failMethod9() {
        // @security("Return high value - expected was low")
        return _lowLowHighI();
    }

    @Constraints({ "low <= @return", "@return[ = high", "@return[[ = high" })
    public int[][] failMethod10() {
        // @security("Return high value - expected was low")
        // @security("Flow from high to low")
        return _lowLowHighI();
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = low" })
    public int[][] failMethod11() {
        // @security("Return high value - expected was low")
        return _lowHighHighI();
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = high" })
    public int[][] failMethod12() {
        // @security("Return high value - expected was low")
        return _lowHighHighI();
    }

    @Constraints({ "low <= @return", "@return[ = high", "@return[[ = low" })
    public int[][] failMethod13() {
        // @security("Return high value - expected was low")
        return _lowHighHighI();
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = low" })
    public int[][] failMethod14() {
        // @security("Return high value - expected was low")
        // @security("Flow from high to low")
        return _highHighHighI();
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = high" })
    public int[][] failMethod15() {
        // @security("Return high value - expected was low")
        // @security("Flow from high to low")
        return _highHighHighI();
    }

    @Constraints({ "low <= @return", "@return[ = high", "@return[[ = high" })
    public int[][] failMethod16() {
        // @security("Return high value - expected was low")
        return _highHighHighI();
    }

    @Constraints({ "low <= @return", "@return[ = high", "@return[[ = low" })
    public int[][] failMethod17() {
        // @security("Return high value - expected was low")
        // @security("Flow from high to low")
        return _highHighHighI();
    }

    @Constraints({ "high <= @return", "@return[ = high", "@return[[ = low" })
    public int[][] failMethod18() {
        // @security("Return high value - expected was low")
        return _highHighHighI();
    }

    @Constraints({ "high <= @return", "@return[ = low", "@return[[ = high" })
    public int[][] failMethod19() {
        // @security("Return high value - expected was low")
        return _highHighHighI();
    }

    @Constraints({ "high <= @return", "@return[ = low", "@return[[ = low" })
    public int[][] failMethod20() {
        // @security("Return high value - expected was low")
        return _highHighHighI();
    }

    public void failMethod21() {
        // @security("Parameter stronger than allowed")
        low_I(highIField);
    }

    public void failMethod22() {
        // @security("Parameter stronger than allowed")
        lowLow_I(lowHighIField);
    }

    public void failMethod23() {
        // @security("Parameter stronger than allowed")
        lowLow_I(highHighIField);
    }

    public void failMethod24() {
        // @security("Parameter stronger than allowed")
        lowHigh_I(highHighIField);
    }

    public void failMethod25() {
        // @security("Parameter stronger than allowed")
        lowLowLow_I(lowLowHighIField);
    }

    public void failMethod26() {
        // @security("Parameter stronger than allowed")
        lowLowLow_I(lowHighHighIField);
    }

    public void failMethod27() {
        // @security("Parameter stronger than allowed")
        lowLowLow_I(highHighHighIField);
    }

    public void failMethod28() {
        // @security("Parameter stronger than allowed")
        lowLowHigh_I(lowHighHighIField);
    }

    public void failMethod29() {
        // @security("Parameter stronger than allowed")
        lowLowHigh_I(highHighHighIField);
    }

    public void failMethod30() {
        // @security("Parameter stronger than allowed")
        lowHighHigh_I(highHighHighIField);
    }

    public void failMethod31() {
        // @security("Parameter stronger than allowed")
        low$low_I(highIField, highIField);
    }

    public void failMethod32() {
        // @security("Parameter stronger than allowed")
        low$low_I(lowIField, highIField);
    }

    public void failMethod33() {
        // @security("Parameter stronger than allowed")
        low$low_I(highIField, lowIField);
    }

    public void failMethod34() {
        // @security("Parameter stronger than allowed")
        low$lowLow_I(lowIField, lowHighIField);
    }

    public void failMethod35() {
        // @security("Parameter stronger than allowed")
        low$lowLow_I(lowIField, highHighIField);
    }

    public void failMethod36() {
        // @security("Parameter stronger than allowed")
        low$lowLow_I(highIField, lowLowIField);
    }

    public void failMethod37() {
        // @security("Parameter stronger than allowed")
        low$lowLow_I(highIField, lowHighIField);
    }

    public void failMethod38() {
        // @security("Parameter stronger than allowed")
        low$lowLow_I(highIField, highHighIField);
    }

    public void failMethod39() {
        // @security("Parameter stronger than allowed")
        lowLow$lowLow_I(lowLowIField, lowHighIField);
    }

    public void failMethod40() {
        // @security("Parameter stronger than allowed")
        lowLow$lowLow_I(lowLowIField, highHighIField);
    }

    public void failMethod41() {
        // @security("Parameter stronger than allowed")
        lowLow$lowLow_I(lowHighIField, lowLowIField);
    }

    public void failMethod42() {
        // @security("Parameter stronger than allowed")
        lowLow$lowLow_I(lowHighIField, lowHighIField);
    }

    public void failMethod43() {
        // @security("Parameter stronger than allowed")
        lowLow$lowLow_I(lowHighIField, highHighIField);
    }

    public void failMethod44() {
        // @security("Parameter stronger than allowed")
        lowLow$lowLow_I(highHighIField, lowLowIField);
    }

    public void failMethod45() {
        // @security("Parameter stronger than allowed")
        lowLow$lowLow_I(highHighIField, lowHighIField);
    }

    public void failMethod46() {
        // @security("Parameter stronger than allowed")
        lowLow$lowLow_I(highHighIField, highHighIField);
    }

    public void failMethod47() {
        // @security("Parameter stronger than allowed")
        lowLow$lowHigh_I(lowLowIField, lowLowIField);
    }

    public void failMethod48() {
        // @security("Parameter stronger than allowed")
        lowLow$lowHigh_I(lowLowIField, highHighIField);
    }

    public void failMethod49() {
        // @security("Parameter stronger than allowed")
        lowLow$lowHigh_I(lowHighIField, highHighIField);
    }

    public void failMethod50() {
        // @security("Parameter stronger than allowed")
        lowLow$lowHigh_I(highHighIField, highHighIField);
    }

    public void failMethod51() {
        // @security("Parameter stronger than allowed")
        lowLow$lowHigh_I(lowHighIField, lowLowIField);
    }

    public void failMethod52() {
        // @security("Parameter stronger than allowed")
        lowLow$lowHigh_I(highHighIField, lowLowIField);
    }

    public void failMethod53() {
        // @security("Parameter stronger than allowed")
        lowLow$lowHigh_I(lowHighIField, lowHighIField);
    }

    public void failMethod54() {
        // @security("Parameter stronger than allowed")
        lowLow$lowHigh_I(highHighIField, lowHighIField);
    }

    public void failMethod55() {
        // @security("Parameter stronger than allowed")
        lowHigh$lowLow_I(lowLowIField, lowLowIField);
    }

    public void failMethod56() {
        // @security("Parameter stronger than allowed")
        lowHigh$lowLow_I(highHighIField, lowLowIField);
    }

    public void failMethod57() {
        // @security("Parameter stronger than allowed")
        lowHigh$lowLow_I(lowLowIField, lowHighIField);
    }

    public void failMethod58() {
        // @security("Parameter stronger than allowed")
        lowHigh$lowLow_I(lowHighIField, lowHighIField);
    }

    public void failMethod59() {
        // @security("Parameter stronger than allowed")
        lowHigh$lowLow_I(highHighIField, lowHighIField);
    }

    public void failMethod60() {
        // @security("Parameter stronger than allowed")
        lowHigh$lowLow_I(lowLowIField, highHighIField);
    }

    public void failMethod61() {
        // @security("Parameter stronger than allowed")
        lowHigh$lowLow_I(lowHighIField, highHighIField);
    }

    public void failMethod62() {
        // @security("Parameter stronger than allowed")
        lowHigh$lowLow_I(highHighIField, highHighIField);
    }

    public void failMethod63() {
        // @security("Parameter stronger than allowed")
        lowHigh$lowHigh_I(lowLowIField, lowLowIField);
    }

    public void failMethod64() {
        // @security("Parameter stronger than allowed")
        lowHigh$lowHigh_I(lowLowIField, lowHighIField);
    }

    public void failMethod65() {
        // @security("Parameter stronger than allowed")
        lowHigh$lowHigh_I(lowLowIField, highHighIField);
    }

    public void failMethod66() {
        // @security("Parameter stronger than allowed")
        lowHigh$lowHigh_I(lowHighIField, lowLowIField);
    }

    public void failMethod67() {
        // @security("Parameter stronger than allowed")
        lowHigh$lowHigh_I(lowHighIField, highHighIField);
    }

    public void failMethod68() {
        // @security("Parameter stronger than allowed")
        lowHigh$lowHigh_I(highHighIField, highHighIField);
    }

    public void failMethod69() {
        // @security("Parameter stronger than allowed")
        lowHigh$highHigh_I(lowLowIField, lowLowIField);
    }

    public void failMethod70() {
        // @security("Parameter stronger than allowed")
        lowHigh$highHigh_I(lowLowIField, lowHighIField);
    }

    public void failMethod71() {
        // @security("Parameter stronger than allowed")
        lowHigh$highHigh_I(lowLowIField, highHighIField);
    }

    public void failMethod72() {
        // @security("Parameter stronger than allowed")
        lowHigh$highHigh_I(lowHighIField, lowLowIField);
    }

    public void failMethod73() {
        // @security("Parameter stronger than allowed")
        lowHigh$highHigh_I(highHighIField, lowLowIField);
    }

    public void failMethod74() {
        // @security("Parameter stronger than allowed")
        lowHigh$highHigh_I(highHighIField, lowHighIField);
    }

    public void failMethod75() {
        // @security("Parameter stronger than allowed")
        lowHigh$highHigh_I(highHighIField, highHighIField);
    }

    public void failMethod76() {
        // @security("Parameter stronger than allowed")
        highHigh$lowHigh_I(lowLowIField, lowLowIField);
    }

    public void failMethod77() {
        // @security("Parameter stronger than allowed")
        highHigh$lowHigh_I(lowHighIField, lowLowIField);
    }

    public void failMethod78() {
        // @security("Parameter stronger than allowed")
        highHigh$lowHigh_I(highHighIField, lowLowIField);
    }

    public void failMethod79() {
        // @security("Parameter stronger than allowed")
        highHigh$lowHigh_I(lowLowIField, lowHighIField);
    }

    public void failMethod80() {
        // @security("Parameter stronger than allowed")
        highHigh$lowHigh_I(lowLowIField, highHighIField);
    }

    public void failMethod81() {
        // @security("Parameter stronger than allowed")
        highHigh$lowHigh_I(lowHighIField, highHighIField);
    }

    public void failMethod82() {
        // @security("Parameter stronger than allowed")
        highHigh$lowHigh_I(highHighIField, highHighIField);
    }

    public void failMethod83() {
        // @security("Parameter stronger than allowed")
        highHigh$highHigh_I(lowLowIField, lowLowIField);
    }

    public void failMethod84() {
        // @security("Parameter stronger than allowed")
        highHigh$highHigh_I(lowLowIField, lowHighIField);
    }

    public void failMethod85() {
        // @security("Parameter stronger than allowed")
        highHigh$highHigh_I(lowHighIField, lowLowIField);
    }

    @Constraints("low <= @return")
    public int failMethod86() {
        // @security("Invalid lower bound of return")
        return sl0_sl0I(highIField);
    }

    @Constraints("low <= @return")
    public int failMethod87() {
        // @security("Invalid lower bound of return")
        return sl0$sl1_sl0Xsl1I(highSField, lowIField);
    }

    @Constraints("low <= @return")
    public int failMethod88() {
        // @security("Invalid lower bound of return")
        return sl0$sl1_sl0Xsl1I(lowIField, highSField);
    }

    @Constraints("low <= @return")
    public int failMethod89() {
        // @security("Invalid lower bound of return")
        return sl0$sl1_sl0Xsl1I(highSField, highSField);
    }

    @Constraints({ "@0 <= high", "low <= @return" })
    public int failMethod90(int a0) {
        // @security("return depends on argument")
        return a0;
    }

    @Constraints({ "@0 <= low", "@0[ = low", "low <= @return",
                  "@return[ = high" })
    public int[] failMethod91(int[] a0) {
        // @security("Invalid array reference")
        return a0;
    }

    @Constraints({ "@0 <= low", "@0[ = high", "low <= @return",
                  "@return[ = low" })
    public int[] failMethod92(int[] a0) {
        // @security("Invalid array reference")
        return a0;
    }

    @Constraints({ "@0 <= high", "@0[ = low", "low <= @return",
                  "@return[ = low" })
    public int[] failMethod93(int[] a0) {
        // @security("return depends on argument")
        return a0;
    }

    @SuppressWarnings("unused")
    @Constraints({ "@0 <= high", "low <= @return" })
    public int failMethod94(int a0) {
        int i = failMethod94(a0);
        // @security("return depends on argument")
        return a0;
    }

}
