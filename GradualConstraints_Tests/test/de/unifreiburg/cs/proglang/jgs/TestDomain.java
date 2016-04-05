package de.unifreiburg.cs.proglang.jgs;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Vars;
import de.unifreiburg.cs.proglang.jgs.signatures.FieldTable;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import de.unifreiburg.cs.proglang.jgs.typing.*;
import de.unifreiburg.cs.proglang.jgs.util.Interop;
import org.apache.commons.lang3.tuple.Pair;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import scala.Option;
import soot.*;
import soot.jimple.StaticInvokeExpr;

import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.param;
import static de.unifreiburg.cs.proglang.jgs.util.Interop.asJavaStream;
import static java.util.stream.Collectors.toSet;

public class TestDomain {

    public static final LowHigh levels = new LowHigh();
    public static final TypeDomain<Level> types = new TypeDomain<>(levels);
    public static final Constraints<Level> cstrs = new Constraints<>(types);
    public static final MethodSignatures<Level> sigs = new MethodSignatures<>();
    public static final ConstraintSetFactory<Level> csets =
            new NaiveConstraintsFactory<>(types);

    ///////
    // casts
    public static final SootClass castClass = new SootClass("testCasts");
    public static final SootMethod castHighToDyn =
            makeCastMethod("castHighToDyn");
    public static final SootMethod castLowToDyn =
            makeCastMethod("castLowToDyn");
    public static final SootMethod castDynToLow =
            makeCastMethod("castDynToLow");
    public static final SootMethod castDynToHigh =
            makeCastMethod("castDynToHigh");
    public static final SootMethod castCxHighToDyn =
            makeContextCastMethod("castCxHighToDyn");
    public static final SootMethod castCxDynToHigh =
            makeContextCastMethod("castCxDynToHigh");
    public static final SootMethod castCxEnd =
            makeContextCastMethod("castCxEnd");

    static {
        if (Scene.v().containsClass("CastClass")) {
            Scene.v().removeClass(Scene.v().getSootClass("CastClass"));
        }
        Scene.v().addClass(castClass);
    }

    private static SootMethod makeCastMethod(String name) {
        SootMethod result =
                new SootMethod(name, Collections.singletonList(RefType.v()), RefType.v(), Modifier.STATIC);
        castClass.addMethod(result);
        return result;
    }

    private static SootMethod makeContextCastMethod(String name) {
        SootMethod result =
                new SootMethod(name, Collections.emptyList(), VoidType.v(), Modifier.STATIC);
        castClass.addMethod(result);
        return result;
    }


    public static final Casts<Level> casts = new Casts<Level>() {

        private boolean castEquals(SootMethod declaredCast, SootMethod m) {
            return m.getName().equals(declaredCast.getName());
        }

        @Override
        public Option<ValueCast<Level>> detectValueCastFromCall(StaticInvokeExpr e) {
            SootMethod m = e.getMethod();
            BiFunction<Type<Level>, Type<Level>, Option<ValueCast<Level>>>
                    makeCast =
                    (t1, t2) -> {
                        Optional<ValueCast<Level>> result = Vars.getAll(e.getArg(0)).findFirst().map(value -> makeValueCast(t1, t2, Option.apply(value)));
                        return Interop.asScalaOption(result);
                    };
            if (castEquals(castHighToDyn, m)) {
                return makeCast.apply(THIGH, DYN);
            } else if (castEquals(castLowToDyn, m)) {
                return makeCast.apply(TLOW, DYN);
            } else if (castEquals(castDynToLow, m)) {
                return makeCast.apply(DYN, TLOW);
            } else if (castEquals(castDynToHigh, m)) {
                return makeCast.apply(DYN, THIGH);
            } else {
                return Option.empty();
            }
        }

        @Override
        public Option<CxCast<Level>> detectContextCastStartFromCall(StaticInvokeExpr e) {
            SootMethod m = e.getMethod();
            BiFunction<Type<Level>, Type<Level>, Option<CxCast<Level>>>
                    makeCast =
                    (t1, t2) -> Option.apply(makeContextCast(t1, t2));
            if (castEquals(castCxDynToHigh, m)) {
                return makeCast.apply(DYN, THIGH);
            } else if (castEquals(castCxHighToDyn, m)) {
                return makeCast.apply(THIGH, DYN);
            } else {
                return Option.empty();
            }
        }

        @Override
        public boolean detectContextCastEndFromCall(StaticInvokeExpr e) {
            return castEquals(castCxEnd, e.getMethod());
        }
    };

