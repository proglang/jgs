package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Graphs;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import soot.Unit;
import soot.jimple.*;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.DominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.jimpleutils.Graphs.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.*;


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
        this.mbTyping = mkMbTyping(code.init, tvars, code.signatures, code.fields);
    }

    @Test
    public void testSequences() throws TypingException {
        Stmt first = j.newAssignStmt(code.localX, j.newAddExpr(code.localX, code.localY));
        Stmt last = j.newAssignStmt(code.localY, j.newAddExpr(code.localX, code.localZ));
        // x = x + y;
        // y = x + z;
        DirectedGraph<Unit> g = seq(
                first,
                last
        );

        assertEquivalentConstraints(g,
                (BodyTypingResult<Level> finalResult) -> makeNaive(asList(
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
    public void testIfBranches() throws TypingException {
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

        // misc assertions, to be sure
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
        BodyTypingResult<Level> r = analyze(g);
        //TODO-needs-test: write some tests that check minimallySubsumes is not trivial!!!
        assertThat(r.getConstraints().projectTo(new HashSet<>(asList(code.init.get(code.varX), code.init.get(code.varY)))), is(minimallySubsumes(makeNaive(singletonList(compC(code.init.get(code.varX), code.init.get(code.varY)))))));
        assertThat(r.getConstraints().projectTo(new HashSet<>(singletonList(code.init.get(code.varX)))), is(minimallySubsumes(makeNaive(Collections.emptyList()))));
        assertThat(r.getConstraints(), is(minimallySubsumes(makeNaive(Collections.emptyList()))));
        assertThat(r.getConstraints().projectTo(new HashSet<>(singletonList(r.finalTypeVariableOf(code.varX)))), is(minimallySubsumes(makeNaive(Collections.emptyList()))));
        assertThat(r.getConstraints().projectTo(new HashSet<>(asList(code.init.get(code.varX), r.finalTypeVariableOf(code.varX)))), is(minimallySubsumes(makeNaive(Collections.emptyList()))));
        assertMinimalSubsumption(g,
                finalResult -> makeNaive(singletonList(compC(code.init.get(code.varY), code.init.get(code.varX)))));


    }

    @Test
    public void testNestedBranches() throws TypingException {

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
    public void testNestedBranches2() throws TypingException {
        Expr cond = j.newEqExpr(code.localY, code.localY);
        Stmt body = j.newAssignStmt(code.localY, code.localZ);
        Stmt afterLoop = j.newAssignStmt(code.localX, code.localY);
        Supplier<Stmt> incZ = () -> j.newAssignStmt(code.localZ, j.newAddExpr(code.localZ, IntConstant.v(1)));

        /* while (y = y) { if( x = x) { y = z }; z = z + 1 }; x = y; z = z + 1; */
        /*  abstractConstraints: {y, z, x} <= x* ; {x, z,y} <= z** */
        /*  ( x <= z* through the loop and the indirect flow from x to y) */
        DirectedGraph<Unit> g = seq(branchWhile(cond, seq(branchIf(j.newEqExpr(code.localX, code.localX), singleton(body), singleton(j.newNopStmt())), incZ.get())), afterLoop, incZ.get());

        assertEquivalentConstraints(g,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varZ))
                )),
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ))));

        BodyTypingResult<Level> r = analyze(g);
