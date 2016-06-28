package de.unifreiburg.cs.proglang.jgs.instrumentation;

import soot.Local;
import soot.jimple.Stmt;

/**
 * A map for the variable typing in a method.
 */
public interface VarTyping<Level> {

    /**
     * Given an @{code instantiation}, return the type for local @{code l} before statement @{code s}.
     */
    Type<Level> getBefore(Instantiation<Level> instantiation, Stmt s, Local l);
    /**
     * Given an @{code instantiation}, return the type for local @{code l} after statement @{code s}.
     */
    Type<Level> getAfter(Instantiation<Level> instantiation, Stmt s, Local l);
}
