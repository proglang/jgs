package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Graphs;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import soot.Unit;
import soot.jimple.Expr;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.DominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.jimpleutils.Graphs.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
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
        this.tvars = new TypeVars();
        this.j = Jimple.v();
        this.pc = tvars.topLevelContext();
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

        assertEquivalentConstraints(g,
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
        assertEquivalentConstraints(g,
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

        assertEquivalentConstraints(g,
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


        assertEquivalentConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)))),
                finalResult -> new HashSet<>(asList(finalResult.finalTypeVariableOf(code.varY), code.init.get(code.varY), code.init.get(code.varX), code.init.get(code.varZ))));

        // should finish in a few iterations
        assertEquivalentConstraints(g,
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

        assertEquivalentConstraints(g,
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
        assertEquivalentConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)))),
                finalResult -> new HashSet<>(asList(finalResult.finalTypeVariableOf(code.varY), code.init.get(code.varY), code.init.get(code.varX), code.init.get(code.varZ))));


        /* if (y = y) { x = 1}; x = 1*/
        cond = j.newEqExpr(code.localY, code.localY);
        g = seq(branchIf(cond, singleton(j.newAssignStmt(code.localX, IntConstant.v(1))), singleton(j.newNopStmt())), j.newAssignStmt(code.localX, IntConstant.v(1)));
        Result<Level> r = analyze(g);
        //TODO: write some tests that check minimallySubsumes is not trivial!!!
        assertThat(r.getConstraints().projectTo(new HashSet<>(asList(code.init.get(code.varX), code.init.get(code.varY)))), is(minimallySubsumes(makeNaive(asList(compC(code.init.get(code.varX), code.init.get(code.varY)))))));
        assertThat(r.getConstraints().projectTo(new HashSet<>(Collections.singletonList(code.init.get(code.varX)))), is(minimallySubsumes(makeNaive(Collections.emptyList()))));
        assertThat(r.getConstraints(), is(minimallySubsumes(makeNaive(Collections.emptyList()))));
        assertThat(r.getConstraints().projectTo(new HashSet<>(Collections.singletonList(r.finalTypeVariableOf(code.varX)))), is(minimallySubsumes(makeNaive(Collections.emptyList()))));
        assertThat(r.getConstraints().projectTo(new HashSet<>(asList(code.init.get(code.varX), r.finalTypeVariableOf(code.varX)))), is(minimallySubsumes(makeNaive(Collections.emptyList()))));
        assertMinimalSubsumption(g,
                finalResult -> makeNaive(asList(compC(code.init.get(code.varY), code.init.get(code.varX)))));


    }

    @Test
    public void testNestedBranches() throws TypeError {

        Expr cond = j.newEqExpr(code.localY, code.localY);
        Stmt body = j.newAssignStmt(code.localY, code.localZ);
        Stmt afterLoop = j.newAssignStmt(code.localX, code.localY);
        Supplier<Stmt> incZ = () -> j.newAssignStmt(code.localZ, j.newAddExpr(code.localZ, IntConstant.v(1)));

 /* if (y = y) { if( x = x) { y = z }; z = z + 1 }; x = y; z = z + 1; */
        DirectedGraph<Unit> g = seq(branchIf(cond, seq(branchIf(j.newEqExpr(code.localX, code.localX), singleton(body), singleton(j.newNopStmt())), incZ.get()), singleton(j.newNopStmt())), afterLoop, incZ.get());
        assertEquivalentConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varZ))
                )),
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ))));

    }

    @Test
    public void testNestedBranches2() throws TypeError {
        Expr cond = j.newEqExpr(code.localY, code.localY);
        Stmt body = j.newAssignStmt(code.localY, code.localZ);
        Stmt afterLoop = j.newAssignStmt(code.localX, code.localY);
        Supplier<Stmt> incZ = () -> j.newAssignStmt(code.localZ, j.newAddExpr(code.localZ, IntConstant.v(1)));

        /* while (y = y) { if( x = x) { y = z }; z = z + 1 }; x = y; z = z + 1; */
        /*  signature: {y, z, x} <= x* ; {z,y} <= z** */
        DirectedGraph<Unit> g = seq(branchWhile(cond, seq(branchIf(j.newEqExpr(code.localX, code.localX), singleton(body), singleton(j.newNopStmt())), incZ.get())), afterLoop, incZ.get());

        assertEquivalentConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varZ))
                )),
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ))));

        Result<Level> r = analyze(g);
