package de.unifreiburg.cs.proglang.jgs.typing;

import java.util.HashSet;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.*;
import static java.util.Arrays.asList;

/**
 * Method signtures. Internal representation of method signatures of the form
 * 
 * M where <signature-constraints> and <effect>
 * 
 * Signature constraints are similar to regular constraints but instead of
 * relating type variables, they relate special symbols, which are:
 * 
 * - Parameter names - "@return" - Security Levels
 * 
 * Effects are just sets of security Levels
 * 
 * @author fennell
 *
 */
public class MethodSig<Level> {
    
    /* Effects */

    public static <Level> Effects<Level> emptyEffect() {
        return new Effects<>(new HashSet<>());
    }

    @SafeVarargs
    public static <Level> Effects<Level> union(Effects<Level>... effectSets) {
        HashSet<Type<Level>> result = new HashSet<>();
        for (Effects<Level> es : effectSets) {
            result.addAll(es.effectSet);
        }
        return new Effects<>(result);
    }

    public final static class Effects<Level> {
        private final HashSet<Type<Level>> effectSet;

        private Effects(HashSet<Type<Level>> effects) {
            this.effectSet = effects;
        }

        @SafeVarargs
        public final Effects<Level> add(Type<Level>... types) {
            HashSet<Type<Level>> result = new HashSet<>(this.effectSet);
            result.addAll(asList(types));
            return new Effects<>(result);
        }
    }
    
    
    /*  */

}
