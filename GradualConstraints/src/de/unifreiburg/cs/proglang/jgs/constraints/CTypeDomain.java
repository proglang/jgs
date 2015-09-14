package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Optional;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;
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

    public static abstract class CType<Level> {

        public abstract Optional<Type<Level>> tryApply(Assignment<Level> a);

        public abstract Stream<TypeVar> variables();

        public final Type<Level> apply(Assignment<Level> a) {
            return tryApply(a).orElseThrow(() -> new RuntimeException("Unknown variable: "
                                                                      + this.toString()
                                                                      + " Ass.: "
                                                                      + a.mappedVariables()
                                                                         .toString()));
        }


    }

    public CType<Level> literal(Type<Level> t) {
        return new Literal<>(t);
    }

    public static <Level> CType<Level> variable(TypeVar v) {
        return new Variable<>(v);
    }

    public static class Literal<Level> extends CType<Level> {

        @Override
        public String toString() {
            return "Literal [t=" + t + "]";
        }

        public final Type<Level> t;

        private Literal(Type<Level> t) {
            super();
            this.t = t;
        }

        @Override
        public Optional<Type<Level>> tryApply(Assignment<Level> a) {
            return Optional.of(this.t);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
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
            @SuppressWarnings("rawtypes")
            Literal other = (Literal) obj;
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

    public static class Variable<Level> extends CType<Level> {

        public final TypeVar v;

        private Variable(TypeVar v) {
            super();
            this.v = v;
        }

        @Override
        public Optional<Type<Level>> tryApply(Assignment<Level> a) {
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
            @SuppressWarnings("rawtypes")
            Variable other = (Variable) obj;
            if (v == null) {
                if (other.v != null)
                    return false;
            } else if (!v.equals(other.v))
                return false;
            return true;
        }


        @Override
        public Stream<TypeVar> variables() {
            return Stream.of(this.v);
        }

    }

}
