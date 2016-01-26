package de.unifreiburg.cs.proglang.jgs.signatures;

import de.unifreiburg.cs.proglang.jgs.constraints.*;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.*;
import static java.util.Arrays.asList;
import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;
import static java.util.stream.Collectors.toList;

/**
 * Method signtures. Internal representation of method signatures of the form
 * <p>
 * M where <signature-constraints> and <effect>
 * <p>
 * Signature constraints are similar to regular constraints but instead of relating type variables, they relate special
 * symbols, which are:
 * <p>
 * - Parameter names - "@return" - Security Levels
 * <p>
 * Effects are just sets of security Levels
 *
 * @author fennell
 */
public class MethodSignatures<Level> {

    public MethodSignatures() {
    }

    /**
     * Signatures: constraints + effects
     */
    //TODO: this is a dangerous factory method, as it allows to create signatures that are not connected to any method
    // I (fennell) have been bitten once already.. should find a way to prevent this mistake
    public static <Level> Signature<Level> makeSignature(SigConstraintSet<Level> constraints, Effects<Level> effects) {
        return new Signature<>(constraints, effects);
    }

    public static class Signature<Level> {
        public final SigConstraintSet<Level> constraints;
        public final Effects<Level> effects;

        private Signature(SigConstraintSet<Level> constraints, Effects<Level> effects) {
            this.constraints = constraints;
            this.effects = effects;
        }

        @Override
        public String toString() {
            return String.format("C:%s, E:%s", constraints.toString(), effects.toString());
        }

        public Signature<Level> addConstraints(Stream<SigConstraint<Level>> sigs) {
            SigConstraintSet<Level> newConstraints = this.constraints.addAll(sigs);
            return makeSignature(newConstraints, this.effects);
        }
    }

    /* Effects */
    public static <Level> Effects<Level> emptyEffect() {
        return new Effects<>(new HashSet<>());
    }

    @SafeVarargs
    public static <Level> Effects<Level> effects(Type<Level> type, Type<Level>... types) {
        return MethodSignatures.<Level>emptyEffect().add(type, types);
    }

    @SafeVarargs
    public static <Level> Effects<Level> union(Effects<Level>... effectSets) {
        HashSet<Type<Level>> result = new HashSet<>();
        for (Effects<Level> es : effectSets) {
            result.addAll(es.effectSet);
        }
        return new Effects<>(result);
    }

    // TODO: move out of signatures and in its dedicated file in the typing package (as BodyTypingResult also uses it and the "concept" makes sense independently of MethodSignatures)
    public final static class Effects<Level> {
        private final HashSet<Type<Level>> effectSet;

        private Effects(HashSet<Type<Level>> effects) {
            this.effectSet = effects;
        }

        public final Effects<Level> add(Type<Level> type, Type<Level>... types) {
            return this.add(Stream.concat(Stream.of(type), asList(types).stream()).collect(toList()));
        }

        public final Effects<Level> add(Type<Level> type) {
            return this.add((Stream.of(type)).collect(toList()));
        }

        public final Effects<Level> add(Effects<Level> other) {
            return this.add(other.stream().collect(toList()));
        }

        public final Effects<Level> add(Collection<Type<Level>> types) {
            HashSet<Type<Level>> result = new HashSet<>(this.effectSet);
            result.addAll(types);
            return new Effects<>(result);
        }

        public final Stream<Type<Level>> stream() {
            return this.effectSet.stream();
        }

        private boolean covers(TypeDomain<Level> types, Type<Level> t) {
            return this.effectSet.stream().anyMatch(cand -> types.le(cand, t));
        }

        // TODO: get rid of the optional and just rename the method to "missingEffects" or something
        public final Optional<Effects> checkSubsumptionOf(TypeDomain<Level> types, Effects<Level> other) {
            HashSet<Type<Level>> notCovered = new HashSet<>();
            other.stream().filter(t -> !(this.covers(types,t))).forEach(notCovered::add);
            if (notCovered.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(new Effects(notCovered));
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Effects<?> effects = (Effects<?>) o;

            return !(effectSet != null ? !effectSet.equals(effects.effectSet) : effects.effectSet != null);

        }

        @Override
        public int hashCode() {
            return effectSet != null ? effectSet.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "{" +
                    "" + effectSet +
                    '}';
        }
    }

