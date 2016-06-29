package de.unifreiburg.cs.proglang.jgs.examples;

import soot.*;
import soot.jimple.IfStmt;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Hand-crafted jimple classes to illustrate how "selecitve" intrumentation should work.
 */
public class Code {

    static Jimple j = Jimple.v();

    // Class to hold exmaple methods and fields
    static SootClass exClass = makeClass("InstrumentationExamples");

    // Fields
    static SootField fieldA = makeField("fieldA", IntType.v(), exClass);
    static SootField fieldB = makeField("fieldB", RefType.v("java.lang.String"), exClass);
    static SootField fieldC = makeField("fieldC", RefType.v("java.lang.Object"), exClass);

    // Generic local variable
    static Local localX = j.newLocal("x", IntType.v());
    static Local localY = j.newLocal("y", IntType.v());
    static Local localZ = j.newLocal("z", IntType.v());

    // Generic parameters
    static ParameterRef param0 = j.newParameterRef(IntType.v(), 0);
    static ParameterRef param1 = j.newParameterRef(IntType.v(), 1);

    // A placeholder for the targets of if statements
    static Stmt placeholder = j.newNopStmt();

    // Methods

    /*
      int max(int p0, int p1) {
        int x;
        int y;
        int z;
       01: x := p0;
       02: y := p1;
        1: z = x;
        2: if (z < y) goto 3;
       22:   goto 4;
        3:   z = y;
        4: return z;
      }
     */
    public static final Stmt max_01_id_X_p0 = j.newIdentityStmt(localX, param0);
    public static final Stmt max_02_id_Y_p1 = j.newIdentityStmt(localY, param1);
    public static final Stmt max_1_assign_Z_X = j.newAssignStmt(localZ, localX);
    public static final Stmt max_4_return_Z = j.newReturnStmt(localZ);
    public static final Stmt max_22_goto_4 = j.newGotoStmt(max_4_return_Z);
    public static final IfStmt max_2_if_Z_lt_Y = j.newIfStmt(j.newLtExpr(localZ, localY), max_22_goto_4);
    public static final Stmt max_3_assign_Z_Y = j.newAssignStmt(localZ, localY);
    public static SootMethod max = makeMethod(exClass,
                                "max",
                                asList(IntType.v(), IntType.v()),
                                IntType.v(),
                                BodyBuilder.begin()
                                           .seq(max_01_id_X_p0)
                                           .seq(max_02_id_Y_p1)
                                           .seq(max_1_assign_Z_X)
                                           .seq(max_2_if_Z_lt_Y)
                                           .seq(max_22_goto_4)
                                           .seq(max_3_assign_Z_Y)
                                           .seq(max_4_return_Z)
                                           .build()
                                );


    /**
     * Helper method to create a fresh class.
     */
    private static SootClass makeClass(String name) {
        // reset the testClass
        if (Scene.v().containsClass(name)) {
            Scene.v().removeClass(Scene.v().getSootClass(name));
        }
        SootClass result = new SootClass(name);
        Scene.v().addClass(result);
        return result;
    }

    /**
     * Helper method to create a field.
     */
    private static SootField makeField(String name, Type t, SootClass cl) {
        SootField f = new SootField(name, t);
        cl.addField(f);
        return f;
    }

    /**
     * Helper method to create a method
     */
    private static SootMethod makeMethod(SootClass cl,
                                         String name,
                                         List<? extends Type> params,
                                         Type ret,
                                         Body b) {
        SootMethod m = new SootMethod(name, params, ret);
        cl.addMethod(m);
        m.setActiveBody(b);
        return m;
    }
}
