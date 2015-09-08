package de.unifreiburg.cs.proglang.jgs.constraints;

/**
 * A context for (anonymous) type variables. Allows to create fresh variables and to lookup
 * existing ones.
 * 
 * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
 *
 */
public class TypeVars {
    private final String prefix;
    private int count = 0;
    
    public TypeVars(String prefix) {
        super();
        this.prefix = prefix;
    }

    /**
     * Create a fresh type variable.
     * 
     * @return A type variable which is unique for this context.
     */
    public TypeVar fresh() {
        String suffix = Integer.toString(count);
        this.count++;
        return new TypeVar(prefix + suffix);
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
