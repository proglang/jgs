package de.unifreiburg.cs.proglang.jgs.constraints;

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.TestDomain.*;
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
        v0 = tvars.fresh();
        v1 = tvars.fresh();
        v2 = tvars.fresh();
        v3 = tvars.fresh();

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
