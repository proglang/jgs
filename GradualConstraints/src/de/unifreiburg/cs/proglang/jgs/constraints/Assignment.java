package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Collections;
import java.util.Map;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.util.NotImplemented;

/**
 * An assignment from type variables to types.
 * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
 *
 * @param <Level>
 */
public class Assignment<Level> {
    
    private final Map<TypeVar, TypeDomain<Level>.Type> ass;

    public Assignment(Map<TypeVar, TypeDomain<Level>.Type> ass) {
        super();
        this.ass = ass;
    }

    /**
     * @return An immutable reference to the map that represents the assignment.
     */
    public Map<TypeVar, TypeDomain<Level>.Type> get() {
        return Collections.unmodifiableMap(this.ass);
    }
    
    /**
     * Apply an assignment to a constraint element to get a proper type
     * @param t The constraint element
     * @return The type after applying the assignment.
     */
    public TypeDomain<Level>.Type applyTo(CTypeDomain<Level> t) {
        throw new NotImplemented("Assignment.applyTo");
    }
    
    
    

}
