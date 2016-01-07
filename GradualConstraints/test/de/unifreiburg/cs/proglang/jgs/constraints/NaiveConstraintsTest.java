package de.unifreiburg.cs.proglang.jgs.constraints;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.types;
import static java.util.Arrays.equals;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import soot.jimple.spark.ondemand.EverythingHeuristic;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;
import static java.util.Collections.*;

public class NaiveConstraintsTest {

    private TypeVars tvars;
    private SomeConstraintSets cs;

    @Before public void setUp() {

        tvars = new TypeVars();
        cs = new SomeConstraintSets(tvars);
    }

    @Test public void testSatisfiability() {
        assertThat(makeNaive(Collections.emptyList()), is(sat()));
        assertThat(cs.x0_le_x1_le_x2_le_x3_le_x1, is(sat()));
        assertThat(cs.x1_le_H_le_x2__x1_le_x2, is(sat()));
        assertThat(cs.pub_le_x_le_dyn, is(sat()));
        assertThat("SAT(x < HIGH, x < ?)",
                makeNaive(Arrays.asList(leC(cs.x1, literal(DYN)),
                        leC(cs.x1,
                                literal(THIGH)))), is(sat()));

        assertThat("~SAT(HIGH < LOW)",
                makeNaive(Collections.singletonList(leC(literal(THIGH),
                        literal(TLOW)))), not(is(sat())));
        assertThat("~SAT(HIGH < x, x < y , y < LOW)",
                makeNaive(asList(leC(literal(THIGH), cs.x1),
                        leC(cs.x1, cs.x2),
                        leC(cs.x2, literal(TLOW)))), not(is(sat())));
        assertThat("SAT(LOW < x, x < ?)",
                makeNaive(asList(leC(cs.x1, literal(DYN)),
                        leC(literal(TLOW), cs.x1))), not(is(sat())));
    }

    @Test public void testSatAssignments() {
        Optional<Assignment<Level>> result, expected;
        result = cstrs.satisfyingAssignment(makeNaive(Arrays.asList(leC(cs.x1, literal(THIGH)),
                                         leC(literal(THIGH), cs.x2),
                                         leC(cs.x2,
                                             cs.x1))),Collections.emptySet());
        expected = Optional.of(Assignments.builder(cs.v1, THIGH)
                                          .add(cs.v2, THIGH)
                                          .build());
        assertEquals("x1 = x2 = HIGH", expected, result);

        result = cstrs.satisfyingAssignment(makeNaive(Arrays.asList(leC(cs.x1, literal(DYN)),
                                         leC(cs.x1,
                                             literal(THIGH)))), Collections.emptySet());
        expected = Optional.of(Assignments.builder(cs.v1, PUB).build());
        assertEquals("x = pub", expected, result);
    }

    @Test public void testImplications() {
        /*
         * - more constraints imply less constraints 
         */
        ConstraintSet<Level> more = makeNaive(asList(leC(cs.x1, literal(THIGH)),
                                                     leC(literal(THIGH), cs.x2),
                                                     leC(cs.x2, cs.x1)));
        ConstraintSet<Level> less = makeNaive(asList(leC(cs.x1, literal(THIGH)),
                                                     leC(literal(THIGH),
                                                         cs.x2)));
        assertThat("more => less", more, implies(less));
        assertThat("less /=> (significant) more", less, not(implies(more)));

        assertThat("more => more+trivial",
                   more, implies(more.add(leC(cs.x1, cs.x1))
                                    .add(leC(literal(PUB), cs.x2))));

        /*
         * - < LOW constraints imply < High constraints
         */
        ConstraintSet<Level> lowerLess =
                makeNaive(asList(leC(cs.x1, literal(TLOW)),
                                 leC(literal(THIGH), cs.x2),
                                 leC(cs.x1, cs.x2)));
        assertThat("lowLess => less", lowerLess, implies(less));
        assertThat("loweLess /=> less", less, not(implies(lowerLess)));

        /*
         * - unsat constraints imply arbitrary constraints 
         */
        ConstraintSet<Level> unsat =
                makeNaive(Arrays.asList(leC(literal(THIGH), cs.x1),
                                        leC(cs.x1, cs.x2),
                                        leC(cs.x2, literal(TLOW))));
        assertThat("unsat => more", unsat,implies(more));
        assertThat("unsat => less", unsat,implies(less));
        assertThat("unsat => lowerLess", unsat,implies(lowerLess));
        assertThat("unsat => unsat", unsat,implies(unsat));
    }

