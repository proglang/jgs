package de.unifreiburg.cs.proglang.jgs.instrumentation;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews;
import soot.SootField;

/**
 * A mapping from fields to types.
 */
public interface FieldTyping<Level> {

    /**
     * Return the type of field {@code f}.
     */
    TypeViews.TypeView<Level> get(SootField f);
}