    /* Signatures */
    public static <Level> SigConstraintSet<Level> signatureConstraints(Stream<SigConstraint<Level>> sigSet) {
        return new SigConstraintSet<>(sigSet);
    }

    public SigConstraintSet<Level> toSignatureConstraintSet(ConstraintSet<Level> constraints, Map<TypeVar, Symbol.Param> params, TypeVar retVar) {
        Set<TypeVar> relevantVars = Stream.concat(Collections.singleton(retVar).stream(), params.keySet().stream()).collect(Collectors.toSet());

        CTypeSwitch<Level, Symbol<Level>> toSymbol = new CTypeSwitch<Level, Symbol<Level>>() {
            @Override
            public Symbol<Level> caseLiteral(Type<Level> t) {
                return Symbol.literal(t);
            }

            @Override
            public Symbol<Level> caseVariable(TypeVar v) {
                Symbol.Param p = Optional.ofNullable(params.get(v)).orElseThrow(() -> new NoSuchElementException(String.format("Type variable %s not found in parameter map: %s", v, params)));
                return p;
            }
        };

        return signatureConstraints(constraints.projectTo(params.keySet()).stream().map(c -> {
            Symbol<Level> lhs = c.getLhs().accept(toSymbol);
            Symbol<Level> rhs = c.getRhs().accept(toSymbol);
            switch (c.kind) {
                case LE:
                    return le(lhs, rhs);
                case COMP:
                    return comp(lhs, rhs);
                case DIMPL:
                    return dimpl(lhs, rhs);
                default:
                    throw new IllegalArgumentException(String.format("Unexpected case for ConstraintKind: %s", c));
            }
        }));
    }


    public static <Level> SigConstraint<Level> le(Symbol<Level> lhs, Symbol<Level> rhs) {
        return new SigConstraint<>(lhs, rhs, Constraints::le);
    }

    public static <Level> SigConstraint<Level> comp(Symbol<Level> lhs, Symbol<Level> rhs) {
        return new SigConstraint<>(lhs, rhs, Constraints::le);
    }

    public static <Level> SigConstraint<Level> dimpl(Symbol<Level> lhs, Symbol<Level> rhs) {
        return new SigConstraint<>(lhs, rhs, Constraints::le);
    }

    /* Signature constraints */
    public static class SigConstraintSet<Level> {
        private final Set<SigConstraint<Level>> sigSet;

        private SigConstraintSet(Stream<SigConstraint<Level>> sigSet) {
            this.sigSet = sigSet.collect(Collectors.toSet());
        }

        public Stream<Constraint<Level>> toTypingConstraints(Map<Symbol<Level>, TypeVar> mapping) {
            return this.sigSet.stream().map(c -> c.toTypingConstraint(mapping));
        }

        @Override
        public String toString() {
            return sigSet.toString();
        }

        public Stream<Symbol<Level>> symbols() {
            return this.stream().flatMap(SigConstraint::symbols);
        }

        public Stream<SigConstraint<Level>> stream() {
            return sigSet.stream();
        }

        public SigConstraintSet<Level> addAll(Stream<SigConstraint<Level>> sigs) {
            return signatureConstraints(Stream.concat(this.sigSet.stream(), sigs));
        }
    }

    public static class SigConstraint<Level> {

        private final Symbol<Level> lhs;
        private final Symbol<Level> rhs;
        private final BiFunction<CType<Level>, CType<Level>, Constraint<Level>>
                toConstraint;

        private SigConstraint(Symbol<Level> lhs,
                              Symbol<Level> rhs,
                              BiFunction<CType<Level>, CType<Level>, Constraint<Level>> toConstraint) {
            super();
            this.lhs = lhs;
            this.rhs = rhs;
            this.toConstraint = toConstraint;
        }

        public Constraint<Level> toTypingConstraint(Map<Symbol<Level>, TypeVar> tvarMapping) {
            return toConstraint.apply(lhs.toCType(tvarMapping),
                    rhs.toCType(tvarMapping));
        }

        public Stream<Symbol<Level>> symbols() {
            return Stream.of(lhs, rhs);
        }

        @Override
        public String toString() {
            return String.format("%s ? %s", lhs.toString(), rhs.toString());
        }
    }

}
