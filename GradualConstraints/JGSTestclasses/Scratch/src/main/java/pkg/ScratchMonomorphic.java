package pkg;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class ScratchMonomorphic {

    public static void main(String[] args) {

    }

int answer = 42;

@Sec("HIGH")
    int high = 123;

@Sec("HIGH")
    Object highObject = new Object();


static int aStatic = 0;

@Constraints({"LOW <= @0 ", "@0 <= @ret"})
@Effects({})
static int aStaticMethod(int i) {
    if (i == 0) {
       return 1;
     } else {
        return 0;
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
