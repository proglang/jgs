package de.unifreiburg.cs.proglang.jgs.instrumentation;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView;
import de.unifreiburg.cs.proglang.jgs.typing.TypingException;
import scala.Option;
import scala.util.Try;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;

/**
 * An interface to detect casts from method calls. In JGS, casts are identity
 * methods that BasicStatementTyping.generate treats specially.
 * <p>
 * Created by fennell on 11/13/15.
 */
public abstract class ACasts<Level> implements Casts<Level> {

    @Override
    public boolean isValueCast(Stmt s) {
        try {
            return detectValueCastFromStmt(s).isDefined();
        } catch (TypingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public ValueConversion<Level> getValueCast(Stmt s) {
        try {
            Option<ValueCast<Level>> mcast = detectValueCastFromStmt(s);
            if (mcast.isDefined()) {
                return mcast.get();
            } else {
                throw new IllegalArgumentException("No value cast at statement: " + s);
            }
        } catch (TypingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean isCxCastStart(Stmt s) {
        try {
            return detectContextCastStartFromStmt(s).isDefined();
        } catch (TypingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Conversion<Level> getCxCast(Stmt s) {
            try {
                Option<CxCast<Level>> mcast = detectContextCastStartFromStmt(s);
                if (mcast.isDefined()) {
                    return mcast.get();
                } else {
                    throw new IllegalArgumentException("No context cast starts at statement: " + s);
                }
            } catch (TypingException e) {
                throw new IllegalStateException(e);
            }
        }

    @Override
    public boolean isCxCastEnd(Stmt s) {
        throw new RuntimeException("Not Implemented!");
    }

    public static class CxCast<Level> implements Casts.Conversion<Level> {
        public final TypeView<Level> sourceType;
        public final TypeView<Level> destType;

        public CxCast(TypeView<Level> sourceType, TypeView<Level> destType) {
            this.sourceType = sourceType;
            this.destType = destType;
        }

        @Override
        public String toString() {
            return String.format("cx(%s => %s)",
                    this.destType,
                    this.sourceType);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CxCast<?> cxCast = (CxCast<?>) o;

            if (sourceType != null ? !sourceType.equals(cxCast.sourceType) :
                cxCast.sourceType != null) return false;
            return destType != null ? destType.equals(cxCast.destType) :
                   cxCast.destType == null;

        }

        @Override
        public int hashCode() {
            int result = sourceType != null ? sourceType.hashCode() : 0;
            result = 31 * result + (destType != null ? destType.hashCode() : 0);
            return result;
        }

        @Override
        public Type<Level> getSrcType() {
            return this.sourceType;
        }

        @Override
        public Type<Level> getDestType() {
            return this.destType;
        }
    }

    public static class ValueCast<Level> implements Casts.ValueConversion<Level> {
        public final TypeView<Level> sourceType;
        public final TypeView<Level> destType;
        public final Option<Var<?>> value;

        public ValueCast(TypeView<Level> sourceType,
                            TypeView<Level> destType,
                            Option<Var<?>> value) {
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

        @Override
        public Type<Level> getSrcType() {
            return this.sourceType;
        }

        @Override
        public Type<Level> getDestType() {
            return this.destType;
        }

        @Override
        public Value getSrcValue() {
            // TODO: this blows up if we are casting a constant... we should provide values here instead of vars
            return (Value)value.get().repr;
        }
    }

    public Option<ValueCast<Level>> detectValueCastFromStmt(Stmt s) throws TypingException {
        if (s.containsInvokeExpr()) {
            InvokeExpr e = s.getInvokeExpr();
            if (e instanceof StaticInvokeExpr) {
                Try<Option<ValueCast<Level>>> result = this.detectValueCastFromCall((StaticInvokeExpr) e);
                if (result.isSuccess()) {
                    return result.get();
                } else {
                    throw new TypingException(result.failed().get().getMessage());
                }
            }
        }
        return Option.empty();
    }

    public Option<CxCast<Level>> detectContextCastStartFromStmt(Stmt s)
            throws TypingException {
        if (s.containsInvokeExpr()) {
            InvokeExpr e = s.getInvokeExpr();
            if (e instanceof StaticInvokeExpr) {
                Try<Option<CxCast<Level>>> result = this.detectContextCastStartFromCall((StaticInvokeExpr) e);
                if (result.isSuccess()) {
                    return result.get();
                } else {
                    throw new TypingException(result.failed().get().getMessage());
                }
            }
        }
        return Option.empty();
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

    public abstract Try<Option<ValueCast<Level>>> detectValueCastFromCall(StaticInvokeExpr e);

    public abstract Try<Option<CxCast<Level>>> detectContextCastStartFromCall(StaticInvokeExpr e);

    public abstract boolean detectContextCastEndFromCall(StaticInvokeExpr e);

    protected ValueCast<Level> makeValueCast(TypeView<Level> source, TypeView<Level> destination, Option<Var<?>> value) {
        return new ValueCast<>(source, destination, value);
    }

    protected CxCast<Level> makeContextCast(TypeView<Level> source, TypeView<Level> destination) {
        return new CxCast<>(source, destination);
    }
}
