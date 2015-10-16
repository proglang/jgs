package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;

/**
 * A naive implementation of a constraint set. It is based on
 * java.util.Set<Constraint<Level>> and checks satisfiability by enumerating all
 * assignments.
 * 
 * @author fennell
 *
 */
class NaiveConstraints<Level> extends ConstraintSet<Level> {

    private final HashSet<Constraint<Level>> cs;

    NaiveConstraints(TypeDomain<Level> types,
                     Collection<Constraint<Level>> cs) {
        super(types);
        this.cs = new HashSet<>(cs);
    }

    @Override
    public boolean isSatisfiedFor(Assignment<Level> a) {
        return this.cs.stream().allMatch(c -> c.isSatisfied(a));
    }

    @Override
    public ConstraintSet<Level> add(Constraint<Level> c) {
        HashSet<Constraint<Level>> newCs = new HashSet<>(this.cs);
        newCs.add(c);
        return new NaiveConstraints<>(types, newCs);
    }

    @Override
    public ConstraintSet<Level> add(ConstraintSet<Level> other) {
        return new NaiveConstraints<>(types,
                                      other.stream()
                                           .collect(Collectors.toList()));
    }

    @Override
    public Stream<Constraint<Level>> stream() {
        return this.cs.stream();
    }

    @Override
    public boolean implies(ConstraintSet<Level> other) {
        return this.enumerateSatisfyingAssignments(other.variables().collect(Collectors.toSet()))
                   .allMatch(ass -> other.isSatisfiedFor(ass));

    }

    @Override
    public boolean isSat() {
        return this.satisfyingAssignment(Collections.emptyList()).isPresent();
    }

    /**
     * Enumerate all assignments that satisfy this constraint set.
     * 
     * @param requiredVariables
     *            The minimal set of variables that should be bound by a
     *            satisfying assignment. The actual assignments may bind more
     *            variables.
     */
    public Stream<Assignment<Level>> enumerateSatisfyingAssignments(Collection<TypeVar> requiredVariables) {
        Set<TypeVar> variables =
            cs.stream()
              .flatMap(c -> c.variables())
              .collect(Collectors.toSet());
        variables.addAll(requiredVariables);
        return Assignments.enumerateAll(types, new LinkedList<>(variables))
                          .filter(a -> this.isSatisfiedFor(a));
    }

    @Override
    public Optional<Assignment<Level>> satisfyingAssignment(Collection<TypeVar> requiredVariables) {
        return this.enumerateSatisfyingAssignments(requiredVariables).findAny();
    }

}
