package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.SomeConstraintSets;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import org.junit.Before;
import org.junit.Test;
import soot.IntType;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static de.unifreiburg.cs.proglang.jgs.typing.MethodSignatures.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;

/**
 * Created by fennell on 10/29/15.
 */
public class MethodSignaturesTest {

    private Jimple j;
    private SomeConstraintSets cs;
    private TypeVars tvars;

    @Before public void setUp() throws Exception {
        this.j = Jimple.v();
        tvars = new TypeVars("x");
        this.cs = new SomeConstraintSets(tvars);
    }

    @Test public void testConstraints() {
        ParameterRef x = j.newParameterRef(IntType.v(), 0);
        ParameterRef y = j.newParameterRef(IntType.v(), 1);

        Symbol<Level> s1 = param(x);
        Symbol<Level> s2 = param(y);

        Map<Symbol<Level>, TypeVar> mapping = new HashMap<>();
        mapping.put(s1, cs.v1);
        mapping.put(s2, cs.v2);
        mapping.put(ret(), cs.v3);

        // a typical constraint for an "add" method: @return >= x, @return >= y
        List<SigConstraint<Level>> sig =
                asList(leS(param(x), ret()), leS(param(y), ret()));
        ConstraintSet<Level> sigAsCSet =
                makeNaive((signatureConstraints(sig).toTypingConstraints(mapping)).collect(Collectors.toSet()));
        assertThat(sigAsCSet, (equivalent(cs.x1_le_x3__x2_le_x3)));
    }
}