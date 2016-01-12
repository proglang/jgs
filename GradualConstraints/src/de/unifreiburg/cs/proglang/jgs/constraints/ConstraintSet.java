package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;

import static java.util.stream.Collectors.toSet;

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
    abstract public boolean isSatisfiedFor(TypeDomain<Level> types, Assignment<Level> a);

    /**
     * Return a new set with the given constraint <code>c</code> added.
     */
    abstract public ConstraintSet<Level> add(Constraint<Level> c);

    abstract public ConstraintSet<Level> add(ConstraintSet<Level> other);

    /**
     * @return True if the constraints of this set imply those of <code>other</code>
     */
    abstract public boolean implies(TypeDomain<Level> types, ConstraintSet<Level> other);

    /**
     * @return True if this set of constraints is satisfiable.
     */
    abstract public boolean isSat(TypeDomain<Level> types);

    /**
     * @return A counterexample why <code>this</code> does not subsume <code>other</code>
     */
    abstract public Optional<Assignment<Level>> subsumptionCounterExample(ConstraintSet<Level> other);

    public class RefinementError {

        private final ConstraintSet<Level> concrete;
        private final ConstraintSet<Level> projected ;
        private final Assignment<Level> counterExample;

        private RefinementError(ConstraintSet<Level> concrete, ConstraintSet<Level> projected, Assignment<Level> counterExample) {
            this.concrete = concrete;
            this.projected = projected;
            this.counterExample = counterExample;
        }

        public ConstraintSet<Level> getProjected() {
            return projected;
        }

        public Assignment<Level> getCounterExample() {
            return counterExample;
        }

        public Set<Constraint<Level>> getApplied() {
            return projected.stream().filter(c -> !Constraints.isTrivial(types, c.apply(counterExample))).collect(toSet());
        }
    }

    public  Optional<RefinementError> signatureCounterExample(TypeVars tvars, ConstraintSet<Level> other) {
        /* TODO-needs-test: cs1.subsumes(cs2) === cs1.subsumes(cs2.projectForSignature(tvars, cs1))
                            === cs1.projectForSignature(tvars, cs1).subsumes(cs2)
                            === cs1.projectForSignature(tvars, cs1).subsumes(cs2.projectForSignature(tvars, cs1)) */

        // the projection is just an optimization to keep the number of variables small
        ConstraintSet<Level> projected = other.projectForSignature(tvars,other);
        Optional<Assignment<Level>> counterExample = this.subsumptionCounterExample(projected);

        return counterExample.map(ce -> new RefinementError(other, projected, ce));
    }

    /**
     * Find a satisfying assignment for this constraint set. (Optional)
     *
     * @param requiredVariables The minimal set of variables that should be bound by a satisfying assignment. The actual
     *                          assignments may bind more variables.
     */
    public Optional<Assignment<Level>> satisfyingAssignment(TypeDomain<Level> types, Collection<TypeVar> requiredVariables) {
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
     * @return the stream of constriants where assignement <code>a</code> is applied where possible.
     */
    public Stream<Constraint<Level>> apply(Assignment<Level> a) {
        return this.stream().map(c -> c.apply(a));
    }


    /**
     * @return True if <code>other</code> is satisfiable for all of the satisfying assignments of <code>this</code>.
     */
    public boolean subsumes(ConstraintSet<Level> other) {
        return !this.subsumptionCounterExample(other).isPresent();
    }

    /**
     * @return true if <code>this</code> is a suitable signature for <code>other</code>
     */
    public boolean isSignatureOf(TypeVars tvars, ConstraintSet<Level> other) {
        return !this.signatureCounterExample(tvars, other).isPresent();
    }


    public ConstraintSet<Level> projectForSignature(TypeVars tvars, ConstraintSet<Level> sig) {
        Set<TypeVar> vars = Stream.concat(Stream.of(tvars.topLevelContext()), sig.variables()).collect(toSet());
        return this.projectTo(vars);
    }

    /**
     * Create a set of constraints that only mentions the type variables in <code>typeVars</code>. The resulting set,
     * interpreted as a ConstraintSet, should impose the same restrictions on the variables in <code>typeVars</code> as
     * the original set.
     */
    public abstract ConstraintSet<Level> projectTo(Set<TypeVar> typeVars);
}
