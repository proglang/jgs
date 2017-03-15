package pkg;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class IntrumentationExamples {

    /*
        The annotation @Sec specified the security type of fields. During instrumentation, the field type can be accessed through the @{code FieldTyping} interface.
     */
    @Sec("LOW")
    int lowField; // This is a static field. It does not require instrumentation.

    @Sec("HIGH")
    int highField; // another static field.

    @Sec("?")
    int dynField; // This is a dynamic field. It requires instrumentation.

    @Sec("pub")
    int pubField; // This is a public field. It requires no instrumentation but can be involved in dynamic computations, even w/o a cast.


    /*
        The annotation @Constraints specifies the security type for method paramters and return values. During instrumentation, these types can be accessed through the {@code Instantiation} inteface.
     */
    @Constraints({"LOW <= @1", "HIGH <= @2", "HIGH <= @ret"})
    int add(int x, int y) { // a static method that requires no instrumentation.
        return x + y;
    }

    @Constraints({"? <= @1", "? <= @2", "? <= @ret"})
    int addDynamic(int x, int y) { // a dynamic method that requires instrumentation everywhere
        return x + y;
    }

    /*
       In method bodies, security types of local variables and contexts are inferred, that is they are not specified by annotations.
       The interfaces {@code VarTyping} and {@code CxTyping} provide the type information for variable and program counters during instrumentation.
     */
    @Constraints({"? <= @1", "HIGH <= @2", "? <= @ret"})
    int returnFirst(int x, int y) {
        int y2 = y + y; // y2 has static type HIGH; does not need to be instrumented.
        int x2 = x * x; // x2 has dynamic type; it does need instrumentation
        // the context here has type "pub"; it does not need instrumentation. In the update of x2, it should be considered LOW (or ignored)
        int z;
        if (x == 42) { // now the context becomes dynamic. It needs to be instrumented.
            z = 42;    // z has dynamic type. It needs to be instrumented
        } else {
            z = 0;
        }
        return z;
    }

    // TODO: effects
    // TODO: casts


}
