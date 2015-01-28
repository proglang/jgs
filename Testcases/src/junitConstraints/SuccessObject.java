package junitConstraints;

import security.Definition.Constraints;
import security.Definition.FieldSecurity;
import stubs.Obj;

public class SuccessObject extends stubs.MinimalFields {

    public static void main(String[] args) {
    }

    @Constraints("@pc <= low")
    public SuccessObject() {
    }

    @FieldSecurity("low")
    public Obj lowObj = new Obj();

    @FieldSecurity("high")
    public Obj highObj = new Obj();

    @Constraints("low <= @return")
    public int successObject1() {
        return lowObj.lowIField;
    }

    @Constraints("high <= @return")
    public int successObject2() {
        return lowObj.lowIField;
    }

    @Constraints("high <= @return")
    public int successObject3() {
        return lowObj.highIField;
    }

    @Constraints("high <= @return")
    public int successObject4() {
        return highObj.highIField;
    }

    @Constraints("high <= @return")
    public int successObject5() {
        return highObj.lowIField;
    }

    @Constraints("low <= @return")
    public int successObject6() {
        return lowObj._lowI();
    }

    @Constraints("high <= @return")
    public int successObject7() {
        return lowObj._lowI();
    }

    @Constraints("high <= @return")
    public int successObject8() {
        return lowObj._highI();
    }

    @Constraints("high <= @return")
    public int successObject9() {
        return highObj._highI();
    }

    @Constraints("high <= @return")
    public int successObject10() {
        return highObj._lowI();
    }

    @Constraints("low <= @return")
    public int successObject11() {
        return lowObj.low_lowI(lowIField);
    }

    @Constraints("low <= @return")
    public int successObject12() {
        return lowObj.high_lowI(lowIField);
    }

    @Constraints("low <= @return")
    public int successObject13() {
        return lowObj.high_lowI(highIField);
    }

    @Constraints("high <= @return")
    public int successObject14() {
        return lowObj.low_highI(lowIField);
    }

    @Constraints("high <= @return")
    public int successObject15() {
        return lowObj.high_highI(lowIField);
    }

    @Constraints("high <= @return")
    public int successObject16() {
        return lowObj.high_highI(highIField);
    }

    @Constraints("high <= @return")
    public int successObject17() {
        return highObj.low_lowI(lowIField);
    }

    @Constraints("high <= @return")
    public int successObject18() {
        return highObj.high_lowI(lowIField);
    }

    @Constraints("high <= @return")
    public int successObject19() {
        return highObj.high_lowI(highIField);
    }

    @Constraints("high <= @return")
    public int successObject20() {
        return highObj.low_highI(lowIField);
    }

    @Constraints("high <= @return")
    public int successObject21() {
        return highObj.high_highI(lowIField);
    }

    @Constraints("high <= @return")
    public int successObject22() {
        return highObj.high_highI(highIField);
    }

    @Constraints({ "low <= @return", "low = @return[" })
    public int[] successObject23() {
        return lowObj.lowLowIField;
    }

    @Constraints({ "high <= @return", "low = @return[" })
    public int[] successObject24() {
        return lowObj.lowLowIField;
    }

    @Constraints({ "high <= @return", "low = @return[" })
    public int[] successObject25() {
        return highObj.lowLowIField;
    }

    @Constraints({ "low <= @return", "high = @return[" })
    public int[] successObject26() {
        return lowObj.lowHighIField;
    }

    @Constraints({ "high <= @return", "high = @return[" })
    public int[] successObject27() {
        return lowObj.lowHighIField;
    }

    @Constraints({ "high <= @return", "high = @return[" })
    public int[] successObject28() {
        return highObj.lowHighIField;
    }

    @Constraints({ "high <= @return", "high = @return[" })
    public int[] successObject29() {
        return lowObj.highHighIField;
    }

    @Constraints({ "high <= @return", "high = @return[" })
    public int[] successObject30() {
        return highObj.highHighIField;
    }

}
