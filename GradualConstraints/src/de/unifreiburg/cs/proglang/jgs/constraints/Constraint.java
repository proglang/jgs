package de.unifreiburg.cs.proglang.jgs.constraints;

public interface Constraint<Level> {

    boolean isSatisfied(Assignment<Level> a);

}
