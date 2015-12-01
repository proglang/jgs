package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSetFactory;
import de.unifreiburg.cs.proglang.jgs.constraints.NaiveConstraintsFactory;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import org.junit.Before;
import org.junit.Test;
import soot.IntType;
import soot.jimple.Jimple;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.*;
import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;
import static org.junit.Assert.assertThat;

/**
 * Created by fennell on 12/1/15.
 */
public class ResultTest {

    private Jimple j;
    private TypeVars tvars;
    private Code code;
    private ConstraintSetFactory<LowHigh.Level> csets;

    @Before
    public void setUp() {
        this.tvars = new TypeVars();
        this.j = Jimple.v();
        this.code = new Code(tvars);
        this.csets = new NaiveConstraintsFactory<>(types);
    }

    @Test
    public void joinTest() {
        Environment before = code.init;
        Var<?> v = Var.fromLocal(j.newLocal("v", IntType.v()));
        TypeVars.TypeVar tv1 = tvars.testParam(v, "1");
        TypeVars.TypeVar tv2 = tvars.testParam(v, "2");
        Environment first = before.add(v, tv1);
        Environment second = before.add(v, tv2);
        ConstraintSet<Level> cs1 = csets.fromCollection(Collections.singletonList(leC(code.tvarX, tv1)));
        ConstraintSet<Level> cs2 = csets.fromCollection(Collections.singletonList(leC(code.tvarY, tv2)));
        Result<Level> r1 = Result.addConstraints(Result.fromEnv(csets, first), cs1);
        Result<Level> result = Result.join(r1,
                Result.addConstraints(Result.fromEnv(csets, second), cs2), csets, tvars
        );

        TypeVar freshV = tvars.join(tv1, tv2);
        ConstraintSet<Level> cs = cs1.add(cs2).add(leC(tv1, freshV)).add(leC(tv2, freshV));
        Result<Level> expected = Result.addConstraints(Result.fromEnv(csets, before.add(v, freshV)), cs);


        assertThat(result, is(equalTo(expected)));

        assertThat(Result.join(Result.trivialCase(csets), r1, csets, tvars), is(r1));
        assertThat(Result.join(r1, Result.trivialCase(csets), csets, tvars), is(r1));
    }
}
