package junitConstraints;

import static security.Definition.*;

public class SuccessFieldStatic extends stubs.Fields {

    public static void main(String[] args) {
    }

    // Static fields

    @Constraints("@pc <= low")
    public void successField1() {
        lowSField = mkLow(42);
    }

    @Constraints("@pc <= high")
    public void successField2() {
        highSField = mkLow(42);
    }

    @Constraints("@pc <= high")
    public void successField3() {
        highSField = mkHigh(42);
    }

    public void successField4() {
        highSField = mkLow(42);
    }

    public void successField5() {
        highSField = mkHigh(42);
    }

    @Constraints("low <= @return")
    public int successField6() {
        return lowSField;
    }

    @Constraints("high <= @return")
    public int successField7() {
        return lowSField;
    }

    @Constraints("high <= @return")
    public int successField8() {
        return highSField;
    }

    @Constraints({ "low <= @return", "low = @return[" })
    public int[] successField9() {
        return lowLowSField;
    }

    @Constraints({ "low <= @return", "high = @return[" })
    public int[] successField10() {
        return lowHighSField;
    }

    @Constraints({ "high <= @return", "high = @return[" })
    public int[] successField11() {
        return lowHighSField;
    }

    @Constraints({ "high <= @return", "high = @return[" })
    public int[] successField12() {
        return highHighSField;
    }

    @Constraints({ "low <= @return", "low = @return[", "low = @return[[" })
    public int[][] successField13() {
        return lowLowLowSField;
    }

    @Constraints({ "low <= @return", "low = @return[", "high = @return[[" })
    public int[][] successField14() {
        return lowLowHighSField;
    }

    @Constraints({ "low <= @return", "high = @return[", "high = @return[[" })
    public int[][] successField15() {
        return lowHighHighSField;
    }

    @Constraints({ "high <= @return", "high = @return[", "high = @return[[" })
    public int[][] successField16() {
        return lowHighHighSField;
    }

    @Constraints({ "high <= @return", "high = @return[", "high = @return[[" })
    public int[][] successField17() {
        return highHighHighSField;
    }

    @Constraints("@pc <= low")
    public void successField18() {
        lowLowSField = arrayIntLow(42);
    }

    @Constraints("@pc <= low")
    public void successField19() {
        lowLowSField[23] = 42;
    }

    @Constraints("@pc <= low")
    public void successField20() {
        lowHighSField = arrayIntHigh(1);
    }

    @Constraints("@pc <= low")
    public void successField21() {
        lowHighSField[23] = 42;
    }

    @Constraints("@pc <= low")
    public void successField22() {
        lowHighSField[23] = mkHigh(42);
    }

    @Constraints("@pc <= high")
    public void successField23() {
        highHighSField = arrayIntHigh(mkHigh(42));
    }

    @Constraints("@pc <= high")
    public void successField24() {
        highHighSField = arrayIntHigh(42);
    }

    @Constraints("@pc <= high")
    public void successField25() {
        highHighSField[23] = mkHigh(42);
    }

    @Constraints("@pc <= high")
    public void successField26() {
        highHighSField[23] = 42;
    }

    @Constraints("@pc <= low")
    public void successField27() {
        lowLowLowSField = arrayIntLowLow(42, 7);
    }

    @Constraints("@pc <= low")
    public void successField28() {
        lowLowLowSField[23] = arrayIntLow(42);
    }

    @Constraints("@pc <= low")
    public void successField29() {
        lowLowLowSField[23][7] = 42;
    }

    @Constraints("@pc <= low")
    public void successField30() {
        lowLowHighSField = arrayIntLowHigh(42, 7);
    }

    @Constraints("@pc <= low")
    public void successField31() {
        lowLowHighSField[23] = arrayIntHigh(42);
    }

    @Constraints("@pc <= low")
    public void successField32() {
        lowLowHighSField[23][7] = mkHigh(42);
    }

    @Constraints("@pc <= low")
    public void successField33() {
        lowLowHighSField[23][7] = mkLow(42);
    }

    @Constraints("@pc <= low")
    public void successField34() {
        lowHighHighSField = arrayIntHighHigh(42, 7);
    }

    @Constraints("@pc <= low")
    public void successField35() {
        lowHighHighSField = arrayIntHighHigh(42, mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void successField36() {
        lowHighHighSField[23] = arrayIntHigh(42);
    }

    @Constraints("@pc <= low")
    public void successField37() {
        lowHighHighSField[23] = arrayIntHigh(mkHigh(42));
    }

    @Constraints("@pc <= low")
    public void successField38() {
        lowHighHighSField[23][7] = mkHigh(42);
    }

    @Constraints("@pc <= low")
    public void successField39() {
        lowHighHighSField[23][7] = 42;
    }

    @Constraints("@pc <= high")
    public void successField40() {
        highHighHighSField = arrayIntHighHigh(42, 7);
    }

    @Constraints("@pc <= high")
    public void successField41() {
        highHighHighSField = arrayIntHighHigh(42, mkHigh(7));
    }

    @Constraints("@pc <= high")
    public void successField42() {
        highHighHighSField = arrayIntHighHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints("@pc <= high")
    public void successField43() {
        highHighHighSField = arrayIntHighHigh(mkHigh(42), 7);
    }

    @Constraints("@pc <= high")
    public void successField44() {
        highHighHighSField[23] = arrayIntHigh(42);
    }

    @Constraints("@pc <= high")
    public void successField45() {
        highHighHighSField[23] = arrayIntHigh(mkHigh(42));
    }

    @Constraints("@pc <= high")
    public void successField46() {
        highHighHighSField[23][7] = 42;
    }

    @Constraints("@pc <= high")
    public void successField47() {
        highHighHighSField[23][7] = mkHigh(42);
    }
}