//        // TODO-performance: find a solution for testing minimal subsumption more efficiently (without enumerating everything)
        ConstraintSet<Level> sig = makeNaive(asList(
                        /* {z,y,x} <= z** */
                leC(code.init.get(code.varZ), r.finalTypeVariableOf(code.varZ)),
                leC(code.init.get(code.varY), r.finalTypeVariableOf(code.varZ)),
                leC(code.init.get(code.varX), r.finalTypeVariableOf(code.varZ)),

                        /* {y, z, x} <= x* */
                leC(code.init.get(code.varZ), r.finalTypeVariableOf(code.varX)),
                leC(code.init.get(code.varY), r.finalTypeVariableOf(code.varX)),
                leC(code.init.get(code.varX), r.finalTypeVariableOf(code.varX))));

        assertThat(sig.isSatisfiedFor(types, Assignments.builder(code.init.get(code.varX), PUB)
                        .add(r.finalTypeVariableOf(code.varX), DYN)
                        .add(code.init.get(code.varY), PUB)
                        .add(r.finalTypeVariableOf(code.varZ), TLOW)
                        .add(code.init.get(code.varZ), PUB).build())
                , is(true));
        assertThat(r.getConstraints(), refines(tvars, sig));
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
    public void testReflexivePostDom() throws TypingException {
        Code.LoopWithReflexivePostdom g = code.new LoopWithReflexivePostdom();
        @SuppressWarnings("unchecked") DominatorsFinder<Unit> postdoms = new MHGPostDominatorsFinder(g);
        // In Soot the immediate dominator is not the reflexive one
        assertThat(postdoms.getImmediateDominator(g.nIf), not(is(g.nIf)));
        assertThat(postdoms.getImmediateDominator(g.nIf), is(g.nExit));
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testIllegalExitNode() throws TypingException {
        Code.LoopWhereIfIsExitNode g = code.new LoopWhereIfIsExitNode();
        thrown.expect(TypingException.class);
        thrown.expectMessage(containsString("Branching statement is an exit node"));
        analyze(g);
    }

    @Test
    public void testTrivialIf() throws TypingException {
        Code.TrivialIf g = code.new TrivialIf();
        BodyTypingResult<Level> r = analyze(g);
        assertThat(r, is(BodyTypingResult.fromEnv(new NaiveConstraintsFactory<>(types), r.getFinalEnv())));
    }

    @Test
    public void testDoWhileLoop() throws TypingException {
        Code.SimpleDoWhile g = code.new SimpleDoWhile();
        assertEquivalentConstraints(g,
                r -> makeNaive(asList(leC(code.init.get(code.varY), r.finalTypeVariableOf(code.varX)), leC(code.init.get(code.varZ), r.finalTypeVariableOf(code.varX)))),
                r -> Stream.of(code.init.get(code.varY), code.init.get(code.varZ), r.finalTypeVariableOf(code.varX)).collect(Collectors.toSet()));
    }

    @Test
    public void testIncreasingLoop() throws TypingException {
        Code.LoopIncrease g = code.new LoopIncrease();
        assertEquivalentConstraints(g,
                r -> makeNaive(asList(leC(code.init.get(code.varY), r.finalTypeVariableOf(code.varX)), leC(code.init.get(code.varZ), r.finalTypeVariableOf(code.varX)))),
                r -> Stream.of(code.init.get(code.varY), code.init.get(code.varZ), r.finalTypeVariableOf(code.varX)).collect(Collectors.toSet()));
    }

    @Test
    public void testSpaghetti1() throws TypingException {
        Code.Spaghetti1 g = code.new Spaghetti1();
        assertEquivalentConstraints(g,
                r -> makeNaive(asList(leC(code.init.get(code.varY), r.finalTypeVariableOf(code.varX)), leC(code.init.get(code.varZ), r.finalTypeVariableOf(code.varX)))),
                r -> Stream.of(code.init.get(code.varY), code.init.get(code.varZ), r.finalTypeVariableOf(code.varX)).collect(Collectors.toSet()));

    }

    @Test
    public void testMultipleExits() throws TypingException {

        Code.MultipleReturns g = code.new MultipleReturns();
        assertThat("Unexpected exit nodes: " + g.getTails(), g.getTails().stream().collect(toSet()), is(Stream.of(g.returnZ, g.returnZero, g.returnX).collect(Collectors.toSet())));
        assertThat("Unexpected intro nodes: " + g.getHeads(), g.getHeads().stream().collect(toSet()), is(Stream.of(g.ifO).collect(Collectors.toSet())));
        List<Unit> postdomsOfIfO = new MHGPostDominatorsFinder(g).getDominators(g.ifO);
        assertThat("IfO is not its only postdominator: ", postdomsOfIfO, is(equalTo(singletonList(g.ifO))));
        assertThat("Ify does not have the right successors",
                g.getSuccsOf(g.ifY).stream().collect(toSet()),
                is(equalTo(Stream.of(g.returnX, g.returnZero).collect(Collectors.toSet()))));
        BodyTypingResult<Level> a = analyze(g);
        ConstraintSet<Level> expected = makeNaive(asList(
                leC(code.init.get(code.varX), tvars.ret()),
                leC(code.init.get(code.varY), tvars.ret()),
                leC(code.init.get(code.varO), tvars.ret()),
                leC(code.init.get(code.varZ), tvars.ret())
        ));
        ConstraintSet<Level> projected = a.getConstraints().projectTo(Stream.concat(Stream.of(tvars.ret()), Stream.of(code.varX, code.varY, code.varZ, code.varO).map(a::finalTypeVariableOf)).collect(Collectors.toSet()));
        assertThat("Not equivalent. Orig: " + a.getConstraints(), projected, is(equivalent(expected)));
    }


    @Test
    //@Test(timeout = 3000)
    public void testWhile() throws TypingException {
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

        BodyTypingResult<Level> result = analyze(g);
        Function<BodyTypingResult<Level>, ConstraintSet<Level>> getConstraints = finalResult -> makeNaive(asList(
                leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ)),
                compC(code.init.get(code.varZ), code.init.get(code.varY))));

        assertThat(makeNaive(getConstraints.apply(result).apply(
                Assignments.builder(code.init.get(code.varZ), TLOW)
                        .add(code.init.get(code.varY), DYN).build()).collect(toSet())), not(is(sat())));

        assertThat(makeNaive(result.getConstraints().apply(
                Assignments.builder(code.init.get(code.varZ), TLOW)
                        .add(code.init.get(code.varY), DYN).build()).collect(toSet())), not(is(sat())));

        assertThat("Unsat projected fewer", makeNaive(result.getConstraints().projectTo(Stream.of(code.init.get(code.varZ), code.init.get(code.varY), result.finalTypeVariableOf(code.varY), result.finalTypeVariableOf(code.varZ)).collect(toSet())).apply(
                Assignments.builder(code.init.get(code.varZ), TLOW)
                        .add(code.init.get(code.varY), DYN).build()).collect(toSet())), not(is(sat())));
        assertThat("Unsat projected", makeNaive(result.getConstraints().projectTo(Stream.of(code.init.get(code.varZ), code.init.get(code.varY), result.finalTypeVariableOf(code.varZ)).collect(toSet())).apply(
                Assignments.builder(code.init.get(code.varZ), TLOW)
                        .add(code.init.get(code.varY), DYN).build()).collect(toSet())), not(is(sat())));

