package junitLevel;

import static security.Definition.*;

public class FailIfElse {

    @ParameterSecurity({ "low" })
    public static void main(String[] args) {
    }

    @ReturnSecurity("low")
    public int ifReturnExpr() {
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        // @security("The returned value has a stronger security level than expected.")
        return conditionHigh ? thenHigh : elseHigh;
    }

    @ReturnSecurity("low")
    public int ifReturnExpr2() {
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        // @security("The returned value has a stronger security level than expected.")
        return conditionLow ? thenHigh : elseHigh;
    }

    @ReturnSecurity("low")
    public int ifReturnExpr3() {
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        // @security("The returned value has a stronger security level than expected.")
        return conditionHigh ? thenHigh : elseLow;
    }

    @ReturnSecurity("low")
    public int ifReturnExpr4() {
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionLow = mkLow(false);
        // @security("The returned value has a stronger security level than expected.")
        return conditionLow ? thenHigh : elseLow;
    }

    @ReturnSecurity("low")
    public int ifReturnExpr5() {
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        // @security("The returned value has a stronger security level than expected.")
        return conditionHigh ? thenLow : elseHigh;
    }

    @ReturnSecurity("low")
    public int ifReturnExpr6() {
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        // @security("The returned value has a stronger security level than expected.")
        return conditionLow ? thenLow : elseHigh;
    }

    @ReturnSecurity("low")
    public int ifReturnExpr7() {
        int thenLow = mkLow(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        // @security("The returned value has a stronger security level than expected.")
        return conditionHigh ? thenLow : elseLow;
    }

    @ReturnSecurity("low")
    public int ifReturn() {
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @security("The returned value has a stronger security level than expected.")
            return thenHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return elseHigh;
    }

    @ReturnSecurity("low")
    public int ifReturn2() {
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            // @security("The returned value has a stronger security level than expected.")
            return thenHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return elseHigh;
    }

    @ReturnSecurity("low")
    public int ifReturn3() {
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @security("The returned value has a stronger security level than expected.")
            return thenHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return elseLow;
    }

    @ReturnSecurity("low")
    public int ifReturn4() {
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            // @security("The returned value has a stronger security level than expected.")
            return thenHigh;
        }
        return elseLow;
    }

