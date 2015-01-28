package junitConstraints;

import static security.Definition.*;
import security.Definition.FieldSecurity;

public class SuccessSpecial03 {

    @Constraints("@pc <= low")
    private static class InnerClass {

        @FieldSecurity("low")
        public static int low = 10;

        @FieldSecurity("high")
        public static int high = 1;

    }

    public static void main(String[] args) {
    }

    // pcs entfernen nach inequality, stack initialisieren mit Sig
    @Constraints("@pc <= low")
    public void successSpecial1() {
        InnerClass.low = 5;
        if (mkHigh(true)) {
            InnerClass.high = 5;
        }
    }

}
