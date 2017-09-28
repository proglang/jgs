package utils.dominator;

import de.unifreiburg.cs.proglang.jgs.examples.BodyBuilder;
import org.junit.Before;
import org.junit.Test;
import soot.*;
import soot.jimple.*;
import utils.BodyGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class WriteEffectCollectorTest {

    /**
     * Test for a simple if program: <br>
     * <code>
     *     a = 5;
     *     b = 4;
     *     if (a == 2) {
     *         b = 3;
     *     } else {
     *         a = 4;
     *     }
     * </code> <br/>
     * <b>Write Effect</b> is then {a, b}
     * @throws Exception
     */
    @Test
    public void SimpleIf() throws Exception {
        Local a = Jimple.v().newLocal("a", IntType.v());
        Local b = Jimple.v().newLocal("b", IntType.v());

        Stmt asA5 = Jimple.v().newAssignStmt(a, IntConstant.v(5));
        Stmt asB4 = Jimple.v().newAssignStmt(b, IntConstant.v(4));

        Stmt asB3 = Jimple.v().newAssignStmt(b, IntConstant.v(3));
        Stmt asA4 = Jimple.v().newAssignStmt(a, IntConstant.v(4));

        Stmt ifA2 = Jimple.v().newIfStmt(
                Jimple.v().newEqExpr(a, IntConstant.v(2)),
                asB3
                );

        Stmt ifEnd = Jimple.v().newNopStmt();
        Stmt goEnd = Jimple.v().newGotoStmt(ifEnd);

        Body body = BodyBuilder.begin()
                            .seq(asA5).seq(asB4)
                            .seq(ifA2).seq(asA4).seq(goEnd) // else branch
                               .seq(asB3).seq(ifEnd) // then branch
                               .build();

        WriteEffectCollector coll = new WriteEffectCollector(body);
        coll.collectWriteEffect();
        assertEquals(new HashSet<>(Arrays.asList(a, b)), coll.get( Local.class, ifEnd));
    }

    /**
     * Test for a if program with a branching factor of 3: <br>
     * <code>
     *     a = 3;
     *     b = 2;
     *     c = 7;
     *     if (a == 4) {
     *         c = 5;
     *     } else if (a == 3) {
     *         a = b + c;
     *     } else {
     *         b = a - c;
     *     }
     * </code>
     * @throws Exception Any Exception, that appears while testing
     */
    @Test
    public void IfBranchFacThree() throws Exception {
        Local a = Jimple.v().newLocal("a", IntType.v());
        Local b = Jimple.v().newLocal("b", IntType.v());
        Local c = Jimple.v().newLocal("c", IntType.v());

        Stmt asA3 = Jimple.v().newAssignStmt(a, IntConstant.v(3));
        Stmt asB2 = Jimple.v().newAssignStmt(b, IntConstant.v(2));
        Stmt asC7 = Jimple.v().newAssignStmt(c, IntConstant.v(7));

        Stmt asC5 = Jimple.v().newAssignStmt(c, IntConstant.v(5));
        Stmt adBC = Jimple.v().newAssignStmt(a, Jimple.v().newAddExpr(b, c));
        Stmt sbAC = Jimple.v().newAssignStmt(b, Jimple.v().newSubExpr(a, c));

        Stmt ifEnd = Jimple.v().newNopStmt();
        Stmt gEnd1 = Jimple.v().newGotoStmt(ifEnd);
        Stmt gEnd2 = Jimple.v().newGotoStmt(ifEnd);

        Stmt ifA4 = Jimple.v().newIfStmt(Jimple.v().newEqExpr(a, IntConstant.v(4)), asC5);
        Stmt ifA3 = Jimple.v().newIfStmt(Jimple.v().newEqExpr(a, IntConstant.v(3)), adBC);

        Body body = BodyBuilder.begin()
                               .seq(asA3).seq(asB2).seq(asC7)
                               .seq(ifA4)
                               .seq(ifA3).seq(sbAC).seq(gEnd1) // Else branch
                               .seq(adBC).seq(gEnd2) // else if branch
                               .seq(asC5).seq(ifEnd) // if branch
                .build();

        WriteEffectCollector coll = new WriteEffectCollector(body);
        coll.collectWriteEffect();
        assertEquals(new HashSet<>(Arrays.asList(a, b, c)), coll.get(Local.class, ifEnd));
    }

    /**
     * Test for a if program with a nesting factor of 2: <br>
     * <code>
     *     a = 3;
     *     b = 2 * a;
     *     c = a - b;
     *     if (b == c) {
     *         c = 5;
     *         if (a == 3) {
     *             b = 2;
     *         }
     *     } else {
     *         a = b + c;
     *     }
     * </code>
     * @throws Exception Any Exception, that appears while testing
     */
    @Test
    public void IfNestingDepthTwo() throws Exception {
        Local a = Jimple.v().newLocal("a", IntType.v());
        Local b = Jimple.v().newLocal("b", IntType.v());
        Local c = Jimple.v().newLocal("c", IntType.v());

        Stmt asA = Jimple.v().newAssignStmt(a, IntConstant.v(3));
        Stmt asB = Jimple.v().newAssignStmt(b, Jimple.v().newMulExpr(IntConstant.v(2), a));
        Stmt asC = Jimple.v().newAssignStmt(c, Jimple.v().newSubExpr(a, b));

        Stmt asC5 = Jimple.v().newAssignStmt(c, IntConstant.v(5));
        Stmt asB2 = Jimple.v().newAssignStmt(b, IntConstant.v(2));
        Stmt addS = Jimple.v().newAssignStmt(a, Jimple.v().newAddExpr(b, c));

        Stmt if1 = Jimple.v().newIfStmt(Jimple.v().newEqExpr(b, c), asC5);
        Stmt if2 = Jimple.v().newIfStmt(Jimple.v().newEqExpr(a, IntConstant.v(3)), asB2);

        Stmt ifEnd1 = Jimple.v().newNopStmt();
        Stmt ifEnd2 = Jimple.v().newNopStmt();
        Stmt goEnd1 = Jimple.v().newGotoStmt(ifEnd1);
        Stmt goEnd2 = Jimple.v().newGotoStmt(ifEnd2);

        Body body = BodyBuilder.begin()
                               .seq(asA).seq(asB).seq(asC)
                               .seq(if1)
                                   .seq(addS).seq(goEnd1) // IF-1 else branch
                               .seq(asC5) // IF - 1 then branch
                                   .seq(if2).seq(goEnd2) // IF-2 not present else branch
                                   .seq(asB2).seq(ifEnd2) // IF-2 then branch
                                   .seq(ifEnd1)
                               .build();

        WriteEffectCollector col = new WriteEffectCollector(body);
        col.collectWriteEffect();
        // Write Effect of IF - 1, shall be c and a. b is only effected within IF 2
        assertEquals(new HashSet<>(Arrays.asList(a, c)), col.get(Local.class, ifEnd1));
        assertEquals(new HashSet<>(Arrays.asList(b)), col.get(Local.class, ifEnd2));
    }

    /**
     * A Test for an if program, with no Post Dominator:
     * <br><code>
     *     a = 30;
     *     b = 12;
     *     if (b == input) {
     *         return a;
     *     } else {
     *         return b;
     *     }
     * </code>
     *
     * @throws Exception Any Exception, that appears while testing
     */
    @Test
    public void IfNoPostDom() throws Exception {
        // Creating Jimple Class and Method, as well as Body without BodyBuilder,
        // because of the return types
        SootClass testClass = new SootClass("TestClass");
        SootMethod testMethod = new SootMethod("example", Collections.singletonList(IntType.v()), IntType.v());
        testClass.addMethod(testMethod);
        Scene.v().addClass(testClass);

        Body body = Jimple.v().newBody(testMethod);
        PatchingChain<Unit> chain = body.getUnits();

        Local a = Jimple.v().newLocal("a", IntType.v());
        Local b = Jimple.v().newLocal("b", IntType.v());
        Local in = Jimple.v().newLocal("input", IntType.v());

        Stmt inP = Jimple.v().newIdentityStmt(in, Jimple.v().newParameterRef(IntType.v(), 0));

        Stmt asA = Jimple.v().newAssignStmt(a, IntConstant.v(30));
        Stmt asB = Jimple.v().newAssignStmt(a, IntConstant.v(12));

        Stmt retA = Jimple.v().newReturnStmt(a);
        Stmt retB = Jimple.v().newReturnStmt(b);

        Value cond = Jimple.v().newEqExpr(b, in);

        Stmt if1 = Jimple.v().newIfStmt(cond, retA);

        chain.add(inP);
        chain.add(asA);
        chain.add(asB);
        chain.add(if1);
        chain.add(retB);
        chain.add(retA);

        WriteEffectCollector col = new WriteEffectCollector(body);
        col.collectWriteEffect();
        assertEquals(Collections.emptySet(), col.getAll(Local.class));
    }

    /**
     * A so called Spaghetti Code, that has Jumps, that can not be expressed by
     * normal Java.
     */
    @Test
    public void SimplerJumpCode() {
        Local a = Jimple.v().newLocal("a", IntType.v());
        Local b = Jimple.v().newLocal("b", IntType.v());

        Stmt asB = Jimple.v().newAssignStmt(b, IntConstant.v(3));
        Stmt asA = Jimple.v().newAssignStmt(a, IntConstant.v(2));


        Stmt if1 = Jimple.v().newIfStmt(Jimple.v().newEqExpr(a, b), asB);
        Stmt el1 = Jimple.v().newGotoStmt(asA);

        Stmt if2 = Jimple.v().newIfStmt(Jimple.v().newEqExpr(b, IntConstant.v(3)), if1);

        Body body = BodyBuilder.begin()
                               .seq(if1).seq(el1).seq(asB)
                               .seq(if2).seq(asA)
                               .build();

        WriteEffectCollector col = new WriteEffectCollector(body);
        col.collectWriteEffect();
        assertEquals(new HashSet<>(Arrays.asList(a, b)), col.get(Local.class, asA));
    }

    /**
     * Tests a post condition <br>
     * <code>
     *     a = 3;
     *     b = 4;
     *     do {
     *         a = 5
     *     } while (a == b)
     *     b = 3;
     * </code>
     */
    @Test
    public void PostConditionJump() {
        Local a = Jimple.v().newLocal("a", IntType.v());
        Local b = Jimple.v().newLocal("b", IntType.v());

        Stmt asB = Jimple.v().newAssignStmt(b, IntConstant.v(3));
        Stmt asA = Jimple.v().newAssignStmt(a, IntConstant.v(5));

        Stmt if1 = Jimple.v().newIfStmt(Jimple.v().newEqExpr(a, b), asA);

        Body body = BodyBuilder.begin()
                               .seq(Jimple.v().newAssignStmt(a, IntConstant.v(3)))
                               .seq(Jimple.v().newAssignStmt(b, IntConstant.v(4)))
                               .seq(asA)
                               .seq(if1)
                               .seq(asB)
                               .build();
        WriteEffectCollector col = new WriteEffectCollector(body);
        col.collectWriteEffect();
        assertEquals(new HashSet<>(Arrays.asList(a)), col.getAll(Local.class));
    }

}