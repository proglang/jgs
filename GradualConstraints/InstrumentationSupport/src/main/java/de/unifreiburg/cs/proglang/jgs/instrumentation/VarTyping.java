package de.unifreiburg.cs.proglang.jgs.instrumentation;

import soot.Local;
import soot.jimple.Stmt;

/**
 * A map for the variable typing in a method.
 */
public interface VarTyping<Level> {

    /**
     * Given an @{code instantiation}, return the type for local @{code l} before statement @{code s}.
     * Returns either static, dynamic or public (low)
     * @param instantiation describes the kind of instantiation, for examples: (D, D) => D
     */
    Type<Level> getBefore(Instantiation<Level> instantiation, Stmt s, Local l);
    /**
     * Given an @{code instantiation}, return the type for local @{code l} after statement @{code s}.
     */
    Type<Level> getAfter(Instantiation<Level> instantiation, Stmt s, Local l);
}