    public static final MethodTyping<Level> mtyping =
            new MethodTyping<>(csets, cstrs, casts);


    //////
    // Levels
    public static final Level HIGH = levels.top();
    public static final Level LOW = levels.bottom();

    public static Level lub(Level l1, Level l2) {
        return levels.lub(l1, l2);
    }

    public static Level glb(Level l1, Level l2) {
        return levels.glb(l1, l2);
    }

    public static boolean le(Level l1, Level l2) {
        return levels.le(l1, l2);
    }

    ///////
    // Types
    public static final Type<Level> THIGH = types.level(HIGH);
    public static final Type<Level> TLOW = types.level(LOW);
    public static final Type<Level> DYN = types.dyn();
    public static final Type<Level> PUB = types.pub();

    public static Type<Level> tglb(Type<Level> l1,
                                   Type<Level> l2) {
        return types.glb(l1, l2);
    }

    public static boolean tle(Type<Level> l1,
                              Type<Level> l2) {
        return types.le(l1, l2);
    }

    ////////////
    // Constraints
    public static final CType<Level> CHIGH = CTypes.literal(THIGH);
    public static final CType<Level> CLOW = CTypes.literal(TLOW);
    public static final CType<Level> CDYN = CTypes.literal(DYN);
    public static final CType<Level> CPUB = CTypes.literal(PUB);

    public static Constraint<Level> leC(CType<Level> lhs,
                                        CType<Level> rhs) {
        return Constraints.le(lhs, rhs);
    }

    public static Constraint<Level> leC(CType<Level> ct, TypeVar v) {
        return leC(ct, CTypes.variable(v));
    }

    public static Constraint<Level> leC(TypeVar lhs,
                                        TypeVar rhs) {
        return Constraints.le(CTypes.variable(lhs), CTypes.variable(rhs));
    }

    public static Constraint<Level> leC(TypeVar lhs,
                                        CType<Level> rhs) {
        return Constraints.le(CTypes.variable(lhs), rhs);
    }

    public static Constraint<Level> compC(CType<Level> lhs,
                                          CType<Level> rhs) {
        return Constraints.comp(lhs, rhs);
    }

    public static Constraint<Level> compC(TypeVar lhs,
                                          TypeVar rhs) {
        return compC(CTypes.variable(lhs), CTypes.variable(rhs));
    }

    public static Constraint<Level> dimplC(CType<Level> lhs,
                                           CType<Level> rhs) {
        return Constraints.dimpl(lhs, rhs);
    }

    public static SigConstraint<Level> leS(Symbol<Level> sc1, Symbol<Level> sc2) {
        return sigs.le(sc1, sc2);
    }

    public static SigConstraint<Level> leS(int paramPos, Symbol<Level> sc2) {
        return sigs.le(param(paramPos), sc2);
    }

    //////////////
    // Constraint sets
    public static ConstraintSet<Level> makeNaive(Collection<Constraint<Level>> cs) {
        return new NaiveConstraints<>(types, cs);
    }

    public static SootMethod dummyMethod = new SootMethod("notAConstructor", Collections.emptyList(), VoidType.v());

    ///////
    // BasicStatementTyping
    public static BasicStatementTyping<Level> mkTyping(TypeVars.MethodTypeVars tvars) {
        // TODO do not pass a method to Typings ... pass a more sensible structure that contains exactly the required info
        return new BasicStatementTyping<>(new NaiveConstraintsFactory<>(types), tvars, cstrs, dummyMethod);
    }

