package de.unifreiburg.cs.proglang.jgs.typing;

import java.util.Collections;

/**
 * Companion class to <code>Environment</code>
 * @author fennell
 *
 */
public class Environments {
    
    public static Environment makeEmpty() {
        return new Environment(Collections.emptyMap());
    }

}
