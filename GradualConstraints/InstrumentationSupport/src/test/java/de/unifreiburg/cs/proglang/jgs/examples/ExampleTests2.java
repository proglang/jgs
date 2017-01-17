package de.unifreiburg.cs.proglang.jgs.examples;

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.instrumentation.CxTyping;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Methods;
import de.unifreiburg.cs.proglang.jgs.instrumentation.VarTyping;
import org.junit.Test;
import soot.Local;
import soot.jimple.Expr;
import soot.jimple.Stmt;

import static de.unifreiburg.cs.proglang.jgs.examples.ExampleTests.results;
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



    @Test
    public void sum_D_D__D() {

        // Assume we get the following objects from the type analysis
        // (we are using the LowHigh lattice)
        Methods<LowHigh.Level> methods = results.max_methods_D_D__D(); // WHATS THAT?
        VarTyping<LowHigh.Level> varTyping = results.max_varTyping();  // für jedes statement und pc steht drin, ob dyn oder stat überprüft werden soll
        // CxTyping<LowHigh.Level> cxTyping = results.sum_cxTyping();     // WHATS THAT? The level of the PC for each statement?! why the weird name?

        // first get an instantiation for max WHATS THAT?
        Instantiation<LowHigh.Level> instantiation = methods.getMonomorphicInstantiation(Code.max);

        // lets look at the individual statements:
        Stmt s;

        // Let soot create new local variables
        Local x = Code.localX;
        Local y = Code.localY;
        Local z = Code.localZ;

        // x := x + y
        // PC is public
        // before: x, y are DYN
        // after: z is DYN
        // --> update z's label with the join of x and y. Do not perform NSU check
        s = Code.add_2_Z__ADD_X_Y;
        // assertTrue(cxTyping.get(instantiation, s).isPublic());
        //assertTrue(varTyping.getBefore(instantiation, s, y).isDynamic());
        //assertTrue(varTyping.getBefore(instantiation, s, z).isDynamic());

    }

}
