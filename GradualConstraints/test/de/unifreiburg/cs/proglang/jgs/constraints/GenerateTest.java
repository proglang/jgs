package de.unifreiburg.cs.proglang.jgs.constraints;

import static org.junit.Assert.*;


import org.junit.Test;

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import soot.IntType;
import soot.Value;
import soot.jimple.Jimple;
import soot.jimple.Stmt;

import static de.unifreiburg.cs.proglang.jgs.constraints.TestDomain.*;

public class GenerateTest {
    
    private CTypeDomain<Level> ctypes;
    private Typing<Level> typing = mkTyping(ctypes);

    @Test
    public void testLocals() {
        Jimple j = Jimple.v();
        Value localX = j.newLocal("x", IntType.v());

        /* x = x */
        Stmt s = j.newAssignStmt(localX, localX);
        Typing<Level>.Result r = typing.generate(s);
        assertTrue(true);
    }

}
