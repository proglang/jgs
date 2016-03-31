package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.BodyBuilder;
import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Assumptions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import soot.Body;
import soot.Unit;
import soot.jimple.*;
import soot.toolkits.graph.*;

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
        Stmt first = j.newAssignStmt(code.localX,
                                     j.newAddExpr(code.localX, code.localY));
        Stmt last = j.newAssignStmt(code.localY,
                                    j.newAddExpr(code.localX, code.localZ));
        // x = x + y;
        // y = x + z;
        Body b = BodyBuilder
                .begin()
                .seq(first)
                .seq(last)
                .build();

        assertEquivalentConstraints(
                b,
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
        b = BodyBuilder
                .begin()
                .seq(j.newAssignStmt(code.localX, j.newAddExpr(code.localX, code.localY)))
                .seq(j.newAssignStmt(code.localY, j.newVirtualInvokeExpr(code.localO, code.ignoreSnd_int_int__int.makeRef(), asList(code.localX, code.localZ))))
                .seq(j.newAssignStmt(code.localY, j.newAddExpr(code.localX, code.localY)))
                .build();
        assertEquivalentConstraints(b,
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
        b = BodyBuilder
            .begin()
            .seq(j.newAssignStmt(code.localX, j.newAddExpr(code.localX, code.localY)))
            .seq(j.newAssignStmt(code.localY, j.newVirtualInvokeExpr(code.localO, code.ignoreSnd_int_int__int.makeRef(), asList(code.localZ, code.localX))))
            .seq(j.newAssignStmt(code.localY, j.newAddExpr(code.localX, code.localY)))
            .build();

        assertEquivalentConstraints(b,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varO), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)),
                        leC(pc, finalResult.finalTypeVariableOf(code.varY)))),

                finalResult -> new HashSet<>(asList(pc, code.init.get(code.varX), code.init.get(code.varY),
                        code.init.get(code.varZ), code.init.get(code.varO), finalResult.finalTypeVariableOf(code.varY))));

    }
    // TODO: fix and re-enable tests w/ BodyBuilder
    @Test
    public void testIfBranches() throws TypingException {

        /* if (x = y) { y = z };
        */
        Stmt assignment = j.newAssignStmt(code.localY, code.localZ);
        Body b = BodyBuilder.begin()
                            .ite(j.newEqExpr(code.localX, code.localY),
                                 assignment,
                                 j.newNopStmt())
                            .build();


        assertEquivalentConstraints(b,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)))),
                finalResult -> new HashSet<>(asList(finalResult.finalTypeVariableOf(code.varY), code.init.get(code.varY), code.init.get(code.varX), code.init.get(code.varZ))));

        // should finish in a few iterations
        assertEquivalentConstraints(b,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)))),
                finalResult -> new HashSet<>(asList(finalResult.finalTypeVariableOf(code.varY), code.init.get(code.varY), code.init.get(code.varX), code.init.get(code.varZ))));

        /* if (x = y) {  } { y = z};
        */
        Stmt els = j.newAssignStmt(code.localY, code.localZ);
        b = BodyBuilder.begin()
                       .ite(j.newEqExpr(code.localX, code.localY),
                            j.newNopStmt(),
                            els)
                       .build();

        assertEquivalentConstraints(b,
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
        b = BodyBuilder
                .begin()
                .ite(cond, thn, els)
                .build();
        assertEquivalentConstraints(b,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY)))),
                finalResult -> new HashSet<>(asList(finalResult.finalTypeVariableOf(code.varY), code.init.get(code.varY), code.init.get(code.varX), code.init.get(code.varZ))));


        /* if (y = y) { x = 1}; x = 1*/
        cond = j.newEqExpr(code.localY, code.localY);
        b = BodyBuilder
                .begin()
                .ite(cond, j.newAssignStmt(code.localX, IntConstant.v(1)), j.newNopStmt())
                .seq(j.newAssignStmt(code.localX, IntConstant.v(1)))
                .build();
        BodyTypingResult<Level> r = analyze(b);
        //TODO-needs-test: write some tests that check minimallySubsumes is not trivial!!!
        assertThat(r.getConstraints().projectTo(new HashSet<>(asList(code.init.get(code.varX), code.init.get(code.varY)))), is(minimallySubsumes(makeNaive(singletonList(compC(code.init.get(code.varX), code.init.get(code.varY)))))));
        assertThat(r.getConstraints().projectTo(new HashSet<>(singletonList(code.init.get(code.varX)))), is(minimallySubsumes(makeNaive(Collections.emptyList()))));
        assertThat(r.getConstraints(), is(minimallySubsumes(makeNaive(Collections.emptyList()))));
        assertThat(r.getConstraints().projectTo(new HashSet<>(singletonList(r.finalTypeVariableOf(code.varX)))), is(minimallySubsumes(makeNaive(Collections.emptyList()))));
        assertThat(r.getConstraints().projectTo(new HashSet<>(asList(code.init.get(code.varX), r.finalTypeVariableOf(code.varX)))), is(minimallySubsumes(makeNaive(Collections.emptyList()))));
        assertMinimalSubsumption(b,
                finalResult -> makeNaive(singletonList(compC(code.init.get(code.varY), code.init.get(code.varX)))));


    }

    @Test
    public void testNestedBranches() throws TypingException {

        Expr cond = j.newEqExpr(code.localY, code.localY);
        Stmt body = j.newAssignStmt(code.localY, code.localZ);
        Stmt afterLoop = j.newAssignStmt(code.localX, code.localY);
        Supplier<Stmt> incZ = () -> j.newAssignStmt(code.localZ, j.newAddExpr(code.localZ, IntConstant.v(1)));

 /* if (y = y) { if( x = x) { y = z }; z = z + 1 }; x = y; z = z + 1; */
        Body b = BodyBuilder
                .begin()
                .ite(cond,
                     BodyBuilder.begin().ite(j.newEqExpr(code.localX, code.localX), body, j.newNopStmt()).seq(incZ.get()),
                     BodyBuilder.begin().seq(j.newNopStmt()))
                .seq(afterLoop)
                .seq(incZ.get())
                .build();
        assertEquivalentConstraints(b,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varZ))
                )),
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ))));

    }
