package junitConstraints;

import static security.Definition.*;

public class FailFieldStatic extends stubs.Fields {

    public static void main(String[] args) {
    }

    // Static fields

    @Constraints("low <= @return")
    public int failField1() {
        // @security("Return high value - expected was low")
        return highSField;
    }

    @Constraints("low <= @return")
    public int failField2() {
        int i = highSField;
        // @security("Return high value - expected was low")
        return i;
    }

    @Constraints({ "low <= @return", "high = @return[" })
    public int[] failField3() {
        // @security("Illegal flow from high to low")
        // @security("Return low value - expected was high")
        return lowLowSField;
    }

    @Constraints({ "low <= @return", "low = @return[" })
    public int[] failField4() {
        // @security("Return high value - expected was low")
        return lowHighSField;
    }

    @Constraints({ "high <= @return", "low = @return[" })
    public int[] failField5() {
        // @security("Return high value - expected was low")
        return lowHighSField;
    }

    @Constraints({ "low <= @return", "low = @return[" })
    public int[] failField6() {
        // @security("Return high value - expected was low")
        // @security("Illegal flow from high to low")
        return highHighSField;
    }

    @Constraints({ "low <= @return", "high = @return[" })
    public int[] failField7() {
        // @security("Return high value - expected was low")
        return highHighSField;
    }

    @Constraints({ "high <= @return", "low = @return[" })
    public int[] failField8() {
        // @security("Illegal flow from high to low")
        return highHighSField;
    }

    @Constraints({ "low <= @return", "low = @return[", "high = @return[[" })
    public int[][] failField9() {
        // @security("Return low value - expected was high")
        // @security("Illegal flow from high to low")
        return lowLowLowSField;
    }

    @Constraints({ "low <= @return", "high = @return[", "high = @return[[" })
    public int[][] failField10() {
        // @security("Return low value - expected was high")
        // @security("Illegal flow from high to low")
        return lowLowLowSField;
    }

    @Constraints({ "low <= @return", "high = @return[", "low = @return[[" })
    public int[][] failField11() {
        // @security("Return low value - expected was high")
        // @security("Illegal flow from high to low")
        return lowLowLowSField;
    }

    @Constraints({ "low <= @return", "low = @return[", "low = @return[[" })
    public int[][] failField12() {
        // @security("Illegal flow from high to low")
        return lowLowHighSField;
    }

    @Constraints({ "low <= @return", "high = @return[", "low = @return[[" })
    public int[][] failField13() {
        // @security("Return low value - expected was high")
        // @security("Illegal flow from high to low")
        return lowLowHighSField;
    }

    @Constraints({ "low <= @return", "high = @return[", "high = @return[[" })
    public int[][] failField14() {
        // @security("Return low value - expected was high")
        // @security("Illegal flow from high to low")
        return lowLowHighSField;
    }

    @Constraints({ "low <= @return", "low = @return[", "low = @return[[" })
    public int[][] failField15() {
        // @security("Illegal flow from high to low")
        return lowHighHighSField;
    }

    @Constraints({ "low <= @return", "low = @return[", "high = @return[[" })
    public int[][] failField16() {
        // @security("Illegal flow from high to low")
        return lowHighHighSField;
    }

    @Constraints({ "low <= @return", "high = @return[", "low = @return[[" })
    public int[][] failField17() {
        // @security("Illegal flow from high to low")
        return lowHighHighSField;
    }

    @Constraints({ "low <= @return", "low = @return[", "low = @return[[" })
    public int[][] failField18() {
        // @security("Return high value - expected was low")
        // @security("Illegal flow from high to low")
        return highHighHighSField;
    }

    @Constraints({ "low <= @return", "low = @return[", "high = @return[[" })
    public int[][] failField19() {
        // @security("Return high value - expected was low")
        // @security("Illegal flow from high to low")
        return highHighHighSField;
    }

    @Constraints({ "low <= @return", "high = @return[", "high = @return[[" })
    public int[][] failField20() {
        // @security("Return high value - expected was low")
        return highHighHighSField;
    }

    @Constraints({ "low <= @return", "high = @return[", "low = @return[[" })
    public int[][] failField21() {
        // @security("Return high value - expected was low")
        // @security("Illegal flow from high to low")
        return highHighHighSField;
    }

    @Constraints({ "low <= @return", "low = @return[", "high = @return[[" })
    public int[][] failField22() {
        // @security("Return high value - expected was low")
        // @security("Illegal flow from high to low")
        return highHighHighSField;
    }

    @Constraints({ "high <= @return", "low = @return[", "low = @return[[" })
    public int[][] failField23() {
        // @security("Illegal flow from high to low")
        return highHighHighSField;
    }

