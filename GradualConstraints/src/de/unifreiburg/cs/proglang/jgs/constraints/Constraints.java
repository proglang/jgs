package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType;
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

    public static <Level> Constraint<Level> le(CType<Level> lhs, CType<Level> rhs) {
        return new Constraint(Constraint.Kind.LE, lhs, rhs);
    }

    public static <Level> Constraint<Level> comp(CType<Level> lhs, CType<Level> rhs) {
        return new Constraint(Constraint.Kind.COMP, lhs, rhs);
    }

    public static <Level> Constraint<Level> dimpl(CType<Level> lhs, CType<Level> rhs) {
        return new Constraint(Constraint.Kind.DIMPL, lhs, rhs);
    }

    public Optional<Assignment<Level>> satisfyingAssignment(ConstraintSet<Level> levelConstraintSet, Collection<TypeVar> vars) {
        return levelConstraintSet.satisfyingAssignment(this.types, vars);
    }

    public boolean implies(ConstraintSet<Level> left, ConstraintSet<Level> right) {
        return left.implies(types, right);
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
