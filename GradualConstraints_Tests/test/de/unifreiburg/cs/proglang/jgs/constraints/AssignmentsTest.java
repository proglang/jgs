package de.unifreiburg.cs.proglang.jgs.constraints;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.util.Interop;
import org.junit.Before;
import org.junit.Test;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.Assignments.*;
import static java.util.Arrays.asList;

public class AssignmentsTest {

    private TypeVars tvars;
    private TypeVar x, y;

    @Before
    public void setUp() {
        tvars = new TypeVars();
        Code code = new Code(tvars);
        x = tvars.testParam(code.varX, "");
        y = tvars.testParam(code.varY, "");
    }

    @Test
    public void testEmpty() {
        Set<Assignment<Level>> expected, result;
        expected = Collections.singleton(Assignments.empty());
        result =
                Interop.asJavaStream(enumerateAll(types, Collections.<TypeVar>emptySet())).collect(Collectors.toSet());
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
            Interop.asJavaStream(enumerateAll(types,
                         Collections.singleton(x))).collect(Collectors.toSet());
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
        Set<Assignment<Level>> result = Interop.asJavaStream(Assignments
                                                   .enumerateAll(types,
                                                                 new HashSet<>(Arrays.asList(x, y))))
                                                   .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

}
