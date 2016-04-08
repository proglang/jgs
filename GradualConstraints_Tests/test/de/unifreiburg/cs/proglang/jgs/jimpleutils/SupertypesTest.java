package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.signatures.Signature;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodWithSignature;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import soot.IntType;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Jimple;

import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.leS;
import static de.unifreiburg.cs.proglang.jgs.signatures.Effects.emptyEffect;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.makeSignature;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.literal;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.ret;
import static de.unifreiburg.cs.proglang.jgs.util.Interop.asJavaStream;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// TODO: Supertypes needs more comprehensive tests
public class SupertypesTest {

    private Jimple j;
    private TypeVars tvars;
    private Code code;
    private TypeVar pc;

    @Before
    public void setUp() {
        this.tvars = new TypeVars();
        this.j = Jimple.v();
        this.pc = tvars.topLevelContext();
        this.code = new Code(tvars);
    }

    @Test
    public void testSubTypeOf_testClass() {
        SootMethod testCallee_override = new SootMethod("testCallee", emptyList(), IntType.v());
        Signature<Level> sig = makeSignature(
                testCallee_override.getParameterCount(),
                Stream.of(leS(literal(THIGH), ret())).collect(toList()),
                emptyEffect());
        Pair<SootClass, SignatureTable<Level>> derived = code.makeDerivedClass("SubTestClass", code.testClass,
                asList(MethodWithSignature.make(
                        testCallee_override,
                        sig)));
        SootClass derivedClass = derived.getKey();

        assertThat("Derived has no superclass", derivedClass.hasSuperclass());
        assertThat(asJavaStream(Supertypes.enumerate(derivedClass)).collect(toList()), is(equalTo(asList(code.testClass))));
        assertThat("Derived is no subtype", Supertypes.subTypeOf(derived.getKey(), code.testClass));
    }

    @Test
    public void testOverrides_testCallee_int() {
        SootMethod testCallee_override = new SootMethod("testCallee", emptyList(), IntType.v());
        Signature<Level> sig = makeSignature(testCallee_override.getParameterCount(),
                Stream.of(leS(literal(THIGH), ret())).collect(toList()),
                emptyEffect());
        Pair<SootClass, SignatureTable<Level>> derived = code.makeDerivedClass("SubTestClass", code.testClass,
                asList(MethodWithSignature.make(
                        testCallee_override,
                        sig)));

        assertThat("Names not equal", testCallee_override.getName(), is(equalTo(code.testCallee__int.getName())));
        assertThat("Parameternot equal", testCallee_override.getName(), is(equalTo(code.testCallee__int.getName())));
        assertThat("Method does not override", Supertypes.overrides(testCallee_override, code.testCallee__int), is(true));
        assertThat("Could not find overridden method", asJavaStream(Supertypes.findOverridden(testCallee_override)).collect(toList()), is(equalTo(asList(code.testCallee__int))));
    }




}
