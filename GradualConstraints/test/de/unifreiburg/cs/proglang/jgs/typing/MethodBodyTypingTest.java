package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.Constraint;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.NaiveConstraints;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Graphs;
import org.junit.Before;
import org.junit.Test;
import soot.Unit;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.jimpleutils.Graphs.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
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
        this.tvars = new TypeVars("");
        this.j = Jimple.v();
        this.pc = tvars.fresh("pc");
        this.code = new Code(tvars);
        this.mbTyping = mkMbTyping(code.init, tvars);
    }

    @Test(timeout = 1000)
    public void testSequences() throws TypeError {
        Stmt first = j.newAssignStmt(code.localX, j.newAddExpr(code.localX, code.localY));
        Stmt last = j.newAssignStmt(code.localY, j.newAddExpr(code.localX, code.localZ));
        // x = x + y;
        // y = x + z;
        DirectedGraph<Unit> g = seq(
                first,
                last
        );

        assertConstraints(g,
                (Result<Level> finalResult) -> makeNaive(asList(
                        leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)),
                        leC(pc, finalResult.finalTypeVariableOf(code.varY)))),
                finalResult -> new HashSet<>(asList(pc, code.init.get(code.varX), code.init.get(code.varY),
                        code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY))));

        // x = x + y;
        // y = o.ignoreSnd_int_int__int(x, z);
        // y = x + y;
        g = seq(
                j.newAssignStmt(code.localX, j.newAddExpr(code.localX, code.localY)),
                j.newAssignStmt(code.localY, j.newVirtualInvokeExpr(code.localO, code.ignoreSnd_int_int__int.makeRef(), asList(code.localX, code.localZ))),
                j.newAssignStmt(code.localY, j.newAddExpr(code.localX, code.localY))
        );
        assertConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varO), finalResult.finalTypeVariableOf(code.varY)),
                        leC(pc, finalResult.finalTypeVariableOf(code.varY)))),

                finalResult -> new HashSet<>(asList(pc, code.init.get(code.varX), code.init.get(code.varY),
                        code.init.get(code.varZ), code.init.get(code.varO), finalResult.finalTypeVariableOf(code.varY))));

        // x = x + y;
        // y = o.ignoreSnd_int_int__int(z, x);
        // y = x + y;
        g = seq(
                j.newAssignStmt(code.localX, j.newAddExpr(code.localX, code.localY)),
                j.newAssignStmt(code.localY, j.newVirtualInvokeExpr(code.localO, code.ignoreSnd_int_int__int.makeRef(), asList(code.localZ, code.localX))),
                j.newAssignStmt(code.localY, j.newAddExpr(code.localX, code.localY))
        );

        assertConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varO), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)),
                        leC(pc, finalResult.finalTypeVariableOf(code.varY)))),

                finalResult -> new HashSet<>(asList(pc, code.init.get(code.varX), code.init.get(code.varY),
                        code.init.get(code.varZ), code.init.get(code.varO), finalResult.finalTypeVariableOf(code.varY))));
    }

    @Test
    public void testBranches() throws TypeError {
        DirectedGraph<Unit> g;

        /* if (x = y) { y = z };
        */
        g = branchIf(j.newEqExpr(code.localX, code.localY),
                singleton(j.newAssignStmt(code.localY, code.localZ)),
                singleton(j.newNopStmt()));

        assertConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)))),
                finalResult -> new HashSet<>(asList(finalResult.finalTypeVariableOf(code.varY), code.init.get(code.varY), code.init.get(code.varX), code.init.get(code.varZ))));
    }

    private void assertConstraints(DirectedGraph<Unit> g, Function<Result<Level>, ConstraintSet<Level>> getExpected, Function<Result<Level>, Set<TypeVars.TypeVar>> getVarsToProject) throws TypeError {
        ForwardFlowAnalysis<Unit, MethodBodyTyping.ResultBox<Level>> gen = mbTyping.generate(g, pc, code.signatures);
        List<Unit> tails = g.getTails();
        if (tails.size() != 1) {
            throw new RuntimeException("Unit graph does not have a single tail: " + g.toString());
        }
        Unit last = g.getTails().get(0);
        Result<Level> finalResult = gen.getFlowAfter(last).getResult();
        Set<TypeVars.TypeVar> varsToProject = getVarsToProject.apply(finalResult);
        ConstraintSet<Level> expected = getExpected.apply(finalResult);
        assertThat(String.format("The constraints of %s projected to %s", Graphs.toString(g), varsToProject),
                NaiveConstraints.minimize(finalResult.getConstraints().projectTo(varsToProject)), is(equivalent(expected)));
    }
}