//        // TODO: find a solution for testing minimal subsumption more efficiently (without enumerating everything)
        ConstraintSet<Level> sig = makeNaive(asList(
                        /* {z,y} <= z** */
                        leC(code.init.get(code.varZ), r.finalTypeVariableOf(code.varZ)),
                        leC(code.init.get(code.varY), r.finalTypeVariableOf(code.varZ)),

                        /* {y, z, x} <= x* */
                        leC(code.init.get(code.varZ), r.finalTypeVariableOf(code.varX)),
                        leC(code.init.get(code.varY), r.finalTypeVariableOf(code.varX)),
                        leC(code.init.get(code.varX), r.finalTypeVariableOf(code.varX))));

        // TODO: syntax for assignments would be convenient
        assertThat(sig.isSatisfiedFor(types, Assignments.builder(code.init.get(code.varX), PUB)
                        .add(r.finalTypeVariableOf(code.varX), DYN)
                        .add(code.init.get(code.varY), PUB)
                        .add(r.finalTypeVariableOf(code.varZ), TLOW)
                        .add(code.init.get(code.varZ), PUB).build())
                        , is(true));
        assertThat(r.getConstraints(), refines(tvars,sig));
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
    public void testReflexivePostDom() throws TypeError {
        Code.LoopWithReflexivePostdom g = code.new LoopWithReflexivePostdom();
        @SuppressWarnings("unchecked") DominatorsFinder<Unit> postdoms = new MHGPostDominatorsFinder(g);
        // In Soot the immediate dominator is not the reflexive one
        assertThat(postdoms.getImmediateDominator(g.nIf), not(is(g.nIf)));
        assertThat(postdoms.getImmediateDominator(g.nIf), is(g.nExit));
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testIllegalExitNode() throws  TypeError {
        Code.LoopWhereIfIsExitNode g = code.new LoopWhereIfIsExitNode();
        thrown.expect(TypeError.class);
        thrown.expectMessage(containsString("Branching statement is an exit node"));
        analyze(g);
    }

    @Test
    public void testTrivialIf() throws TypeError {
        Code.TrivialIf g  = code.new TrivialIf();
        Result<Level> r = analyze(g);
        assertThat(r, is(Result.fromEnv(new NaiveConstraintsFactory<>(types), r.getFinalEnv())));
    }

    @Test
    //@Test(timeout = 3000)
    public void testWhile() throws TypeError {
        /* while (y = y) { y = z };
        */
        Expr cond = j.newEqExpr(code.localY, code.localY);
        Stmt body = j.newAssignStmt(code.localY, code.localZ);
        DirectedGraph<Unit> g = branchWhile(cond, singleton(body));
        assertEquivalentConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY))
                )),
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY))));

        /* while (y = y) { y = z }; x = y;
        */
        Stmt afterLoop = j.newAssignStmt(code.localX, code.localY);
        g = seq(branchWhile(cond, singleton(body)), singleton(afterLoop));
        assertEquivalentConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varX)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varX))
                )),
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varX))));
        assertEquivalentConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varX)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varX)),
                        leC(finalResult.finalTypeVariableOf(code.varY), finalResult.finalTypeVariableOf(code.varX)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY))
                )),
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varX), finalResult.finalTypeVariableOf(code.varY))));

        /* while (y = y) { y = z }; x = y; z = z + 1;
             sig = { z <= z*, z ~ y }
             cset = { z <= y*, y <= y*, y* <= x*, z <= z* }

             unsat for sig: {z:LOW, y:?, z*:LOW}
             sat for {z <= z*}: {z:LOW, y:?, z*:LOW}
         */
        Stmt incZ = j.newAssignStmt(code.localZ, j.newAddExpr(code.localZ, IntConstant.v(1)));
        g = seq(g, incZ);

        Result<Level> result = analyze(g);
        Function<Result<Level>,ConstraintSet<Level>> getConstraints = finalResult -> makeNaive(asList(
                leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ)),
                compC(code.init.get(code.varZ), code.init.get(code.varY))));

        assertThat(makeNaive(getConstraints.apply(result).apply(
                Assignments.builder(code.init.get(code.varZ), TLOW)
                        .add(code.init.get(code.varY), DYN).build()).collect(toSet())), not(is(sat())));

        assertThat(makeNaive(result.getConstraints().apply(
                Assignments.builder(code.init.get(code.varZ), TLOW)
                        .add(code.init.get(code.varY), DYN).build()).collect(toSet())), not(is(sat())));

        assertThat("Unsat projected fewer",makeNaive(result.getConstraints().projectTo(Stream.of(code.init.get(code.varZ), code.init.get(code.varY), result.finalTypeVariableOf(code.varY), result.finalTypeVariableOf(code.varZ)).collect(toSet())).apply(
                Assignments.builder(code.init.get(code.varZ), TLOW)
                        .add(code.init.get(code.varY), DYN).build()).collect(toSet())), not(is(sat())));
        assertThat("Unsat projected",makeNaive(result.getConstraints().projectTo(Stream.of(code.init.get(code.varZ), code.init.get(code.varY), result.finalTypeVariableOf(code.varZ)).collect(toSet())).apply(
                Assignments.builder(code.init.get(code.varZ), TLOW)
                        .add(code.init.get(code.varY), DYN).build()).collect(toSet())), not(is(sat())));

        assertThat("Unsat projected for sig", makeNaive(result.getConstraints().projectForSignature(tvars, getConstraints.apply(result)).apply(
                Assignments.builder(code.init.get(code.varZ), TLOW)
                        .add(code.init.get(code.varY), DYN).build()).collect(toSet())), not(is(sat())));
