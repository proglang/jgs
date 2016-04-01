package de.unifreiburg.cs.proglang.jgs.constraints;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;

import java.util.Collection;
import java.util.Collections;

public class NaiveConstraintsFactory<Level> implements ConstraintSetFactory<Level> {
    
    private final TypeDomain<Level> types;


    public NaiveConstraintsFactory(TypeDomain<Level> types) {
        super();
        this.types = types;
    }

    @Override
    public ConstraintSet<Level> empty() {
        return new NaiveConstraints<>(types, Collections.emptySet());
    }

    @Override
    public ConstraintSet<Level> fromCollection(Collection<Constraint<Level>> cs) {
        return new NaiveConstraints<>(types, cs);
    }

}
