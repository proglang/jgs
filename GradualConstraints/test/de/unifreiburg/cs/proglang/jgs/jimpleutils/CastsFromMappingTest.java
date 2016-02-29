package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts.ValueCast;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import scala.Option;
import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.StaticInvokeExpr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

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
        soot.Type t = RefType.v();
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

    @Test
    public void testValid() {
        Map<String,String> valueCasts = new HashMap<>();
        valueCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castHighToDyn", "HIGH ~> ?");
        valueCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castLowToDyn", "LOW ~> ?");
        Map<String,String> cxCasts = new HashMap<>();
        cxCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castCxHighToDyn", "HIGH ~> ?");
        cxCasts.put("de.unifreiburg.cs.proglang.jgs.support.Casts.castCxLowToDyn", "LOW ~> ?");
        String cxCastEnd = "de.unifreiburg.cs.proglang.jgs.support.Casts.castCxEnd";

        Casts<Level> casts = new CastsFromMapping<>(CastsFromMapping.<Level>parseConversionMap(types.typeParser(), valueCasts).get(),
                                                            CastsFromMapping.<Level>parseConversionMap(types.typeParser(), cxCasts).get(),
                                                            cxCastEnd);

        assertThat(casts.detectValueCastFromCall(makeCall_1("castHighToDyn")),
                   convertsBetween(THIGH, DYN));
        assertThat(casts.detectValueCastFromCall(makeCall_0("cxCastHighToDyn")), is(Optional.empty()));
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

        Casts<Level> casts = new CastsFromMapping<>(CastsFromMapping.<Level>parseConversionMap(types.typeParser(), valueCasts).get(),
                                                    CastsFromMapping.<Level>parseConversionMap(types.typeParser(), cxCasts).get(),
                                                    cxCastEnd);

    }
}
