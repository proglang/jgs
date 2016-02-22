package de.unifreiburg.cs.proglang.jgs.signatures;

import de.unifreiburg.cs.proglang.jgs.TestDomain.*;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.SigConstraint;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.Signature;
import org.junit.Before;
import org.junit.Test;
import soot.IntType;
import soot.jimple.Jimple;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.emptyEffect;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.makeSignature;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.literal;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.ret;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

/**
 * Some tests for signatures.
 */
public class SigSetTest {

    private TypeVars tvars;

    @Before
    public void setUp() {
        tvars = new TypeVars();
    }

    private static Signature<Level> makeSig(List<SigConstraint<Level>> cs) {
        return makeSignature(0, cs, emptyEffect());
    }

    private static Signature<Level> makeSig(SigConstraint<Level>... cs) {
        return makeSig(asList(cs));
    }

    private static Signature<Level> makeSig(SigConstraint<Level> c) {
        return makeSig(singletonList(c));
    }

    private static Signature<Level> makeEmptySig() {
        return makeSig(emptyList());
    }


    @Test
    public void testRefinementOfEmpty() {
        assertThat(makeSig(leS(literal(TLOW), ret())), notRefines(makeEmptySig()));
        assertThat(makeEmptySig(), refines(makeSig(leS(literal(TLOW), ret()))));
    }

}
