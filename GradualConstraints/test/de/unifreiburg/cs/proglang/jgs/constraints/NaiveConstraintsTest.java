package de.unifreiburg.cs.proglang.jgs.constraints;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeDomain.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;

import static de.unifreiburg.cs.proglang.jgs.constraints.TestDomain.*;
import static java.util.Arrays.asList;

public class NaiveConstraintsTest {

    private TypeVars tvars;
    private TypeVar v0, v1, v2, v3;

    private CTypeDomain<Level> ctypes;
    private CType<Level> x0, x1, x2, x3;

    @Before
    public void setUp() {
        ctypes = new CTypeDomain<>();

        tvars = new TypeVars("x");
        v0 = tvars.fresh();
        v1 = tvars.fresh();
        v2 = tvars.fresh();
        v3 = tvars.fresh();

        x0 = ctypes.variable(v0);
        x1 = ctypes.variable(v1);
        x2 = ctypes.variable(v2);
        x3 = ctypes.variable(v3);
    }

    @Test
    public void testSatisfiability() {
        assertTrue("SAT(empty)", makeNaive(Arrays.asList()).isSat());
        assertTrue("SAT(<free variables>)",
                   makeNaive(Arrays.asList(leC(x0, x1),
                                           leC(x1, x2),
                                           leC(x3, x1))).isSat());
        assertTrue("SAT(x1 < HIGH, HIGH < x2, x1 < x2)",
                   makeNaive(Arrays.asList(leC(x1, ctypes.literal(THIGH)),
                                           leC(ctypes.literal(THIGH), x2),
                                           leC(x1, x2))).isSat());
        assertTrue("SAT(PUB < x, x < ?",
                   makeNaive(Arrays.asList(leC(x1, ctypes.literal(DYN)),
                                           leC(ctypes.literal(PUB),
                                               x1))).isSat());
        assertTrue("SAT(x < HIGH, x < ?)",
                   makeNaive(Arrays.asList(leC(x1, ctypes.literal(DYN)),
                                           leC(x1,
                                               ctypes.literal(THIGH)))).isSat());

        assertFalse("~SAT(HIGH < LOW)",
                    makeNaive(Arrays.asList(leC(ctypes.literal(THIGH),
                                                ctypes.literal(TLOW)))).isSat());
        assertFalse("~SAT(HIGH < x, x < y , y < LOW)",
                    makeNaive(Arrays.asList(leC(ctypes.literal(THIGH), x1),
                                            leC(x1, x2),
                                            leC(x2,
                                                ctypes.literal(TLOW)))).isSat());
        assertFalse("SAT(LOW < x, x < ?)",
                    makeNaive(Arrays.asList(leC(x1, ctypes.literal(DYN)),
                                            leC(ctypes.literal(TLOW),
                                                x1))).isSat());
    }

    @Test
    public void testSatAssignments() {
        Optional<Assignment<Level>> result, expected;
        result = makeNaive(Arrays.asList(leC(x1, ctypes.literal(THIGH)),
                                         leC(ctypes.literal(THIGH), x2),
                                         leC(x2, x1))).satisfyingAssignment();
        expected =
            Optional.of(Assignments.builder(v1, THIGH).add(v2, THIGH).build());
        assertEquals("x1 = x2 = HIGH", expected, result);

        result =
            makeNaive(Arrays.asList(leC(x1, ctypes.literal(DYN)),
                                    leC(x1,
                                        ctypes.literal(THIGH)))).satisfyingAssignment();
        expected = Optional.of(Assignments.builder(v1, PUB).build());
        assertEquals("x = pub", expected, result);
    }

    @Test
    public void testImplications() {
        /*
         * - more constraints imply less constraints 
         */
        ConstraintSet<Level> more =
            makeNaive(asList(leC(x1, ctypes.literal(THIGH)),
                             leC(ctypes.literal(THIGH), x2),
                             leC(x2, x1)));
        ConstraintSet<Level> less =
            makeNaive(asList(leC(x1, ctypes.literal(THIGH)),
                             leC(ctypes.literal(THIGH), x2)));
        assertTrue("more => less", more.implies(less));
        assertFalse("less /=> (significant) more", less.implies(more));

        assertTrue("more => more+trivial",
                   more.implies(more.add(leC(x1, x1))
                                    .add(leC(ctypes.literal(PUB), x2))));

        /*
         * - < LOW constraints imply < High constraints
         */
        ConstraintSet<Level> lowerLess =
            makeNaive(asList(leC(x1, ctypes.literal(TLOW)),
                             leC(ctypes.literal(THIGH), x2),
                             leC(x1, x2)));
        assertTrue("lowLess => less", lowerLess.implies(less));
        assertFalse("loweLess => less", less.implies(lowerLess));

        /*
         * - unsat constraints imply arbitrary constraints 
         */
        ConstraintSet<Level> unsat = makeNaive(Arrays.asList(leC(ctypes.literal(THIGH), x1),
                                            leC(x1, x2),
                                            leC(x2,
                                                ctypes.literal(TLOW))));
        assertTrue("unsat => more", unsat.implies(more));
        assertTrue("unsat => less", unsat.implies(less));
        assertTrue("unsat => lowerLess", unsat.implies(lowerLess));
        assertTrue("unsat => unsat", unsat.implies(unsat));
    }

}