    @ReturnSecurity("low")
    public int ifReturn5() {
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @security("The returned value has a stronger security level than expected.")
            return thenLow;
        }
        // @security("The returned value has a stronger security level than expected.")
        return elseHigh;
    }

    @ReturnSecurity("low")
    public int ifReturn6() {
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            return thenLow;
        }
        // @security("The returned value has a stronger security level than expected.")
        return elseHigh;
    }

    @ReturnSecurity("low")
    public int ifReturn7() {
        int thenLow = mkLow(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @security("The returned value has a stronger security level than expected.")
            return thenLow;
        }
        // @security("The returned value has a stronger security level than expected.")
        return elseLow;
    }

    @ReturnSecurity("low")
    public int ifElseReturn() {
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @security("The returned value has a stronger security level than expected.")
            return thenHigh;
        } else {
            // @security("The returned value has a stronger security level than expected.")
            return elseHigh;
        }
    }

    @ReturnSecurity("low")
    public int ifElseReturn2() {
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            // @security("The returned value has a stronger security level than expected.")
            return thenHigh;
        } else {
            // @security("The returned value has a stronger security level than expected.")
            return elseHigh;
        }
    }

    @ReturnSecurity("low")
    public int ifElseReturn3() {
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @security("The returned value has a stronger security level than expected.")
            return thenHigh;
        } else {
            // @security("The returned value has a stronger security level than expected.")
            return elseLow;
        }
    }

    @ReturnSecurity("low")
    public int ifElseReturn4() {
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            // @security("The returned value has a stronger security level than expected.")
            return thenHigh;
        } else {
            return elseLow;
        }
    }

    @ReturnSecurity("low")
    public int ifElseReturn5() {
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @security("The returned value has a stronger security level than expected.")
            return thenLow;
        } else {
            // @security("The returned value has a stronger security level than expected.")
            return elseHigh;
        }
    }

    @ReturnSecurity("low")
    public int ifElseReturn6() {
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            return thenLow;
        } else {
            // @security("The returned value has a stronger security level than expected.")
            return elseHigh;
        }
    }

    @ReturnSecurity("low")
    public int ifElseReturn7() {
        int thenLow = mkLow(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @security("The returned value has a stronger security level than expected.")
            return thenLow;
        } else {
            // @security("The returned value has a stronger security level than expected.")
            return elseLow;
        }
    }

    @ReturnSecurity("low")
    public int ifElseAssign() {
        int result = mkHigh(42);
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            result = thenHigh;
        } else {
            result = elseHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public int ifElseAssign2() {
        int result = mkHigh(42);
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            result = thenHigh;
        } else {
            result = elseHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public int ifElseAssign3() {
        int result = mkLow(42);
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            result = thenHigh;
        } else {
            result = elseHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public int ifElseAssign4() {
        int result = mkLow(42);
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            result = thenHigh;
        } else {
            result = elseHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public int ifElseAssign5() {
        int result = mkHigh(42);
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            result = thenHigh;
        } else {
            result = elseLow;
        }
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public int ifElseAssign6() {
        int result = mkHigh(42);
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            result = thenHigh;
        } else {
            result = elseLow;
        }
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public int ifElseAssign7() {
        int result = mkLow(42);
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            result = thenHigh;
        } else {
            result = elseLow;
        }
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public int ifElseAssign8() {
        int result = mkLow(42);
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            result = thenHigh;
        } else {
            result = elseLow;
        }
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public int ifElseAssign9() {
        int result = mkHigh(42);
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            result = thenLow;
        } else {
            result = elseHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public int ifElseAssign10() {
        int result = mkHigh(42);
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            result = thenLow;
        } else {
            result = elseHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public int ifElseAssign11() {
        int result = mkLow(42);
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            result = thenLow;
        } else {
            result = elseHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public int ifElseAssign12() {
        int result = mkLow(42);
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            result = thenLow;
        } else {
            result = elseHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public int ifElseAssign13() {
        int result = mkHigh(42);
        int thenLow = mkLow(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            result = thenLow;
        } else {
            result = elseLow;
        }
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public int ifElseAssign14() {
        int result = mkLow(42);
        int thenLow = mkLow(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            result = thenLow;
        } else {
            result = elseLow;
        }
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("high")
    @WriteEffect({ "low" })
    public int ifElseAssignField() {
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = thenHigh;
        } else {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = elseHigh;
        }
        return lowField;
    }

    @ReturnSecurity("high")
    @WriteEffect({ "low" })
    public int ifElseAssignField2() {
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = thenHigh;
        } else {
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = elseHigh;
        }
        return lowField;
    }

    @ReturnSecurity("high")
    @WriteEffect({ "low" })
    public int ifElseAssignField3() {
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = thenHigh;
        } else {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = elseLow;
        }
        return lowField;
    }

    @ReturnSecurity("high")
    @WriteEffect({ "low" })
    public int ifElseAssignField4() {
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = thenHigh;
        } else {
            lowField = elseLow;
        }
        return lowField;
    }

    @ReturnSecurity("high")
    @WriteEffect({ "low" })
    public int ifElseAssignField5() {
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = thenLow;
        } else {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = elseHigh;
        }
        return lowField;
    }

    @ReturnSecurity("high")
    @WriteEffect({ "low" })
    public int ifElseAssignField6() {
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            lowField = thenLow;
        } else {
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = elseHigh;
        }
        return lowField;
    }

    @ReturnSecurity("high")
    @WriteEffect({ "low" })
    public int ifElseAssignField7() {
        int thenLow = mkLow(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = thenLow;
        } else {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = elseLow;
        }
        return lowField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "high" })
    public int ifElseAssignField8() {
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            highField = thenHigh;
        } else {
            highField = elseHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return highField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "high" })
    public int ifElseAssignField9() {
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            highField = thenHigh;
        } else {
            highField = elseHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return highField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "low" })
    public int ifElseAssignField10() {
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = thenHigh;
        } else {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = elseHigh;
        }
        return lowField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "low" })
    public int ifElseAssignField11() {
        int thenHigh = mkHigh(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = thenHigh;
        } else {
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = elseHigh;
        }
        return lowField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "high" })
    public int ifElseAssignField12() {
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            highField = thenHigh;
        } else {
            highField = elseLow;
        }
        // @security("The returned value has a stronger security level than expected.")
        return highField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "high" })
    public int ifElseAssignField13() {
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            highField = thenHigh;
        } else {
            highField = elseLow;
        }
        // @security("The returned value has a stronger security level than expected.")
        return highField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "low" })
    public int ifElseAssignField14() {
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = thenHigh;
        } else {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = elseLow;
        }
        return lowField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "low" })
    public int ifElseAssignField15() {
        int thenHigh = mkHigh(42);
        int elseLow = mkLow(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = thenHigh;
        } else {
            lowField = elseLow;
        }
        return lowField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "high" })
    public int ifElseAssignField16() {
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            highField = thenLow;
        } else {
            highField = elseHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return highField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "high" })
    public int ifElseAssignField17() {
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            highField = thenLow;
        } else {
            highField = elseHigh;
        }
        // @security("The returned value has a stronger security level than expected.")
        return highField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "low" })
    public int ifElseAssignField18() {
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = thenLow;
        } else {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = elseHigh;
        }
        return lowField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "low" })
    public int ifElseAssignField19() {
        int thenLow = mkLow(42);
        int elseHigh = mkHigh(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            lowField = thenLow;
        } else {
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = elseHigh;
        }
        return lowField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "high" })
    public int ifElseAssignField20() {
        int thenLow = mkLow(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            highField = thenLow;
        } else {
            highField = elseLow;
        }
        // @security("The returned value has a stronger security level than expected.")
        return highField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "high" })
    public int ifElseAssignField21() {
        int thenLow = mkLow(42);
        int elseLow = mkLow(42);
        boolean conditionLow = mkLow(false);
        if (conditionLow) {
            highField = thenLow;
        } else {
            highField = elseLow;
        }
        // @security("The returned value has a stronger security level than expected.")
        return highField;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "low" })
    public int ifElseAssignField22() {
        int thenLow = mkLow(42);
        int elseLow = mkLow(42);
        boolean conditionHigh = mkHigh(false);
        if (conditionHigh) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = thenLow;
        } else {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = elseLow;
        }
        return lowField;
    }

    @FieldSecurity("low")
    public int lowField = mkLow(42);

    @FieldSecurity("high")
    public int highField = mkHigh(42);

    @WriteEffect({ "low", "high" })
    public FailIfElse() {
        super();
    }

}
