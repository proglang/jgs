package junitConstraints;

import static security.Definition.*;

public class FailLevelFunction {

    public static void main(String[] args) {
    }

    @Constraints({ "high <= @0" })
    public void failLevelFunction1(int i) {
        // @security("Argument has stronger constraints")
        mkLow(i);
        // @security("Inconsistent constraint set, because of previous invalidity")
        return;
    }

    @Constraints({ "@return <= low" })
    public int failLevelFunction2() {
        int high = mkHigh(42);
        // @security("The returned value has a stronger security level than expected.")
        // @security("double subsign & inequality")
        return high;
    }

}