    ///////
    // Method body typing
    public static MethodBodyTyping<Level> mkMbTyping(Environment env, TypeVars tvars, SignatureTable<Level> signatures, FieldTable<Level> fields) {
        return new MethodBodyTyping<>(dummyMethod, tvars, new NaiveConstraintsFactory<>(types), cstrs, casts, signatures, fields);
    }
    /////
    // JUnit matcher

    public static Matcher<Assignment<Level>> satisfies(Constraint<Level> cstr) {
        return new Satisfies(cstr);
    }

    private static class Satisfies extends TypeSafeMatcher<Assignment<Level>> {
        public final Constraint<Level> cstr;

        public Satisfies(Constraint<Level> cstr) {
            this.cstr = cstr;
        }

        @Override
        protected boolean matchesSafely(Assignment<Level> levelAssignment) {
            return cstrs.isSatisfyingAssignment(cstr, levelAssignment);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("satisfies ").appendValue(cstr);
        }
    }

    public static Matcher<ConstraintSet<Level>> sat() {
        return new Sat();
    }

    private static class Sat extends TypeSafeMatcher<ConstraintSet<Level>> {

        @Override
        protected boolean matchesSafely(ConstraintSet<Level> levelConstraintSet) {
            return cstrs.isSat(levelConstraintSet);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("SAT");
        }

    }

