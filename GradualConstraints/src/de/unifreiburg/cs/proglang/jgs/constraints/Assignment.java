package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeDomain.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.util.NotImplemented;

/**
 * An assignment from type variables to types.
 * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
 *
 * @param <Level>
 */
public class Assignment<Level> {
    
    private final Map<TypeVar, Type<Level>> ass;

    public Assignment(Map<TypeVar, Type<Level>> ass) {
        super();
        this.ass = ass;
    }

    /**
     * @return An immutable reference to the map that represents the assignment.
     */
    public Map<TypeVar, Type<Level>> get() {
        return Collections.unmodifiableMap(this.ass);
    }
    
    /**
     * Apply an assignment to a constraint element to get a proper type
     * @param t The constraint element
     * @return The type after applying the assignment.
     */
    public Type<Level> applyTo(CType<Level> t) {
        throw new NotImplemented("Assignment.applyTo");
    }
    
    @Override
    public String toString() {
        return ass.toString();
    }
    
    public Set<TypeVar> mappedVariables() {
        return ass.keySet();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ass == null) ? 0 : ass.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        @SuppressWarnings("rawtypes")
        Assignment other = (Assignment) obj;
        if (ass == null) {
            if (other.ass != null)
                return false;
        } else if (!ass.equals(other.ass))
            return false;
        return true;
    }
    
}
