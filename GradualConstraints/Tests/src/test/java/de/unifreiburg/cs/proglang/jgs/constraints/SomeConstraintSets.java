package de.unifreiburg.cs.proglang.jgs.constraints;

import de.unifreiburg.cs.proglang.jgs.instrumentation.Var;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Vars;
import soot.IntType;
import soot.jimple.Jimple;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.*;
import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static java.util.Arrays.asList;

/**
 * Created by fennell on 10/29/15.
 */
public class SomeConstraintSets {

    public final TypeVar v0, v1, v2, v3;
    public final CType<Level> x0, x1, x2, x3;

    public final ConstraintSet<Level> x0_le_x1_le_x2_le_x3_le_x1;
    public final ConstraintSet<Level> x1_le_H_le_x2__x1_le_x2;
    public final ConstraintSet<Level> pub_le_x_le_dyn;
    public final ConstraintSet<Level> x1_le_x3__x2_le_x3;

    public SomeConstraintSets(TypeVars tvars) {
        Var<?> l0 = Vars.fromLocal(Jimple.v().newLocal("v0", IntType.v()));
        Var<?> l1 = Vars.fromLocal(Jimple.v().newLocal("v1", IntType.v()));
        Var<?> l2 = Vars.fromLocal(Jimple.v().newLocal("v2", IntType.v()));
        Var<?> l3 = Vars.fromLocal(Jimple.v().newLocal("v3", IntType.v()));
        v0 = tvars.testParam(l0, "");
        v1 = tvars.testParam(l1, "");
        v2 = tvars.testParam(l2, "");
        v3 = tvars.testParam(l3, "");

        x0 = variable(v0);
        x1 = variable(v1);
        x2 = variable(v2);
        x3 = variable(v3);

        x0_le_x1_le_x2_le_x3_le_x1 =
                makeNaive(asList(leC(x0, x1), leC(x1, x2), leC(x3, x1)));
        x1_le_H_le_x2__x1_le_x2 = makeNaive(asList(leC(x1, literal(THIGH)),
                                                   leC(literal(THIGH), x2),
                                                   leC(x1, x2)));
        pub_le_x_le_dyn =
                makeNaive(asList(leC(x1, literal(DYN)), leC(literal(PUB), x1)));

        x1_le_x3__x2_le_x3 =
                makeNaive(asList(leC(x1, x3), leC(x2, x3)));
    }

}
