package de.unifreiburg.cs.proglang.jgs.constraints;


import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView;

/**
 * The domain of constraint elements. We have type variables and literal
 * security types.
 * 
 * @author Luminous Fennell
 *
 */
public class CTypes {

    public static abstract class CType<Level> {


        public abstract CTypeViews.CTypeView<Level> inspect();

    }

    public static <Level> CType<Level> literal(TypeView<Level> t) {
        return new Literal<>(t);
    }

    public static <Level> CType<Level> variable(TypeVar v) {
        return new Variable<>(v);
    }

    public static class Literal<Level> extends CType<Level> {

        @Override
        public String toString() {
            return t.toString();
        }

        public final TypeView<Level> t;

        private Literal(TypeView<Level> t) {
            super();
            this.t = t;
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
        public CTypeViews.CTypeView<Level> inspect() {
            return new CTypeViews.Lit<Level>(this.t);
        }



    }

    public static class Variable<Level> extends CType<Level> {

        public final TypeVar v;

        private Variable(TypeVar v) {
            super();
            this.v = v;
        }

        @Override
        public String toString() {
            return v.toString();
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
        public CTypeViews.CTypeView<Level> inspect() {
            return new CTypeViews.Variable<Level>(this.v);
        }


    }

}
