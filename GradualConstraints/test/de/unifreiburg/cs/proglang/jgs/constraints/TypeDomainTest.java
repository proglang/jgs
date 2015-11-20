package de.unifreiburg.cs.proglang.jgs.constraints;

import static org.junit.Assert.*;

import org.junit.Test;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;

public class TypeDomainTest {

    @Test
    public void test() {
        assertEquals(THIGH, tglb(THIGH, THIGH));
        assertEquals(TLOW, tglb(THIGH, TLOW));
        assertEquals(PUB, tglb(THIGH, DYN));
        assertEquals(PUB, tglb(TLOW, DYN));
        assertTrue("LOW <= HIGH", tle(TLOW, THIGH));
        assertFalse("HIGH /<= LOW", tle(THIGH, TLOW));
        assertFalse("DYN /<= LOW", tle(DYN, TLOW));
        assertFalse("HIGH /<= DYN", tle(THIGH, DYN));
        assertTrue("PUB <= DYN", tle(PUB, DYN));
        assertTrue("PUB <= THIGH", tle(PUB, THIGH));
    }

}
