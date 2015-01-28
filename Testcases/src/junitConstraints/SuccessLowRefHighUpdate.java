package junitConstraints;

// Quick throw-away examples and testcases for the security type checker
import static security.Definition.*;

public class SuccessLowRefHighUpdate {

    @FieldSecurity("high")
    boolean high;
    @FieldSecurity("low")
    boolean f;

    @Constraints({ "@return <= low" })
    public boolean noleak(boolean init) {
        SuccessLowRefHighUpdate x = new SuccessLowRefHighUpdate();
        SuccessLowRefHighUpdate y = new SuccessLowRefHighUpdate();
        x.high = init;
        y.high = !x.high;
        // this is not a leak.
        return x == y;
    }

    public static void main(String[] args) {
    }
}
