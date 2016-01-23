package de.unifreiburg.cs.proglang.jgs;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import de.unifreiburg.cs.proglang.jgs.typing.BasicStatementTyping;
import de.unifreiburg.cs.proglang.jgs.typing.Environment;
import de.unifreiburg.cs.proglang.jgs.typing.MethodBodyTyping;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import soot.*;
import soot.jimple.StaticInvokeExpr;

import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import static java.util.stream.Collectors.toSet;

public class TestDomain {

    public static final LowHigh levels = new LowHigh();
    public static final TypeDomain<Level> types = new TypeDomain<>(levels);
    public static final Constraints<Level> cstrs = new Constraints<>(types);
    public static final MethodSignatures<Level> sigs = new MethodSignatures<>();

    ///////
    // casts
    public static final SootClass castClass = new SootClass("castClass");
    public static final SootMethod castHighToDyn = makeCastMethod("castHighToDyn");
    public static final SootMethod castLowToDyn = makeCastMethod("castLowToDyn");
    public static final SootMethod castDynToLow = makeCastMethod("castDynToLow");
    public static final SootMethod castDynToHigh = makeCastMethod("castDynToHigh");


    private static SootMethod makeCastMethod(String name) {
        SootMethod result = new SootMethod(name, Collections.singletonList(RefType.v()), RefType.v(), Modifier.STATIC);
        castClass.addMethod(result);
        return result;
    }

    public static final Casts<Level> casts = new Casts<Level>() {

        @Override public Optional<Cast<Level>> detectFromCall(StaticInvokeExpr e) {
            SootMethod m = e.getMethod();
            BiFunction<Type<Level>,Type<Level>,Optional<Cast<Level>>> makeCast =
                    (t1, t2) ->  Var.getAll(e.getArg(0)).findFirst().map(value -> makeCast(t1, t2,value));
            if (m.equals(castHighToDyn)) {
                return makeCast.apply(THIGH, DYN);
            } else if (m.equals(castLowToDyn)) {
                return makeCast.apply(TLOW, DYN);
            } else if (m.equals(castDynToLow)) {
                return makeCast.apply(DYN, TLOW);
            } else if (m.equals(castDynToHigh)) {
                return makeCast.apply(DYN, THIGH);
            } else {
                return Optional.empty();
            }
        }
    };


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
        return Constraints.le( CTypes.variable(lhs), CTypes.variable(rhs));
    }

    public static Constraint<Level> leC(TypeVar lhs,
                                        CType<Level> rhs) {
        return Constraints.le( CTypes.variable(lhs), rhs);
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

    //////////////
    // Constraint sets
    public static ConstraintSet<Level> makeNaive(Collection<Constraint<Level>> cs) {
        return new NaiveConstraints<>(types, cs);
    }

    ///////
    // BasicStatementTyping
    public static BasicStatementTyping<Level> mkTyping(TypeVars.MethodTypeVars tvars) {
        return new BasicStatementTyping<>(new NaiveConstraintsFactory<>(types), tvars, cstrs);
    }

    ///////
    // Method body typing
    public static MethodBodyTyping<Level> mkMbTyping(Environment env, TypeVars tvars, SignatureTable<Level> signatures) {
        return new MethodBodyTyping<>(tvars, new NaiveConstraintsFactory<>(types), cstrs, casts, signatures);
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
     * Holds for constraint sets where each assignment satisfying the left set also satisfies the right set.
     *
     * Note: Alpha-renaming is <b>not</b> taken into account, i.e. assertThat((v1 <= v2), implies(v0 <= v1)) does not hold.
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
     * Holds for constraint sets that are satisfied by the <b>same assignments</b>.
     *
     * Note: Alpha-renaming is <b>not</b> taken into account, i.e. assertThat((v1 <= v2), is(equivalent(v0 <= v1))) does not hold.
     */
    public static Matcher<ConstraintSet<Level>> equivalent(final ConstraintSet<Level> other) {
        return new TypeSafeMatcher<ConstraintSet<Level>>() {
            @Override
            protected boolean matchesSafely(ConstraintSet<Level> levelConstraintSet) {
                return cstrs.implies(levelConstraintSet, other) && cstrs.implies(other, levelConstraintSet);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" equivalent to ").appendValue(other);
            }
        };
    }

    /**
     * Holds for constraint sets that subsume each other. In contrast to equivalent sets, constraint sets that subsume each other may contain different sets of variables. Only the assignments of the common variables needs to be consistent.
     *
     * Note: Alpha-renaming is <b>not</b> taken into account, i.e. assertThat((v1 <= v2), is(equivalent(v0 <= v1))) does not hold.
     */
    public static Matcher<ConstraintSet<Level>> minimallySubsumes(final ConstraintSet<Level> other) {
        return new TypeSafeMatcher<ConstraintSet<Level>>() {
            @Override
            protected boolean matchesSafely(ConstraintSet<Level> levelConstraintSet) {
                return cstrs.subsubmes(levelConstraintSet, other) && cstrs.subsubmes(other, levelConstraintSet);
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

    //TODO: this is basically like signatureCounterExample but directly accepts TypeVars instead of Parameter to project. Remove this code duplication
    public static Matcher<ConstraintSet<Level>> refines(TypeVars tvars, final ConstraintSet<Level> other) {
        return new TypeSafeMatcher<ConstraintSet<Level>>() {
            Optional<ConstraintSet<Level>.RefinementError> maybeError;
            @Override
            protected boolean matchesSafely(ConstraintSet<Level> levelConstraintSet) {
                ConstraintSet<Level> projected = levelConstraintSet.projectTo(Stream.concat(Stream.of(tvars.topLevelContext()), other.variables()).collect(Collectors.toSet()));
                maybeError = other.subsumptionCounterExample(projected).map(ce -> other.new RefinementError(levelConstraintSet, projected, ce));
                return !maybeError.isPresent();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" refines ").appendValue(other);
            }

            @Override
            protected void describeMismatchSafely(ConstraintSet<Level> item, Description mismatchDescription) {
                ConstraintSet<Level>.RefinementError error = maybeError.get();
                mismatchDescription.appendText(String.format("was (projected to sig) %s\n Counterexample: %s\n Conflicting: %s", error.getProjected(), error.getCounterExample(), error.getApplied()));
            }
        };
    }
}
