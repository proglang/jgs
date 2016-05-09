package de.unifreiburg.cs.proglang.jgs.instrumentation;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView;
import soot.baf.Inst;
import soot.jimple.Stmt;

/**
 * A map for the context-types in a method.
 */
public interface CxTyping<Level> {

    /**
     *
     * Given an @{code instantiation}, return the type for local @{code l} at statement @{code s}.
     */
    TypeView<Level> get(Instantiation<Level> instantiation, Stmt s);


}
