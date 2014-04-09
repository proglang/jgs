package exgradual;


import static security.Definition.*;

public class Example {
    
    static class C {
        // Sec: high
    		@FieldSecurity("high")
        Object h;
        // Sec: low
    		@FieldSecurity("low")
        Object l;
        
        // Constr: h <= high, l <= low
    		@MethodConstraints({
    			"@0 <= high",
    			"@1 <= low" 
    		})
    		@ParameterSecurity({"low", "low"})
        public C(Object h, Object l) {
            this.h = h;
            this.l = l;
        }
        
        // Fixed security signature:
        // Sec: void[Low](Low)
        //      ^     ^    ^
        //   return  pc    parameter
        @ParameterSecurity({"low"})
        public void sendLow(Object o) {
           // ... send o over a low channel. 
        }
        

    }
    
    // Instantiation and assignement, fixed fields.
    // This is exactly as in the current implementation
    public static void fixedFieldsNoMethods() {
        // Sec high
        Object oH = mkHigh(new Object());
        // Sec low
        Object oL = mkLow(new Object());
        
        // that's fine
        C cH = mkHigh(new C (oH, oL));
        // that's fine too, (o:low) <= high)
        C cL = mkLow(new C (oL, oL));
        
        // that's an error: cH:high !<= cl.l:low
        cL.l = cH;
    }
    
    
    static class D {
       // Sec: low
    	 @FieldSecurity("low")
       Object l; 
       
       // note: no annotation
       // i1 <= low, ret = i2
    	 @MethodConstraints({
    		 "@0 <= low",
    		 "@return = @0"
    	 })
    	 @ParameterSecurity({"low", "low"})
       @ReturnSecurity("low")
       public C m(C i1, C i2) {
          l = i1;
          return i2;
       }
    }
    
    public static void methodCall() {
        // as before:
        Object oH = mkHigh(new Object());
        Object oL = mkLow(new Object());
        C cH = mkHigh(new C (oH, oL));
        C cL = mkLow(new C (oL, oL));
        
        // make a D
        D d = mkLow(new D());
        
        // ok, as the first argument is low
        C c1 = d.m(cL, cH); // c1 is now high
        
        // error: c1 is high
        C c2 = d.m(c1, cL);
        
        // ok
        C c3 = d.m(cL, cL); // c3 is low
    }
    
    static class DRec {
       // Sec: low
    	 @FieldSecurity("low")
       Object l; 
       
       // note: no annotation
    	 @ParameterSecurity({"low", "low", "low"})
       @ReturnSecurity("low")
       public C m(C i1, C i2, boolean b) {
          return m1(i2, i1, !b);
       }
       
       @ParameterSecurity({"low", "low", "low"})
       @ReturnSecurity("low")
       public C m1(C i1, C i2, boolean b) {
           if (b) {
               return m(i1, i2, !b);
           } else {
               return i1;
           }
       }
    }
    
    public static void recursiveCall() {
        // as before:
        Object oH = mkHigh(new Object());
        Object oL = mkLow(new Object());
        C cH = mkHigh(new C (oH, oL));
        C cL = mkLow(new C (oL, oL));
        
        // make a D
        DRec d = mkLow(new DRec());
        // ok
        d.m(cL, cL, true);
        
        // ok, result is h, m takes two high arguments
        d.m(cL, cH, false);
        
    }
    
    // Other ideas:
    
    // special join method (like +): the formula is evaluated
    // not strictly needed but good for examples
    // Sec: join c1 c2
    @ParameterSecurity({"low", "low"})
    @ReturnSecurity("low")
    static C combine (C c1, C c2) {
        //...
        return null;
    }
    
    @ParameterSecurity("low")
    public static void main(String[] args) {
			
		}
    
    //special constructor annotation that let the result security level be a join of the constructor's arguments
    // e.g. new Integer(iLow + iHigh) should be high automatically

}
