package pkg;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class ScratchMonomorphic {

    @Constraints({"LOW <= @0"})
    public static void main(String[] args) {

    }

int answer = 42;

@Sec("HIGH")
    int high = 123;

@Sec("HIGH")
    Object highObject = new Object();


static int aStatic = 0;

    // dynamisch: ? <= @0
    @Constraints({"? <= @0", "@ret <= ?"})
    @Effects({})
    static String aStaticMethod2(String s) {
        String x = s;
        //String x = Casts.cast("HIGH ~> ?", s);
        if (x == null) {
            x = "hi";
            //return Casts.cast("? ~> LOW", x);
            return "hi";
        } else {
            return null;
        }
    }

// dynamisch: ? <= @0
@Constraints({"LOW <= @0", "@ret <= LOW"})
@Effects({})
static String aStaticMethod(String s) {
    String x = s;
    //String x = Casts.cast("HIGH ~> ?", s);
    if (x == null) {
       //return Casts.cast("? ~> LOW", x);
        return "hi";
     } else {
        return null;
        }
  }

@Constraints({"pub <= @0", "? <= @1", "@0 <= @ret", "@1 <= @ret"})
public int methodWithImplicitFlows(boolean i, boolean j) {
    int z = 0;
    if (i) {
        z += 10;
    }
    if (j) {
        z += 1;
    }
    return z;
}

}
