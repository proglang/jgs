package junitLevel;

import static security.Definition.*;

public class FailIdFunctions {

    @ParameterSecurity({ "low" })
    public static void main(String[] args) {
    }

    @ReturnSecurity("low")
    public int returnLowSecurity() {
        int high = mkHigh(42);
        // @security("The returned value has a stronger security level than expected.")
        return high;
    }

    public void changeSecurityHigh2Low() {
        int high = mkHigh(42);
        // @security("The security level of the ID-function argument should be weaker or equal to the level of the function.")
        mkLow(high);
        return;
    }

}