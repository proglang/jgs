package de.unifreiburg.cs.proglang.jgs.constraints;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;

import static de.unifreiburg.cs.proglang.jgs.constraints.TestDomain.*;

public class NaiveConstraintsTest {

    private TypeVars tvars;
    private TypeVar v0, v1, v2, v3;

    private CTypeDomain<Level> ctypes;
    private CTypeDomain<Level>.CType x0, x1, x2, x3;

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
        assertFalse("~SAT(HIGH < LOW)",
                    makeNaive(Arrays.asList(leC(ctypes.literal(THIGH),
                                                ctypes.literal(TLOW)))).isSat());
        assertFalse("~SAT(HIGH < x, x < y , y < LOW)",
                    makeNaive(Arrays.asList(leC(ctypes.literal(THIGH), x1),
                                            leC(x1, x2),
                                            leC(x2,
                                                ctypes.literal(TLOW)))).isSat());
    }

    @Test
    public void testSatAssignments() {
        // TODO test something:
        /*
         * - constraints that pull all variables to TLOW - ... transitively -
         * unsatisfiable constraints have no solutions
         */
    }

    @Test
    public void testImplications() {
        // TODO test something:
        /*
         * - more constraints imply less constraints - unsat constraints imply
         * arbitrary constraints - < LOW constraints imply < High constraints
         */
    }

}
