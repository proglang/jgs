package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import soot.jimple.InvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;

import java.util.Optional;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.*;

/**
 * An interface to detect casts from method calls. In JGS, casts are identity
 * methods that BasicStatementTyping.generate treats specially.
 * <p>
 * Created by fennell on 11/13/15.
 */
public abstract class Casts<Level> {

    public static class CxCast<Level> {
        public final Type<Level> sourceType;
        public final Type<Level> destType;

        public CxCast(Type<Level> sourceType, Type<Level> destType) {
            this.sourceType = sourceType;
            this.destType = destType;
        }

        @Override
        public String toString() {
            return String.format("cx(%s => %s)",
                    this.destType,
                    this.sourceType);
        }
    }

    public static class ValueCast<Level> {
        public final Type<Level> sourceType;
        public final Type<Level> destType;
        public final Var<?> value;

        protected ValueCast(Type<Level> sourceType,
                            Type<Level> destType,
                            Var<?> value) {
            this.sourceType = sourceType;
            this.destType = destType;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("(%s <= %s)%s",
                    this.destType,
                    this.sourceType,
                    this.value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            ValueCast<?> cast = (ValueCast<?>) o;

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

        @Override
        public int hashCode() {
            int result = sourceType != null ? sourceType.hashCode() : 0;
            result = 31 * result + (destType != null ? destType.hashCode() : 0);
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }
    }

    public Optional<CxCast<Level>> detectContextCastStartFromStmt(Stmt s) {
        if (s.containsInvokeExpr()) {
            InvokeExpr e = s.getInvokeExpr();
            if (e instanceof StaticInvokeExpr) {
                return this.detectContextCastStartFromCall((StaticInvokeExpr) e);
            }
        }
        return Optional.empty();
    }

    public boolean detectContextCastEndFromStmt(Stmt s) {
        if (s.containsInvokeExpr()) {
            InvokeExpr e = s.getInvokeExpr();
            if (e instanceof StaticInvokeExpr) {
                return this.detectContextCastEndFromCall((StaticInvokeExpr) e);
            }
        }
        return false;
    }

    public abstract Optional<ValueCast<Level>> detectValueCastFromCall(StaticInvokeExpr e);

    public abstract Optional<CxCast<Level>> detectContextCastStartFromCall(StaticInvokeExpr e);

    public abstract boolean detectContextCastEndFromCall(StaticInvokeExpr e);

    protected ValueCast<Level> makeValueCast(Type<Level> source, Type<Level> destination, Var<?> value) {
        return new ValueCast<>(source, destination, value);
    }

    protected CxCast<Level> makeContextCast(Type<Level> source, Type<Level> destination) {
        return new CxCast<>(source, destination);
    }
}
