package junitConstraints;

import static security.Definition.*;

public class FailArrayAssign extends stubs.SpecialArrays {

    public static void main(String[] args) {
    }

    @Constraints({ "@pc <= low" })
    void assignField() {
        // @security("array with high content assigned to array with low content")
        this.lowLow = arrayIntHigh(1);
    }

    @Constraints({ "@pc <= low" })
    void assignArrayCell() {
        // @security("high value assigned to low array cell")
        this.lowLow[0] = mkHigh(42);
    }

}
