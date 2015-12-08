package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;

/**
 * Class for static utility methods for working with assignments.
 *
 * @author fennell
 */
public class Assignments {

    public static <Level> Assignment<Level> empty() {
        return new Assignment<Level>(new HashMap<>());
    }

    public static <Level> Stream<Assignment<Level>> enumerateAll(TypeDomain<Level> types,
                                              Set<TypeVar> variables) {
        return enumerateAll(types, new LinkedList<>(variables));
    }

    private static <Level> Stream<Assignment<Level>> enumerateAll(TypeDomain<Level> types,
                                                                 LinkedList<TypeVar> variables) {
        /**
         * enumerateAll v:vs = let rest = (enumerateAll vs) in concatMap (:rest) (map (v |->) types)
         *
         * enumerateAll [v] = map (v |->) types
         *
         * enumerateAll [] = []
         */
        if (variables.isEmpty()) {
            return Stream.of(Assignments.empty());
        } else {
            LinkedList<TypeVar> variableRest = new LinkedList<>(variables);
            TypeVar v = variableRest.remove();
            // TODO: can we be a bit more efficient by memoizing this stream?
            Supplier<Stream<Assignment<Level>>> rest = () -> enumerateAll(types, variableRest);
            return types.enumerate().flatMap(t -> rest.get().map(ass -> {
                Map<TypeVar, Type<Level>> m =
                        new HashMap<>(ass.get());
                m.put(v, t);
                return new Assignment<Level>(m);
            }));
        }
    }


    public static <Level> Builder<Level> builder(TypeVar v,
                                                 Type<Level> t) {
        return new Builder<Level>().add(v, t);
    }

    public static class Builder<Level> {
        private final Map<TypeVar, Type<Level>> ass =
                new HashMap<>();

        private Builder() {
        }

        ;

        public Assignment<Level> build() {
            return new Assignment<>(this.ass);
        }

        public Builder<Level> add(TypeVar v, Type<Level> t) {
            this.ass.put(v, t);
            return this;
        }

    }

}
