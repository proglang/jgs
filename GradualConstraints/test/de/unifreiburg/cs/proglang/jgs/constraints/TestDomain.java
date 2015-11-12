package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Collection;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import de.unifreiburg.cs.proglang.jgs.typing.Typing;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;

public class TestDomain {

    public static final LowHigh levels = new LowHigh();
    public static final TypeDomain<Level> types = new TypeDomain<>(levels);
    public static final Constraints<Level> cstrs = new Constraints<>(types);
    public static final MethodSignatures<Level> sigs = new MethodSignatures<>(cstrs);

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
        return cstrs.le(lhs, rhs);
    }

    public static Constraint<Level> leC(TypeVar lhs,
                                        TypeVar rhs) {
        return cstrs.le( CTypes.variable(lhs), CTypes.variable(rhs));
    }

    public static Constraint<Level> leC(TypeVar lhs,
                                        CType<Level> rhs) {
        return cstrs.le( CTypes.variable(lhs), rhs);
    }

    public static Constraint<Level> compC(CType<Level> lhs,
                                           CType<Level> rhs) {
        return cstrs.comp(lhs, rhs);
    }

    public static Constraint<Level> dimplC(CType<Level> lhs,
                                           CType<Level> rhs) {
        return cstrs.dimpl(lhs, rhs);
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
    // Typing
    public static Typing<Level> mkTyping(TypeVars tvars) {
        return new Typing<>(new NaiveConstraintsFactory<>(types), types, tvars, cstrs);
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
            return cstr.isSatisfied(levelAssignment);
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
            return levelConstraintSet.isSat();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("SAT");
        }

    }

    public static Matcher<ConstraintSet<Level>> equivalent(final ConstraintSet<Level> other) {
        return new TypeSafeMatcher<ConstraintSet<Level>>() {
            @Override
            protected boolean matchesSafely(ConstraintSet<Level> levelConstraintSet) {
                return levelConstraintSet.implies(other) && other.implies(levelConstraintSet);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" equivalent to ").appendValue(other);
            }
        };
    }

}
