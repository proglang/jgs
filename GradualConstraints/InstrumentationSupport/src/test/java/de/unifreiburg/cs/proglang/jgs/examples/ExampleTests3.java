package de.unifreiburg.cs.proglang.jgs.examples;

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.instrumentation.CxTyping;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Methods;
import de.unifreiburg.cs.proglang.jgs.instrumentation.VarTyping;
import org.junit.Test;
import soot.Local;
import soot.jimple.Stmt;

import static org.junit.Assert.assertTrue;

/**
 * Created by Nicolas Müller on 17.01.17.
 */
public class ExampleTests3 {

    /*
      int update_on_bool(bool p0, int p1) {
          int x;
          boolean b;
          0: x := p0;
          1: b := p1;
          2: if not b goto 4;
          3: inc(x);
          4: return x;          // if ist "geschlossen" nach dem INC, return_x ist ein postdominator, von daher ist der
                                // PC wieder public
      }
     */

    // This object contains hypothetical analysis results as examples
    static AnalysisResults3<LowHigh.Level> results = new AnalysisResults3<>();

    @Test
    public void sum_D_D__D() {

        // Assume we get the following objects from the type analysis
        // (we are using the LowHigh lattice)
        Methods<LowHigh.Level> methods = results.up_methods_D_D__D();
        VarTyping<LowHigh.Level> varTyping = results.up_varTyping();
        CxTyping<LowHigh.Level> cxTyping = results.up_cxTyping();

        // first get an instantiation for max WHATS THAT?
        Instantiation<LowHigh.Level> instantiation = methods.getMonomorphicInstantiation(Code.update);         // Instantiation of type D_D__D

        // lets look at the individual statements:
        Stmt s;

        // Let soot create new local variables
        Local x = Code.localX;
        Local b = Code.localB;

        // 0: x := p0;
        // pc is public
        // before: x is public
        // after: x is dynamic
        s = Code.up_0_id_X_p0;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getBefore(instantiation, s, x).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, x).isDynamic());

        // 1: b := p1
        // before: b is public
        // after: b is dynamic
        s = Code.up_1_id_B_p2;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getBefore(instantiation, s, b).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, b).isDynamic());

        // 2: if not b goto 4;
        // pc is dynamic AFTER this statement, but right here it is still public                RIGHT?
        s = Code.up_2_if_not_B;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, x).isDynamic());
        assertTrue(varTyping.getAfter(instantiation, s, b).isDynamic());


        // 3: inc(x);
        // pc is dynamic
        // --> NSU check necessary; b might be high and x might be low
        s = Code.up_3_inc_B;
        assertTrue(cxTyping.get(instantiation, s).isDynamic());
        assertTrue(varTyping.getAfter(instantiation, s, x).isDynamic());
        assertTrue(varTyping.getAfter(instantiation, s, b).isDynamic());

        // 4: return b;
        // no NSU
        // --> PC is again public, da wo die IFs geschlossen werden, wird der "effekt" des if wieder aufgehoben
        s = Code.up_4_return_B;
        assertTrue(cxTyping.get(instantiation, s).isDynamic());
    }

    @Test
    public void sum_P_D__D() {

        // Assume we get the following objects from the type analysis
        // (we are using the LowHigh lattice)
        Methods<LowHigh.Level> methods = results.up_methods_P_D__D();
        VarTyping<LowHigh.Level> varTyping = results.up_varTyping();
        CxTyping<LowHigh.Level> cxTyping = results.up_cxTyping();

        // first get an instantiation for max WHATS THAT?
        Instantiation<LowHigh.Level> instantiation = methods.getMonomorphicInstantiation(Code.update);         // Instantiation of type D_D__D

        // lets look at the individual statements:
        Stmt s;

        // Let soot create new local variables
        Local x = Code.localX;
        Local b = Code.localB;

        // 0: x := p0;
        // pc is public
        // before: x is public
        // after: x is dynamic
        s = Code.up_0_id_X_p0;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getBefore(instantiation, s, x).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, x).isDynamic());

        // 1: b := p1
        // before: b is public
        // after: b is dynamic
        s = Code.up_1_id_B_p2;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getBefore(instantiation, s, b).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, b).isPublic());

        // 2: if not b goto 4;
        // --> pc is not dynamic, but stays public since guard b is public                      RIGHT?
        s = Code.up_2_if_not_B;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, x).isDynamic());
        assertTrue(varTyping.getAfter(instantiation, s, b).isPublic());


        // 3: inc(x);
        // --> no nsu check necessary, no change in respect to line 2,                      RIGHT?
        s = Code.up_3_inc_B;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, x).isDynamic());
        assertTrue(varTyping.getAfter(instantiation, s, b).isPublic());

        // 4: return b;
        // ---> no NSU. Output b would be public, output x would be dynamic. PC stays public the whole time.
        s = Code.up_4_return_B;
        assertTrue(varTyping.getAfter(instantiation, s, x).isDynamic());
    }


    @Test
    public void sum_P_P__P() {

        // Assume we get the following objects from the type analysis
        // (we are using the LowHigh lattice)
        Methods<LowHigh.Level> methods = results.up_methods_P_P__P();
        VarTyping<LowHigh.Level> varTyping = results.up_varTyping();
        CxTyping<LowHigh.Level> cxTyping = results.up_cxTyping();

        // first get an instantiation for max WHATS THAT?
        Instantiation<LowHigh.Level> instantiation = methods.getMonomorphicInstantiation(Code.update);         // Instantiation of type D_D__D

        // lets look at the individual statements:
        Stmt s;

        // Let soot create new local variables
        Local x = Code.localX;
        Local b = Code.localB;

        // 0: x := p0;

        s = Code.up_0_id_X_p0;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getBefore(instantiation, s, x).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, x).isPublic());

        // 1: b := p1

        s = Code.up_1_id_B_p2;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getBefore(instantiation, s, b).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, b).isPublic());

        // 2: if not b goto 4;
        s = Code.up_2_if_not_B;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, x).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, b).isPublic());


        // 3: inc(x);
        s = Code.up_3_inc_B;
        assertTrue(cxTyping.get(instantiation, s).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, x).isPublic());
        assertTrue(varTyping.getAfter(instantiation, s, b).isPublic());

        // 4: return b;
        s = Code.up_4_return_B;
        assertTrue(varTyping.getAfter(instantiation, s, x).isPublic());
    }
}
