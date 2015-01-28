package junitLevel;

import static security.Definition.*;

public class FailLoop {

    @ParameterSecurity({ "low" })
    public static void main(String[] args) {
    }

    @WriteEffect({ "high" })
    @ParameterSecurity({ "high" })
    public void forLoopField2(int arg1High) {
        for (int i = 0; i < arg1High; i++) {
            // @security("The security level of the ID-function argument should be weaker or equal to the level of the function.")
            highField = mkLow(42);
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "high" })
    public void forLoopField3(int arg1High) {
        for (int i = 0; i < arg1High; i++) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = mkHigh(42);
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "high" })
    public void forLoopField4(int arg1High) {
        for (int i = 0; i < arg1High; i++) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            // @security("The security level of the ID-function argument should be weaker or equal to the level of the function.")
            lowField = mkLow(42);
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "low" })
    public void forLoopField7(int arg1Low) {
        for (int i = 0; i < arg1Low; i++) {
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = mkHigh(42);
        }
    }

    @ParameterSecurity({ "high" })
    @ReturnSecurity("high")
    public int forLoopLocal2(int arg1High) {
        int var1High = mkHigh(42);
        for (int i = 0; i < arg1High; i++) {
            // @security("The security level of the ID-function argument should be weaker or equal to the level of the function.")
            var1High = mkLow(42);
        }
        return var1High;
    }

    @ParameterSecurity({ "high" })
    @ReturnSecurity("high")
    public int forLoopLocal4(int arg1High) {
        int var1Low = mkLow(42);
        for (int i = 0; i < arg1High; i++) {
            // @security("The security level of the ID-function argument should be weaker or equal to the level of the function.")
            var1Low = mkLow(42);
        }
        return var1Low;
    }

    @ParameterSecurity({ "high" })
    @ReturnSecurity("low")
    public int forLoopLocal9(int arg1High) {
        int var1High = mkHigh(42);
        for (int i = 0; i < arg1High; i++) {
            var1High = mkHigh(42);
        }
        // @security("The returned value has a stronger security level than expected.")
        return var1High;
    }

    @ParameterSecurity({ "high" })
    @ReturnSecurity("low")
    public int forLoopLocal10(int arg1High) {
        int var1High = mkHigh(42);
        for (int i = 0; i < arg1High; i++) {
            // @security("The security level of the ID-function argument should be weaker or equal to the level of the function.")
            var1High = mkLow(42);
        }
        // @security("The returned value has a stronger security level than expected.")
        return var1High;
    }

    @ParameterSecurity({ "high" })
    @ReturnSecurity("low")
    public int forLoopLocal11(int arg1High) {
        int var1Low = mkLow(42);
        for (int i = 0; i < arg1High; i++) {
            var1Low = mkHigh(42);
        }
        // @security("The returned value has a stronger security level than expected.")
        return var1Low;
    }

    @ParameterSecurity({ "high" })
    @ReturnSecurity("low")
    public int forLoopLocal12(int arg1High) {
        int var1Low = mkLow(42);
        for (int i = 0; i < arg1High; i++) {
            // @security("The security level of the ID-function argument should be weaker or equal to the level of the function.")
            var1Low = mkLow(42);
        }
        // @security("The returned value has a stronger security level than expected.")
        return var1Low;
    }

    @ParameterSecurity({ "low" })
    @ReturnSecurity("low")
    public int forLoopLocal13(int arg1Low) {
        int var1High = mkHigh(42);
        for (int i = 0; i < arg1Low; i++) {
            var1High = mkHigh(42);
        }
        // @security("The returned value has a stronger security level than expected.")
        return var1High;
    }

    @ParameterSecurity({ "low" })
    @ReturnSecurity("low")
    public int forLoopLocal14(int arg1Low) {
        int var1High = mkHigh(42);
        for (int i = 0; i < arg1Low; i++) {
            var1High = mkLow(42);
        }
        // @security("The returned value has a stronger security level than expected.")
        return var1High;
    }

    @ParameterSecurity({ "low" })
    @ReturnSecurity("low")
    public int forLoopLocal15(int arg1Low) {
        int var1Low = mkLow(42);
        for (int i = 0; i < arg1Low; i++) {
            var1Low = mkHigh(42);
        }
        // @security("The returned value has a stronger security level than expected.")
        return var1Low;
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "high" })
    public void whileLoopField5(int arg1High) {
        int var1High = mkHigh(42);
        while (var1High < arg1High) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = var1High++;
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "high" })
    public void whileLoopField6(int arg1High) {
        int var1Low = mkLow(42);
        while (var1Low < arg1High) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = var1Low++;
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "high" })
    public void whileLoopField7(int arg1High) {
        int var1High = mkHigh(42);
        while (var1High < arg1High) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = var1High++;
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "high" })
    public void whileLoopField8(int arg1High) {
        int var1Low = mkLow(42);
        while (var1Low < arg1High) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = var1Low++;
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "low" })
    public void whileLoopField13(int arg1Low) {
        int var1High = mkHigh(42);
        while (var1High < arg1Low) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = var1High++;
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "low" })
    public void whileLoopField15(int arg1Low) {
        int var1High = mkHigh(42);
        while (var1High < arg1Low) {
            // @sideeffect("Write effect inside of a stronger branch")
            // @security("The security level of the assigned value is stronger than the security level of the field.")
            lowField = var1High++;
        }
    }

    @ParameterSecurity({ "high" })
    @ReturnSecurity("low")
    public int whileLoopLocal9(int arg1High) {
        int var1High = mkHigh(42);
        while (var1High < arg1High) {
            var1High = var1High++;
        }
        // @security("The returned value has a stronger security level than expected.")
        return var1High;
    }

    @ParameterSecurity({ "high" })
    @ReturnSecurity("low")
    public int whileLoopLocal10(int arg1High) {
        int var1High = mkHigh(42);
        while (var1High < arg1High) {
            var1High = var1High++;
        }
        // @security("The returned value has a stronger security level than expected.")
        return var1High;
    }

    @ParameterSecurity({ "high" })
    @ReturnSecurity("low")
    public int whileLoopLocal11(int arg1High) {
        int var1Low = mkLow(42);
        while (var1Low < arg1High) {
            var1Low = var1Low++;
        }
        // @security("The returned value has a stronger security level than expected.")
        return var1Low;
    }

    @ParameterSecurity({ "high" })
    @ReturnSecurity("low")
    public int whileLoopLocal12(int arg1High) {
        int var1Low = mkLow(42);
        while (var1Low < arg1High) {
            var1Low = var1Low++;
        }
        // @security("The returned value has a stronger security level than expected.")
        return var1Low;
    }

    @ParameterSecurity({ "low" })
    @ReturnSecurity("low")
    public int whileLoopLocal13(int arg1Low) {
        int var1High = mkHigh(42);
        while (var1High < arg1Low) {
            var1High = var1High++;
        }
        // @security("The returned value has a stronger security level than expected.")
        return var1High;
    }

    @ParameterSecurity({ "low" })
    @ReturnSecurity("low")
    public int whileLoopLocal14(int arg1Low) {
        int var1High = mkHigh(42);
        while (var1High < arg1Low) {
            var1High = var1High++;
        }
        // @security("The returned value has a stronger security level than expected.")
        return var1High;
    }

    @WriteEffect({ "high" })
    @ParameterSecurity({ "high", "high" })
    public void forLoopIfField2(int arg1High, boolean arg2High) {
        for (int i = 0; i < arg1High; i++) {
            if (arg2High) {
                // @security("The security level of the ID-function argument should be weaker or equal to the level of the function.")
                highField = mkLow(42);
            }
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "high", "high" })
    public void forLoopIfField3(int arg1High, boolean arg2High) {
        for (int i = 0; i < arg1High; i++) {
            if (arg2High) {
                // @sideeffect("Write effect inside of a stronger branch")
                // @security("The security level of the assigned value is stronger than the security level of the field.")
                lowField = mkHigh(42);
            }
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "high", "high" })
    public void forLoopIfField4(int arg1High, boolean arg2High) {
        for (int i = 0; i < arg1High; i++) {
            if (arg2High) {
                // @sideeffect("Write effect inside of a stronger branch")
                // @security("The security level of the assigned value is stronger than the security level of the field.")
                // @security("The security level of the ID-function argument should be weaker or equal to the level of the function.")
                lowField = mkLow(42);
            }
        }
    }

    @WriteEffect({ "high" })
    @ParameterSecurity({ "high", "low" })
    public void forLoopIfField6(int arg1High, boolean arg2Low) {
        for (int i = 0; i < arg1High; i++) {
            if (arg2Low) {
                // @security("The security level of the ID-function argument should be weaker or equal to the level of the function.")
                highField = mkLow(42);
            }
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "high", "low" })
    public void forLoopIfField7(int arg1High, boolean arg2Low) {
        for (int i = 0; i < arg1High; i++) {
            if (arg2Low) {
                // @sideeffect("Write effect inside of a stronger branch")
                // @security("The security level of the assigned value is stronger than the security level of the field.")
                lowField = mkHigh(42);
            }
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "high", "low" })
    public void forLoopIfField8(int arg1High, boolean arg2Low) {
        for (int i = 0; i < arg1High; i++) {
            if (arg2Low) {
                // @sideeffect("Write effect inside of a stronger branch")
                // @security("The security level of the ID-function argument should be weaker or equal to the level of the function.")
                // @security("The security level of the assigned value is stronger than the security level of the field.")
                lowField = mkLow(42);
            }
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "low", "high" })
    public void forLoopIfField11(int arg1Low, boolean arg2High) {
        for (int i = 0; i < arg1Low; i++) {
            if (arg2High) {
                // @sideeffect("Write effect inside of a stronger branch")
                // @security("The security level of the assigned value is stronger than the security level of the field.")
                lowField = mkHigh(42);
            }
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "low", "high" })
    public void forLoopIfField12(int arg1Low, boolean arg2High) {
        for (int i = 0; i < arg1Low; i++) {
            if (arg2High) {
                // @sideeffect("Write effect inside of a stronger branch")
                // @security("The security level of the ID-function argument should be weaker or equal to the level of the function.")
                // @security("The security level of the assigned value is stronger than the security level of the field.")
                lowField = mkLow(42);
            }
        }
    }

    @WriteEffect({ "high" })
    @ParameterSecurity({ "low", "high" })
    public void forLoopIfField10(int arg1Low, boolean arg2High) {
        for (int i = 0; i < arg1Low; i++) {
            if (arg2High) {
                // @security("The security level of the ID-function argument should be weaker or equal to the level of the function.")
                highField = mkLow(42);
            }
        }
    }

    @WriteEffect({ "low" })
    @ParameterSecurity({ "low", "low" })
    public void forLoopIfField15(int arg1Low, boolean arg2Low) {
        for (int i = 0; i < arg1Low; i++) {
            if (arg2Low) {
                // @security("The security level of the assigned value is stronger than the security level of the field.")
                lowField = mkHigh(42);
            }
        }
    }

    @FieldSecurity("low")
    int lowField = mkLow(42);

    @FieldSecurity("high")
    int highField = mkHigh(42);

    @WriteEffect({ "low", "high" })
    public FailLoop() {
        super();
    }

}
