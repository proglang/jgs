package de.unifreiburg.cs.proglang.jgs.signatures;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;

import java.util.*;

import static java.util.Arrays.asList;

 // TODO: move out of signatures and in its dedicated file in the typing package (as BodyTypingResult also uses it and the "concept" makes sense independently of MethodSignatures)
public final class Effects<Level> implements Iterable<Type<Level>> {
    private final HashSet<Type<Level>> effectSet;

    Effects(HashSet<Type<Level>> effects) {
        this.effectSet = effects;
    }

     /* Effects */
     public static <Level> Effects<Level> emptyEffect() {
         return new Effects<>(new HashSet<>());
     }

     @SafeVarargs
     public static <Level> Effects<Level> makeEffects(Type<Level> type, Type<Level>... types) {
         return Effects.<Level>emptyEffect().add(type, types);
     }

     public static <Level> Effects<Level> makeEffects(Collection<Type<Level>> types) {
         return Effects.<Level>emptyEffect().add(types);
     }

     @SafeVarargs
     public static <Level> Effects<Level> union(Effects<Level>... effectSets) {
         HashSet<Type<Level>> result = new HashSet<>();
         for (Effects<Level> es : effectSets) {
             result.addAll(es.effectSet);
         }
         return new Effects<>(result);
     }

     public final Effects<Level> add(Type<Level> type, Type<Level>... types) {
        return this.add(type).add(asList(types));
    }

    public final Effects<Level> add(Type<Level> type) {
        return this.add(Collections.singletonList(type));
    }

    public final Effects<Level> add(Effects<Level> other) {
        return this.add(other.effectSet);
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

    public final Iterator<Type<Level>> stream() {
        return this.effectSet.iterator();
    }

    private boolean covers(TypeDomain<Level> types, Type<Level> t) {
        for (Type<Level> cand : effectSet) {
            if (types.le(cand, t)) {
                return true;
            }
        }
        return false;
    }

    public final EffectRefinementResult<Level> refines(TypeDomain<Level> types, Effects<Level> other) {
        HashSet<Type<Level>> notCovered = new HashSet<>();
        for (Type<Level> t : this.effectSet) {
            if (!(other.covers(types, t))) {
                notCovered.add(t);
            }
        }
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
    public Iterator<Type<Level>> iterator() {
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
