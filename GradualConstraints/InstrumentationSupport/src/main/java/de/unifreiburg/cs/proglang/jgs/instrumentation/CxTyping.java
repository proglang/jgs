package de.unifreiburg.cs.proglang.jgs.instrumentation;

import soot.jimple.Stmt;

/**
 * A map for the context-types in a method.
 */
public interface CxTyping<Level> {

    /**
     * Return the context type for statement @{code s}, given a particular instantiation.
     */
    Type<Level> get(Instantiation<Level> instantiation, Stmt s);


}
