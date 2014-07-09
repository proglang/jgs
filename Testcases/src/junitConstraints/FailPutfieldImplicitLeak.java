package junitConstraints;

// Quick throw-away examples and testcases for the security type checker
import static security.Definition.*;

public class FailPutfieldImplicitLeak {
    
    @FieldSecurity("high")
    boolean high;
    @FieldSecurity("low")
    boolean f;

    @Constraints({"@pc <= low"})
    public boolean leak(boolean init) {
        this.high = init;
        FailPutfieldImplicitLeak x = new FailPutfieldImplicitLeak();
        x.f = true;
        FailPutfieldImplicitLeak y = x;
        if (this.high) {
            y = new FailPutfieldImplicitLeak();
        }
        y.f = false;
        // @security("The returned value has a stronger security level than expected.")
        return x.f;
    }
   
    @Constraints({"@pc <= low"})
    public static void main(String[] args) {
        FailPutfieldImplicitLeak s = new FailPutfieldImplicitLeak();
        boolean result1 = s.leak(true);
        boolean result2 = s.leak(false);
        System.out.print(String.format("%s %s", result1, result2));
        if (result1 != result2) {
            System.out.println(": There was a leak!");
        } else {
            System.out.println(": The program is secure!");
        }
    }
}
