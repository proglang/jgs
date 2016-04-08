package de.unifreiburg.cs.proglang.jgs.signatures;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

 // TODO: move out of signatures and in its dedicated file in the typing package (as BodyTypingResult also uses it and the "concept" makes sense independently of MethodSignatures)
public final class Effects<Level> implements Iterable<TypeDomain.Type<Level>> {
    private final HashSet<TypeDomain.Type<Level>> effectSet;

    Effects(HashSet<TypeDomain.Type<Level>> effects) {
        this.effectSet = effects;
    }

     /* Effects */
     public static <Level> Effects<Level> emptyEffect() {
         return new Effects<>(new HashSet<>());
     }

     @SafeVarargs
     public static <Level> Effects<Level> makeEffects(TypeDomain.Type<Level> type, TypeDomain.Type<Level>... types) {
         return Effects.<Level>emptyEffect().add(type, types);
     }

     public static <Level> Effects<Level> makeEffects(Collection<TypeDomain.Type<Level>> types) {
         return Effects.<Level>emptyEffect().add(types);
     }

     @SafeVarargs
     public static <Level> Effects<Level> union(Effects<Level>... effectSets) {
         HashSet<TypeDomain.Type<Level>> result = new HashSet<>();
         for (Effects<Level> es : effectSets) {
             result.addAll(es.effectSet);
         }
         return new Effects<>(result);
     }

     public final Effects<Level> add(TypeDomain.Type<Level> type, TypeDomain.Type<Level>... types) {
        return this.add(Stream.concat(Stream.of(type), asList(types).stream()).collect(toList()));
    }

    public final Effects<Level> add(TypeDomain.Type<Level> type) {
        return this.add((Stream.of(type)).collect(toList()));
    }

    public final Effects<Level> add(Effects<Level> other) {
        return this.add(other.stream().collect(toList()));
    }

    public final Effects<Level> add(Collection<TypeDomain.Type<Level>> types) {
        HashSet<TypeDomain.Type<Level>> result = new HashSet<>(this.effectSet);
        result.addAll(types);
        return new Effects<>(result);
    }

    public boolean isEmpty() {
        return this.effectSet.isEmpty();
    }

    private <T> List<T> id(List<T> l) {
        return l;
    }

    public final Stream<TypeDomain.Type<Level>> stream() {
        return this.effectSet.stream();
    }

    private boolean covers(TypeDomain<Level> types, TypeDomain.Type<Level> t) {
        return this.effectSet.stream().anyMatch(cand -> types.le(cand, t));
    }

    public final EffectRefinementResult<Level> refines(TypeDomain<Level> types, Effects<Level> other) {
        HashSet<TypeDomain.Type<Level>> notCovered = new HashSet<>();
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

    @Override
    public Iterator<TypeDomain.Type<Level>> iterator() {
        return this.effectSet.iterator();
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
 }
