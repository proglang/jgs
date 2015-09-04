package de.unifreiburg.cs.proglang.jgs.constraints;

import de.unifreiburg.cs.proglang.jgs.util.NotImplemented;

public class LeConstraint<Level> implements Constraint<Level> {
    
    public final CTypeDomain<Level>.CType lhs;
    public final CTypeDomain<Level>.CType rhs;
    
    public LeConstraint(CTypeDomain<Level>.CType lhs, CTypeDomain<Level>.CType rhs) {
        super();
        this.lhs = lhs;
        this.rhs = rhs;
    }



    @Override
    public boolean isSatisfied(Assignment<Level> a) {
        throw new NotImplemented("isSatisfied for LeConstraints");
    }

}
