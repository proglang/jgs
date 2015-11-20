package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.*;

/**
 * A naive implementation of a constraint set. It is based on
 * java.util.Set<Constraint<Level>> and checks satisfiability by enumerating all
 * assignments.
 *
 * @author fennell
 *
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
        HashSet<Constraint<Level>> result = new HashSet<>(cs);
        HashSet<Constraint<Level>> old = new HashSet<>();
        do {
            old.addAll(result);
            old.stream().forEach(x_to_y -> {
                Stream<CType<Level>> cands = old.stream().filter(c -> c.getLhs().equals(x_to_y.getRhs())).map(c -> c.getRhs());
                cands.forEach(rhs -> result.add(Constraints.le(x_to_y.getLhs(), rhs)));
            });
        } while (!result.equals(old));
        return result;
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
     * @param requiredVariables
     *            The minimal set of variables that should be bound by a
     *            satisfying assignment. The actual assignments may bind more
     *            variables.
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
