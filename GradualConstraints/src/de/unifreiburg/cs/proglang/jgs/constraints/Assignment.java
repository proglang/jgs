package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Collections;
import java.util.Map;

/**
 * An assignment from type variables to types.
 * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
 *
 * @param <Level>
 */
public class Assignment<Level> {
    
    private final Map<TypeVars, TypeDomain<Level>> ass;

    public Assignment(Map<TypeVars, TypeDomain<Level>> ass) {
        super();
        this.ass = ass;
    }

    /**
     * @return An immutable reference to the map that represents the assignment.
     */
    public Map<TypeVars, TypeDomain<Level>> get() {
        return Collections.unmodifiableMap(this.ass);
    }
    
    
    

}
