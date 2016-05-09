package de.unifreiburg.cs.proglang.jgs.instrumentation;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView;

/**
 * Represents the instantiation of the type variables of a method's signature.
 */
public interface Instantiation<Level> {

    /**
     *
     * Return the type for parameter number @{code param}.
     */
    TypeView<Level> get(int param);
}
