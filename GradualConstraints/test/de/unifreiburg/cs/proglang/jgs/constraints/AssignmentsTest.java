package de.unifreiburg.cs.proglang.jgs.constraints;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;

import static de.unifreiburg.cs.proglang.jgs.constraints.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.Assignments.*;
import static java.util.Arrays.asList;

public class AssignmentsTest {

    private TypeVars tvars;
    private TypeVar x, y;

    @Before
    public void setUp() {
        tvars = new TypeVars("x");
        x = tvars.fresh();
        y = tvars.fresh();
    }

    @Test
    public void testEmpty() {
        Set<Assignment<Level>> expected, result;
        expected = Collections.singleton(Assignments.empty());
        result =
            enumerateAll(types, new LinkedList<>()).collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    public void testOneVar() {
        Set<Assignment<Level>> expected, result;
        expected = new HashSet<>(asList(builder(x, PUB).build(),
                                        builder(x, TLOW).build(),
                                        builder(x, THIGH).build(),
                                        builder(x, DYN).build()));
        result =
            enumerateAll(types,
                         new LinkedList<>(Arrays.asList(x))).collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    public void testTwoVars() {
        Set<Assignment<Level>> expected;
        expected =
            new HashSet<>(Arrays.asList(builder(x, PUB).add(y, PUB).build(),
                                        builder(x, PUB).add(y, TLOW).build(),
                                        builder(x, PUB).add(y, THIGH).build(),
                                        builder(x, PUB).add(y, DYN).build(),
                                        builder(x, TLOW).add(y, PUB).build(),
                                        builder(x, TLOW).add(y, TLOW).build(),
                                        builder(x, TLOW).add(y, THIGH).build(),
                                        builder(x, TLOW).add(y, DYN).build(),
                                        builder(x, THIGH).add(y, PUB).build(),
                                        builder(x, THIGH).add(y, TLOW).build(),
                                        builder(x, THIGH).add(y, THIGH).build(),
                                        builder(x, THIGH).add(y, DYN).build(),
                                        builder(x, DYN).add(y, PUB).build(),
                                        builder(x, DYN).add(y, TLOW).build(),
                                        builder(x, DYN).add(y, THIGH).build(),
                                        builder(x, DYN).add(y, DYN).build()));
        Set<Assignment<Level>> result = Assignments
                                                   .enumerateAll(types,
                                                                 new LinkedList<>(Arrays.asList(x,
                                                                                                y)))
                                                   .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

}
