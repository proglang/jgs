package utils.dominator;

import de.unifreiburg.cs.proglang.jgs.examples.BodyBuilder;
import org.junit.Before;
import org.junit.Test;
import soot.*;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;

import java.util.Arrays;

import static org.junit.Assert.*;

public class WriteEffectCollectorTest {

    private Body b;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void get() throws Exception {
        Local a = Jimple.v().newLocal("a", IntType.v());
        b = BodyBuilder.begin()
                       .seq(Jimple.v().newAssignStmt(a, IntConstant.v(0)))
                       .ite(
                               Jimple.v().newEqExpr(a, IntConstant.v(2)),
                               BodyBuilder.begin()
                                          .seq(Jimple.v().newAssignStmt(a, IntConstant.v(5)))
                                          .seq(Jimple.v().newAssignStmt(a, Jimple.v().newAddExpr(a, a))),
                               BodyBuilder.begin().seq(Jimple.v().newAssignStmt(a, IntConstant.v(5)))
                       )
                       .build();

        WriteEffectCollector<Local> dom = new WriteEffectCollector(Local.class, b);
        assertEquals(Arrays.asList(a), dom.get());
    }

    @Test
    public void get1() throws Exception {
        Local a = Jimple.v().newLocal("a", IntType.v());
        b = BodyBuilder.begin()
                       .seq(Jimple.v().newAssignStmt(a, IntConstant.v(0)))
                       .ite(
                               Jimple.v().newEqExpr(a, IntConstant.v(2)),
                               BodyBuilder.begin()
                                          .seq(Jimple.v().newAssignStmt(a, Jimple.v().newAddExpr(a, a)))
                                          .ite(Jimple.v().newEqExpr(a, IntConstant.v(5)),
                                               Jimple.v().newAssignStmt(a, IntConstant.v(42)),
                                               Jimple.v().newAssignStmt(a, IntConstant.v(88))
                                               )
                                          ,
                               BodyBuilder.begin()
                               .ite(Jimple.v().newEqExpr(a, IntConstant.v(5)),
                                   Jimple.v().newAssignStmt(a, IntConstant.v(42)),
                                   Jimple.v().newAssignStmt(a, IntConstant.v(88))
                               )
                                          .seq(Jimple.v().newAssignStmt(a, IntConstant.v(5)))
                       )
                       .build();
        WriteEffectCollector dom = new WriteEffectCollector(Local.class, b);
        assertEquals(Arrays.asList(a), dom.get());
    }
}