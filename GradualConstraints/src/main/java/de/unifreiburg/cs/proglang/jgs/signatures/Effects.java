package de.unifreiburg.cs.proglang.jgs.signatures;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import static de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.*;

import java.util.*;

import static java.util.Arrays.asList;

 // TODO: move out of signatures and in its dedicated file in the typing package (as BodyTypingResult also uses it and the "concept" makes sense independently of MethodSignatures)
public final class Effects<Level> implements Iterable<TypeView<Level>> {
    private final HashSet<TypeView<Level>> effectSet;

    Effects(HashSet<TypeView<Level>> effects) {
        this.effectSet = effects;
    }

     /* Effects */
     public static <Level> Effects<Level> emptyEffect() {
         return new Effects<>(new HashSet<TypeView<Level>>());
     }

     @SafeVarargs
     public static <Level> Effects<Level> makeEffects(TypeView<Level> type, TypeView<Level>... types) {
         return Effects.<Level>emptyEffect().add(type, types);
     }

     public static <Level> Effects<Level> makeEffects(Collection<TypeView<Level>> types) {
         return Effects.<Level>emptyEffect().add(types);
     }

     @SafeVarargs
     public static <Level> Effects<Level> union(Effects<Level>... effectSets) {
         HashSet<TypeView<Level>> result = new HashSet<>();
         for (Effects<Level> es : effectSets) {
             result.addAll(es.effectSet);
         }
         return new Effects<>(result);
     }

     public final Effects<Level> add(TypeView<Level> type, TypeView<Level>... types) {
        return this.add(type).add(asList(types));
    }

    public final Effects<Level> add(TypeView<Level> type) {
        return this.add(Collections.singletonList(type));
    }

    public final Effects<Level> add(Effects<Level> other) {
        return this.add(other.effectSet);
    }

    public final Effects<Level> add(Collection<TypeView<Level>> types) {
        HashSet<TypeView<Level>> result = new HashSet<>(this.effectSet);
        result.addAll(types);
        return new Effects<>(result);
    }

    public boolean isEmpty() {
        return this.effectSet.isEmpty();
    }

    private <T> List<T> id(List<T> l) {
        return l;
    }

    public final Iterator<TypeView<Level>> stream() {
        return this.effectSet.iterator();
    }

    private boolean covers(TypeDomain<Level> types, TypeView<Level> t) {
        for (TypeView<Level> cand : effectSet) {
            if (types.le(cand, t)) {
                return true;
            }
        }
        return false;
    }

    public final EffectRefinementResult<Level> refines(TypeDomain<Level> types, Effects<Level> other) {
        HashSet<TypeView<Level>> notCovered = new HashSet<>();
        for (TypeView<Level> t : this.effectSet) {
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
    public Iterator<TypeView<Level>> iterator() {
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
