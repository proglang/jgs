package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;

/**
 * A context for constraints, i.e. constraints for a particular type domain. 
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

    public Constraint<Level> le(CType<Level> lhs, CType<Level> rhs) {
        return new LeConstraint(lhs, rhs);
    }

    public Constraint<Level> comp(CType<Level> lhs, CType<Level> rhs) {
        return new CompConstraint(lhs, rhs);
    }

    public Constraint<Level> dimpl(CType<Level> lhs, CType<Level> rhs) {
        return new DImplConstraint(lhs, rhs);
    }

    private abstract class AConstraint implements Constraint<Level> {
        public final CType<Level> lhs;
        public final CType<Level> rhs;

        public AConstraint(CType<Level> lhs, CType<Level> rhs) {
            super();
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public Stream<TypeVar> variables() {
            return Stream.concat(lhs.variables(), rhs.variables());
        }
        
        @Override
        public String toString() {
            return String.format("%s %s %s", this.lhs, this.getSymbol(), this.rhs);
        }
        
        abstract protected String getSymbol();

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
            result = prime * result + ((rhs == null) ? 0 : rhs.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            @SuppressWarnings("unchecked")
            AConstraint other = (AConstraint) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (lhs == null) {
                if (other.lhs != null)
                    return false;
            } else if (!lhs.equals(other.lhs))
                return false;
            if (rhs == null) {
                if (other.rhs != null)
                    return false;
            } else if (!rhs.equals(other.rhs))
                return false;
            return true;
        }

        @SuppressWarnings("rawtypes")
        private Constraints getOuterType() {
            return Constraints.this;
        }
        
        
    }

    private class LeConstraint extends AConstraint {

        @Override
        protected String getSymbol() {
            return "<=";
        }

        public LeConstraint(CType<Level> lhs, CType<Level> rhs) {
            super(lhs, rhs);
        }

        @Override
        public boolean isSatisfied(Assignment<Level> a) {
            return types.le(this.lhs.apply(a), this.rhs.apply(a));
        }
        

    }

    private class CompConstraint extends AConstraint {

        public CompConstraint(CType<Level> lhs,
                              CType<Level> rhs) {
            super(lhs, rhs);
        }

        @Override
        public boolean isSatisfied(Assignment<Level> a) {
            Type<Level> tlhs, trhs;
            tlhs = this.lhs.apply(a);
            trhs = this.rhs.apply(a);
            return types.le(tlhs, trhs) || types.le(trhs, tlhs);

        }

        @Override
        protected String getSymbol() {
            return "~";
        }

    }

    private class DImplConstraint extends AConstraint {

        public DImplConstraint(CType<Level> lhs,
                               CType<Level> rhs) {
            super(lhs, rhs);
        }

        @Override
        public boolean isSatisfied(Assignment<Level> a) {
            // lhs = ? --> rhs <= ?
            return (!this.lhs.apply(a).equals(types.dyn())
                    || types.le(this.rhs.apply(a), types.dyn()));
        }

        @Override
        protected String getSymbol() {
            return "?=>";
        }

    }
}
