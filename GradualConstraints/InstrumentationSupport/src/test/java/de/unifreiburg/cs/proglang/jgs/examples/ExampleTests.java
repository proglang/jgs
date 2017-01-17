package de.unifreiburg.cs.proglang.jgs.examples;

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.instrumentation.CxTyping;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Methods;
import de.unifreiburg.cs.proglang.jgs.instrumentation.VarTyping;
import org.junit.Test;
import soot.Local;
import soot.jimple.Stmt;

import static org.junit.Assert.*;

/**
 * These unit tests should serve as an example for the use of the
 * instrumentation interfaces.
 */
public class ExampleTests {

    // This object contains hypothetical analysis results as examples
    static AnalysisResults<LowHigh.Level> results = new AnalysisResults<>();

    /*
     * Method max, as defined in examples.Code
     */

    /*
      int max(int p0, int p1) {
        int x;
        int y;
        int z;
       01: x := p0;
       02: y := p1;
        1: z = x;
        2: if (z < y) goto 3;
       22:   goto 4;
        3:   z = y;
        4: return z;
      }
     */

    /*
     * This is a test of method max where the parameters and return types are
      * all typed as dynamic.
     */
    @Test
    public void test_max_D_D__D() {
        // Assume we get the following objects from the type analysis
        // (we are using the LowHigh lattice)
        Methods<LowHigh.Level> methods = results.max_methods_D_D__D();
        VarTyping<LowHigh.Level> varTyping = results.max_varTyping(); // für jedes statement und pc steht drin, ob dyn oder stat überprüft werden soll
        CxTyping<LowHigh.Level> cxTyping = results.max_cxTyping();

        // first get an instantiation for max
        // instantiation is a Soot object of type SootMethod. It is created in Code.java, and contains all the soot Statements the max programm
        // consits of.
        Instantiation<LowHigh.Level> instantiation = methods.getMonomorphicInstantiation(Code.max);

        // lets look at the individual statements:
        Stmt s;
        // we have the variables x,y,z to care about
        Local x = Code.localX;
        Local y = Code.localY;
        Local z = Code.localZ;

        // max_1_assign_Z_X: z = x
        //   pc is public
        //   before: x is dynamic,
        //   after: z is dynamic
        //   --> update z's label with x, no NSU check required
        //   (things after "-->" is what the instrumentation should do in this case)
        s = Code.max_2_assign_Z_X;
        assertTrue(cxTyping.get(instantiation, s).isPublic());  // get statement s in instantiation "instantiation"
        assertTrue(varTyping.getBefore(instantiation, s, x).isDynamic());
        assertTrue(varTyping.getAfter(instantiation, s, z).isDynamic());

        // max_2_if_Z_lt_Y : if (z<y) goto 3;
        //   pc is public,
        //   before: y is dynamic, z is dynamic
        //   --> update the pc label with join(y, z) for the body of the if
        s = Code.max_3_if_Z_lt_Y;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getBefore(instantiation, s, y).isDynamic());
        assertTrue(varTyping.getBefore(instantiation, s, z).isDynamic());

        // max_3_assign_Z_Y : z = y
        //   pc is dynamic
        //   before: y is dynamic
        //   after: z is dynamic
        //   --> update z's label with join(y, pc), do NSU check
        s = Code.max_5_assign_Z_Y;
        assertTrue(cxTyping.get(instantiation, s).isDynamic());
        assertTrue(varTyping.getBefore(instantiation, s, y).isDynamic());
        assertTrue(varTyping.getAfter(instantiation, s, z).isDynamic());

    }

    @Test
    /*
     * Now the second parameter of max is public
     */
    public void test_max_D_P__D() {
        Methods<LowHigh.Level> methods = results.max_methods_D_P__D();
        VarTyping<LowHigh.Level> varTyping = results.max_varTyping();
        CxTyping<LowHigh.Level> cxTyping = results.max_cxTyping();

        Instantiation<LowHigh.Level> instantiation = methods.getMonomorphicInstantiation(Code.max);

        Stmt s;
        Local x = Code.localX;
        Local y = Code.localY;
        Local z = Code.localZ;

        // max_1_assign_Z_X: z = x
        //   pc is public
        //   before: x is dynamic,
        //   after: z is dynamic
        //   --> update z's label with x, no NSU check required
        //   (same as before)
        s = Code.max_2_assign_Z_X;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getBefore(instantiation, s, x).isDynamic());
        assertTrue(varTyping.getAfter(instantiation, s, z).isDynamic());

        // max_2_if_Z_lt_Y : if (z<p1) goto 3;
        //   pc is public,
        //   before: p1 is public, z is dynamic
        //   --> update the pc label with z for the body of the if (no need to consider p1
        s = Code.max_3_if_Z_lt_Y;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getBefore(instantiation, s, y).isPublic());
        assertTrue(varTyping.getBefore(instantiation, s, z).isDynamic());

        // max_3_assign_Z_Y : z = p1
        //   pc is dynamic
        //   before: p1 is public
        //   after: z is dynamic
        //   --> update z's label with pc, do NSU check (no need to consider p1)
        s = Code.max_5_assign_Z_Y;
        assertTrue(cxTyping.get(instantiation, s).isDynamic());
        assertTrue(varTyping.getBefore(instantiation, s, y).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, z).isDynamic());
    }
}
