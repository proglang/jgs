package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;

import java.util.Collections;
import java.util.Map;

/**
 * Companion class to <code>Environment</code>
 * @author fennell
 *
 */
public class Environments {
    
    public static Environment makeEmpty() {
        return new Environment(Collections.emptyMap());
    }

    public static Environment fromMap(Map<Var<?>, TypeVars.TypeVar> m) {
        return new Environment(m);
    }

}
