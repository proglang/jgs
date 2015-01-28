package junitConstraints;

import static security.Definition.*;

public class SuccessFieldInstance extends stubs.Fields {

    public static void main(String[] args) {
    }

    // Instance fields

    @Constraints("@pc <= low")
    public void successField1() {
        lowIField = mkLow(42);
    }

    @Constraints("@pc <= high")
    public void successField2() {
        highIField = mkLow(42);
    }

    @Constraints("@pc <= high")
    public void successField3() {
        highIField = mkHigh(42);
    }

    public void successField4() {
        highIField = mkLow(42);
    }

    public void successField5() {
        highIField = mkHigh(42);
    }

    @Constraints("low <= @return")
    public int successField6() {
        return lowIField;
    }

    @Constraints("high <= @return")
    public int successField7() {
        return lowIField;
    }

    @Constraints("high <= @return")
    public int successField8() {
        return highIField;
    }

    @Constraints({ "low <= @return", "low = @return[" })
    public int[] successField9() {
        return lowLowIField;
    }

    @Constraints({ "low <= @return", "high = @return[" })
    public int[] successField10() {
        return lowHighIField;
    }

    @Constraints({ "high <= @return", "high = @return[" })
    public int[] successField11() {
        return lowHighIField;
    }

    @Constraints({ "high <= @return", "high = @return[" })
    public int[] successField12() {
        return highHighIField;
    }

    @Constraints({ "low <= @return", "low = @return[", "low = @return[[" })
    public int[][] successField13() {
        return lowLowLowIField;
    }

    @Constraints({ "low <= @return", "low = @return[", "high = @return[[" })
    public int[][] successField14() {
        return lowLowHighIField;
    }

    @Constraints({ "low <= @return", "high = @return[", "high = @return[[" })
    public int[][] successField15() {
        return lowHighHighIField;
    }

    @Constraints({ "high <= @return", "high = @return[", "high = @return[[" })
    public int[][] successField16() {
        return lowHighHighIField;
    }

    @Constraints({ "high <= @return", "high = @return[", "high = @return[[" })
    public int[][] successField17() {
        return highHighHighIField;
    }

    @Constraints("@pc <= low")
    public void successField18() {
        lowLowIField = arrayIntLow(42);
    }

    @Constraints("@pc <= low")
    public void successField19() {
        lowLowIField[23] = 42;
    }

    @Constraints("@pc <= low")
    public void successField20() {
        lowHighIField = arrayIntHigh(1);
    }

    @Constraints("@pc <= low")
    public void successField21() {
        lowHighIField[23] = 42;
    }

    @Constraints("@pc <= low")
    public void successField22() {
        lowHighIField[23] = mkHigh(42);
    }

    @Constraints("@pc <= high")
    public void successField23() {
        highHighIField = arrayIntHigh(mkHigh(42));
    }

    @Constraints("@pc <= high")
    public void successField24() {
        highHighIField = arrayIntHigh(42);
    }

    @Constraints("@pc <= high")
    public void successField25() {
        highHighIField[23] = mkHigh(42);
    }

    @Constraints("@pc <= high")
    public void successField26() {
        highHighIField[23] = 42;
    }

    @Constraints("@pc <= low")
    public void successField27() {
        lowLowLowIField = arrayIntLowLow(42, 7);
    }

    @Constraints("@pc <= low")
    public void successField28() {
        lowLowLowIField[23] = arrayIntLow(42);
    }

    @Constraints("@pc <= low")
    public void successField29() {
        lowLowLowIField[23][7] = 42;
    }

    @Constraints("@pc <= low")
    public void successField30() {
        lowLowHighIField = arrayIntLowHigh(42, 7);
    }

    @Constraints("@pc <= low")
    public void successField31() {
        lowLowHighIField[23] = arrayIntHigh(42);
    }

    @Constraints("@pc <= low")
    public void successField32() {
        lowLowHighIField[23][7] = mkHigh(42);
    }

    @Constraints("@pc <= low")
    public void successField33() {
        lowLowHighIField[23][7] = mkLow(42);
    }

    @Constraints("@pc <= low")
    public void successField34() {
        lowHighHighIField = arrayIntHighHigh(42, 7);
    }

    @Constraints("@pc <= low")
    public void successField35() {
        lowHighHighIField = arrayIntHighHigh(42, mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void successField36() {
        lowHighHighIField[23] = arrayIntHigh(42);
    }

    @Constraints("@pc <= low")
    public void successField37() {
        lowHighHighIField[23] = arrayIntHigh(mkHigh(42));
    }

    @Constraints("@pc <= low")
    public void successField38() {
        lowHighHighIField[23][7] = mkHigh(42);
    }

    @Constraints("@pc <= low")
    public void successField39() {
        lowHighHighIField[23][7] = 42;
    }

    @Constraints("@pc <= high")
    public void successField40() {
        highHighHighIField = arrayIntHighHigh(42, 7);
    }

    @Constraints("@pc <= high")
    public void successField41() {
        highHighHighIField = arrayIntHighHigh(42, mkHigh(7));
    }

    @Constraints("@pc <= high")
    public void successField42() {
        highHighHighIField = arrayIntHighHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints("@pc <= high")
    public void successField43() {
        highHighHighIField = arrayIntHighHigh(mkHigh(42), 7);
    }

    @Constraints("@pc <= high")
    public void successField44() {
        highHighHighIField[23] = arrayIntHigh(42);
    }

    @Constraints("@pc <= high")
    public void successField45() {
        highHighHighIField[23] = arrayIntHigh(mkHigh(42));
    }

    @Constraints("@pc <= high")
    public void successField46() {
        highHighHighIField[23][7] = 42;
    }

    @Constraints("@pc <= high")
    public void successField47() {
        highHighHighIField[23][7] = mkHigh(42);
    }
}
