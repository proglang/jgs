package de.unifreiburg.cs.proglang.jgs.typing;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.typing.Typing;
import de.unifreiburg.cs.proglang.jgs.typing.Typing.TypeError;
import soot.IntType;
import soot.Local;
import soot.jimple.Jimple;
import soot.jimple.Stmt;

import static de.unifreiburg.cs.proglang.jgs.constraints.TestDomain.*;
import static java.util.Arrays.asList;

public class GenerateTest {

    private Typing<Level> typing;
    private TypeVars tvars;
    private Jimple j;
    private TypeVar pc;

    private static boolean equivalent(ConstraintSet<Level> cs1,
                                      ConstraintSet<Level> cs2) {
        return cs1.implies(cs2) && cs2.implies(cs1);
    }

    private void assertEquiv(ConstraintSet<Level> cs1,
                             ConstraintSet<Level> cs2) {
        assertTrue(String.format("Not equivalent\n%s\n%s",
                                 cs1.toString(),
                                 cs2.toString()),
                   equivalent(cs1, cs2));
    }

    @Before
    public void setUp() {
        this.tvars = new TypeVars("v");
        this.j = Jimple.v();
        this.pc = tvars.fresh();
        this.typing = mkTyping(tvars);
    }

    @Test
    public void testLocals() throws TypeError {
        Local localX = j.newLocal("x", IntType.v());
        Var<?> varX = Var.fromLocal(localX);
        TypeVar tvarX = tvars.fresh();

        Stmt s;
        Environment init;
        Typing.Result<Level> r;
        ConstraintSet<Level> expected, unexpected;
        TypeVar tvarXFinal;

        /* x = x */
        s = j.newAssignStmt(localX, localX);
        init = Environments.makeEmpty().add(varX, tvarX);
        r = typing.generate(s, init, this.pc);
        tvarXFinal = r.finalTypeVariableOf(varX);

        expected = makeNaive(asList(
                                    leC(r.initialTypeVariableOf(varX),
                                        r.finalTypeVariableOf(varX)),
                                    leC(this.pc, tvarXFinal)));
        assertEquiv(r.getConstraints(), expected);

        assertTrue(r.getConstraints().isSat());

        ConstraintSet<Level> pc_HIGH_finalX_LOW =
            makeNaive(asList(leC(CHIGH, CTypes.variable(this.pc)),
                             leC(CTypes.variable(tvarXFinal), CLOW)));
        ConstraintSet<Level> unsatConstraints =
            r.getConstraints().add(pc_HIGH_finalX_LOW);
        assertFalse(String.format("Should not be SAT: %s\nassignment: %s",
                                  unsatConstraints,
                                  unsatConstraints.satisfyingAssignment(Collections.emptyList())),
                    unsatConstraints.isSat());
    }

}
