package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import org.junit.Before;
import org.junit.Test;
import soot.IntType;
import soot.jimple.Jimple;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;
import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class EnvironmentTest {

    private Jimple j;
    private TypeVars tvars;
    private Code code;

    @Before
    public void setUp() {
        this.tvars = new TypeVars("");
        this.j = Jimple.v();
        this.code = new Code(tvars);
    }

    @Test
    public void testJoin () {
        Environment before = code.init;
        Var<?> v = Var.fromLocal(j.newLocal("v", IntType.v()));
        TypeVars.TypeVar tv1 = tvars.fresh("v");
        TypeVars.TypeVar tv2 = tvars.fresh("v");
        Environment first = before.add(v, tv1);
        Environment second = before.add(v, tv2);

        Environment.JoinResult<Level> joinResult = Environment.join(first, second, tvars);
        Environment newEnv = joinResult.env;
        ConstraintSet<Level> cs = makeNaive(joinResult.constraints.collect(toList()));
        TypeVar freshTv = joinResult.env.get(v);
        assertThat(freshTv, not(anyOf(is(tv1), is(tv2))));

        ConstraintSet<Level> expected = makeNaive(asList(leC(tv1, freshTv), leC(tv2, freshTv)));
        assertThat(cs, is(equivalent(expected)));

    }
}
