package de.unifreiburg.cs.proglang.jgs.examples;

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Methods;
import de.unifreiburg.cs.proglang.jgs.instrumentation.VarTyping;
import de.unifreiburg.cs.proglang.jgs.instrumentation.CxTyping;
import org.junit.Test;
import soot.Local;
import soot.jimple.Stmt;

import static org.junit.Assert.assertTrue;


/**
 * Created by Nicolas Müller on 16.01.17.
 */
public class ExampleTests2 {


    /*
      int sum( int p0, int p1) {
          int x;
          int y;
          int z;
          0: x := p0;
          1: y := p1;
          2: z := x + y;
          3: return z;
      }
     */

    // This object contains hypothetical analysis results as examples
    static AnalysisResults2<LowHigh.Level> results = new AnalysisResults2<>();

    @Test
    public void sum_D_D__D() {

        // Assume we get the following objects from the type analysis
        // (we are using the LowHigh lattice)
        Methods<LowHigh.Level> methods = results.sum_methods_D_D__D(); // this is probably a list of methods
        VarTyping<LowHigh.Level> varTyping = results.sum_varTyping();  // für jedes statement und pc steht drin, ob dyn oder stat überprüft werden soll
        CxTyping<LowHigh.Level> cxTyping = results.sum_cxTyping();     // WHATS THAT? The level of the PC for each statement?! why the weird name?

        // first get an instantiation for max WHATS THAT?
        Instantiation<LowHigh.Level> instantiation = methods.getMonomorphicInstantiation(Code.sum);         // Instantiation of type D_D__D

        // lets look at the individual statements:
        Stmt s;

        // Let soot create new local variables
        Local x = Code.localX;
        Local y = Code.localY;
        Local z = Code.localZ;

        // x := p0
        // before x is public, afterwards it's dynamic. Pc is public. No NSU
        s = Code.max_0_id_X_p0;
        assertTrue(varTyping.getBefore(instantiation, s, x).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, x).isDynamic());


        // x := x + y
        // PC is public
        // before: x, y are DYN
        // after: z is DYN
        // --> update z's label with the join of x and y. Do not perform NSU check
        s = Code.add_2_assign_Z_SUM_X_Y;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getBefore(instantiation, s, x).isDynamic());
        assertTrue(varTyping.getBefore(instantiation, s, y).isDynamic());
        assertTrue(varTyping.getBefore(instantiation, s, z).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, z).isDynamic());
        assertTrue(!varTyping.getAfter(instantiation, s, z).isPublic());
    }


    @Test
    public void sum_P_D__D() {

        // Assume we get the following objects from the type analysis
        // (we are using the LowHigh lattice)
        Methods<LowHigh.Level> methods = results.sum_methods_P_D__D(); // this is probably a list of methods
        VarTyping<LowHigh.Level> varTyping = results.sum_varTyping();  // für jedes statement und pc steht drin, ob dyn oder stat überprüft werden soll
        CxTyping<LowHigh.Level> cxTyping = results.sum_cxTyping();     // WHATS THAT? The level of the PC for each statement?! why the weird name?

        // first get an instantiation for max WHATS THAT?
        Instantiation<LowHigh.Level> instantiation = methods.getMonomorphicInstantiation(Code.sum);         // Instantiation of type D_D__D

        // lets look at the individual statements:
        Stmt s;

        // Let soot create new local variables
        Local x = Code.localX;
        Local y = Code.localY;
        Local z = Code.localZ;

        // x := p0
        // before x is public, afterwards it's dynamic. Pc is public. No NSU
        s = Code.max_0_id_X_p0;
        assertTrue(varTyping.getBefore(instantiation, s, x).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, x).isPublic());

        assertTrue(!varTyping.getAfter(instantiation, s, x).isDynamic());


        // x := x + y
        // PC is public
        // before: x, y are DYN
        // after: z is DYN
        // --> update z's label with the join of x and y. Do not perform NSU check
        s = Code.add_2_assign_Z_SUM_X_Y;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getBefore(instantiation, s, x).isPublic());
        assertTrue(varTyping.getBefore(instantiation, s, y).isDynamic());
        assertTrue(varTyping.getBefore(instantiation, s, z).isPublic());

        assertTrue(varTyping.getAfter(instantiation, s, z).isDynamic());
        assertTrue(!varTyping.getAfter(instantiation, s, z).isPublic());
    }
}
