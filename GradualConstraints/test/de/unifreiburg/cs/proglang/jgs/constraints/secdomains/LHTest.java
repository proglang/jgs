package de.unifreiburg.cs.proglang.jgs.constraints.secdomains;

import static org.junit.Assert.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.TestDomain.*;

import org.junit.Test;

public class LHTest {
    
    private final LH levels = new LH();

    @Test
    public void test() {
       assertEquals(levels.top(), HIGH);
       assertEquals(levels.bottom(), LOW);
       assertTrue("LOW < HIGH", levels.le(levels.bottom(), levels.top()));
       assertFalse("HIGH /< LOW", levels.le(levels.top(), levels.bottom()));
       assertEquals(HIGH, lub(LOW, HIGH));
       assertEquals(HIGH, lub(HIGH, LOW));
       assertEquals(LOW, glb(HIGH, LOW));
    }

}
