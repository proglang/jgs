package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;

public interface Constraint<Level> {

    boolean isSatisfied(Assignment<Level> a);
    
    Stream<TypeVar> variables();

}
