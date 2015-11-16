package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import soot.jimple.StaticInvokeExpr;

import java.util.Optional;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.*;

/**
 * An interface to detect casts from method calls. In JGS, casts are identity methods that BasicStatementTyping.generate treats specially.
 * <p>
 * Created by fennell on 11/13/15.
 */
public abstract class Casts<Level> {

    public static class Cast<Level> {
        public final Type<Level> sourceType;
        public final Type<Level> destType;
        public final Var<?> value;

        protected Cast(Type<Level> sourceType,
                       Type<Level> destType,
                       Var<?> value) {
            this.sourceType = sourceType;
            this.destType = destType;
            this.value = value;
        }

        @Override public String toString() {
            return String.format("(%s <= %s)%s",
                                 this.destType,
                                 this.sourceType,
                                 this.value);
        }

        @Override public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Cast<?> cast = (Cast<?>) o;

            if (sourceType != null ?
                !sourceType.equals(cast.sourceType) :
                cast.sourceType != null)
                return false;
            if (destType != null ?
                !destType.equals(cast.destType) :
                cast.destType != null)
                return false;
            return !(value != null ?
                     !value.equals(cast.value) :
                     cast.value != null);

        }

        @Override public int hashCode() {
            int result = sourceType != null ? sourceType.hashCode() : 0;
            result = 31 * result + (destType != null ? destType.hashCode() : 0);
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }
    }

    public abstract Optional<Cast<Level>> detectFromCall(StaticInvokeExpr e);
    protected Cast<Level> makeCast(Type<Level>source, Type<Level> destination, Var<?> value) {
        return new Cast<>(source, destination, value);
    }
}
