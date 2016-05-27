package de.unifreiburg.cs.proglang.jgs.instrumentation;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView;
import soot.jimple.Stmt;

/**
 * A map for the context-types in a method.
 */
public interface CxTyping<Level> {

    TypeView<Level> get(Stmt s);


}
