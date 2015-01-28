package junitConstraints;

import static security.Definition.*;
import security.Definition.Constraints;

public class FailWhile extends stubs.MinimalFields {

    public static void main(String[] args) {
    }

    @Constraints("@pc <= low")
    public void failWhile1() {
        while (lowIField <= 42) {
            // @security("Illegal flow from high to low")
            lowIField = mkHigh(42);
        }
    }

    @Constraints("@pc <= low")
    public void failWhile2() {
        while (highIField <= 42) {
            // @security("Illegal flow from high to low")
            lowIField = 42;
        }
    }

    @Constraints("@pc <= low")
    public void failWhile3() {
        while (highIField <= 42) {
            // @security("Illegal flow from high to low")
            lowIField = mkHigh(42);
        }
    }

    @Constraints("@pc <= low")
    public void failWhile4() {
        for (int i = 42; i < lowIField; i++) {
            // @security("Illegal flow from high to low")
            lowIField = mkHigh(42);
        }
    }

    @Constraints("@pc <= low")
    public void failWhile5() {
        for (int i = 42; i < highIField; i++) {
            // @security("Illegal flow from high to low")
            lowIField = 42;
        }
    }

    @Constraints("@pc <= low")
    public void failWhile6() {
        for (int i = 42; i < highIField; i++) {
            // @security("Illegal flow from high to low")
            lowIField = mkHigh(42);
        }
    }

    @Constraints("@pc <= low")
    public void failWhile7() {
        while (lowIField <= 42) {
            // @security("Illegal flow from high to low")
            if (lowIField <= 23)
                lowIField = mkHigh(42);
        }
    }

    @Constraints("@pc <= low")
    public void failWhile8() {
        while (lowIField <= 42) {
            // @security("Illegal flow from high to low")
            if (highIField <= 23)
                lowIField = 42;
        }
    }

    @Constraints("@pc <= low")
    public void failWhile9() {
        while (highIField <= 42) {
            // @security("Illegal flow from high to low")
            if (lowIField <= 23)
                lowIField = 42;
        }
    }

    @Constraints("@pc <= low")
    public void failWhile10() {
        while (highIField <= 42) {
            // @security("Illegal flow from high to low")
            if (highIField <= 23)
                lowIField = 42;
        }
    }

    @Constraints("@pc <= low")
    public void failWhile11() {
        while (lowIField <= 42) {
            // @security("Illegal flow from high to low")
            if (highIField <= 23)
                lowIField = mkHigh(42);
        }
    }

    @Constraints("@pc <= low")
    public void failWhile12() {
        while (highIField <= 42) {
            // @security("Illegal flow from high to low")
            if (lowIField <= 23)
                lowIField = mkHigh(42);
        }
    }

    @Constraints("@pc <= low")
    public void failWhile13() {
        while (highIField <= 42) {
            // @security("Illegal flow from high to low")
            if (highIField <= 23)
                lowIField = mkHigh(42);
        }
    }

    @Constraints("@pc <= low")
    public void failWhile14() {
        int i = lowIField;
        while (i <= 42) {
            if (i <= -23)
                i = mkHigh(7);
            // @security("Illegal flow from high to low")
            lowIField = i;
        }
    }

    @Constraints("@pc <= low")
    public void failWhile15() {
        int i = highIField;
        while (i <= 42) {
            if (i <= -23)
                i = 7;
            // @security("Illegal flow from high to low")
            lowIField = i;
        }
    }

    @Constraints("@pc <= low")
    public void failWhile16() {
        int i = highIField;
        while (i <= 42) {
            if (i <= -23)
                i = mkHigh(7);
            // @security("Illegal flow from high to low")
            lowIField = i;
        }
    }

    @Constraints("@pc <= low")
    public void failWhile17() {
        int i = lowIField;
        while (i <= 42) {
            lowIField = i;
            // @security("Illegal flow from high to low")
            if (i <= -23)
                i = mkHigh(7);
        }
    }

    @Constraints("@pc <= low")
    public void failWhile18() {
        int i = highIField;
        while (i <= 42) {
            // @security("Illegal flow from high to low")
            lowIField = i;
            if (i <= -23)
                i = 7;
        }
    }

    @Constraints("@pc <= low")
    public void failWhile19() {
        int i = highIField;
        while (i <= 42) {
            // @security("Illegal flow from high to low")
            lowIField = i;
            if (i <= -23)
                i = mkHigh(7);
        }
    }

    @Constraints("low <= @return")
    public int failWhile20() {
        int result = 42;
        while (highIField <= 23) {
            result = result + 1;
        }
        // @security("weaker return level expected")
        return result;
    }

    @Constraints("@pc <= low")
    public void failWhile21() {
        do {
            // @security("Illegal flow from high to low")
            lowIField = mkHigh(23);
        } while (lowIField <= 42);
    }

    @Constraints("@pc <= low")
    public void failWhile22() {
        do {
            // @security("Illegal flow from high to low")
            lowIField = mkHigh(23);
        } while (highIField <= 42);
    }

    @Constraints("@pc <= low")
    public void failWhile23() {
        do {
            // @security("Illegal flow from high to low")
            lowIField = 23;
        } while (highIField <= 42);
    }

}
