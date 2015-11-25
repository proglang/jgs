package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.*;
import static java.util.Arrays.asList;

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
        return closeTransitively(withImplications(cs.stream()).collect(Collectors.toSet()));
    }

    /// close constraints cs transitively. The set is effectively partitioned by constraint-kind and then closed.
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
                            result.add(Constraints.make(x_to_y.kind, lhs, rhs));
                        });
                });
        } while (!result.equals(old));
        return result;
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

    @Override
    public Set<Constraint<Level>> projectTo(Set<TypeVar> typeVars) {
        Set<Constraint<Level>> closure = close(this.cs);
        return closure.stream().filter(c -> {
            return typeVars.contains(c.getLhs()) || typeVars.contains(c.getRhs());
        }).collect(Collectors.toSet());
    }

    @Override
    public boolean implies(TypeDomain<Level> types, ConstraintSet<Level> other) {
        return this.enumerateSatisfyingAssignments(types, other.variables()
                .collect(Collectors.toSet()))
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
                cs.stream().flatMap(c -> c.variables()).collect(Collectors.toSet());
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
        return "{" + cs + "}";
    }

}
