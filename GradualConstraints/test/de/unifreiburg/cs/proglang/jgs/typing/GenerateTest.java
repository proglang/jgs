package de.unifreiburg.cs.proglang.jgs.typing;

import static org.junit.Assert.*;

import org.junit.Test;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.typing.Typing;
import soot.IntType;
import soot.Local;
import soot.jimple.Jimple;
import soot.jimple.Stmt;

import static de.unifreiburg.cs.proglang.jgs.constraints.TestDomain.*;
import static java.util.Arrays.asList;

public class GenerateTest {

    private CTypeDomain<Level> ctypes;
    private Typing<Level> typing = mkTyping(ctypes);

    private static boolean equivalent(ConstraintSet<Level> cs1,
                                      ConstraintSet<Level> cs2) {
        return cs1.implies(cs2) && cs2.implies(cs1);
    }

    @Test
    public void testLocals() {
        Jimple j = Jimple.v();
        Local localX = j.newLocal("x", IntType.v());

        Stmt s;
        Typing<Level>.Result r;

        /* x = x */
        s = j.newAssignStmt(localX, localX);
        r = typing.generate(s);
        assertTrue(equivalent(r.getConstraints(),
                              makeNaive(asList(leC(r.finalTypeVariableOf(localX),
                                                   r.finalTypeVariableOf(localX))))));
    }

}
