package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Optional;

/**
 * An immutable set of constraints.
 * @author fennell
 *
 * @param <Level>
 */
public abstract class ConstraintSet<Level> implements Iterable<Constraint<Level>>{

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
    abstract public boolean isSatisfied(Assignment<Level> a);

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
     */
    public Optional<Assignment<Level>> sat() {
        throw new RuntimeException("sat() operation not supported");
    }
    
    
}
