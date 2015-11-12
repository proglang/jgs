package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.typing.Typing.TypeError;
import org.junit.Before;
import org.junit.Test;
import soot.*;
import soot.jimple.Jimple;
import soot.jimple.Stmt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static de.unifreiburg.cs.proglang.jgs.constraints.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable.makeTable;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;

public class GenerateTest {

    private Typing<Level> typing;
    private TypeVars tvars;
    private Jimple j;
    private TypeVar pc;
    private Local localX, localY, localZ;
    private Var<?> varX, varY, varZ;
    private TypeVar tvarX, tvarY, tvarZ;

    // classes and methods for tests
    private SignatureTable<Level> signatures;
    private SootClass testClass;
    private SootMethod testCallee__int;
    private SootField testLowField_int;

    private SootMethod testCallee_int_int__int;
    private SootMethod ignoreSnd_int_int__int;
    private SootMethod writeToLowReturn0_int__int;

    // initial environment for the testcases
    private Environment init;

    private void assertUnsat(ConstraintSet<Level> unsatConstraints) {
        assertFalse(String.format("Should not be SAT: %s\nassignment: %s",
                                  unsatConstraints,
                                  unsatConstraints.satisfyingAssignment(
                                          Collections.emptyList())),
                    unsatConstraints.isSat());
    }

    @Before public void setUp() {
        this.tvars = new TypeVars("v");
        this.j = Jimple.v();
        this.pc = tvars.fresh();
        this.typing = mkTyping(tvars);

        this.localX = j.newLocal("x", IntType.v());
        this.localY = j.newLocal("y", IntType.v());
        this.localZ = j.newLocal("z", IntType.v());
        this.varX = Var.fromLocal(localX);
        this.varY = Var.fromLocal(localY);
        this.varZ = Var.fromLocal(localZ);
        this.tvarX = tvars.fresh();
        this.tvarY = tvars.fresh();
        this.tvarZ = tvars.fresh();

        this.init = Environments.makeEmpty()
                                .add(varX, tvarX)
                                .add(varY, tvarY)
                                .add(varZ, tvarZ);

        this.testClass = new SootClass("TestClass");
        this.testLowField_int = new SootField("testLowField", IntType.v());
        testClass.addField(this.testLowField_int);

        Map<SootMethod, Signature<Level>> sigMap = new HashMap<>();
        Symbol.Param<Level> param_x = param(IntType.v(), 0);
        Symbol.Param<Level> param_y = param(IntType.v(), 1);

        // Method:
        this.testCallee__int = new SootMethod("testCallee",
                                              Collections.emptyList(),
                                              IntType.v());
        this.testClass.addMethod(testCallee__int);
        sigMap.put(this.testCallee__int,
                   makeSignature(signatureConstraints(Collections.singleton(leS(
                           Symbol.literal(PUB),
                           ret()))), emptyEffect()));

        // Method:
        this.testCallee_int_int__int = new SootMethod("testCallee",
                                                      asList(IntType.v(),
                                                             IntType.v()),
                                                      IntType.v());
        this.testClass.addMethod(testCallee_int_int__int);
        SigConstraintSet<Level> sigCstrs =
                signatureConstraints(asList(leS(param_x, ret()),
                                            leS(param_y, ret())));
        sigMap.put(this.testCallee_int_int__int,
                   makeSignature(sigCstrs, emptyEffect()));

        // Method:
        this.ignoreSnd_int_int__int = new SootMethod("ignoreSnd",
                                                     asList(IntType.v(),
                                                            IntType.v()),
                                                     IntType.v());
        this.testClass.addMethod(ignoreSnd_int_int__int);
        sigCstrs = signatureConstraints(asList(leS(param_x, ret())));
        sigMap.put(this.ignoreSnd_int_int__int,
                   makeSignature(sigCstrs, emptyEffect()));

        //Method:
        this.writeToLowReturn0_int__int = new SootMethod("writeToLow",
                                             singletonList(IntType.v()),
                                             IntType.v());
        this.testClass.addMethod(this.writeToLowReturn0_int__int);
        sigCstrs = signatureConstraints(asList((leS(param_x, literal(TLOW)))));
        sigMap.put(this.writeToLowReturn0_int__int, makeSignature(sigCstrs, effects(TLOW)));

        // freeze signatures
        this.signatures = makeTable(sigMap);
    }

