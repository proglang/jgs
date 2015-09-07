package de.unifreiburg.cs.proglang.jgs.constraints;

/**
 * A context for constraints.
 * 
 * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
 *
 * @param <Level>
 */
public class Constraints<Level> {

    private final TypeDomain<Level> types;

    public Constraints(TypeDomain<Level> types) {
        super();
        this.types = types;
    }
    
    public Constraint<Level> le(CTypeDomain<Level>.CType lhs, CTypeDomain<Level>.CType rhs) {
        return new LeConstraint(lhs, rhs);
    }

    private class LeConstraint implements Constraint<Level> {

        public final CTypeDomain<Level>.CType lhs;
        public final CTypeDomain<Level>.CType rhs;

        public LeConstraint(CTypeDomain<Level>.CType lhs,
                CTypeDomain<Level>.CType rhs) {
            super();
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public boolean isSatisfied(Assignment<Level> a) {
            return types.le(this.lhs.apply(a), this.rhs.apply(a));
        }
    }
}
