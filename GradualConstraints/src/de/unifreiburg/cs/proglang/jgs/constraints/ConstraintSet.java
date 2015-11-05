package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;

/**
 * An immutable set of constraints.
 *
 * @param <Level>
 * @author fennell
 */
public abstract class ConstraintSet<Level> {

    protected final TypeDomain<Level> types;

    public ConstraintSet(TypeDomain<Level> types) {
        super();
        this.types = types;
    }

    /**
     * Check if an assignment satisfies this constraint set.
     *
     * @param a
     * @return
     */
    abstract public boolean isSatisfiedFor(Assignment<Level> a);

    /**
     * Return a new set with the given constraint <code>c</code> added.
     */
    abstract public ConstraintSet<Level> add(Constraint<Level> c);

    abstract public ConstraintSet<Level> add(ConstraintSet<Level> other);

    /**
     * @return True if the constraints of this set imply those of <code>other</code>
     */
    abstract public boolean implies(ConstraintSet<Level> other);

    /**
     * @return True if this set of constraints is satisfiable.
     */
    abstract public boolean isSat();

    /**
     * Find a satisfying assignment for this constraint set. (Optional)
     *
     * @param requiredVariables The minimal set of variables that should be bound by a satisfying assignment. The actual
     *                          assignments may bind more variables.
     */
    public Optional<Assignment<Level>> satisfyingAssignment(Collection<TypeVar> requiredVariables) {
        throw new RuntimeException("sat() operation not supported");
    }

    public abstract Stream<Constraint<Level>> stream();

    /**
     * @return The stream of variables contained in this set.
     */
    public Stream<TypeVar> variables() {
        return this.stream().flatMap(c -> c.variables());
    }


    /**
     * Create a set of constraints that only mentions the type variables in <code>typeVars</code>. The resulting set,
     * interpreted as a ConstraintSet, should impose the same restrictions on the variables in <code>typeVars</code> as
     * the original set.
     */
    public abstract Set<Constraint<Level>> projectTo(Set<TypeVar> typeVars);
}