//
    @Test
    public void testNestedBranches2() throws TypingException {
        Expr cond = j.newEqExpr(code.localY, code.localY);
        Stmt body = j.newAssignStmt(code.localY, code.localZ);
        Stmt afterLoop = j.newAssignStmt(code.localX, code.localY);
        Supplier<Stmt> incZ = () -> j.newAssignStmt(code.localZ, j.newAddExpr(code.localZ, IntConstant.v(1)));

        /* while (y = y) { if( x = x) { y = z }; z = z + 1 }; x = y; z = z + 1; */
        /*  signature: {y, z, x} <= x* ; {x, z,y} <= z** */
        /*  ( x <= z* through the loop and the indirect flow from x to y) */
        Body b =
                BodyBuilder
                .begin()
                .whileLoop(cond, BodyBuilder.begin().ite(j.newEqExpr(code.localX, code.localX), body, j.newNopStmt()).seq(incZ.get()))
                .seq(afterLoop)
                .seq(incZ.get()).build();

        assertEquivalentConstraints(b,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ)),
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varZ))
                )),
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ))));

        BodyTypingResult<Level> r = analyze(b);
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
    public void testIllegalExitNode() throws Assumptions.Violation {
        Code.LoopWhereIfIsExitNode g = code.new LoopWhereIfIsExitNode();
        thrown.expect(Assumptions.Violation.class);
        thrown.expectMessage(containsString("Branching statement is an exit node"));
        Assumptions.validUnitGraph(g);
    }

    @Test
    public void testTrivialIf() throws TypingException {
        Stmt ret = j.newReturnStmt(IntConstant.v(0));
        Body b = BodyBuilder.begin()
                .seq(j.newIfStmt(j.newEqExpr(IntConstant.v(0), IntConstant.v(0)), ret))
                .seq(ret).build();
        BodyTypingResult<Level> r = analyze(b);
        assertThat(r, is(BodyTypingResult.fromEnv(new NaiveConstraintsFactory<>(types), r.getFinalEnv())));
    }

    @Test
    public void testDoWhileLoop() throws TypingException {
        Stmt nSet = j.newAssignStmt(code.localX, code.localY);
        Stmt nIf = j.newIfStmt(j.newEqExpr(code.localZ, IntConstant.v(0)), nSet);
        Stmt nExit = j.newReturnStmt(IntConstant.v(42));
        Body b = BodyBuilder
                .begin()
                .seq(nSet)
                .seq(nIf)
                .seq(nExit)
                .build();
        assertEquivalentConstraints(b,
                r -> makeNaive(asList(leC(code.init.get(code.varY), r.finalTypeVariableOf(code.varX)), leC(code.init.get(code.varZ), r.finalTypeVariableOf(code.varX)))),
                r -> Stream.of(code.init.get(code.varY), code.init.get(code.varZ), r.finalTypeVariableOf(code.varX)).collect(Collectors.toSet()));
    }

    @Test
    public void testIncreasingLoop() throws TypingException {
        Stmt nExit = j.newReturnStmt(IntConstant.v(42));
        Stmt nSetZ = j.newAssignStmt(code.localZ, code.localY);
        Stmt nSetX = j.newAssignStmt(code.localX, IntConstant.v(0));
        Stmt nIf = j.newIfStmt(j.newEqExpr(code.localZ, IntConstant.v(0)), nSetZ);
        Stmt gotoIf = j.newGotoStmt(nIf);
        Body b = BodyBuilder
                .begin()
                .seq(nIf)
                .seq(nExit)
                .seq(nSetZ)
                .seq(nSetX)
                .seq(gotoIf)
                .build();
        assertEquivalentConstraints(b,
                r -> makeNaive(asList(leC(code.init.get(code.varY), r.finalTypeVariableOf(code.varX)), leC(code.init.get(code.varZ), r.finalTypeVariableOf(code.varX)))),
                r -> Stream.of(code.init.get(code.varY), code.init.get(code.varZ), r.finalTypeVariableOf(code.varX)).collect(Collectors.toSet()));
    }

    @Test
    public void testSpaghetti1() throws TypingException {
        Stmt setX = j.newAssignStmt(code.localX, IntConstant.v(0));
        Stmt if1 = j.newIfStmt(j.newEqExpr(code.localY, code.localY), setX);
        Stmt if2 = j.newIfStmt(j.newEqExpr(code.localZ, code.localZ), setX);
        Stmt gotoIf2 = j.newGotoStmt(if2);
        Stmt exit = j.newReturnStmt(IntConstant.v(42));
        Stmt gotoEndFromIf1 = j.newGotoStmt(exit);
        Stmt gotoEnd = j.newGotoStmt(exit);
        Body b = BodyBuilder
                .begin()
                .seq(if1)
                .seq(gotoEndFromIf1)
                .seq(if2)
                .seq(gotoEnd)
                .seq(setX)
                .seq(gotoIf2)
                .seq(exit).build();
        assertEquivalentConstraints(b,
                r -> makeNaive(asList(leC(code.init.get(code.varY), r.finalTypeVariableOf(code.varX)), leC(code.init.get(code.varZ), r.finalTypeVariableOf(code.varX)))),
                r -> Stream.of(code.init.get(code.varY), code.init.get(code.varZ), r.finalTypeVariableOf(code.varX)).collect(Collectors.toSet()));

    }

    @Test
    public void testMultipleExits() throws TypingException {

        Stmt returnX = j.newReturnStmt(code.localX);
        Stmt returnZ = j.newReturnStmt(code.localZ);
        Stmt ifY = j.newIfStmt(j.newEqExpr(code.localY, code.localY), returnX);
        Stmt ifO = j.newIfStmt(j.newEqExpr(code.localO, code.localO), ifY);
        Stmt returnZero = j.newReturnStmt(IntConstant.v(0));
        Body b = BodyBuilder
                .begin()
                .seq(ifO)
                .seq(returnZ)
                .seq(ifY)
                .seq(returnZero)
                .seq(returnX)
                .build();
        DirectedGraph<Unit> g = new BriefUnitGraph(b);
        assertThat("Unexpected exit nodes: " + g.getTails(), g.getTails().stream().collect(toSet()), is(Stream.of(returnZ, returnZero, returnX).collect(Collectors.toSet())));
        assertThat("Unexpected intro nodes: " + g.getHeads(), g.getHeads().stream().collect(toSet()), is(Stream.of(ifO).collect(Collectors.toSet())));
        List<Unit> postdomsOfIfO = new MHGPostDominatorsFinder(g).getDominators(ifO);
        assertThat("IfO is not its only postdominator: ", postdomsOfIfO, is(equalTo(singletonList(ifO))));
        assertThat("Ify does not have the right successors",
                g.getSuccsOf(ifY).stream().collect(toSet()),
                is(equalTo(Stream.of(returnX, returnZero).collect(Collectors.toSet()))));
        BodyTypingResult<Level> a = analyze(b);
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
        Body b = BodyBuilder
                .begin()
                .whileLoop(cond, body)
                .build();
        assertEquivalentConstraints(b,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varY)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY))
                )),
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varY))));

        /* while (y = y) { y = z }; x = y;
        */
        Stmt afterLoop = j.newAssignStmt(code.localX, code.localY);
        b = BodyBuilder
            .begin()
            .whileLoop(cond, body)
            .seq(afterLoop).build();
        assertEquivalentConstraints(b,
                finalResult -> makeNaive(asList(
                        leC(code.init.get(code.varY), finalResult.finalTypeVariableOf(code.varX)),
                        leC(code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varX))
                )),
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), code.init.get(code.varX), finalResult.finalTypeVariableOf(code.varX))));
        assertEquivalentConstraints(b,
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
        b = BodyBuilder.begin(b).seq(incZ).build();

        BodyTypingResult<Level> result = analyze(b);
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
        assertEquivalentConstraints(b, getConstraints,
                finalResult -> new HashSet<>(asList(code.init.get(code.varY), code.init.get(code.varZ), finalResult.finalTypeVariableOf(code.varZ))));


    }

    private BodyTypingResult<Level> analyze(Body b) throws TypingException {
        return mbTyping.generateResult(b, pc, code.init);
    }

    private void assertEquivalentConstraints(Body b, Function<BodyTypingResult<Level>, ConstraintSet<Level>> getExpected, Function<BodyTypingResult<Level>, Set<TypeVars.TypeVar>> getVarsToProject) throws TypingException {
        BodyTypingResult<Level> finalResult = analyze(b);
        Set<TypeVars.TypeVar> varsToProject = getVarsToProject.apply(finalResult);
        ConstraintSet<Level> expected = getExpected.apply(finalResult);
        assertThat(String.format("The constraints of %s projected to %s", b, varsToProject),
                NaiveConstraints.minimize(finalResult.getConstraints().projectTo(varsToProject)), is(equivalent(expected)));
    }

    private void assertMinimalSubsumption(Body b, Function<BodyTypingResult<Level>, ConstraintSet<Level>> getExpected) throws TypingException {
        BodyTypingResult<Level> finalResult = analyze(b);
        ConstraintSet<Level> expected = getExpected.apply(finalResult);
        assertThat(String.format("The constraints of %s", b.toString()),
                NaiveConstraints.minimize(finalResult.getConstraints()), minimallySubsumes(expected));

    }

}
