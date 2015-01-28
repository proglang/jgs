package junitConstraints;

import static security.Definition.*;
import security.Definition.Constraints;
import security.Definition.FieldSecurity;

public class FailArray extends stubs.SpecialArrays {

    public static void main(String[] args) {
    }

    @FieldSecurity("low")
    public int lowIField;

    @FieldSecurity("high")
    public int highIField;

    @Constraints("@pc <= low")
    public void failArray1() {
        // @security("Illegal flow from high to low")
        lowLow = arrayIntLow(mkHigh(42));
    }

    @Constraints("@pc <= low")
    public void failArray2() {
        // @security("Illegal flow from high to low")
        lowLow = arrayIntHigh(42);
    }

    @Constraints("@pc <= low")
    public void failArray3() {
        // @security("Illegal flow from high to low")
        lowLow = arrayIntHigh(mkHigh(42));
    }

    @Constraints("@pc <= low")
    public void failArray4() {
        // @security("Illegal flow from high to low")
        lowHigh = arrayIntLow(42);
    }

    @Constraints("@pc <= low")
    public void failArray5() {
        // @security("Illegal flow from high to low")
        lowHigh = arrayIntLow(mkHigh(42));
    }

    @Constraints("@pc <= low")
    public void failArray6() {
        // @security("Illegal flow from high to low")
        lowHigh = arrayIntHigh(mkHigh(42));
    }

    public void failArray7() {
        // @security("Illegal flow from high to low")
        highHigh = arrayIntLow(42);
    }

    public void failArray8() {
        // @security("Illegal flow from high to low")
        highHigh = arrayIntLow(mkHigh(42));
    }

    @Constraints("@pc <= low")
    public void failArray9() {
        // @security("Illegal flow from high to low")
        lowLow[23] = mkHigh(42);
    }

    @Constraints("low <= @return")
    public int failArray10() {
        // @security("weaker return level expected")
        return highHigh.length;
    }

    @Constraints({ "low <= @return", "@return[ = high" })
    public int[] failArray11() {
        // @security("weaker return level expected")
        // @security("Inconsistent constraint set")
        return lowLow;
    }

    @Constraints({ "high <= @return", "@return[ = high" })
    public int[] failArray12() {
        // @security("weaker return level expected")
        return lowLow;
    }

    @Constraints({ "low <= @return", "@return[ = low" })
    public int[] failArray13() {
        // @security("weaker return level expected")
        return lowHigh;
    }

    @Constraints({ "high <= @return", "@return[ = low" })
    public int[] failArray14() {
        // @security("weaker return level expected")
        return lowHigh;
    }

    @Constraints({ "low <= @return", "@return[ = low" })
    public int[] failArray15() {
        // @security("weaker return level expected")
        // @security("Inconsistent constraint set")
        return highHigh;
    }

    @Constraints({ "high <= @return", "@return[ = low" })
    public int[] failArray16() {
        // @security("weaker return level expected")
        return highHigh;
    }

    @Constraints({ "low <= @return", "@return[ = high" })
    public int[] failArray17() {
        // @security("weaker return level expected")
        return highHigh;
    }

    @Constraints({ "low <= @return" })
    public int failArray18() {
        // @security("weaker return level expected")
        return lowHigh[23];
    }

    @Constraints({ "low <= @return" })
    public int failArray19() {
        // @security("weaker return level expected")
        return highHigh[23];
    }

