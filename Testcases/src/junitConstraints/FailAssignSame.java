package junitConstraints;

// Quick throw-away examples and testcases for the security type checker
import static security.Definition.*;

public class FailAssignSame {
    
    public int assignSame() {
        int x = mkHigh(42);
        x = x;
		// @security("The returned value has a stronger security level than expected.")
        return x;
    }
    
    public static void main(String[] args) {
        
    }
}
