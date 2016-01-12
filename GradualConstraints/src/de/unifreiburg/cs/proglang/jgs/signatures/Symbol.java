package de.unifreiburg.cs.proglang.jgs.signatures;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import soot.EquivalentValue;
import soot.jimple.Jimple;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * JGS Symbols that occur in method signatures. They correspond to Jimples Parameters, @return, or literals
 */
public abstract class Symbol<Level> {
    /**
     * Create a parameter symbol from a type and a position.
     *
     * Why not use ParameterRef? Because it only implements pointer equality and there is no way to get to the ParameterRefs of a SootMethod (that I know of)
     */
    public static <Level> Param<Level> param(soot.Type t, int pos) {
        return new Param<>(new EquivalentValue(Jimple.v().newParameterRef(t, pos)));
    }

    // TODO-performance: make a singleton out of this
    public static <Level> Symbol<Level> ret() {
        return new Return<>();
    }

    public static <Level> Symbol<Level> literal(TypeDomain.Type<Level> t) {
        return new Literal<>(t);
    }

    @Override public abstract String toString();

    public abstract CTypes.CType<Level> toCType(Map<Symbol<Level>, TypeVars.TypeVar> tvarMapping);

    public static class Param<Level> extends Symbol<Level> {
        private final EquivalentValue me;

        @Override public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Param<?> param = (Param<?>) o;

            return !(me != null ? !me.equals(param.me) : param.me != null);

        }

        @Override public int hashCode() {
            return me != null ? me.hashCode() : 0;
        }

        private Param(EquivalentValue me) {
            super();
            this.me = me;
        }

        @Override
        public String toString() {
            return String.format("@%s", this.me.toString());
        }

        @Override
        public CTypes.CType<Level> toCType(Map<Symbol<Level>, TypeVars.TypeVar> tvarMapping) {
            return Optional.ofNullable(tvarMapping.get(this))
                    .map(CTypes::<Level>variable)
                    .orElseThrow(() -> new NoSuchElementException(String.format(
                            "No mapping for %s: %s",
                            this,
                            tvarMapping.toString())));
        }
    }

    public static class Return<Level> extends Symbol<Level> {

        private Return() {
        }

        @Override
        public String toString() {
            return "@return";
        }

        @Override
        public CTypes.CType<Level> toCType(Map<Symbol<Level>, TypeVars.TypeVar> tvarMapping) {
            return Optional.ofNullable(tvarMapping.get(this))
                    .map(CTypes::<Level>variable)
                    .orElseThrow(() -> new NoSuchElementException(String.format(
                            "No mapping for %s: %s",
                            this.toString(),
                            tvarMapping.toString())));
        }

        @Override
        // TODO-performance: should be a singleton
        public boolean equals(Object other) {
            return this.getClass().equals(other.getClass());
        }

        @Override
        public int hashCode() {
            return this.getClass().hashCode();
        }

    }

    public static class Literal<Level> extends Symbol<Level> {
        final TypeDomain.Type<Level> me;

        private Literal(TypeDomain.Type<Level> me) {
            super();
            this.me = me;
        }

        @Override
        public String toString() {
            return me.toString();
        }

        @Override
        public CTypes.CType<Level> toCType(Map<Symbol<Level>, TypeVars.TypeVar> tvarMapping) {
            return CTypes.literal(this.me);
        }

        @Override public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Literal<?> literal = (Literal<?>) o;

            return !(me != null ? !me.equals(literal.me) : literal.me != null);

        }

        @Override public int hashCode() {
            return me != null ? me.hashCode() : 0;
        }
    }
}
