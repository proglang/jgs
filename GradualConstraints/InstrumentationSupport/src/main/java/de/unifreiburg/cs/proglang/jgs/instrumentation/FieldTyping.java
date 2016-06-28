package de.unifreiburg.cs.proglang.jgs.instrumentation;

import soot.SootField;

/**
 * A mapping from fields to types.
 */
public interface FieldTyping<Level> {

    /**
     * Return the type of field {@code f}.
     */
    Type<Level> get(SootField f);
}
