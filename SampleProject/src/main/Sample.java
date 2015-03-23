package main;

import static security.Definition.*;
import security.Definition.Constraints;
import security.Definition.FieldSecurity;
import security.Definition.ParameterSecurity;
import security.Definition.ReturnSecurity;

public class Sample {
	
    @FieldSecurity("high")
    public int highField;
    
    @FieldSecurity("low")
    public int lowField;
    
    @Constraints({"@pc <= low"})
    public Sample(){}
    
    @ParameterSecurity({ "low" })
    public static void main(String[] args) {
    }

    @Constraints({"@pc <= low"})
    public void successAssign() {
        highField  = 42;
        lowField = 42;
    }
    
    @Constraints({ "high <= @return" })
    public int returnExpr1() {
        int High = mkHigh(42);
        return High;
    }
    
    // @Constraints("@pc <= low")
    public void successWhile() {
        while (lowField <= 42) {
            lowField = 42;
        }
    }
    
    /*
     * This part leads to illegal flows
     */
    
    @Constraints("@pc <= low")
    public void highToLow() {
    	lowField = mkHigh(22);
    }
    
    @Constraints("@pc <= low")
    public void failWhile() {
        while (highField <= 42) {
        	highField = 42;
            lowField = 42;
        }
    }
    
}
