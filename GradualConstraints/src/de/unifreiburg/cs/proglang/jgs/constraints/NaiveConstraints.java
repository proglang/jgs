package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.*;
import static java.util.stream.Collectors.toSet;

/**
 * A naive implementation of a constraint set. It is based on
 * java.util.Set<Constraint<Level>> and checks satisfiability by enumerating all
 * assignments.
 *
 * @author fennell
 */
public class NaiveConstraints<Level> extends ConstraintSet<Level> {

    private final HashSet<Constraint<Level>> cs;

    public NaiveConstraints(TypeDomain<Level> types,
                            Collection<Constraint<Level>> cs) {
        super(types);
        this.cs = new HashSet<>(cs);
    }

    @Override
    public boolean isSatisfiedFor(TypeDomain<Level> types, Assignment<Level> a) {
        return this.cs.stream().allMatch(c -> c.isSatisfied(types, a));
    }

    public ConstraintSet<Level> add(Collection<Constraint<Level>> other) {
        HashSet<Constraint<Level>> newCs = new HashSet<>(this.cs);
        newCs.addAll(other);
        return new NaiveConstraints<>(types, newCs);
    }

    @Override
    public ConstraintSet<Level> add(Constraint<Level> c) {
        return this.add(Collections.singleton(c));
    }

    @Override
    public ConstraintSet<Level> add(ConstraintSet<Level> other) {
        return this.add(other.stream().collect(Collectors.toList()));
    }

    @Override
    public Stream<Constraint<Level>> stream() {
        return this.cs.stream();
    }

    static <Level> HashSet<Constraint<Level>> close(Set<Constraint<Level>> cs) {
        return closeTransitively(withImplications(cs.stream()).collect(toSet()));
    }

    /// close constraints cs transitively. The set is effectively partitioned by constraint-kind and then closed. Does not add reflexive constraints.
    static <Level> HashSet<Constraint<Level>> closeTransitively(Set<Constraint<Level>> cs) {
        HashSet<Constraint<Level>> result = new HashSet<>(cs);
        HashSet<Constraint<Level>> old = new HashSet<>();
        do {
            old.addAll(result);
            old.stream().forEach(x_to_y -> {
                Stream<CType<Level>> cands;
                        cands = matchingRhs(x_to_y, old);
                        cands.forEach(rhs -> {
                            CType lhs = x_to_y.getLhs();
                            if (!lhs.equals(rhs)) {
                                result.add(Constraints.make(x_to_y.kind, lhs, rhs));
                            }
                        });
                });
        } while (!result.equals(old));
        return result;
    }


    public static <Level> ConstraintSet<Level> minimize(ConstraintSet<Level> cs) {
        return new NaiveConstraints<>(cs.types, minimize(cs.stream().collect(toSet())));
    }
    /**
     * Try minimize to <code>cs</code>. Currently checks if the le constraints cover any other kinds of constraints.
     */
    static <Level> Set<Constraint<Level>> minimize(Set<Constraint<Level>> cs) {
        Predicate<Constraint<Level>> isLeConstraint = c -> c.kind.equals(Constraint.Kind.LE);
        BiFunction<Constraint<Level>, Constraint<Level>, Boolean> covers = (cLe, c) -> {
            Constraint<Level> scLe = Constraints.symmetricOf(cLe);
            return Constraints.equalComponents(cLe, c) || Constraints.equalComponents(scLe, c);
        };
        Supplier<Stream<Constraint<Level>>> leCs = () -> cs.stream().filter(isLeConstraint);
        Stream<Constraint<Level>> notLeCs = cs.stream().filter(isLeConstraint.negate());
        HashSet<Constraint<Level>> seen = new HashSet<>();
        Stream<Constraint<Level>> minimizedNonLEs = notLeCs.flatMap(cNle -> {
            if (leCs.get().anyMatch(c -> covers.apply(c, cNle))) {
                return Stream.<Constraint<Level>>empty();
            } else {
                return Stream.<Constraint<Level>>of(cNle);
            }
        });
        return Stream.concat(leCs.<Constraint<Level>>get(), minimizedNonLEs).collect(toSet());
    }


    static <Level> Stream<Constraint<Level>> withImplications(Stream<Constraint<Level>> cs) {
        return cs.flatMap(Constraints::implicationsOf);
    }

    // TODO: what happens when { x1 <= x2, x2 ~ x3, x3 <= x4} ... nothing in particular.. what is the lemma? .. I need a test for this, e.g. is everything still equivalent when removing x3
    static <Level> Stream<CType<Level>> matchingRhs(Constraint<Level> left, Set<Constraint<Level>> old) {
        return old.stream().filter(c -> {
            return c.getLhs().equals(left.getRhs()) && c.kind == left.kind;
        }).map(c -> c.getRhs());
    }

    public static <Level> Stream<Constraint<Level>> projectTo(Set<Constraint<Level>> cs, Collection<TypeVar> typeVarCol) {
        Set<Constraint<Level>> closure = close(cs);
        Set<CType<Level>> typeVars = typeVarCol.stream().map(CTypes::<Level>variable).collect(Collectors.<CType<Level>>toSet());
        return closure.stream().filter(c -> {
            return typeVars.contains(c.getLhs()) && typeVars.contains(c.getRhs());
        });
    }

    @Override
    public ConstraintSet<Level> projectTo(Set<TypeVar> typeVars) {
        Set<Constraint<Level>> constraintSet = this.cs;
        return new NaiveConstraints<>(this.types, projectTo(constraintSet, typeVars).collect(toSet()));
    }

    @Override
    public boolean implies(TypeDomain<Level> types, ConstraintSet<Level> other) {
        return this.enumerateSatisfyingAssignments(types, other.variables()
                .collect(toSet()))
                .allMatch(ass -> other.isSatisfiedFor(types, ass));

    }

    @Override
    public boolean isSat(TypeDomain<Level> types) {
        return this.satisfyingAssignment(types, Collections.emptyList()).isPresent();
    }

    /**
     * Enumerate all assignments that satisfy this constraint set.
     *
     * @param requiredVariables The minimal set of variables that should be bound by a
     *                          satisfying assignment. The actual assignments may bind more
     *                          variables.
     */
    public Stream<Assignment<Level>> enumerateSatisfyingAssignments(TypeDomain<Level> types, Collection<TypeVar> requiredVariables) {
        Set<TypeVar> variables =
                cs.stream().flatMap(c -> c.variables()).collect(toSet());
        variables.addAll(requiredVariables);
        return Assignments.enumerateAll(types, new LinkedList<>(variables))
                .filter(a -> this.isSatisfiedFor(types, a));
    }

    @Override
    public Optional<Assignment<Level>> satisfyingAssignment(TypeDomain<Level> types, Collection<TypeVar> requiredVariables) {
        return this.enumerateSatisfyingAssignments(types, requiredVariables).findAny();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        Predicate<Constraint<Level>> isLe = c -> c.kind.equals(Constraint.Kind.LE);
        Consumer<Constraint<Level>> append = c -> {result.append(c.toString()); result.append(", "); };
        cs.stream().filter(isLe).forEach(append);
        cs.stream().filter(isLe.negate()).forEach(append);
        result.append("}");
        return result.toString();
    }

}
