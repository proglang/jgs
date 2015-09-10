package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Optional;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;

/**
 * The domain of constraint elements. We have type variables and literal
 * security types.
 * 
 * @author Luminous Fennell
 *
 * @param <Level>
 */
public class CTypeDomain<Level> {

    public abstract class CType {

        public abstract Optional<TypeDomain<Level>.Type> tryApply(Assignment<Level> a);
        public abstract Stream<TypeVar> variables();

        public final TypeDomain<Level>.Type apply(Assignment<Level> a) {
            return tryApply(a).orElseThrow(() -> new RuntimeException("Unknown variable: "
                                                                      + this.toString()
                                                                      + " Ass.: "
                                                                      + a.mappedVariables()
                                                                         .toString()));
        }

        protected boolean contextEquals(CType other) {
            return this.getOuterType().equals(other.getOuterType());
        }

        private CTypeDomain<Level> getOuterType() {
            return CTypeDomain.this;
        }
    }

    public CType literal(TypeDomain<Level>.Type t) {
        return new Literal(t);
    }

    public CType variable(TypeVar v) {
        return new Variable(v);
    }

    public class Literal extends CType {

        @Override
        public String toString() {
            return "Literal [t=" + t + "]";
        }

        public final TypeDomain<Level>.Type t;

        private Literal(TypeDomain<Level>.Type t) {
            super();
            this.t = t;
        }

        @Override
        public Optional<TypeDomain<Level>.Type> tryApply(Assignment<Level> a) {
            return Optional.of(this.t);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + super.getOuterType().hashCode();
            result = prime * result + ((t == null) ? 0 : t.hashCode());
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
            @SuppressWarnings("unchecked")
            Literal other = (Literal) obj;
            if (!contextEquals(other)) {
                throw new RuntimeException("Compairing constraints from different contexts");
            }
            if (t == null) {
                if (other.t != null)
                    return false;
            } else if (!t.equals(other.t))
                return false;
            return true;
        }

        @Override
        public Stream<TypeVar> variables() {
            return Stream.empty();
        }

    }

    public class Variable extends CType {

        public final TypeVar v;

        private Variable(TypeVar v) {
            super();
            this.v = v;
        }

        @Override
        public Optional<TypeDomain<Level>.Type> tryApply(Assignment<Level> a) {
            return Optional.ofNullable(a.get().get(v));
        }

        @Override
        public String toString() {
            return "Variable [v=" + v + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((v == null) ? 0 : v.hashCode());
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
            @SuppressWarnings("unchecked")
            Variable other = (Variable) obj;
            if (!contextEquals(other)) {
                throw new RuntimeException("Compairing constraints from different contexts");
            }
            if (v == null) {
                if (other.v != null)
                    return false;
            } else if (!v.equals(other.v))
                return false;
            return true;
        }

        @SuppressWarnings("rawtypes")
        private CTypeDomain getOuterType() {
            return CTypeDomain.this;
        }

        @Override
        public Stream<TypeVar> variables() {
            return Stream.of(this.v);
        }

    }

}
