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
public class Intro {

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

    // Our first method. It has a LOW effect, as it writes to a LOW sink
    @Effects({"LOW"})
    void printPublicField() {
        IO.printInt(publicField);
    }

    void storeSomeInt() {
        highField = IO.someInt;
    }

    void storeSomeObject() {
	if (highField == 0) {
	    someString = IO.someString;
	}
    }

    // Similarly we can write to a low field announce the
    // corresponding effect
    @Effects({"pub"})
    void zeroOutLowField() {
        this.lowField = 0;
        this.dynamicField = 0;
    }

    // Omitting such an annotation yields an error
    void zeroOutLowField_quietly() {
        this.lowField = 0;
    }

    // Also writing a high securiy value to a low field directly
    @Effects({"LOW"})
    void leakyCopy() {
        // this.lowField = this.highField;
        IO.printInt(this.highField);
    }

    // type dynamic 
    @Effects({"?"})
    void publicToDynamic() {
        this.dynamicField = this.publicField;
    }

    // this is a casts
    @Effects({"?"})
    void castHighToDynamic(String s) {
        this.dynamicField = Casts.castHighToDyn(this.highField);
        // this.dynamicField = Casts.cast("H ~> ?", this.highField);
    }


    @Constraints({"@0 <= @ret"})
    int writeInt(int i) {
        return i + 1;
        // this.dynamicField = Casts.castHighToDyn(i);
    }

    // writeInt_?
    // writeInt_STATIC { LOW <= @0 }
    //  .. writeInt_LOW
    //  .. writeInt_HIGH
    //   ... 

    // a leak with casts
    @Effects({"LOW"})
    void dynamicLeak() {
        this.lowField = Casts.castDynToLow(Casts.castHighToDyn(this.highField));
    }

    // constraints
    @Constraints({"@0 <= @ret"})
    int explicit(int i, int j) {
        return i + j;
    }

    int implicit(int i, int j) {
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

    // explicit flow through fields using variables
    void explicitWithVars() {
        int x = this.highField;
        int y = x + 1;
        this.lowField = y;
    }

    // indirect flows involving fields
    void implicitFields() {
        if (this.highField == 1) {
            this.lowField = 1;
        }
    }


}