    @Test
    public void testClosureCandidates() {
        Constraint<Level> c = leC(cs.x1, cs.x2);
        Set<Constraint<Level>> old = makeNaive(asList(leC(cs.x1, cs.x2), leC(cs.x2, cs.x3), compC(cs.x3, cs.x1), dimplC(cs.x1, cs.x2))).stream().collect(toSet());
        assertThat(NaiveConstraints.matchingRhs(c, old).collect(toSet()), is(Stream.of(cs.x3).collect(toSet())));

    }

    @Test(timeout = 1000)
    public void testLeClosure() {
        Set<Constraint<Level>> tmp = makeNaive(asList(leC(cs.x1, cs.x2), leC(cs.x2, cs.x3))).stream().collect(toSet());
        ConstraintSet<Level> closed = makeNaive(NaiveConstraints.close(tmp));
        ConstraintSet<Level> expected = makeNaive(asList(leC(cs.x1, cs.x2), leC(cs.x2, cs.x3), leC(cs.x1, cs.x3)));
        assertThat(closed, is(equivalent(expected)));
    }


    @Test
    public void testCompClosure() {
        Set<Constraint<Level>> tmp = makeNaive(asList(compC(cs.x1, cs.x2), compC(cs.x2, cs.x3))).stream().collect(toSet());
        ConstraintSet<Level> closed = makeNaive(NaiveConstraints.close(tmp));
        // closing compatibility constraints does not have an effect
        ConstraintSet<Level> expected = makeNaive(asList(compC(cs.x1, cs.x2), compC(cs.x2, cs.x3)));

        assertThat(closed, is(equivalent(expected)));

        // closing compatibility constraints does not have an effect. This is the same test as above
        tmp = makeNaive(asList(compC(cs.x2, cs.x1) , compC(cs.x2, cs.x3))).stream().collect(toSet());
        closed = makeNaive(NaiveConstraints.close(tmp));
        expected = makeNaive(asList(compC(cs.x1, cs.x2), compC(cs.x2, cs.x3)));

        assertThat(closed, is(equivalent(expected)));

        tmp = makeNaive(asList(leC(cs.x1, cs.x2), leC(cs.x3, cs.x2))).stream().collect(Collectors.toSet());
        closed = makeNaive(NaiveConstraints.close(tmp));
        expected = makeNaive(asList(leC(cs.x1, cs.x2), leC(cs.x3, cs.x2), compC(cs.x1, cs.x3)));

        assertThat(closed, is(equivalent(expected)));
    }

    void assertProjection(Collection<Constraint<Level>> cs, Collection<TypeVars.TypeVar> vars, Collection<Constraint<Level>> expectedSet) {
        ConstraintSet<Level> projected = makeNaive(cs).projectTo(new HashSet<>(vars));
        ConstraintSet<Level> expected = makeNaive(expectedSet);
        assertThat(String.format("%s projected to %s", cs, vars), projected
                , is(equivalent(expected)));
    }

    @Test
    public void testProjection1() {
        Set<Constraint<Level>> tmp = Stream.of(leC(cs.x1, cs.x2), leC(cs.x2, cs.x3)).collect(toSet());
        assertProjection(tmp, asList(cs.v1, cs.v3), asList(leC(cs.x1, cs.x3)));

        tmp = Stream.of(leC(cs.x1, cs.x2), leC(cs.x2, cs.x3)).collect(toSet());
        assertProjection(tmp, asList(cs.v1), Collections.<Constraint<Level>>emptySet());

        tmp = Stream.of(leC(cs.x1, cs.x2), leC(cs.x2, cs.x3)).collect(toSet());
        assertProjection(tmp, asList(cs.v1, cs.v0), Collections.<Constraint<Level>>emptySet());

        tmp = Stream.of(leC(cs.x1, cs.x2), leC(cs.x0, cs.x3), leC(cs.x1, cs.x3)).collect(toSet());
        assertProjection(tmp, asList(cs.v1, cs.v0), Stream.of(compC(cs.x0, cs.x1)).collect(toSet()));

        assertProjection(tmp, asList(cs.v1, cs.v0), Stream.of(compC(cs.x1, cs.x0)).collect(toSet()));
    }

