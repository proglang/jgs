package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.Constraint;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.NaiveConstraints;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Graphs;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import soot.JastAddJ.Opt;
import soot.Unit;
import soot.jimple.Expr;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.DominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.scalar.ForwardFlowAnalysis;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    @Test
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
    public void testIfBranches() throws TypeError {
        DirectedGraph<Unit> g;

        /* if (x = y) { y = z };
        */
        Stmt assignment = j.newAssignStmt(code.localY, code.localZ);
        g = branchIf(j.newEqExpr(code.localX, code.localY),
                singleton(assignment),
                singleton(j.newNopStmt()));


        assertConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)))),
                finalResult -> new HashSet<>(asList(finalResult.finalTypeVariableOf(code.varY), code.init.get(code.varY), code.init.get(code.varX), code.init.get(code.varZ))));

        // should finish in a few iterations
        assertConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)))),
                finalResult -> new HashSet<>(asList(finalResult.finalTypeVariableOf(code.varY), code.init.get(code.varY), code.init.get(code.varX), code.init.get(code.varZ))));

        /* if (x = y) {  } { y = z};
        */
        Stmt els = j.newAssignStmt(code.localY, code.localZ);
        g = branchIf(j.newEqExpr(code.localX, code.localY),
                singleton(j.newNopStmt()),
                singleton(els)
        );

        // TODO: put into individual tests
        assertThat((g.getPredsOf(g.getTails().get(0))), hasItem(els));
        assertThat(code.init.get(code.varY), not(is(analyze(g).finalTypeVariableOf(code.varY))));

        assertConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)))),
                finalResult -> new HashSet<>(asList(finalResult.finalTypeVariableOf(code.varY), code.init.get(code.varY), code.init.get(code.varX), code.init.get(code.varZ))));

        /* if (y = y) { y = z } { y = x};
        */
        Expr cond = j.newEqExpr(code.localY, code.localY);
        Stmt thn = j.newAssignStmt(code.localY, code.localZ);
        els = j.newAssignStmt(code.localY, code.localX);
        g = branchIf(cond,
                singleton(thn),
                singleton(els));
        assertConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)))),
                finalResult -> new HashSet<>(asList(finalResult.finalTypeVariableOf(code.varY), code.init.get(code.varY), code.init.get(code.varX), code.init.get(code.varZ))));

    }

    @Test
    public void testPostDom() {
        /*
        if (y == y) { x = y } { x = z }; z = x; y = x
                                           ^ immediate postdom?
         */
        Expr cond = j.newEqExpr(code.localY, code.localY);
        Stmt thn = j.newAssignStmt(code.localX, code.localY);
        Stmt els = j.newAssignStmt(code.localX, code.localZ);

        Stmt remotePostdom1 = j.newAssignStmt(code.localX, code.localZ);
        Stmt remotePostdom2 = j.newAssignStmt(code.localY, code.localX);

        DirectedGraph<Unit> g = seq(branchIf(cond, singleton(thn), singleton(els)), singleton(remotePostdom1), singleton(remotePostdom2));
        @SuppressWarnings("unchecked") DominatorsFinder<Unit> postdoms = new MHGPostDominatorsFinder(g);

        assertThat(g.getHeads().size(), is(1));
        assertThat(g.getPredsOf(remotePostdom1).size(), is(1));

        Unit sIf = g.getHeads().get(0);
        Unit immediatePostdom = g.getPredsOf(remotePostdom1).get(0);

        assertThat(postdoms.getImmediateDominator(sIf), is(immediatePostdom));
        assertThat(postdoms.getImmediateDominator(sIf), not(is(remotePostdom2)));
        assertThat(postdoms.isDominatedBy(sIf, remotePostdom1), is(true));
        assertThat(postdoms.isDominatedBy(sIf, remotePostdom2), is(true));
        assertThat(postdoms.isDominatedBy(sIf, els), is(false));
    }

    @Test
    //@Test(timeout = 3000)
    public void testWhile() throws TypeError {
        /* while (y = y) { y = z };
        */
        Expr cond = j.newEqExpr(code.localY, code.localY);
        Stmt body = j.newAssignStmt(code.localY, code.localZ);
        DirectedGraph<Unit> g = branchWhile(cond, singleton(body));
        assertConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY))
                )),
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY))));

        /* while (y = y) { y = z }; x = y;
        */
        Stmt afterLoop = j.newAssignStmt(code.localX, code.localY);
        g = seq(branchWhile(cond, singleton(body)), singleton(afterLoop));
        assertConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varX)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varX))
                )),
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varX))));
        assertConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varX)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varX)),
                        leC(finalResult.finalTypeVariableOf(code.varY), finalResult.finalTypeVariableOf(code.varX)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY))
                        )),
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varX), finalResult.finalTypeVariableOf(code.varY))));
    }


    private Result<Level> analyze(DirectedGraph<Unit> g) throws TypeError {
        return mbTyping.generateResult(g, pc, code.init, code.signatures);
    }

    private void assertConstraints(DirectedGraph<Unit> g, Function<Result<Level>, ConstraintSet<Level>> getExpected, Function<Result<Level>, Set<TypeVars.TypeVar>> getVarsToProject) throws TypeError {
        Result<Level> finalResult = analyze(g);
        Set<TypeVars.TypeVar> varsToProject = getVarsToProject.apply(finalResult);
        ConstraintSet<Level> expected = getExpected.apply(finalResult);
        assertThat(String.format("The constraints of %s projected to %s", Graphs.toString(g), varsToProject),
                NaiveConstraints.minimize(finalResult.getConstraints().projectTo(varsToProject)), is(equivalent(expected)));
    }

    private void assertConstraints(DirectedGraph<Unit> g, Function<Result<Level>, ConstraintSet<Level>> getExpected) throws TypeError {
        assertConstraints(g, getExpected, finalResult -> finalResult.getConstraints().variables().collect(toSet()));
    }

}
