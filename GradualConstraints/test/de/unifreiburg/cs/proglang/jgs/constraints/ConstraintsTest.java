package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LH;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LH.Level;

import static de.unifreiburg.cs.proglang.jgs.constraints.TestDomain.*;
import static org.junit.Assert.*;

public class ConstraintsTest {

    private CTypeDomain<LH.Level> ctypes;
    private TypeVars tvars;
    private TypeVar h1, h2, l1, l2, d1, d2, p1, p2;
    private CTypeDomain<LH.Level>.CType ch1, ch2, cl1, cl2, cd1, cd2, cp1, cp2;
    private Map<TypeVar, TypeDomain<LH.Level>.Type> ass;

    // Particular Sets of variables Variable
    private Set<CTypeDomain<Level>.CType> allVariables;
    private Set<CTypeDomain<Level>.CType> nonDyn;
    private Set<CTypeDomain<Level>.CType> allStatic;

    @Before
    public void setUp() {
        ctypes = new CTypeDomain<>();
        tvars = new TypeVars("x");
        h1 = tvars.fresh();
        h2 = tvars.fresh();
        l1 = tvars.fresh();
        l2 = tvars.fresh();
        d1 = tvars.fresh();
        d2 = tvars.fresh();
        p1 = tvars.fresh();
        p2 = tvars.fresh();
        ass = new HashMap<>();
        ass.put(h1, THIGH);
        ass.put(h2, THIGH);
        ass.put(l1, TLOW);
        ass.put(l2, TLOW);
        ass.put(d1, DYN);
        ass.put(d2, DYN);
        ass.put(p1, PUB);
        ass.put(p2, PUB);

        cl1 = ctypes.variable(l1);
        ch1 = ctypes.variable(h1);
        cl2 = ctypes.variable(l2);
        ch2 = ctypes.variable(h2);
        cd1 = ctypes.variable(d1);
        cd2 = ctypes.variable(d2);
        cp1 = ctypes.variable(p1);
        cp2 = ctypes.variable(p2);

        allVariables =
            new HashSet<>(Arrays.asList(cl1,
                                        ch1,
                                        cl2,
                                        ch2,
                                        cd1,
                                        cd2,
                                        cp1,
                                        cp2));

        nonDyn = new HashSet<>(allVariables);
        nonDyn.removeAll(Arrays.asList(cd1, cd2));

        allStatic = new HashSet<>(nonDyn);
        allStatic.removeAll(Arrays.asList(cp1, cp2));
    }

    @Test
    public void testLe() {
        Constraint<Level> LleH = leC(cl1, ch1);
        Assignment<Level> a = new Assignment<>(ass);
        assertTrue("l <= h", LleH.isSatisfied(a));

        Constraint<Level> HleL = leC(ch1, cl1);
        assertFalse("h /<= l", HleL.isSatisfied(a));

        Constraint<Level> HleHigh = leC(ch1, ctypes.literal(THIGH));
        assertTrue("h <= HIGH", HleHigh.isSatisfied(a));

        for (CTypeDomain<Level>.CType t : allVariables) {
            Constraint<Level> c = leC(ctypes.literal(PUB), t);
            assertTrue("all: pub <= " + t.toString(), c.isSatisfied(a));
        }

        for (CTypeDomain<Level>.CType t : nonDyn) {
            Constraint<Level> c = leC(ctypes.literal(DYN), t);
            assertFalse("nonDyn: ? /<= " + t.toString(), c.isSatisfied(a));
        }

        Set<CTypeDomain<Level>.CType> allStatic = new HashSet<>(nonDyn);
        allStatic.removeAll(Arrays.asList(cp1, cp2));
        for (CTypeDomain<Level>.CType t : nonDyn) {
            Constraint<Level> c = leC(ctypes.literal(DYN), t);
            assertFalse(String.format("static %s /<= t", t.toString()),
                        c.isSatisfied(a));
        }
    }

    @Test
    public void testComp() {

        Assignment<Level> a = new Assignment<>(ass);
        for (CTypeDomain<Level>.CType t : allVariables) {
            Constraint<Level> c = compC(ctypes.literal(PUB), t);
            assertTrue("all: pub ~ " + t.toString(), c.isSatisfied(a));
            c = compC(t, ctypes.literal(PUB));
            assertTrue(String.format("all: %s ~ pub", t.toString()),
                       c.isSatisfied(a));
        }

        for (CTypeDomain<Level>.CType t1 : allStatic) {
            for (CTypeDomain<Level>.CType t2 : allStatic) {
                Constraint<Level> c = compC(t1, t2);
                assertTrue(String.format("static: %s ~ %s",
                                         t1.toString(),
                                         t2.toString()),
                           c.isSatisfied(a));
                c = compC(t2, t1);
                assertTrue(String.format("static: %s ~ %s",
                                         t2.toString(),
                                         t1.toString()),
                           c.isSatisfied(a));
            }
        }

        for (CTypeDomain<Level>.CType t : allStatic) {
            assertFalse(String.format("%s /~ ?", t.toString()),
                        compC(t, cd1).isSatisfied(a));
            assertFalse(String.format("? /~ %s", t.toString()),
                        compC(cd1, t).isSatisfied(a));
        }

        assertTrue("d1 ~ d2", compC(cd1, cd2).isSatisfied(a));
        assertTrue("d2 ~ d1", compC(cd2, cd1).isSatisfied(a));

    }

    @Test
    public void testDImpl() {
        Assignment<Level> a = new Assignment<>(ass);

        for (CTypeDomain<Level>.CType t1 : nonDyn) {
            for (CTypeDomain<Level>.CType t2 : allVariables) {
                assertTrue(String.format("%s -->? %s",
                                         t1.toString(),
                                         t2.toString()),
                           dimplC(t1, t2).isSatisfied(a));
            }
        }

        for (CTypeDomain<Level>.CType t1 : allStatic) {
            assertFalse(String.format("static: ? /-->? %s",
                                     t1.toString()),
                       dimplC(cd1, t1).isSatisfied(a));
        }
        
        assertTrue("? -->? ?", dimplC(cd1, cd2).isSatisfied(a));
        assertTrue("? -->? pub", dimplC(cd1, cp2).isSatisfied(a));

    }

}