    @Constraints({ "high <= @return", "high = @return[", "low = @return[[" })
    public int[][] failField24() {
        // @security("Illegal flow from high to low")
        return highHighHighSField;
    }

    @Constraints({ "high <= @return", "low = @return[", "high = @return[[" })
    public int[][] failField25() {
        // @security("Illegal flow from high to low")
        return highHighHighSField;
    }

    @Constraints("@pc <= low")
    public void failField26() {
        // @security("Assignment of high value to low field")
        lowSField = mkHigh(42);
    }

    @Constraints("@pc <= low")
    public void failField27() {
        // @security("Assignment of high value to low field")
        lowLowSField = arrayIntHigh(42);
    }

    @Constraints("@pc <= low")
    public void failField28() {
        // @security("Assignment of high value to low field")
        lowLowSField = arrayIntHigh(mkHigh(42));
    }

    @Constraints("@pc <= low")
    public void failField29() {
        // @security("Assignment of high value to low field")
        lowLowSField[23] = mkHigh(42);
    }

    @Constraints("@pc <= low")
    public void failField30() {
        // @security("Assignment of high value to low field")
        lowHighSField = arrayIntHigh(mkHigh(42));
    }

    @Constraints("@pc <= low")
    public void failField31() {
        // @security("Assignment of high value to low field")
        lowHighSField = arrayIntLow(42);
    }

    @Constraints("@pc <= low")
    public void failField32() {
        // @security("Assignment of high value to low field")
        lowHighSField = arrayIntLow(mkHigh(42));
    }

    @Constraints("@pc <= high")
    public void failField33() {
        // @security("Assignment of high value to low field")
        highHighSField = arrayIntLow(mkHigh(42));
    }

    @Constraints("@pc <= high")
    public void failField34() {
        // @security("Assignment of high value to low field")
        highHighSField = arrayIntLow(42);
    }

    @Constraints("@pc <= low")
    public void failField35() {
        // @security("Assignment of high value to low field")
        lowLowLowSField = arrayIntLowHigh(42, 7);
    }

    @Constraints("@pc <= low")
    public void failField36() {
        // @security("Assignment of high value to low field")
        lowLowLowSField = arrayIntLowHigh(mkHigh(42), 7);
    }