    /**
     * Holds for constraint sets where each assignment satisfying the left set
     * also satisfies the right set.
     * <p>
     * Note: Alpha-renaming is <b>not</b> taken into account, i.e.
     * assertThat((v1 <= v2), implies(v0 <= v1)) does not hold.
     */
    public static Matcher<ConstraintSet<Level>> implies(final ConstraintSet<Level> other) {
        return new TypeSafeMatcher<ConstraintSet<Level>>() {
            @Override
            protected boolean matchesSafely(ConstraintSet<Level> levelConstraintSet) {
                return cstrs.implies(levelConstraintSet, other);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" implies ").appendValue(other);
            }
        };
    }

    public static Matcher<Constraint<Level>> satisfiedBy(final Assignment<Level> ass) {
        return new TypeSafeMatcher<Constraint<Level>>() {
            @Override
            protected boolean matchesSafely(Constraint<Level> levelConstraint) {
                return cstrs.isSatisfyingAssignment(levelConstraint, ass);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" satisfied by ").appendValue(ass);
            }
        };
    }

    /**
     * Holds for constraint sets that are satisfied by the <b>same
     * assignments</b>.
     * <p>
     * Note: Alpha-renaming is <b>not</b> taken into account, i.e.
     * assertThat((v1 <= v2), is(equivalent(v0 <= v1))) does not hold.
     */
    public static Matcher<ConstraintSet<Level>> equivalent(final ConstraintSet<Level> other) {
        return new TypeSafeMatcher<ConstraintSet<Level>>() {
            @Override
            protected boolean matchesSafely(ConstraintSet<Level> levelConstraintSet) {
                return cstrs.implies(levelConstraintSet, other)
                       && cstrs.implies(other, levelConstraintSet);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" equivalent to ").appendValue(other);
            }
        };
    }

    /**
     * Holds for constraint sets that subsume each other. In contrast to
     * equivalent sets, constraint sets that subsume each other may contain
     * different sets of variables. Only the assignments of the common variables
     * needs to be consistent.
     * <p>
     * Note: Alpha-renaming is <b>not</b> taken into account, i.e.
     * assertThat((v1 <= v2), is(equivalent(v0 <= v1))) does not hold.
     */
    public static Matcher<ConstraintSet<Level>> minimallySubsumes(final ConstraintSet<Level> other) {
        return new TypeSafeMatcher<ConstraintSet<Level>>() {
            @Override
            protected boolean matchesSafely(ConstraintSet<Level> levelConstraintSet) {
                return cstrs.subsubmes(levelConstraintSet, other)
                       && cstrs.subsubmes(other, levelConstraintSet);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" minimally subsumes ").appendValue(other);
            }
        };
    }

    public static Matcher<ConstraintSet<Level>> subsumes(final ConstraintSet<Level> other) {
        return new TypeSafeMatcher<ConstraintSet<Level>>() {
            @Override
            protected boolean matchesSafely(ConstraintSet<Level> levelConstraintSet) {
                return cstrs.subsubmes(levelConstraintSet, other);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" subsumes ").appendValue(other);
            }
        };
    }

    private static class SigRefineMatcher extends TypeSafeMatcher<Signature<Level>> {
        private final Signature<Level> other;
        Pair<ConstraintSet.RefinementCheckResult<Level>, EffectRefinementResult<Level>>
                result;

        private SigRefineMatcher(Signature<Level> other) {
            this.other = other;
        }

        @Override
        protected boolean matchesSafely(Signature<Level> concreteSig) {
            result = concreteSig.refines(csets, types, other);
            return result.getLeft().isSuccess()
                   && result.getRight().isSuccess();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" refines ").appendValue(other);
        }

        @Override
        protected void describeMismatchSafely
                (Signature<Level> item, Description mismatchDescription) {
            mismatchDescription.appendValue(result);
        }


    }

    public static Matcher<Signature<Level>> refines(
            final Signature<Level> other) {
        return new SigRefineMatcher(other);
    }

    public static Matcher<Signature<Level>> notRefines(Signature<Level> other) {
        return new SigRefineMatcher(other) {
            @Override
            protected boolean matchesSafely(Signature<Level> concreteSig) {
                return !super.matchesSafely(concreteSig);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" not "); super.describeTo(description);
            }
        };
    }

    private static class RefineMatchter extends TypeSafeMatcher<ConstraintSet<Level>> {
        private final ConstraintSet<Level> abstractConstraints;
        private final TypeVars tvars;
        ConstraintSet.RefinementCheckResult<Level> result;

        private RefineMatchter(ConstraintSet<Level> abstractConstraints, TypeVars tvars) {
            this.abstractConstraints = abstractConstraints;
            this.tvars = tvars;
        }

        @Override
        protected boolean matchesSafely(ConstraintSet<Level> concreteConstraints) {
            // project for efficiency .. be careful to keep top-level context to not loose implicit flows
            // TODO: keeping the top-level context is strange. Should be fixed, i.e. removed or solved in a general way
            // TODO: why don't we keep the top-level context in ConstraintSet.refines?
            ConstraintSet<Level> projected =
                    concreteConstraints.projectTo(Stream.concat(
                            //TODO: remove if correct without

//                            Stream.of(tvars.topLevelContext()),
//                            abstractConstraints.variables()
                            Stream.empty(),
                            asJavaStream(abstractConstraints.variables())
                    ).collect(toSet()));
            result =
                    new ConstraintSet.RefinementCheckResult<>(abstractConstraints, projected, abstractConstraints.doesNotSubsume(projected));
            return result.counterExample().isEmpty();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" refines ").appendValue(abstractConstraints);
        }

        @Override
        protected void describeMismatchSafely(ConstraintSet<Level> item, Description mismatchDescription) {
            mismatchDescription.appendText(
                    "was \nMatched: " + item.toString() + "\n")
                               .appendText(String.format(" projected to: %s\n"
                                       , asJavaStream(abstractConstraints.variables())
                                                            .collect(toSet())
                                                            .toString()))
                               .appendText(result.toString());
        }
    }

    public static Matcher<ConstraintSet<Level>> refines(TypeVars tvars, final ConstraintSet<Level> other) {
        return new RefineMatchter(other, tvars);
    }

    public static Matcher<ConstraintSet<Level>> notRefines(TypeVars tvars, final ConstraintSet<Level> other) {
        return new RefineMatchter(other, tvars) {
            @Override
            protected boolean matchesSafely(ConstraintSet<Level> concreteConstraints) {
                return !super.matchesSafely(concreteConstraints);
            }

        };
    }

    public static Matcher<Collection<SootMethod>> passesSubtypingCheckFor(SignatureTable<Level> signatures) {
        return new TypeSafeMatcher<Collection<SootMethod>>() {
            ClassHierarchyTyping.Result<Level> result;

            @Override
            protected boolean matchesSafely(Collection<SootMethod> sootMethods) {
                result =
                        ClassHierarchyTyping.<Level>checkMethods(csets, types, signatures, Interop.asScalaIterator(sootMethods));
                return result.isSuccess();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" passes subtyping check");
            }

            @Override
            protected void describeMismatchSafely(Collection<SootMethod> item, Description mismatchDescription) {
                mismatchDescription.appendValue(result);
            }
        };
    }

    private static abstract class MethodTypingError {
    }

    private static final class MissingMethod extends MethodTypingError {
        @Override
        public String toString() {
            return "Method is missing from signatures";
        }
    }

    private static final class TypingExceptionError extends MethodTypingError {
        public final TypingException exception;

        private TypingExceptionError(TypingException exception) {
            this.exception = exception;
        }

        @Override
        public String toString() {
            return "Typing exception: " + exception;
        }
    }

    private static final class TypeError extends MethodTypingError {
        public final MethodTyping.Result<Level> typingResult;

        private TypeError(MethodTyping.Result<Level> typingResult) {
            this.typingResult = typingResult;
        }

        @Override
        public String toString() {
            return "result of method typing: \n" + typingResult.toString();
        }
    }

    private abstract static class MethodTypingMatcher extends TypeSafeMatcher<SootMethod> {
        protected MethodTypingError error;
        protected Optional<Signature<Level>> maybeSig;
        private final SignatureTable<Level> signatures;
        private final FieldTable<Level> fields;
        private final TypeVars tvars;

        private MethodTypingMatcher(TypeVars tvars, SignatureTable<Level> signatures, FieldTable<Level> fields) {
            this.signatures = signatures;
            this.fields = fields;
            this.tvars = tvars;
        }

        protected abstract boolean goodResult(MethodTyping.Result<Level> r);

        @Override
        protected boolean matchesSafely(SootMethod method) {
            maybeSig = signatures.get(method);
            return maybeSig.map(sig -> {
                MethodTyping.Result<Level> r;
                try {
                    r = mtyping.check(tvars, signatures, fields, method);
                } catch (TypingException e) {
                    this.error = new TypingExceptionError(e);
                    return false;
                }
                if (!goodResult(r)) {
                    this.error = new TypeError(r);
                    return false;
                } else {
                    return true;
                }
            }).orElseGet(() -> {
                this.error = new MissingMethod();
                return false;
            });
        }

        @Override
        public abstract void describeTo(Description description);

        @Override
        protected abstract void describeMismatchSafely(SootMethod item, Description mismatchDescription);
    }


    public static Matcher<SootMethod> compliesTo(TypeVars tvars, SignatureTable<Level> signatures, FieldTable<Level> fields) {
        return new MethodTypingMatcher(tvars, signatures, fields) {
            @Override
            protected boolean goodResult(MethodTyping.Result<Level> r) {
                return r.isSuccess();
            }

            public void describeTo(Description description) {
                description.appendText(" complies to ").appendValue(maybeSig);
            }

            protected void describeMismatchSafely(SootMethod item, Description mismatchDescription) {
                mismatchDescription.appendText(error.toString());
            }
        };
    }

    public static Matcher<SootMethod> violates(TypeVars tvars, SignatureTable<Level> signatures, FieldTable<Level> fields) {
        return new MethodTypingMatcher(tvars, signatures, fields) {
            @Override
            protected boolean goodResult(MethodTyping.Result<Level> r) {
                return !r.isSuccess();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" violates ").appendValue(maybeSig);
            }

            @Override
            protected void describeMismatchSafely(SootMethod item, Description mismatchDescription) {
                mismatchDescription.appendText(error.toString());
            }
        };

    }
}
