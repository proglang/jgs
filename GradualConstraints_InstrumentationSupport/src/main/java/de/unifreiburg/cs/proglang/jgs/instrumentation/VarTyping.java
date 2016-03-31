package de.unifreiburg.cs.proglang.jgs.instrumentation;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView;
import soot.Local;
import soot.jimple.Stmt;

/**
 * A map for the variable typing in a method
 */
public interface VarTyping<Level> {

    TypeView<Level> get(Stmt s, Local l);
}