    @Constraints({ "@pc <= low" })
    public void failArray20() {
        // @security("Illegal flow from high to low")
        lowLowLow = arrayIntLowLow(42, mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray21() {
        // @security("Illegal flow from high to low")
        lowLowLow = arrayIntLowLow(mkHigh(42), 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray22() {
        // @security("Illegal flow from high to low")
        lowLowLow = arrayIntLowLow(mkHigh(42), mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray23() {
        // @security("Illegal flow from high to low")
        lowLowLow = arrayIntLowHigh(42, 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray24() {
        // @security("Illegal flow from high to low")
        lowLowLow = arrayIntLowHigh(42, mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray25() {
        // @security("Illegal flow from high to low")
        lowLowLow = arrayIntLowHigh(mkHigh(42), 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray26() {
        // @security("Illegal flow from high to low")
        lowLowLow = arrayIntLowHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray27() {
        // @security("Illegal flow from high to low")
        lowLowLow = arrayIntHighHigh(42, 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray28() {
        // @security("Illegal flow from high to low")
        lowLowLow = arrayIntHighHigh(42, mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray29() {
        // @security("Illegal flow from high to low")
        lowLowLow = arrayIntHighHigh(mkHigh(42), 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray30() {
        // @security("Illegal flow from high to low")
        lowLowLow = arrayIntHighHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray31() {
        // @security("Illegal flow from high to low")
        lowLowHigh = arrayIntLowLow(42, 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray32() {
        // @security("Illegal flow from high to low")
        lowLowHigh = arrayIntLowLow(42, mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray33() {
        // @security("Illegal flow from high to low")
        lowLowHigh = arrayIntLowLow(mkHigh(42), 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray34() {
        // @security("Illegal flow from high to low")
        lowLowHigh = arrayIntLowLow(mkHigh(42), mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray35() {
        // @security("Illegal flow from high to low")
        lowLowHigh = arrayIntLowHigh(42, mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray36() {
        // @security("Illegal flow from high to low")
        lowLowHigh = arrayIntLowHigh(mkHigh(42), 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray37() {
        // @security("Illegal flow from high to low")
        lowLowHigh = arrayIntLowHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray38() {
        // @security("Illegal flow from high to low")
        lowLowHigh = arrayIntHighHigh(42, 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray39() {
        // @security("Illegal flow from high to low")
        lowLowHigh = arrayIntHighHigh(42, mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray40() {
        // @security("Illegal flow from high to low")
        lowLowHigh = arrayIntHighHigh(mkHigh(42), 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray41() {
        // @security("Illegal flow from high to low")
        lowLowHigh = arrayIntHighHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray42() {
        // @security("Illegal flow from high to low")
        lowHighHigh = arrayIntLowLow(42, 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray43() {
        // @security("Illegal flow from high to low")
        lowHighHigh = arrayIntLowLow(42, mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray44() {
        // @security("Illegal flow from high to low")
        lowHighHigh = arrayIntLowLow(mkHigh(42), 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray45() {
        // @security("Illegal flow from high to low")
        lowHighHigh = arrayIntLowLow(mkHigh(42), mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray46() {
        // @security("Illegal flow from high to low")
        lowHighHigh = arrayIntLowHigh(42, 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray47() {
        // @security("Illegal flow from high to low")
        lowHighHigh = arrayIntLowHigh(42, mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray48() {
        // @security("Illegal flow from high to low")
        lowHighHigh = arrayIntLowHigh(mkHigh(42), 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray49() {
        // @security("Illegal flow from high to low")
        lowHighHigh = arrayIntLowHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray50() {
        // @security("Illegal flow from high to low")
        lowHighHigh = arrayIntHighHigh(mkHigh(42), 7);
    }

    @Constraints({ "@pc <= low" })
    public void failArray51() {
        // @security("Illegal flow from high to low")
        lowHighHigh = arrayIntHighHigh(mkHigh(42), mkHigh(7));
    }

    public void failArray52() {
        // @security("Illegal flow from high to low")
        highHighHigh = arrayIntLowLow(42, 7);
    }

    public void failArray53() {
        // @security("Illegal flow from high to low")
        highHighHigh = arrayIntLowLow(42, mkHigh(7));
    }

    public void failArray54() {
        // @security("Illegal flow from high to low")
        highHighHigh = arrayIntLowLow(mkHigh(42), 7);
    }

    public void failArray55() {
        // @security("Illegal flow from high to low")
        highHighHigh = arrayIntLowLow(mkHigh(42), mkHigh(7));
    }

    public void failArray56() {
        // @security("Illegal flow from high to low")
        highHighHigh = arrayIntLowHigh(42, 7);
    }

    public void failArray57() {
        // @security("Illegal flow from high to low")
        highHighHigh = arrayIntLowHigh(42, mkHigh(7));
    }

    public void failArray58() {
        // @security("Illegal flow from high to low")
        highHighHigh = arrayIntLowHigh(mkHigh(42), 7);
    }

    public void failArray59() {
        // @security("Illegal flow from high to low")
        highHighHigh = arrayIntLowHigh(mkHigh(42), mkHigh(7));
    }

    @Constraints({ "@pc <= low" })
    public void failArray60() {
        // @security("Illegal flow from high to low")
        lowLowLow[23] = arrayIntLow(mkHigh(42));
    }

    @Constraints({ "@pc <= low" })
    public void failArray61() {
        // @security("Illegal flow from high to low")
        lowLowLow[23] = arrayIntHigh(42);
    }

    @Constraints({ "@pc <= low" })
    public void failArray62() {
        // @security("Illegal flow from high to low")
        lowLowLow[23] = arrayIntHigh(mkHigh(42));
    }

    @Constraints({ "@pc <= low" })
    public void failArray63() {
        // @security("Illegal flow from high to low")
        lowLowHigh[23] = arrayIntLow(42);
    }

    @Constraints({ "@pc <= low" })
    public void failArray64() {
        // @security("Illegal flow from high to low")
        lowLowHigh[23] = arrayIntLow(mkHigh(42));
    }

    @Constraints({ "@pc <= low" })
    public void failArray65() {
        // @security("Illegal flow from high to low")
        lowLowHigh[23] = arrayIntHigh(mkHigh(42));
    }

    public void failArray66() {
        // @security("Illegal flow from high to low")
        lowHighHigh[23] = arrayIntLow(42);
    }

    public void failArray67() {
        // @security("Illegal flow from high to low")
        lowHighHigh[23] = arrayIntLow(mkHigh(42));
    }

    public void failArray68() {
        // @security("Illegal flow from high to low")
        highHighHigh[23] = arrayIntLow(42);
    }

    public void failArray69() {
        // @security("Illegal flow from high to low")
        highHighHigh[23] = arrayIntLow(mkHigh(42));
    }

    @Constraints("@pc <= low")
    public void failArray70() {
        // @security("Illegal flow from high to low")
        lowLowLow[23][7] = mkHigh(42);
    }

    @Constraints("low <= @return")
    public int failArray71() {
        // @security("Illegal flow from high to low")
        return highHighHigh.length;
    }

    @Constraints("low <= @return")
    public int failArray72() {
        // @security("Illegal flow from high to low")
        return lowHighHigh[23].length;
    }

    @Constraints("low <= @return")
    public int failArray73() {
        // @security("Illegal flow from high to low")
        return highHighHigh[23].length;
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = high" })
    public int[][] failArray74() {
        // @security("weaker return level expected")
        // @security("Inconsistent constraint set")
        return lowLowLow;
    }

    @Constraints({ "low <= @return", "@return[ = high", "@return[[ = high" })
    public int[][] failArray75() {
        // @security("weaker return level expected")
        // @security("Inconsistent constraint set")
        return lowLowLow;
    }

    @Constraints({ "high <= @return", "@return[ = high", "@return[[ = high" })
    public int[][] failArray76() {
        // @security("weaker return level expected")
        return lowLowLow;
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = low" })
    public int[][] failArray77() {
        // @security("weaker return level expected")
        return lowLowHigh;
    }

    @Constraints({ "low <= @return", "@return[ = high", "@return[[ = high" })
    public int[][] failArray78() {
        // @security("weaker return level expected")
        // @security("Inconsistent constraint set")
        return lowLowHigh;
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = low" })
    public int[][] failArray79() {
        // @security("weaker return level expected")
        return lowHighHigh;
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = high" })
    public int[][] failArray80() {
        // @security("weaker return level expected")
        return lowHighHigh;
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = low" })
    public int[][] failArray81() {
        // @security("weaker return level expected")
        // @security("Inconsistent constraint set")
        return highHighHigh;
    }

    @Constraints({ "low <= @return", "@return[ = low", "@return[[ = high" })
    public int[][] failArray82() {
        // @security("weaker return level expected")
        // @security("Inconsistent constraint set")
        return highHighHigh;
    }

    @Constraints({ "low <= @return", "@return[ = high", "@return[[ = high" })
    public int[][] failArray83() {
        // @security("weaker return level expected")
        return highHighHigh;
    }

    @Constraints({ "low <= @return", "@return[ = high" })
    public int[] failArray84() {
        // @security("weaker return level expected")
        // @security("Inconsistent constraint set")
        return lowLowLow[23];
    }

    @Constraints({ "high <= @return", "@return[ = high" })
    public int[] failArray85() {
        // @security("weaker return level expected")
        return lowLowLow[23];
    }

    @Constraints({ "low <= @return", "@return[ = low" })
    public int[] failArray86() {
        // @security("weaker return level expected")
        return lowLowHigh[23];
    }

    @Constraints({ "low <= @return", "@return[ = low" })
    public int[] failArray87() {
        // @security("weaker return level expected")
        // @security("Inconsistent constraint set")
        return lowHighHigh[23];
    }

    @Constraints({ "low <= @return", "@return[ = high" })
    public int[] failArray88() {
        // @security("weaker return level expected")
        return lowHighHigh[23];
    }

    @Constraints({ "low <= @return", "@return[ = low" })
    public int[] failArray89() {
        // @security("weaker return level expected")
        // @security("Inconsistent constraint set")
        return highHighHigh[23];
    }

    @Constraints({ "low <= @return", "@return[ = high" })
    public int[] failArray90() {
        // @security("weaker return level expected")
        return highHighHigh[23];
    }

    @Constraints("@pc <= low")
    public void failArray91() {
        // @security("Invalid index")
        lowLow[highIField] = 42;
    }

    @Constraints("@pc <= low")
    public void failArray92() {
        // @security("Invalid index")
        lowLowLow[highIField][lowIField] = 42;
    }

    @Constraints("@pc <= low")
    public void failArray93() {
        // @security("Invalid index")
        lowLowLow[lowIField][highIField] = 42;
    }

    @Constraints("@pc <= low")
    public void failArray94() {
        // @security("Invalid index")
        // @security("Invalid index")
        lowLowLow[highIField][highIField] = 42;
    }

    @Constraints("low <= @return")
    public int failArray96() {
        // @security("Invalid index")
        return lowLow[highIField];
    }

    @Constraints("low <= @return")
    public int failArray97() {
        // @security("Invalid index")
        return lowHigh[lowIField];
    }

    @Constraints("low <= @return")
    public int failArray98() {
        // @security("Invalid index")
        return lowHigh[highIField];
    }

    @Constraints("low <= @return")
    public int failArray99() {
        // @security("Invalid index")
        return highHigh[lowIField];
    }

    @Constraints("low <= @return")
    public int failArray100() {
        // @security("Invalid index")
        return highHigh[highIField];
    }

}
