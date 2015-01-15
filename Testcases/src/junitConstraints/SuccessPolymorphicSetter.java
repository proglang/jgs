package junitConstraints;

// Quick throw-away examples and testcases for the security type checker
import static security.Definition.*;

public class SuccessPolymorphicSetter {
    
    @FieldSecurity("high")
    boolean high;
    @FieldSecurity("low")
    boolean f;

    // this method is completly polymorphic; it should type-check without constraints.
    public SuccessPolymorphicSetter noleak(boolean init) {
        this.high = init;
        return this;
    }
   
    public static void main(String[] args) {
    }
}
