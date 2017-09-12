package de.unifreiburg.cs.proglang.jgs.examples;

import soot.*;
import soot.jimple.*;
import soot.jimple.parser.node.TBoolConstant;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Hand-crafted jimple classes to illustrate how "selecitve" intrumentation should work.
 */
public class Code {

    static Jimple j = Jimple.v();

    // Class to hold exmaple methods and fields
    static SootClass exClass = makeClass("InstrumentationExamples");
    static SootClass exClass2 = makeClass("InstrumentationExamples2");
    static SootClass exClass3 = makeClass("InstrumentationExamples3");

    // Fields
    static SootField fieldA = makeField("fieldA", IntType.v(), exClass);
    static SootField fieldB = makeField("fieldB", RefType.v("java.lang.String"), exClass);
    static SootField fieldC = makeField("fieldC", RefType.v("java.lang.Object"), exClass);

    // Generic local variable
    public static Local localX = j.newLocal("x", IntType.v());
    public static Local localY = j.newLocal("y", IntType.v());
    public static Local localZ = j.newLocal("z", IntType.v());

    public static Local localB = j.newLocal("b", BooleanType.v());

    // Generic parameters
    static ParameterRef param0 = j.newParameterRef(IntType.v(), 0);
    static ParameterRef param1 = j.newParameterRef(IntType.v(), 1);
    static ParameterRef param2 = j.newParameterRef(BooleanType.v(), 2);

    // A placeholder for the targets of if statements
    static Stmt placeholder = j.newNopStmt();

    // Methods for max:
    /*
      int max(int p0, int p1) {
        int x;
        int y;
        int z;
       0: x := p0;
       1: y := p1;
       2: z = x;
       3: if (z < y) goto 5;
       4:   goto 6;
       5:   z = y;
       6: return z;
      }
     */
    public static final Stmt max_0_id_X_p0 = j.newIdentityStmt(localX, param0);
    public static final Stmt max_1_id_Y_p1 = j.newIdentityStmt(localY, param1);
    public static final Stmt max_2_assign_Z_X = j.newAssignStmt(localZ, localX);
    public static final Stmt max_6_return_Z = j.newReturnStmt(localZ);
    public static final Stmt max_4_goto_6 = j.newGotoStmt(max_6_return_Z);
    public static final Stmt max_5_assign_Z_Y = j.newAssignStmt(localZ, localY);
    public static final IfStmt max_3_if_Z_lt_Y = j.newIfStmt(j.newLtExpr(localZ, localY), max_5_assign_Z_Y);

    public static SootMethod max = makeMethod(exClass,
                                "max",
                                asList(IntType.v(), IntType.v()),
                                IntType.v(),
                                BodyBuilder.begin()
                                           .seq(max_0_id_X_p0)
                                           .seq(max_1_id_Y_p1)
                                           .seq(max_2_assign_Z_X)
                                           .seq(max_3_if_Z_lt_Y)
                                           .seq(max_4_goto_6)
                                           .seq(max_5_assign_Z_Y)
                                           .seq(max_6_return_Z)
                                           .build()
                                );


    // METHODS FOR ExampleTests2
     /*
      int sum( int p0, int p1) {
          int x;
          int y;
          int z;
          0: x := p0;
          1: y := p1;
          2: z := x + y;
          3: return z;
      }
     */
    public static final Stmt add_2_assign_Z_SUM_X_Y = j.newAssignStmt(localZ, j.newAddExpr(localX, localY));
    public static final Stmt add_3_return_Z = j.newReturnStmt(localZ);
    public static SootMethod sum = makeMethod(exClass2,
                                    "sum",
                                        asList(IntType.v(), IntType.v()),
                                        IntType.v(),
                                        BodyBuilder.begin()
                                            .seq(max_0_id_X_p0)
                                            .seq(max_1_id_Y_p1)
                                            .seq(add_2_assign_Z_SUM_X_Y)
                                            .seq(add_3_return_Z)
                                            .build()
                                );


    // Methods For ExampleTests3
    /*
      int update_on_bool(bool p0, int p1) {
          int x;
          boolean b;
          0: x := p0;
          1: b := p1;
          2: if not b goto 4;
          3: inc(x);
          4: return b;
      }
     */

    public static final Stmt up_0_id_X_p0 = j.newIdentityStmt(localX, param0);
    public static final Stmt up_1_id_B_p2 = j.newIdentityStmt(localB, param2);
    public static final Stmt up_4_return_B = j.newReturnStmt(localB);
    public static final Stmt up_2_if_not_B = j.newIfStmt(j.newEqExpr(localB, IntConstant.v(0)), up_4_return_B);
    public static final Stmt up_3_inc_B = j.newAssignStmt(localX, j.newAddExpr(localX, IntConstant.v(1)));
    public static SootMethod update = makeMethod(exClass3,
            "update",
            asList(BooleanType.v(), IntType.v()),
            IntType.v(),
            BodyBuilder.begin()
                    .seq(up_0_id_X_p0)
                    .seq(up_1_id_B_p2)
                    .seq(up_2_if_not_B)
                    .seq(up_3_inc_B)
                    .seq(up_4_return_B)
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
        List<Type> typeParams = new ArrayList<>();
        for (Type ty : params) {
            typeParams.add(ty);
        }
        SootMethod m = new SootMethod(name, typeParams, ret);
        cl.addMethod(m);
        m.setActiveBody(b);
        return m;
    }
}