    @Test public void testLocals() throws TypeError {

        Stmt s;
        Typing.Result<Level> r;
        ConstraintSet<Level> expected;
        TypeVar tvarXFinal;
        ConstraintSet<Level> pc_HIGH_finalX_LOW;

        /*********/
        /* x = x */
        /*********/
        s = j.newAssignStmt(localX, localX);
        r = typing.generate(s, init, this.pc, signatures);
        tvarXFinal = r.finalTypeVariableOf(varX);
        pc_HIGH_finalX_LOW =
                makeNaive(asList(leC(CHIGH, CTypes.variable(this.pc)),
                                 leC(CTypes.variable(tvarXFinal), CLOW)));

        expected = makeNaive(asList(leC(r.initialTypeVariableOf(varX),
                                        r.finalTypeVariableOf(varX)),
                                    leC(this.pc, tvarXFinal)));
        assertThat(r.getConstraints(), is(equivalent(expected)));
        assertTrue(r.getConstraints().isSat());

        assertUnsat(r.getConstraints().add(pc_HIGH_finalX_LOW));

        /*********/
        /* x = y */
        /*********/
        // Actually this is very similar to x = x...
        s = j.newAssignStmt(localX, localY);
        r = typing.generate(s, init, this.pc, signatures);
        tvarXFinal = r.finalTypeVariableOf(varX);
        pc_HIGH_finalX_LOW =
                makeNaive(asList(leC(CHIGH, CTypes.variable(this.pc)),
                                 leC(CTypes.variable(tvarXFinal), CLOW)));

        expected = makeNaive(asList(leC(r.initialTypeVariableOf(varY),
                                        r.finalTypeVariableOf(varX)),
                                    leC(this.pc, tvarXFinal)));
        assertThat(r.getConstraints(), is(equivalent(expected)));
        assertTrue(r.getConstraints().isSat());

        assertUnsat(r.getConstraints().add(pc_HIGH_finalX_LOW));

        // final(y) and init(y) are the same
        assertEquals(r.initialTypeVariableOf(varY),
                     r.finalTypeVariableOf(varY));

        TypeVar tvarYInitial = r.initialTypeVariableOf(varY);

        ConstraintSet<Level> y_HIGH_x_LOW =
                makeNaive(asList(leC(CHIGH, CTypes.variable(tvarYInitial)),
                                 leC(CTypes.variable(tvarXFinal), CLOW)));
        assertUnsat(r.getConstraints().add(y_HIGH_x_LOW));

    }

    @Test public void testCall() throws TypeError {
        Stmt s;
        Typing.Result<Level> r;
        ConstraintSet<Level> expected;
        TypeVar finalX;
        TypeVar initX;
        TypeVar initY;
        TypeVar initZ;

        /***********/
        /* int x = y.testCallee() */
        /**********/
        s = j.newAssignStmt(localX,
                            j.newVirtualInvokeExpr(localY,
                                                   testCallee__int.makeRef()));
        r = typing.generate(s, this.init, this.pc, signatures);
        initY = r.initialTypeVariableOf(varY);
        finalX = r.finalTypeVariableOf(varX);

        expected = makeNaive(asList(leC(initY, finalX), leC(this.pc, finalX)));
        assertThat(r.getConstraints(), is(equivalent(expected)));

        /***********/
        /* int x = y.testCallee(x, z) */
        /**********/

        s = j.newAssignStmt(localX,
                            j.newVirtualInvokeExpr(localY,
                                                   testCallee_int_int__int.makeRef(),
                                                   asList(localX, localZ)));

        r = typing.generate(s, this.init, this.pc, signatures);
        initX = r.initialTypeVariableOf(varX);
        initY = r.initialTypeVariableOf(varY);
        initZ = r.initialTypeVariableOf(varZ);
        finalX = r.finalTypeVariableOf(varX);
        expected = makeNaive(asList(leC(initX, finalX),
                                    leC(initZ, finalX),
                                    leC(initY, finalX),
                                    leC(this.pc, finalX)));
        assertThat(r.getConstraints(), equivalent(expected));

        /***********/
        /* int x = y.ignoreSnd(x, z) */
        /**********/
        s = j.newAssignStmt(localX,
                            j.newVirtualInvokeExpr(localY,
                                                   ignoreSnd_int_int__int.makeRef(),
                                                   asList(localX, localZ)));

        r = typing.generate(s, this.init, this.pc, signatures);
        initX = r.initialTypeVariableOf(varX);
        initY = r.initialTypeVariableOf(varY);
        initZ = r.initialTypeVariableOf(varZ);
        finalX = r.finalTypeVariableOf(varX);
        expected = makeNaive(asList(leC(initX, finalX),
                                    leC(initY, finalX),
                                    leC(this.pc, finalX)));
        assertThat(r.getConstraints(), equivalent(expected));

        /***********/
        /* int x = y.writeToLow(x) */
        /**********/
        s = j.newAssignStmt(localY,
                            j.newVirtualInvokeExpr(localY,
                                                   writeToLowReturn0_int__int.makeRef(),
                                                   singletonList(localX)));
        r = typing.generate(s, this.init, this.pc, signatures);
        initX = r.initialTypeVariableOf(varX);
        initY = r.initialTypeVariableOf(varY);
        finalX = r.finalTypeVariableOf(varX);
        expected = makeNaive(asList(leC(initY, finalX),
                                    leC(initX, CLOW),
                                    leC(this.pc, finalX),
                                    leC(this.pc, CLOW)));
        assertThat(r.getConstraints(), equivalent(expected));
    }


}
