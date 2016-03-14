package pkg;

import de.unifreiburg.cs.proglang.jgs.support.*;

/*
  We are working with the classic 2-point security lattice:
  
  LOW <= HIGH
  
  In JGS, SECURITY TYPES the elements of the lattice plus
   - the public type "pub"
   - the dynamic type "?"
   
  Information flows valid if "move up" in the following semi-lattice:
  
      
          HIGH
           |
     ?    LOW
      \   /
       pub 

   Now, let us look at a JGS class
 */
public class MethodBodies {

    //The security type of fields are given by the @Sec annotation
    @Sec("LOW")
    int lowField;

    @Sec("HIGH")
    int highField;
    @Sec("?")
    int dynamicField;

    //Omitting a security type yields a public field
    int publicField;

    // same type as
    @Sec("pub")
    int alsoPublicField;

    Object somePublicObject;
    
    String someString;
    
    /*
      Thus, fields are sources and sinks of fixed security types. 
      Java library method can be further sources and sinks, e.g.

      IO.prinln(int) is defined as a LOW sink 

      (I have some problems getting System.out.println to work directly)
     */

    // OK: Our first method. It has a LOW effect, as it writes to a LOW sink
    @Effects({"LOW"})
    void OK_printPublicField() {
        IO.printInt(publicField);
    }

    // OK: Similarly we can write to a low field announce the
    // corresponding effect
    @Effects({"LOW"})
    void OK_zeroOutLowField() {
        this.lowField = 0;
    }

    // ERROR: Omitting such an annotation yields an error.
    void ERROR_zeroOutLowField_quietly() {
        this.lowField = 0;
    }

    // ERROR: Also writing a high securiy value to a low field directly yields an error
    @Effects({"LOW"})
    void ERROR_leakyCopy() {
         this.lowField = this.highField;
    }

    // ERROR: explicit illegal flow through fields using variables
    void ERROR_explicitWithVars() {
        int x = this.highField;
        int y = x + 1;
        this.lowField = y;
    }

    // OK: type public may flow into type dynamic
    @Effects({"?"})
    void OK_publicToDynamic() {
        this.dynamicField = this.publicField;
    }

    // OK: This assignment uses a casts (HIGH ~> ?) to copy highField into dynamicField.
    @Effects({"?"})
    void OK_castHighToDynamic(String s) {
        this.dynamicField = Casts.castHighToDyn(this.highField);
    }

    // OK: Using casts we can produce a leak. Only run-time enforcement is able to prevent it.
    @Effects({"LOW"})
    void OK_dynamicLeak() {
        this.lowField = Casts.castDynToLow(Casts.castHighToDyn(this.highField));
    }

    // OK: Similar leak using context casts
    //  ATTENTION:
    //  - due to a shorcoming of the implementation, implicit conversions can be problematic inside context casts.
    //  - thus the use of "Integer" for dynH
    @Effects({"?"})
    void OK_dynamicLeakWithContextCast() {
        Integer dynH = Casts.castHighToDyn(this.highField);
        if (dynH == 42) {
            Casts.castCxDynToLow();
            this.lowField = Casts.castDynToLow(dynH);
            Casts.castCxEnd();
        }
    }


    // OK: a pure method with no effect where output depends on input.
    @Constraints({"@0 <= @ret"})
    int OK_plusOne(int i) {
        return i + 1;
    }

    // ERROR: a method with insufficient constraints
    @Constraints({"@0 <= @ret"})
    int ERROR_explicit(int i, int j) {
        return i + j;
    }

    // ERROR: this method tries to hide implicit flows.
    //  this signature would make it right
    // @Constraints({"@0 <= @ret", "@1 <= @ret"})
    int ERROR_implicit(int i, int j) {
        int z = 0;
        if (i == 0 && j == 0) {
            return z;
        }
        if (i == 1) {
            z += 10;
        }
        if (j == 1) {
            z += 1;
        }
        return z;
    }

    // OK: just making the result dynamic
    @Constraints({"? <= @ret"})
    int OK_returnDynWithCast() {
        return Casts.castHighToDyn(this.highField);
    }

    // ERROR: but it is not allowed to downgrade first
    @Constraints({"? <= @ret"})
    int ERROR_returnDynWithCast_illegal() {
        return Casts.castLowToDyn(this.highField);
    }

    // OK: returning from a context casts
    @Constraints({"? <= @ret"})
    @Effects({"HIGH"})
    int OK_returnFromDynCastCx() {
        int result;
        if (this.highField == 1) {
            Casts.castCxHighToDyn();
            result = 1;
            Casts.castCxEnd();
        } else {
            Casts.castCxHighToDyn();
            result = 0;
            Casts.castCxEnd();
        }
        return result;
    }

    // OK: same as above
    @Constraints({"? <= @ret"})
    @Effects({"HIGH"})
    int OK_returnFromDynCastCx_shorter() {
        int result = 0;
        if (this.highField == 1) {
            Casts.castCxHighToDyn();
            result = 1;
            Casts.castCxEnd();
        }
        return result;
    }

    // ERROR: trying something like above, but with a wrong cast
    @Constraints({"? <= @ret"})
    @Effects({"HIGH"})
    int ERROR_returnFromDynCastCx_illegalInIf() {
        int result;
        if (this.highField == 1) {
            Casts.castCxLowToDyn();
            result = 1;
            Casts.castCxEnd();
        } else {
            Casts.castCxHighToDyn();
            result = 0;
            Casts.castCxEnd();
        }
        return result;
    }

    // ERROR: typing conflicts at a join point
    @Constraints({"? <= @ret"})
    @Effects({"HIGH"})
    int ERROR_returnFromDynCastCx_illegalInJoin() {
        int result = this.highField;
        if (this.highField == 1) {
            Casts.castCxHighToDyn();
            result = 1;
            Casts.castCxEnd();
        }
        return result;
    }

}
