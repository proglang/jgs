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
        Value dest = j.newLocal("x", IntType.v());
        Stmt s = j.newAssignStmt(dest, dest);
        Generate g = new Generate();
        s.apply(g);
        assertTrue(g.getResult() == g.getResult());
    }

}
