package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes;
import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.SomeConstraintSets;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import org.junit.Before;
import org.junit.Test;
import soot.IntType;
import soot.jimple.Jimple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.variable;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;

public class MethodSignaturesTest {

    private Jimple j;
    private SomeConstraintSets cs;
    private TypeVars tvars;

    @Before public void setUp() throws Exception {
        this.j = Jimple.v();
        tvars = new TypeVars();
        this.cs = new SomeConstraintSets(tvars);
    }

    @Test public void testConstraints() {
        Symbol<Level> s1 = Symbol.param(0);
        Symbol<Level> s2 = Symbol.param(1);

        Map<Symbol<Level>, CType<Level>> mapping = new HashMap<>();
        mapping.put(s1, variable(cs.v1));
        mapping.put(s2, variable(cs.v2));
        mapping.put(Symbol.ret(), variable(cs.v3));

        // a typical constraint for an "add" method: @return >= x, @return >= y
        Stream<SigConstraint<Level>> sig =
                Stream.of(leS(s1, Symbol.ret()), leS(s2, Symbol.ret()));
        ConstraintSet<Level> sigAsCSet =
                makeNaive((makeSignature(2, sig.collect(toList()), emptyEffect())
                        .constraints.toTypingConstraints(mapping)).collect(Collectors.toSet()));
        assertThat(sigAsCSet, (equivalent(cs.x1_le_x3__x2_le_x3)));
    }
}