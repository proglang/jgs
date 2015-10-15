package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeDomain.CType;
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

    }

    private class LeConstraint extends AConstraint {

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

    }
}
