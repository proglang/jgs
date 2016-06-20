package pkg;

import de.unifreiburg.cs.proglang.jgs.support.*;

/*
  We are working with the popular bottom <= {Alice,Bob,Charlie} <= top security domain.

  Most cases are adapted from "Testclasses/MethodBodies"

 */
public class PersonalDomain {

    @Sec("alice")
    int aliceField;

    @Sec("bob")
    int bobField;

    @Sec("charlie")
    int charlieField;

    @Sec("top")
    int topField;
    @Sec("bottom")
    int bottomField;

    @Sec("?")
    int dynamicField;

    //Omitting a security type yields a public field
    int publicField;

    // same type as
    @Sec("pub")
    int alsoPublicField;

    Object somePublicObject;
    
    String someString;
    
    // OK: Our first method. It has a LOW effect, as it writes to a LOW sink
    @Effects({"bottom"})
    void OK_printPublicField() {
        IO.printInt(publicField);
    }

    // OK: Similarly we can write to a low field announce the
    // corresponding effect
    @Effects({"bottom"})
    void OK_zeroOutLowField() {
        this.bottomField = 0;
    }

    // ERROR: Omitting such an annotation yields an error.
    void ERROR_zeroOutLowField_quietly() {
        this.bottomField = 0;
    }

    // ERROR: Also writing a top securiy value to a bottom field directly yields an error
    @Effects({"bottom"})
    void ERROR_leakyCopy() {
         this.bottomField = this.topField;
    }

    @Effects({"alice"})
    void ERROR_leakyCopy2() {
        this.aliceField = this.bobField;
    }

    // ERROR: explicit illegal flow through fields using variables
    @Effects({"bob"})
    void ERROR_explicitWithVars() {
        int x = this.aliceField;
        int y = x + 1;
        this.bobField = y;
    }

/*
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


*/
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
        return Casts.cast("alice ~> ?", this.aliceField);
    }


    // ERROR: but it is not allowed to downgrade first
    @Constraints({"? <= @ret"})
    int ERROR_returnDynWithCast_illegal() {
        return Casts.cast("bottom ~> ?", this.aliceField);
    }

    // OK: returning from a context casts
    @Constraints({"? <= @ret"})
    @Effects({"top"})
    int OK_returnFromDynCastCx() {
        int result;
        if (this.topField == 1) {
            Casts.castCx("top ~> ?");
            result = 1;
            Casts.castCxEnd();
        } else {
            Casts.castCx("top ~> ?");
            result = 0;
            Casts.castCxEnd();
        }
        return result;
    }

    // OK: same as above
    @Constraints({"? <= @ret"})
    @Effects({"top"})
    int OK_returnFromDynCastCx_shorter() {
        int result = 0;
        if (this.topField == 1) {
            Casts.castCx("top ~> ?");
            result = 1;
            Casts.castCxEnd();
        }
        return result;
    }

    // ERROR: trying something like above, but with a wrong cast
    @Constraints({"? <= @ret"})
    @Effects({"top"})
    int ERROR_returnFromDynCastCx_illegalInIf() {
        int result;
        if (this.topField == 1) {
            Casts.castCx("");
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
    @Effects({"top"})
    int ERROR_returnFromDynCastCx_illegalInJoin() {
        int result = this.topField;
        if (this.topField == 1) {
            Casts.castCx("top ~~> ?");
            result = 1;
            Casts.castCxEnd();
        }
        return result;
    }
}
