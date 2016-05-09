package de.unifreiburg.cs.proglang.jgs.instrumentation;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView;
import soot.Local;
import soot.jimple.Stmt;

/**
 * A map for the variable typing in a method
 */
public interface VarTyping<Level> {

    /**
     *
     * Given an @{code instantiation}, return the type for local @{code l} at statement @{code s}.
     */
    TypeView<Level> get(Instantiation<Level> instantiation, Stmt s, Local l);
}
