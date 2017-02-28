package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.SomeConstraintSets;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.signatures.Effects;
import de.unifreiburg.cs.proglang.jgs.signatures.SigConstraint;
import de.unifreiburg.cs.proglang.jgs.signatures.Param;
import de.unifreiburg.cs.proglang.jgs.signatures.Return;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import de.unifreiburg.cs.proglang.jgs.util.Interop;
import org.junit.Before;
import org.junit.Test;
import soot.jimple.Jimple;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.variable;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.*;

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
        Symbol<Level> s1 = new Param<>(0);
        Symbol<Level> s2 = new Param<>(1);

        Map<Symbol<Level>, CType<Level>> mapping = new HashMap<>();
        mapping.put(s1, variable(cs.v1));
        mapping.put(s2, variable(cs.v2));
        mapping.put(new Return<>(), variable(cs.v3));

        // a typical constraint for an "add" method: @return >= x, @return >= y
        Stream<SigConstraint<Level>> sig =
                Stream.of(leS(s1, new Return<>()), leS(s2, new Return<>()));
        ConstraintSet<Level> sigAsCSet =
                makeNaive(Interop.asJavaStream((makeSignature(2, sig.collect(toList()), Effects.emptyEffect())
                        .constraints.toTypingConstraints(mapping))).collect(Collectors.toSet()));
        assertThat(sigAsCSet, (equivalent(cs.x1_le_x3__x2_le_x3)));
    }
}