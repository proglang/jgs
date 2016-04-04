package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Supertypes;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.Signature;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodWithSignature;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.util.Interop;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import soot.IntType;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Jimple;

import java.util.List;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.literal;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.param;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.ret;
import static de.unifreiburg.cs.proglang.jgs.util.Interop.asJavaStream;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.*;

public class ClassHierarchyTypingTest {

    private Jimple j;
    private TypeVars tvars;
    private Code code;
    private TypeVars.TypeVar pc;

    @Before
    public void setUp() {
        this.tvars = new TypeVars();
        this.j = Jimple.v();
        this.pc = tvars.topLevelContext();
        this.code = new Code(tvars);
    }

    private static List<SootMethod> allMethods(SootClass c) {
        return c.getMethods();
    }

    private static List<SootMethod> allMethods(SootClass ... cs){
        return asList(cs).stream().flatMap(c -> allMethods(c).stream()).collect(toList());
    };

    @Test
    public void testIllegalOverride_testCallee() {
        SootMethod testCallee_override = new SootMethod("testCallee", emptyList(), IntType.v());
        Signature<Level> sig = makeSignature(testCallee_override.getParameterCount(),
                                Stream.of(leS(literal(THIGH), ret())).collect(toList()),
                                emptyEffect());
        Pair<SootClass, SignatureTable<Level>> derived = code.makeDerivedClass("SubTestClass", code.testClass,
                asList(MethodWithSignature.make(
                        testCallee_override,
                        sig)));

        assertThat("Method does not override", Supertypes.overrides(testCallee_override, code.testCallee__int), is(true));
        assertThat(sig, not(refines(derived.getRight().get(code.testCallee__int).get())));
        assertThat("Could not find overridden method", asJavaStream(Supertypes.findOverridden(testCallee_override)).collect(toList()), is(equalTo(asList(code.testCallee__int))));

        assertThat(allMethods(derived.getLeft(), code.testClass), not(passesSubtypingCheckFor(derived.getRight())));
    }

    @Test
    public void testOverrideOfIgnore0Low1ReturnHigh() {
        SootMethod ignore0Low1ReturnHigh_override =
                new SootMethod("ignore0Low1ReturnHigh",
                        code.ignore0Low1ReturnHigh.getParameterTypes(),
                        code.ignore0Low1ReturnHigh.getReturnType());
        Signature<Level> sig = makeSignature(ignore0Low1ReturnHigh_override.getParameterCount(),
                                             Stream.of(leS(param(1), literal(THIGH)), leS(literal(TLOW), ret())).collect(toList()), emptyEffect());
        Pair<SootClass, SignatureTable<Level>> derived = code.makeDerivedClass("SubTestClass", code.testClass,
                asList(MethodWithSignature.make(
                        ignore0Low1ReturnHigh_override,
                        sig)));
        assertThat(allMethods(derived.getLeft(), code.testClass), passesSubtypingCheckFor(derived.getRight()));
    }

    @Test
    public void testIllegalOverrideOfIgnore0Low1ReturnHigh_effects() {
        SootMethod ignore0Low1ReturnHigh_override =
                new SootMethod("ignore0Low1ReturnHigh",
                        code.ignore0Low1ReturnHigh.getParameterTypes(),
                        code.ignore0Low1ReturnHigh.getReturnType());
        Signature<Level> sig = makeSignature(ignore0Low1ReturnHigh_override.getParameterCount(),
                                             Stream.of(leS(param(1), literal(THIGH)), leS(literal(TLOW), ret())).collect(toList()),
                makeEffects(TLOW));
        Pair<SootClass, SignatureTable<Level>> derived = code.makeDerivedClass("SubTestClass", code.testClass,
                asList(MethodWithSignature.make(
                        ignore0Low1ReturnHigh_override,
                        sig)));
        assertThat(allMethods(derived.getLeft(), code.testClass), not(passesSubtypingCheckFor(derived.getRight())));
    }
}
