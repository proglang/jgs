package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A context for type variables. Allows to create fresh variables and to lookup
 * existing ones.
 * 
 * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
 *
 */
public class TypeVars {
    private final Map<String, Integer> names = new HashMap<>();

    /**
     * Create a fresh type variable.
     * 
     * @param prefix
     *            A prefix for the variable name.
     * @return A type variable which is unique for this context.
     */
    public TypeVar fresh(String prefix) {
        String suffix;
        if (names.containsKey(prefix)) {
            int count = names.get(prefix);
            names.put(prefix, count + 1);
            suffix = Integer.toString(count);
        } else {
            names.put(prefix, 1);
            suffix = "";
        }
        return new TypeVar(prefix + suffix);
    }

    /**
     * Lookup a type variable that exists in this context
     * 
     * @param name
     *            The name of the variable
     * @return Just the variable if it was created with <code>fresh<code>,
     *         Nothing otherwise.
     */
    public Optional<TypeVar> tryGet(String name) {
        return names.containsKey(name) ? Optional.of(new TypeVar(name))
                                       : Optional.empty();
    }

    /**
     * Unsafe version of <code>tryGet</code>.
     */
    public TypeVar get(String name) {
        return tryGet(name).orElseThrow(() -> new RuntimeException("Variable "
                                                                   + name
                                                                   + " does not exist!"));
    }

    public static class TypeVar {
        public final String name;

        private TypeVar(String name) {
            super();
            this.name = name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public String toString() {
            return "TypeVar [name=" + name + "]";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TypeVar other = (TypeVar) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }
    }

}