//        assertThat("Unsat projected for sig", makeNaive(result.getConstraints().asSignatureConstraints(tvars, getConstraints.apply(result)).apply(
//                Assignments.builder(code.init.get(code.varZ), TLOW)
//                        .add(code.init.get(code.varY), DYN).build()).collect(toSet())), not(is(sat())));
//        assertThat(makeNaive(getConstraints.apply(result).proapply(
//                Assignments.builder(code.init.get(code.varZ), TLOW)
//                        .add(code.init.get(code.varY), DYN).build()).collect(toSet())), not(is(sat())));

        assertEquivalentConstraints(g, getConstraints,
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ))));


    }

    private BodyTypingResult<Level> analyze(DirectedGraph<Unit> g) throws TypingException {
        return mbTyping.generateResult(g, pc, code.init);
    }

    private void assertEquivalentConstraints(DirectedGraph<Unit> g, Function<BodyTypingResult<Level>, ConstraintSet<Level>> getExpected, Function<BodyTypingResult<Level>, Set<TypeVars.TypeVar>> getVarsToProject) throws TypingException {
        BodyTypingResult<Level> finalResult = analyze(g);
        Set<TypeVars.TypeVar> varsToProject = getVarsToProject.apply(finalResult);
        ConstraintSet<Level> expected = getExpected.apply(finalResult);
        assertThat(String.format("The constraints of %s projected to %s", Graphs.toString(g), varsToProject),
                NaiveConstraints.minimize(finalResult.getConstraints().projectTo(varsToProject)), is(equivalent(expected)));
    }

    private void assertMinimalSubsumption(DirectedGraph<Unit> g, Function<BodyTypingResult<Level>, ConstraintSet<Level>> getExpected) throws TypingException {
        BodyTypingResult<Level> finalResult = analyze(g);
        ConstraintSet<Level> expected = getExpected.apply(finalResult);
        assertThat(String.format("The constraints of %s", Graphs.toString(g)),
                NaiveConstraints.minimize(finalResult.getConstraints()), minimallySubsumes(expected));

    }

}
