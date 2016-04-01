package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;

import static de.unifreiburg.cs.proglang.jgs.constraints.CTypeOps.tryApply;
import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;

/**
 * A context for constraints, i.e. constraints for a particular type domain. 
 * 
 * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
 *
 * @param <Level>
 */
public class Constraints<Level> {

    public final TypeDomain<Level> types;

    public Constraints(TypeDomain<Level> types) {
        super();
        this.types = types;
    }

    public static <Level> Constraint<Level> make(Constraint.Kind kind, CType<Level> lhs, CType<Level> rhs) {
        return new Constraint<>(kind, lhs, rhs);
    }


    public static <Level> Constraint<Level> le(CType<Level> lhs, CType<Level> rhs) {
        return new Constraint<Level>(Constraint.Kind.LE, lhs, rhs);
    }

    public static <Level> Constraint<Level> comp(CType<Level> lhs, CType<Level> rhs) {
        return new Constraint<Level>(Constraint.Kind.COMP, lhs, rhs);
    }

    public static <Level> Constraint<Level> dimpl(CType<Level> lhs, CType<Level> rhs) {
        return new Constraint<Level>(Constraint.Kind.DIMPL, lhs, rhs);
    }

    public static <Level> Constraint<Level> toKind(Constraint.Kind kind, Constraint<Level> c) {
        return make(kind, c.getLhs(), c.getRhs());
    }

    /**
     * Yields the symmetric constraint.
     */
    public static <Level> Constraint<Level> symmetricOf(Constraint<Level> c) {
        return make(c.kind, c.getRhs(), c.getLhs());
    }

    public static <Level> boolean equalComponents(Constraint<Level> c1, Constraint<Level> c2) {
        return c1.getLhs().equals(c2.getLhs()) && c1.getRhs().equals(c2.getRhs());
    }

    public static <Level> boolean isTrivial(TypeDomain<Level> types, Constraint<Level> c) {
        Assignment<Level> a = Assignments.empty();
        return tryApply(a, c.getLhs()).isPresent() && tryApply(a, c.getRhs()).isPresent() && c.isSatisfied(types, Assignments.empty());
    }

    /**
     * Given the constraint <code>c</code>, return literally all the non-reflexive constraints that would be implied by <code>c</code>.
     */
    //TODO-needs-test: write a property-based test for the soundness of the implementation (i.e., the result is equivalent to <code>c</code>)
    public static <Level> Stream<Constraint<Level>> implicationsOf(Constraint<Level> c) {
        switch (c.kind) {
            case LE:
                // x1 <= x2 implies x1 ~ x2
                return Stream.concat(
                        Stream.of(c),
                        implicationsOf(toKind(Constraint.Kind.COMP, c)));
            case COMP:
                //TODO-nsu-support: fix this for NSU support... dimpl is needed
//                Constraint<Level> dimpl = toKind(Constraint.Kind.DIMPL, c);
                // x1 ~ x2 implies x2 ~ x1 and x1 ?-> x2, and x2 ?~> x1
//                return Stream.of(c, symmetricOf(c), dimpl, symmetricOf(dimpl));
                return Stream.of(c, symmetricOf(c));
            case DIMPL:
                return Stream.of(c);
            default:
                throw new RuntimeException("Impossible case");
        }
    }

    public Optional<Assignment<Level>> satisfyingAssignment(ConstraintSet<Level> levelConstraintSet, Collection<TypeVar> vars) {
        return levelConstraintSet.satisfyingAssignment(this.types, vars);
    }

    public boolean implies(ConstraintSet<Level> left, ConstraintSet<Level> right) {
        return left.implies(types, right);
    }

    public boolean subsubmes(ConstraintSet<Level> left, ConstraintSet<Level> right) {
        return left.subsumes(right);
    }


    public boolean isSat(ConstraintSet<Level> cs) {
        return cs.isSat(types);
    }

    public boolean isSatisfyingAssignment(Constraint<Level> c, Assignment<Level> ass) {
        return c.isSatisfied(types, ass);
    }

    public boolean isSatisfyingAssignment(ConstraintSet<Level> cs, Assignment<Level> ass) {
        return cs.isSatisfiedFor(types, ass);
    }
}
