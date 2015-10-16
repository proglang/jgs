package de.unifreiburg.cs.proglang.jgs.typing;

import static org.junit.Assert.*;

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

        /* x = x */
        s = j.newAssignStmt(localX, localX);
        init = Environments.makeEmpty().add(varX, tvarX);
        r = typing.generate(s,init,this.pc);
        assertTrue(equivalent(r.getConstraints(),
                              makeNaive(asList(leC(r.finalTypeVariableOf(varX),
                                                   r.finalTypeVariableOf(varX))))));
    }

}
