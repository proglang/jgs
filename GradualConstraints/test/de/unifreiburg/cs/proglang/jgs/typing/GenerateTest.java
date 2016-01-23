package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.CTypes;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import org.junit.Before;
import org.junit.Test;
import soot.jimple.Jimple;
import soot.jimple.Stmt;

import java.util.Collections;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class GenerateTest {

    private BasicStatementTyping<Level> typing;
    private Jimple j;
    private TypeVars tvars;
    private Code code;
    private TypeVar pc;



    private void assertUnsat(ConstraintSet<Level> unsatConstraints) {
        assertThat(String.format("Should not be SAT: %s\nassignment: %s",
                                  unsatConstraints,
                                  cstrs.satisfyingAssignment(unsatConstraints,
                                          Collections.emptyList())),
                    unsatConstraints,not(is(sat())));
    }

    @Before public void setUp() {
        this.tvars = new TypeVars();
        this.j = Jimple.v();
        this.pc = tvars.topLevelContext();
        this.code = new Code(tvars);
        this.typing = mkTyping(tvars.forTestingSingleStatements());

    }

    @Test public void testLocals() throws TypingException {

        Stmt s;
        BodyTypingResult<Level> r;
        ConstraintSet<Level> expected;
        TypeVar tvarXFinal;
        ConstraintSet<Level> pc_HIGH_finalX_LOW;

        /*********/
        /* x = x */
        /*********/
        s = j.newAssignStmt(code.localX, code.localX);
        r = typing.generate(s, code.init, singleton(this.pc), code.signatures, casts);
        tvarXFinal = r.finalTypeVariableOf(code.varX);
        pc_HIGH_finalX_LOW =
                makeNaive(asList(leC(CHIGH, CTypes.variable(this.pc)),
                                 leC(CTypes.variable(tvarXFinal), CLOW)));

        expected = makeNaive(asList(leC(code.init.get(code.varX),
                                        r.finalTypeVariableOf(code.varX)),
                                    leC(this.pc, tvarXFinal)));
        assertThat(r.getConstraints(), is(equivalent(expected)));
        assertThat(r.getConstraints(),is(sat()));

        assertUnsat(r.getConstraints().add(pc_HIGH_finalX_LOW));

        /*********/
        /* x = y */
        /*********/
        // Actually this is very similar to x = x...
        s = j.newAssignStmt(code.localX, code.localY);
        r = typing.generate(s, code.init, singleton(this.pc), code.signatures, casts);
        tvarXFinal = r.finalTypeVariableOf(code.varX);
        pc_HIGH_finalX_LOW =
                makeNaive(asList(leC(CHIGH, CTypes.variable(this.pc)),
                                 leC(CTypes.variable(tvarXFinal), CLOW)));

        expected = makeNaive(asList(leC(code.init.get(code.varY),
                                        r.finalTypeVariableOf(code.varX)),
                                    leC(this.pc, tvarXFinal)));
        assertThat(r.getConstraints(), is(equivalent(expected)));
        assertThat(r.getConstraints(),is(sat()));

        assertUnsat(r.getConstraints().add(pc_HIGH_finalX_LOW));

        // final(y) and init(y) are the same
        assertEquals(code.init.get(code.varY),
                     r.finalTypeVariableOf(code.varY));

        TypeVar tvarYInitial = code.init.get(code.varY);

        ConstraintSet<Level> y_HIGH_x_LOW =
                makeNaive(asList(leC(CHIGH, CTypes.variable(tvarYInitial)),
                                 leC(CTypes.variable(tvarXFinal), CLOW)));
        assertUnsat(r.getConstraints().add(y_HIGH_x_LOW));

    }

    @Test public void testCall() throws TypingException {
        Stmt s;
        BodyTypingResult<Level> r;
        ConstraintSet<Level> expected;
        TypeVar finalX;
        TypeVar initX;
        TypeVar initY;
        TypeVar initZ;

        /***********/
        /* int x = y.testCallee() */
        /**********/
        s = j.newAssignStmt(code.localX,
                            j.newVirtualInvokeExpr(code.localY,
                                                   code.testCallee__int.makeRef()));
        r = typing.generate(s, code.init, singleton(this.pc), code.signatures, casts);
        initY = code.init.get(code.varY);
        finalX = r.finalTypeVariableOf(code.varX);

        expected = makeNaive(asList(leC(initY, finalX), leC(this.pc, finalX)));
        assertThat(r.getConstraints(), is(equivalent(expected)));

        /***********/
        /* int x = y.testCallee(x, z) */
        /**********/

        s = j.newAssignStmt(code.localX,
                            j.newVirtualInvokeExpr(code.localY,
                                                   code.testCallee_int_int__int.makeRef(),
                                                   asList(code.localX, code.localZ)));

        r = typing.generate(s, code.init, singleton(this.pc), code.signatures, casts);
        initX = code.init.get(code.varX);
        initY = code.init.get(code.varY);
        initZ = code.init.get(code.varZ);
        finalX = r.finalTypeVariableOf(code.varX);
        expected = makeNaive(asList(leC(initX, finalX),
                                    leC(initZ, finalX),
                                    leC(initY, finalX),
                                    leC(this.pc, finalX)));
        assertThat(r.getConstraints(), equivalent(expected));

        /***********/
        /* int x = y.ignoreSnd(x, z) */
        /**********/
        s = j.newAssignStmt(code.localX,
                            j.newVirtualInvokeExpr(code.localY,
                                                   code.ignoreSnd_int_int__int.makeRef(),
                                                   asList(code.localX, code.localZ)));

        r = typing.generate(s, code.init, singleton(this.pc), code.signatures, casts);
        initX = code.init.get(code.varX);
        initY = code.init.get(code.varY);
        initZ = code.init.get(code.varZ);
        finalX = r.finalTypeVariableOf(code.varX);
        expected = makeNaive(asList(leC(initX, finalX),
                                    leC(initY, finalX),
                                    leC(this.pc, finalX)));
        assertThat(r.getConstraints(), equivalent(expected));

        /***********/
        /* int x = y.writeToLow(x) */
        /**********/
        s = j.newAssignStmt(code.localX,
                            j.newVirtualInvokeExpr(code.localY,
                                                   code.writeToLowReturn0_int__int.makeRef(),
                                                   singletonList(code.localX)));
        r = typing.generate(s, code.init, singleton(this.pc), code.signatures, casts);
        initX = code.init.get(code.varX);
        initY = code.init.get(code.varY);
        finalX = r.finalTypeVariableOf(code.varX);
        expected = makeNaive(asList(leC(initY, finalX),
                                    leC(initX, CLOW),
                                    leC(this.pc, finalX),
                                    leC(this.pc, CLOW)));
        assertThat(r.getConstraints(), is(equivalent(expected)));
    }

    @Test public void testCast() throws TypingException {
        Stmt s;
        BodyTypingResult<Level> r;
        ConstraintSet<Level> expected;
        TypeVar finalX;
        TypeVar initX;
        TypeVar initY;
        TypeVar initZ;

        /***********/
    /* int x = (? <- H)y */
        /**********/
        s = j.newAssignStmt(code.localX,
                            j.newStaticInvokeExpr(castLowToDyn.makeRef(),
                                                  asList(code.localY)));
        r = typing.generate(s, code.init, singleton(this.pc), code.signatures, casts);
        initY = code.init.get(code.varY);
        finalX = r.finalTypeVariableOf(code.varX);
        expected = makeNaive(asList(leC(initY, CLOW),
                                    leC(pc, finalX),
                                    leC(CDYN, finalX)));
        assertThat(r.getConstraints(), is(equivalent(expected)));
    }

}