//        assertThat(makeNaive(getConstraints.apply(result).proapply(
//                Assignments.builder(code.init.get(code.varZ), TLOW)
//                        .add(code.init.get(code.varY), DYN).build()).collect(toSet())), not(is(sat())));

        assertEquivalentConstraints(g, getConstraints,
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ))));


    }

    private Result<Level> analyze(DirectedGraph<Unit> g) throws TypeError {
        return mbTyping.generateResult(g, pc, code.init, code.signatures);
    }

    private void assertEquivalentConstraints(DirectedGraph<Unit> g, Function<Result<Level>, ConstraintSet<Level>> getExpected, Function<Result<Level>, Set<TypeVars.TypeVar>> getVarsToProject) throws TypeError {
        Result<Level> finalResult = analyze(g);
        Set<TypeVars.TypeVar> varsToProject = getVarsToProject.apply(finalResult);
        ConstraintSet<Level> expected = getExpected.apply(finalResult);
        assertThat(String.format("The constraints of %s projected to %s", Graphs.toString(g), varsToProject),
                NaiveConstraints.minimize(finalResult.getConstraints().projectTo(varsToProject)), is(equivalent(expected)));
    }

    private void assertMinimalSubsumption(DirectedGraph<Unit> g, Function<Result<Level>, ConstraintSet<Level>> getExpected) throws TypeError {
        Result<Level> finalResult = analyze(g);
        ConstraintSet<Level> expected = getExpected.apply(finalResult);
        assertThat(String.format("The constraints of %s", Graphs.toString(g)),
                NaiveConstraints.minimize(finalResult.getConstraints()),minimallySubsumes(expected));

    }

}
