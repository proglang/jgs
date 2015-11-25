package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.Constraint;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import org.junit.Before;
import org.junit.Test;
import soot.Unit;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;

import java.util.HashSet;
import java.util.Set;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.jimpleutils.Graphs.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.*;


/**
 * Created by fennell on 11/16/15.
 */
public class MethodBodyTypingTest {


    private MethodBodyTyping<Level> mbTyping;
    private Jimple j;
    private TypeVars tvars;
    private Code code;
    private TypeVars.TypeVar pc;

    @Before
    public void setUp() {
        this.tvars = new TypeVars("v");
        this.j = Jimple.v();
        this.pc = tvars.fresh();
        this.code = new Code(tvars);
        this.mbTyping = mkMbTyping(code.init, tvars);
    }

    @Test(timeout = 1000)
    public void testSequences() throws TypeError {
        Stmt first = j.newAssignStmt(code.localX, j.newAddExpr(code.localX, code.localY));
        Stmt last = j.newAssignStmt(code.localY, j.newAddExpr(code.localX, code.localZ));
        DirectedGraph<Unit> g = seq(
                first,
                last
        );
        ForwardFlowAnalysis<Unit, MethodBodyTyping.ResultBox<Level>> gen =
                mbTyping.generate(g, pc, code.signatures);
        Result<Level> finalResult = gen.getFlowAfter(last).getResult();
        Result<Level> beforefinalResult = gen.getFlowBefore(last).getResult();

        ConstraintSet<Level> expected = makeNaive(asList(
                leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varX)),
                leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varX)),
                leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)),
                leC(finalResult.finalTypeVariableOf(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                leC(pc, finalResult.finalTypeVariableOf(code.varY)),
                leC(pc, finalResult.finalTypeVariableOf(code.varX))

        ));

        assertThat(finalResult.getConstraints(), is(equivalent(expected)));

        expected = makeNaive(asList(
                leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)),
                leC(pc, finalResult.finalTypeVariableOf(code.varY)) ));

        Set<TypeVars.TypeVar> varsToProject = new HashSet<>(asList(code.init.get(code.varX), code.init.get(code.varY), code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varX)));
        Set<Constraint<Level>> projected = finalResult.getConstraints().projectTo(varsToProject);
        assertThat(String.format("The projection of %s to %s", finalResult.getConstraints(), varsToProject), makeNaive(projected), is(equivalent(expected)));

    }
}
