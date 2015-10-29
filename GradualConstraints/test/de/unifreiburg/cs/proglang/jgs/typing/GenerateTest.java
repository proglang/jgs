package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.typing.Typing.TypeError;
import org.junit.Before;
import org.junit.Test;
import soot.IntType;
import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Jimple;
import soot.jimple.Stmt;

import java.util.Collections;
import java.util.Map;

import static de.unifreiburg.cs.proglang.jgs.constraints.TestDomain.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class GenerateTest {

    private Typing<Level> typing;
    private TypeVars tvars;
    private Jimple j;
    private TypeVar pc;
    private Local localX, localY;
    private Var<?> varX, varY;
    private TypeVar tvarX, tvarY;

    // classes and methods for tests
    private Map<SootMethod, MethodSignatures<Level>> signatures;
    private SootClass testClass;
    private SootMethod testCallee__int;

    private Environment init; // initial environment for the testcases

    private void assertUnsat(ConstraintSet<Level> unsatConstraints) {
        assertFalse(String.format("Should not be SAT: %s\nassignment: %s",
                                  unsatConstraints,
                                  unsatConstraints.satisfyingAssignment(Collections.emptyList())),
                    unsatConstraints.isSat());
    }

    @Before
    public void setUp() {
        this.tvars = new TypeVars("v");
        this.j = Jimple.v();
        this.pc = tvars.fresh();
        this.typing = mkTyping(tvars);

        this.localX = j.newLocal("x", IntType.v());
        this.localY = j.newLocal("y", IntType.v());
        this.varX = Var.fromLocal(localX);
        this.varY = Var.fromLocal(localY);
        this.tvarX = tvars.fresh();
        this.tvarY = tvars.fresh();

        this.init = Environments.makeEmpty().add(varX, tvarX).add(varY, tvarY);

        this.testClass = new SootClass("TestClass");
        this.testCallee__int =
            new SootMethod("testCallee", Collections.emptyList(), IntType.v());
        this.testClass.addMethod(testCallee__int);
    }

    @Test
    public void testLocals() throws TypeError {

        Stmt s;
        Typing.Result<Level> r;
        ConstraintSet<Level> expected;
        TypeVar tvarXFinal;
        ConstraintSet<Level> pc_HIGH_finalX_LOW;

        /*********/
        /* x = x */
        /*********/
        s = j.newAssignStmt(localX, localX);
        r = typing.generate(s, init, this.pc);
        tvarXFinal = r.finalTypeVariableOf(varX);
        pc_HIGH_finalX_LOW =
            makeNaive(asList(leC(CHIGH, CTypes.variable(this.pc)),
                             leC(CTypes.variable(tvarXFinal), CLOW)));

        expected = makeNaive(asList(
                                    leC(r.initialTypeVariableOf(varX),
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
        r = typing.generate(s, init, this.pc);
        tvarXFinal = r.finalTypeVariableOf(varX);
        pc_HIGH_finalX_LOW =
            makeNaive(asList(leC(CHIGH, CTypes.variable(this.pc)),
                             leC(CTypes.variable(tvarXFinal), CLOW)));

        expected = makeNaive(asList(
                                    leC(r.initialTypeVariableOf(varY),
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

    @Test
    public void testCall() throws TypeError {
        Stmt s;
        Typing.Result<Level> r;
        ConstraintSet<Level> expected;
        TypeVar finalX;
        TypeVar initY;

        /***********/
        /* int x = y.testCallee() */
        /**********/
        s = j.newAssignStmt(localX,
                            j.newVirtualInvokeExpr(localY,
                                                   testCallee__int.makeRef()));
        r = typing.generate(s, this.init, this.pc);
        initY = r.initialTypeVariableOf(varY);
        finalX = r.finalTypeVariableOf(varX);
                
        expected = makeNaive(asList(leC(initY, finalX), leC(this.pc, finalX)));
        assertThat(r.getConstraints(), is(equivalent(expected)));
    }

}
