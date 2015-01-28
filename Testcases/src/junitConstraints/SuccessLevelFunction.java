package junitConstraints;

import static security.Definition.*;
import security.Definition.Constraints;

public class SuccessLevelFunction {

    public static void main(String[] args) {
    }

    @Constraints({ "@0 <= low" })
    public void successLevelFunction1(int i) {
        mkLow(i);
        return;
    }

    @Constraints({ "low <= @0" })
    public void successLevelFunction2(int i) {
        mkHigh(i);
        return;
    }

    @Constraints({ "high <= @0" })
    public void successLevelFunction3(int i) {
        mkHigh(i);
        return;
    }

    @Constraints({ "high <= @return" })
    public int successLevelFunction4() {
        int high = mkHigh(42);
        return high;
    }

    @Constraints({ "high <= @return" })
    public int successLevelFunction5() {
        int low = mkLow(42);
        return low;
    }

    @Constraints({ "low <= @return" })
    public int successLevelFunction6() {
        int low = mkLow(42);
        return low;
    }

    @Constraints({ "high <= @return", "high <= @0" })
    public int successLevelFunction7(int i) {
        int high = mkHigh(i);
        return high;
    }

}
