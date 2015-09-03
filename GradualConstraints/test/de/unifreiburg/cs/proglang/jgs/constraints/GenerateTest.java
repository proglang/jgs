package de.unifreiburg.cs.proglang.jgs.constraints;

import static org.junit.Assert.*;

import org.junit.Test;

import soot.IntType;
import soot.Value;
import soot.jimple.Jimple;
import soot.jimple.Stmt;

public class GenerateTest {

    @Test
    public void test() {
        Jimple j = Jimple.v();
        Value localX = j.newLocal("x", IntType.v());

        /* x = x */
        Stmt s = j.newAssignStmt(localX, localX);
        Generate g = new Generate();
        s.apply(g);
        /* TODO: trivial placeholder test */
        assertTrue(g.getResult() == g.getResult());
    }

}
