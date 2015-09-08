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
    
    public Constraint<Level> comp(CTypeDomain<Level>.CType lhs, CTypeDomain<Level>.CType rhs) {
        return new CompConstraint(lhs, rhs);
    }

    public Constraint<Level> dimpl(CTypeDomain<Level>.CType lhs, CTypeDomain<Level>.CType rhs) {
        return new DImplConstraint(lhs, rhs);
    }
    
    
    private abstract class AConstraint implements Constraint<Level> {
        public final CTypeDomain<Level>.CType lhs;
        public final CTypeDomain<Level>.CType rhs;

        public AConstraint(CTypeDomain<Level>.CType lhs,
                CTypeDomain<Level>.CType rhs) {
            super();
            this.lhs = lhs;
            this.rhs = rhs;
        }
        
        
    }

    private class LeConstraint extends AConstraint {

        public LeConstraint(CTypeDomain<Level>.CType lhs,
                CTypeDomain<Level>.CType rhs) {
            super(lhs, rhs);
        }

        @Override
        public boolean isSatisfied(Assignment<Level> a) {
            return types.le(this.lhs.apply(a), this.rhs.apply(a));
        }
    }
    
    private class CompConstraint extends AConstraint {
        
        public CompConstraint(CTypeDomain<Level>.CType lhs, CTypeDomain<Level>.CType rhs) {
            super(lhs, rhs);
        }

        @Override
        public boolean isSatisfied(Assignment<Level> a) {
            TypeDomain<Level>.Type tlhs, trhs;
            tlhs = this.lhs.apply(a);
            trhs = this.rhs.apply(a);
            return types.le(tlhs, trhs)
                    || 
                    types.le(trhs, tlhs);

        }
    }
    
    private class DImplConstraint extends AConstraint {

        public DImplConstraint(CTypeDomain<Level>.CType lhs, CTypeDomain<Level>.CType rhs) {
            super(lhs, rhs);
        }

        @Override
        public boolean isSatisfied(Assignment<Level> a) {
            // lhs = ? --> rhs <= ?
            return (! this.lhs.apply(a).equals(types.dyn()) || types.le(this.rhs.apply(a), types.dyn()));
        }
        
    }
}
