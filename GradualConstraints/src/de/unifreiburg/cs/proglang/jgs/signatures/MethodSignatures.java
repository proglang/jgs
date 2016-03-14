package de.unifreiburg.cs.proglang.jgs.signatures;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet.RefinementCheckResult;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol.Param;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.param;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.ret;
import static java.util.Arrays.asList;
import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Method signtures. Internal representation of method signatures of the form
 * <p>
 * M where <signature-constraints> and <effect>
 * <p>
 * Signature constraints are similar to regular constraints but instead of
 * relating type variables, they relate special symbols, which are:
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
     * Make a new JGS security signature for a method from a method's parameter
     * count, some constraints and effects. The constraints should only mention
     * parameters in the range of 0..paramCount.
     */
    public static <Level> Signature<Level> makeSignature(int paramCount,
                                                         Collection<SigConstraint<Level>> constraints,
                                                         Effects<Level> effects) {
        // check if all parameters are in range
        Predicate<Param<?>> paramInRange =
                p -> p.position < paramCount && 0 <= p.position;
        if (!constraints.stream().flatMap(SigConstraint::symbols).allMatch(
                s -> !(s instanceof Param)
                     || paramInRange.test(((Param<Level>) s)))
                ) {

            throw new IllegalArgumentException(
                    String.format("Illegal parameter for parameter count %d in constraint %s",
                                  paramCount, constraints));
        }
        Stream<SigConstraint<Level>> paramConstraints =
                IntStream.range(0, paramCount)
                         .mapToObj(i -> trivial(param(i)));
        Stream<SigConstraint<Level>> allConstraints =
                Stream.concat(
                        constraints.stream(),
                        Stream.concat(
                                Stream.<SigConstraint<Level>>of(trivial(ret())),
                                paramConstraints)
                );
        return new Signature<>(signatureConstraints(allConstraints), effects);
    }

    /**
     * Signatures: constraints + effects
     */
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
            SigConstraintSet<Level> newConstraints =
                    this.constraints.addAll(sigs);
            return new Signature<>(newConstraints, this.effects);
        }

        // TODO: use a dedicated result type (to be able to name the second component sensibly)
        public Pair<RefinementCheckResult<Level>, EffectRefinementResult<Level>> refines(ConstraintSetFactory<Level> csets, TypeDomain<Level> types, Signature<Level> other) {
            return Pair.of(this.constraints.refines(csets, types, other.constraints), this.effects.refines(types, other.effects));
        }
    }

    /* Effects */
    public static <Level> Effects<Level> emptyEffect() {
        return new Effects<>(new HashSet<>());
    }

    @SafeVarargs
    public static <Level> Effects<Level> makeEffects(Type<Level> type, Type<Level>... types) {
        return MethodSignatures.<Level>emptyEffect().add(type, types);
    }

    public static <Level> Effects<Level> makeEffects(Collection<Type<Level>> types) {
        return MethodSignatures.<Level>emptyEffect().add(types);
    }

    @SafeVarargs
    public static <Level> Effects<Level> union(Effects<Level>... effectSets) {
        HashSet<Type<Level>> result = new HashSet<>();
        for (Effects<Level> es : effectSets) {
            result.addAll(es.effectSet);
        }
        return new Effects<>(result);
    }

    public static class EffectRefinementResult<Level> {
        @Override
        public String toString() {
            return "missingEffects = " + missingEffects +
                   '}';
        }

        public final Effects<Level> missingEffects;

        public EffectRefinementResult(Effects<Level> missingEffects) {

            this.missingEffects = missingEffects;
        }

        public boolean isSuccess() {
            return missingEffects.isEmpty();
        }
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

        public boolean isEmpty() {
            return this.effectSet.isEmpty();
        }

        private <T> List<T> id(List<T> l) {
            return l;
        }

        public final Stream<Type<Level>> stream() {
            return this.effectSet.stream();
        }

        private boolean covers(TypeDomain<Level> types, Type<Level> t) {
            return this.effectSet.stream().anyMatch(cand -> types.le(cand, t));
        }

        public final EffectRefinementResult<Level> refines(TypeDomain<Level> types, Effects<Level> other) {
            HashSet<Type<Level>> notCovered = new HashSet<>();
            this.stream().filter(t -> !(other.covers(types, t))).forEach(t -> notCovered.add(t));
            return new EffectRefinementResult<>(new Effects<>(notCovered));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Effects<?> effects = (Effects<?>) o;

            return !(effectSet != null ? !effectSet.equals(effects.effectSet) :
                     effects.effectSet != null);

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
    private static <Level> SigConstraintSet<Level> signatureConstraints(Stream<SigConstraint<Level>> sigSet) {
        return new SigConstraintSet<>(sigSet);
    }

    public SigConstraintSet<Level> toSignatureConstraintSet(ConstraintSet<Level> constraints, Map<TypeVar, Param<Level>> params, TypeVar retVar) {
        Set<TypeVar> relevantVars =
                Stream.concat(Collections.singleton(retVar).stream(), params.keySet().stream()).collect(toSet());

        CTypeSwitch<Level, Symbol<Level>> toSymbol =
                new CTypeSwitch<Level, Symbol<Level>>() {
                    @Override
                    public Symbol<Level> caseLiteral(Type<Level> t) {
                        return Symbol.literal(t);
                    }

                    @Override
                    public Symbol<Level> caseVariable(TypeVar v) {
                        Param<Level> p =
                                Optional.ofNullable(params.get(v)).orElseThrow(() -> new NoSuchElementException(String.format("Type variable %s not found in parameter map: %s", v, params)));
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

    /**
     * @return A trivial constraint mentioning {@code s}.
     */
    public static <Level> SigConstraint<Level> trivial(Symbol<Level> s) {
        return le(s, s);
    }

    public static <Level> SigConstraint<Level> le(Symbol<Level> lhs, Symbol<Level> rhs) {
        return new SigConstraint<>(lhs, rhs, Constraint.Kind.LE);
    }

    public static <Level> SigConstraint<Level> comp(Symbol<Level> lhs, Symbol<Level> rhs) {
        return new SigConstraint<>(lhs, rhs, Constraint.Kind.COMP);
    }

    public static <Level> SigConstraint<Level> dimpl(Symbol<Level> lhs, Symbol<Level> rhs) {
        return new SigConstraint<>(lhs, rhs, Constraint.Kind.DIMPL);
    }

    public static <Level> SigConstraint<Level> makeSigConstraint(Symbol<Level> lhs,
                                                                 Symbol<Level> rhs,
                                                                 Constraint.Kind kind) {
        return new SigConstraint<Level>(lhs, rhs,
                                        kind);
    }

    /* Signature constraints */
    public static class SigConstraintSet<Level> {
        private final Set<SigConstraint<Level>> sigSet;

        private SigConstraintSet(Stream<SigConstraint<Level>> sigSet) {
            this.sigSet = sigSet.collect(toSet());
        }

        public Stream<Constraint<Level>> toTypingConstraints(Map<Symbol<Level>, CType<Level>> mapping) {
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

        /**
         * Check if this signature refines another one. This check is at the
         * heart of validating a sound class hierarchy.
         */
        public RefinementCheckResult<Level> refines(ConstraintSetFactory<Level> csets, TypeDomain<Level> types, SigConstraintSet<Level> other) {
            TypeVars tvars = new TypeVars();
            // the two signatures need to talk the same symbols.
            // We add trivial "identity mappings" of the form "s <= s" to a signature,
            // if it is missing a symbol mentioned in the other one.
            Set<Symbol<Level>> symbols =
                    Stream.concat(this.symbols(), other.symbols()).collect(toSet());
            ConstraintSet<Level> cs1 =
                    csets.fromCollection(this.toTypingConstraints(Symbol.identityMapping(tvars, symbols)).collect(toList()));
            ConstraintSet<Level> cs2 =
                    csets.fromCollection(other.toTypingConstraints(Symbol.identityMapping(tvars, symbols)).collect(toList()));
            return cs1.refines(cs2);
        }
    }

    public static class SigConstraint<Level> {

        private final Symbol<Level> lhs;
        private final Symbol<Level> rhs;
        private final Constraint.Kind kind;

        private SigConstraint(Symbol<Level> lhs,
                              Symbol<Level> rhs, Constraint.Kind kind) {
            super();
            this.lhs = lhs;
            this.rhs = rhs;
            this.kind = kind;
        }

        public Constraint<Level> toTypingConstraint(Map<Symbol<Level>, CType<Level>> tvarMapping) {
            return Constraints.make(kind, lhs.toCType(tvarMapping),
                                    rhs.toCType(tvarMapping));
        }

        public Stream<Symbol<Level>> symbols() {
            return Stream.of(lhs, rhs);
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", lhs.toString(), kind.toString(), rhs.toString());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SigConstraint<?> that = (SigConstraint<?>) o;

            if (lhs != null ? !lhs.equals(that.lhs) : that.lhs != null)
                return false;
            if (rhs != null ? !rhs.equals(that.rhs) : that.rhs != null)
                return false;
            return kind == that.kind;

        }

        @Override
        public int hashCode() {
            int result = lhs != null ? lhs.hashCode() : 0;
            result = 31 * result + (rhs != null ? rhs.hashCode() : 0);
            result = 31 * result + (kind != null ? kind.hashCode() : 0);
            return result;
        }

    }

}