    @Constraints("@pc <= low")
    public void failField37() {
        // @security("Assignment of high value to low field")
        lowLowLowSField = arrayIntLowHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField38() {
        // @security("Assignment of high value to low field")
        lowLowLowSField = arrayIntLowHigh(42, mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField39() {
        // @security("Assignment of high value to low field")
        lowLowLowSField = arrayIntHighHigh(42, 7);
    }

    @Constraints("@pc <= low")
    public void failField40() {
        // @security("Assignment of high value to low field")
        lowLowLowSField = arrayIntHighHigh(mkHigh(42), 7);
    }

    @Constraints("@pc <= low")
    public void failField41() {
        // @security("Assignment of high value to low field")
        lowLowLowSField = arrayIntHighHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField42() {
        // @security("Assignment of high value to low field")
        lowLowLowSField = arrayIntHighHigh(42, mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField43() {
        // @security("Assignment of high value to low field")
        lowLowLowSField[23] = arrayIntHigh(42);
    }

    @Constraints("@pc <= low")
    public void failField44() {
        // @security("Assignment of high value to low field")
        lowLowLowSField[23] = arrayIntHigh(mkHigh(42));
    }

    @Constraints("@pc <= low")
    public void failField45() {
        // @security("Assignment of high value to low field")
        lowLowLowSField[23] = arrayIntLow(mkHigh(42));
    }

    @Constraints("@pc <= low")
    public void failField46() {
        // @security("Assignment of high value to low field")
        lowLowLowSField[23][7] = mkHigh(42);
    }

    @Constraints("@pc <= low")
    public void failField47() {
        // @security("Assignment of high value to low field")
        lowLowHighSField = arrayIntLowHigh(mkHigh(42), 7);
    }

    @Constraints("@pc <= low")
    public void failField48() {
        // @security("Assignment of high value to low field")
        lowLowHighSField = arrayIntLowHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField49() {
        // @security("Assignment of high value to low field")
        lowLowHighSField = arrayIntLowHigh(42, mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField50() {
        // @security("Assignment of high value to low field")
        lowLowHighSField = arrayIntLowLow(mkHigh(42), 7);
    }

    @Constraints("@pc <= low")
    public void failField51() {
        // @security("Assignment of high value to low field")
        lowLowHighSField = arrayIntLowLow(mkHigh(42), mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField52() {
        // @security("Assignment of high value to low field")
        lowLowHighSField = arrayIntLowLow(42, mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField53() {
        // @security("Assignment of high value to low field")
        lowLowHighSField = arrayIntHighHigh(42, 7);
    }

    @Constraints("@pc <= low")
    public void failField54() {
        // @security("Assignment of high value to low field")
        lowLowHighSField = arrayIntHighHigh(mkHigh(42), 7);
    }

    @Constraints("@pc <= low")
    public void failField55() {
        // @security("Assignment of high value to low field")
        lowLowHighSField = arrayIntHighHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField56() {
        // @security("Assignment of high value to low field")
        lowLowHighSField = arrayIntHighHigh(42, mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField57() {
        // @security("Assignment of high value to low field")
        lowLowHighSField[23] = arrayIntHigh(mkHigh(42));
    }

    @Constraints("@pc <= low")
    public void failField58() {
        // @security("Assignment of high value to low field")
        lowLowHighSField[23] = arrayIntLow(42);
    }

    @Constraints("@pc <= low")
    public void failField59() {
        // @security("Assignment of high value to low field")
        lowLowHighSField[23] = arrayIntLow(mkHigh(42));
    }

    @Constraints("@pc <= low")
    public void failField60() {
        // @security("Assignment of high value to low field")
        lowHighHighSField = arrayIntLowHigh(mkHigh(42), 7);
    }

    @Constraints("@pc <= low")
    public void failField61() {
        // @security("Assignment of high value to low field")
        lowHighHighSField = arrayIntLowHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField62() {
        // @security("Assignment of high value to low field")
        lowHighHighSField = arrayIntLowHigh(42, mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField63() {
        // @security("Assignment of high value to low field")
        lowHighHighSField = arrayIntLowLow(mkHigh(42), 7);
    }

    @Constraints("@pc <= low")
    public void failField64() {
        // @security("Assignment of high value to low field")
        lowHighHighSField = arrayIntLowLow(mkHigh(42), mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField65() {
        // @security("Assignment of high value to low field")
        lowHighHighSField = arrayIntLowLow(42, mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField66() {
        // @security("Assignment of high value to low field")
        lowHighHighSField = arrayIntHighHigh(mkHigh(42), 7);
    }

    @Constraints("@pc <= low")
    public void failField67() {
        // @security("Assignment of high value to low field")
        lowHighHighSField = arrayIntHighHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField68() {
        // @security("Assignment of high value to low field")
        lowHighHighSField[23] = arrayIntLow(42);
    }

    @Constraints("@pc <= low")
    public void failField69() {
        // @security("Assignment of high value to low field")
        lowHighHighSField[23] = arrayIntLow(mkHigh(42));
    }

    @Constraints("@pc <= low")
    public void failField70() {
        // @security("Assignment of high value to low field")
        highHighHighSField = arrayIntLowHigh(mkHigh(42), 7);
    }

    @Constraints("@pc <= low")
    public void failField71() {
        // @security("Assignment of high value to low field")
        highHighHighSField = arrayIntLowHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField72() {
        // @security("Assignment of high value to low field")
        highHighHighSField = arrayIntLowHigh(42, mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField73() {
        // @security("Assignment of high value to low field")
        highHighHighSField = arrayIntLowLow(mkHigh(42), 7);
    }

    @Constraints("@pc <= low")
    public void failField74() {
        // @security("Assignment of high value to low field")
        highHighHighSField = arrayIntLowLow(mkHigh(42), mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField75() {
        // @security("Assignment of high value to low field")
        highHighHighSField = arrayIntLowLow(42, mkHigh(7));
    }

    @Constraints("@pc <= low")
    public void failField78() {
        // @security("Assignment of high value to low field")
        highHighHighSField[23] = arrayIntLow(42);
    }

    @Constraints("@pc <= low")
    public void failField79() {
        // @security("Assignment of high value to low field")
        highHighHighSField[23] = arrayIntLow(mkHigh(42));
    }

    public void failField80() {
        lowSField = mkLow(42);
        // @security("Missing write effect to low")
        return;
    }

    @Constraints("@pc <= high")
    public void failField81() {
        lowSField = mkLow(42);
        // @security("Missing write effect to low")
        return;
    }

    public void failField82() {
        lowLowSField = arrayIntLow(42);
        // @security("Missing write effect to low")
        return;
    }

    @Constraints("@pc <= high")
    public void failField83() {
        lowLowSField = arrayIntLow(42);
        // @security("Missing write effect to low")
        return;
    }

    public void failField84() {
        lowLowLowSField = arrayIntLowLow(42, 7);
        // @security("Missing write effect to low")
        return;
    }

    @Constraints("@pc <= high")
    public void failField85() {
        lowLowLowSField = arrayIntLowLow(42, 7);
        // @security("Missing write effect to low")
        return;
    }
}