    @Test
    public void testMinimize() {
        Set<Constraint<Level>> tmp = Stream.of(leC(cs.x1, cs.x2), leC(cs.x2, cs.x3), compC(cs.x3, cs.x2), dimplC(cs.x1, cs.x2)).collect(toSet());
        Set<Constraint<Level>> expected = Stream.of(leC(cs.x1, cs.x2), leC(cs.x2, cs.x3)).collect(toSet());
        assertThat(makeNaive(NaiveConstraints.minimize(tmp)), is(equivalent(makeNaive(expected))));
    }

    @Test
    public void testEquivalences() {
        ConstraintSet<Level> cs11 = makeNaive(asList(leC(cs.x0, cs.x1), leC(cs.x2, cs.x1), compC(cs.x2, cs.x0)));
        ConstraintSet<Level> cs22 = makeNaive(asList(leC(cs.x0, cs.x1), leC(cs.x2, cs.x1)));

        assertThat(cs11, is(equivalent(cs22)));

        ConstraintSet<Level> cs1 = makeNaive(asList(leC(cs.x0, cs.x1), compC(cs.x0, cs.x2)));
        ConstraintSet<Level> cs2 = makeNaive(asList(leC(cs.x0, cs.x1), compC(cs.x1, cs.x2)));

        assertThat(cs1, is(not(equivalent(cs2))));
//        Supplier<Stream<TypeDomain.Type<Level>>> allTypes = () -> Stream.of(TLOW, THIGH, DYN, PUB);
        // TODO: factor this out to a reusable assertion
//        allTypes.get().forEach(t1 -> allTypes.get().forEach( t2 -> allTypes.get().forEach(t3 -> {
//            Assignment<Level> ass = Assignments.<Level>builder(cs.v0, t1).add(cs.v1, t2).add(cs.v2, t3).build();
//            boolean cs1Sat = cs1.isSatisfiedFor(types, ass);
//            boolean cs2Sat = cs2.isSatisfiedFor(types, ass);
//            assertThat(String.format("cs1 (%s: %s) does not match cs2 (%s: %s): %s", cs1, cs1Sat, cs2, cs2Sat, ass), cs1Sat , is(cs2Sat));
//        })));
    }

    @Test
    public void testSubsumption() {
        assertThat(makeNaive(singletonList(leC(CHIGH, CTypes.variable(tvars.ret())))), subsumes(makeNaive(singletonList(leC(CLOW, CTypes.variable(tvars.ret()))))));
        assertThat(makeNaive(singletonList(leC(CLOW, CTypes.variable(tvars.ret())))), subsumes(makeNaive(asList(leC(CLOW, cs.x1), leC(cs.x1, tvars.ret()), leC(CHIGH, cs.x2)))));
        assertThat(makeNaive(singletonList(leC(CLOW, CTypes.variable(tvars.ret())))), minimallySubsumes(makeNaive(singletonList(leC(CLOW, CTypes.variable(tvars.ret()))))));
        assertThat(makeNaive(singletonList(leC(CLOW, CTypes.variable(tvars.ret())))), minimallySubsumes(makeNaive(asList(leC(CLOW, cs.x1), leC(cs.x1, tvars.ret()), leC(CHIGH, cs.x2)))));
        assertThat(makeNaive(singletonList(leC(CHIGH, CTypes.variable(tvars.ret())))), not(minimallySubsumes(makeNaive(singletonList(leC(CLOW, CTypes.variable(tvars.ret())))))));
    }
}
