package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts.ValueCast;
import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser;
import de.unifreiburg.cs.proglang.jgs.util.Interop;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import scala.Option;
import scala.util.Success;
import soot.*;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.StaticInvokeExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by fennell on 2/26/16.
 */
public class CastsFromMappingTest {

    private SootClass castClass;

    @Before
    public void setUp() {
        this.castClass = new SootClass("de.unifreiburg.cs.proglang.jgs.support.Casts");
    }

    private StaticInvokeExpr makeCall_0(String methodName) {
        SootMethod m = new SootMethod(methodName, emptyList(), VoidType.v(), Modifier.STATIC);
        this.castClass.addMethod(m);
        return Jimple.v().newStaticInvokeExpr(m.makeRef(), NullConstant.v());
    }

    private StaticInvokeExpr makeCall_1(String methodName) {
        soot.Type t = RefType.v("java.lang.Object");
        SootMethod m = new SootMethod(methodName, singletonList(t), t, Modifier.STATIC);
        this.castClass.addMethod(m);
        return Jimple.v().newStaticInvokeExpr(m.makeRef(), NullConstant.v());
    }

    private static Matcher<Optional<ValueCast<Level>>> convertsBetween(Type<Level> source, Type<Level> dest) {
        return new TypeSafeMatcher<Optional<ValueCast<Level>>>() {
            @Override
            protected boolean matchesSafely(Optional<ValueCast<Level>> levelValueCast) {
                return levelValueCast.map(c -> c.sourceType.equals(source) && c.destType.equals(dest)).orElse(false);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format(" converts from %s to %s", source, dest));
            }
        };
    }

    private static <Level> Map<String, CastUtils.Conversion<Level>> parseConversionMap(AnnotationParser<Type<Level>> typeParser, Map<String,String> casts) {
        Map<String, CastUtils.Conversion<Level>> result = new HashMap<>();
        for (Map.Entry<String, String> e : casts.entrySet()) {
           result.put(e.getKey(), CastUtils.<Level>parseConversion(typeParser, e.getValue()).get());
        }
        return result;
    }

    @Test
    public void testValid() {
        StaticInvokeExpr callCastHighToDyn = makeCall_1("castHighToDyn");
        StaticInvokeExpr callCastLowToDyn = makeCall_1("castLowToDyn");
        StaticInvokeExpr callCxCastHighToDyn = makeCall_0("cxCastHighToDyn");
        StaticInvokeExpr callCxCastLowToDyn = makeCall_0("cxCastLowToDyn");
        StaticInvokeExpr callCxCastEnd = makeCall_0("cxCastEnd");

        assertThat("makeCall_ generates wrong name" + callCastHighToDyn.getMethod().toString(),
                   callCastHighToDyn.getMethod().toString(),
                   is("<de.unifreiburg.cs.proglang.jgs.support.Casts: java.lang.Object castHighToDyn(java.lang.Object)>"));
        assertThat("makeCall_ generates wrong name" + callCxCastHighToDyn.getMethod().toString(),
                   callCxCastHighToDyn.getMethod().toString(),
                   is("<de.unifreiburg.cs.proglang.jgs.support.Casts: void cxCastHighToDyn()>"));

        Map<String,String> valueCasts = new HashMap<>();
        valueCasts.put(callCastHighToDyn.getMethod().toString(), "HIGH ~> ?");
        valueCasts.put(callCastLowToDyn.getMethod().toString(), "LOW ~> ?");
        Map<String,String> cxCasts = new HashMap<>();
        cxCasts.put(callCxCastHighToDyn.getMethod().toString(), "HIGH ~> ?");
        cxCasts.put(callCxCastLowToDyn.getMethod().toString(), "LOW ~> ?");
        String cxCastEnd = callCxCastEnd.getMethod().toString();

        Casts<Level> casts =
                CastsFromMapping$.MODULE$.<Level>apply(parseConversionMap(types.typeParser(), valueCasts),
                                              parseConversionMap(types.typeParser(), cxCasts),
                                              cxCastEnd);

        assertThat(String.format("wrong conversion for %s", callCxCastHighToDyn.getMethod().toString()), Interop.asJavaOptional(casts.detectValueCastFromCall(callCastHighToDyn)),
                   convertsBetween(THIGH, DYN));
        assertThat(String.format("wrong conversion for %s", callCxCastLowToDyn.getMethod().toString()), Interop.asJavaOptional(casts.detectValueCastFromCall(callCastLowToDyn)),
                   convertsBetween(TLOW, DYN));
        assertThat(casts.detectValueCastFromCall(callCxCastHighToDyn), is(Success.apply(Option.empty())));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalid() {
        Map<String,String> valueCasts = new HashMap<>();
        valueCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castHighToDyn", "H ~> ?");
        valueCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castLowToDyn", "L ~> ?");
        Map<String,String> cxCasts = new HashMap<>();
        cxCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castCxHighToDyn", "H ~> ?");
        cxCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castCxLowToDyn", "L ~> ?");
        String cxCastEnd = "de.unifreiburg.cs.proglang.jgs.support.Casts.castCxEnd";

        Casts<Level> casts =
                CastsFromMapping$.MODULE$.<Level>apply(parseConversionMap(types.typeParser(), valueCasts),
                                                       parseConversionMap(types.typeParser(), cxCasts),
                                                       cxCastEnd);

    }
}
