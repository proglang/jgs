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

    Jimple j = Jimple.v();

    // Class to hold exmaple methods and fields
    SootClass exClass = makeClass("InstrumentationExamples");

    // Fields
    SootField fieldA = makeField("fieldA", IntType.v(), exClass);
    SootField fieldB = makeField("fieldB", RefType.v("java.lang.String"), exClass);
    SootField fieldC = makeField("fieldC", RefType.v("java.lang.Object"), exClass);

    // Generic local variable
    Local localX = j.newLocal("x", IntType.v());
    Local localY = j.newLocal("y", IntType.v());
    Local localZ = j.newLocal("z", IntType.v());

    // Generic parameters
    ParameterRef param0 = j.newParameterRef(IntType.v(), 0);
    ParameterRef param1 = j.newParameterRef(IntType.v(), 1);

    // A placeholder for the targets of if statements
    Stmt placeholder = j.newNopStmt();

    // Methods
    /*
      int max(int p0, int p1) {
        int z;
        1: z = p0;
        2: if (z < p1) {
        3:   z = p1;
           }
        4: return z;
      }
     */
    Stmt max_1_assign_Z_p0 = j.newAssignStmt(localZ, param0);
    IfStmt max_2_if_Z_lt_p1 = j.newIfStmt(j.newCmplExpr(localZ, param1), placeholder);
    Stmt max_3_assign_Z_p1 = j.newAssignStmt(localZ, param1);
    Stmt max_4_return_Z = j.newReturnStmt(localZ);
    //BodyBuilder max_3_assign_
    SootMethod max = makeMethod(exClass,
                                "max",
                                asList(IntType.v(), IntType.v()),
                                IntType.v(),
                                BodyBuilder.begin()
                                           .seq(max_1_assign_Z_p0)
                                           .ite(max_2_if_Z_lt_p1,
                                                BodyBuilder.begin()
                                                           .seq(max_3_assign_Z_p1),
                                                BodyBuilder.begin())
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
