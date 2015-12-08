package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Collection;

public interface ConstraintSetFactory<Level> {
    
    ConstraintSet<Level> empty();
    ConstraintSet<Level> fromCollection(Collection<Constraint<Level>> cs);
}
