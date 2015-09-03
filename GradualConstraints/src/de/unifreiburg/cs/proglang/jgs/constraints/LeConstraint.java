package de.unifreiburg.cs.proglang.jgs.constraints;

import de.unifreiburg.cs.proglang.jgs.util.NotImplemented;

public class LeConstraint<Level> implements Constraint<Level> {
    
    public final CTypeDomain<Level> lhs;
    public final CTypeDomain<Level> rhs;
    
    public LeConstraint(CTypeDomain<Level> lhs, CTypeDomain<Level> rhs) {
        super();
        this.lhs = lhs;
        this.rhs = rhs;
    }



    @Override
    public boolean isSatisfied(Assignment<Level> a) {
        throw new NotImplemented("isSatisfied for LeConstraints");
    }

}
