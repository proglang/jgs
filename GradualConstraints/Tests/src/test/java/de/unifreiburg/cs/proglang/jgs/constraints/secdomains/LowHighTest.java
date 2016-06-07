package de.unifreiburg.cs.proglang.jgs.constraints.secdomains;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;

import org.junit.Test;
import scala.Option;

import java.util.Optional;

public class LowHighTest {
    
    private final LowHigh levels = new LowHigh();

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

    @Test
    public void testParser() {
        assertThat(levels.levelParser().parse("LOW"), is(equalTo(Option.apply(LOW))));
        assertThat(levels.levelParser().parse("HIGH"), is(equalTo(Option.apply(HIGH))));
        assertThat(levels.levelParser().parse("lkjas"), is(equalTo(Option.empty())));
        assertThat(levels.readLevel("LOW"), is(equalTo(LOW)));
        assertThat(levels.readLevel("HIGH"), is(equalTo(HIGH)));
    }


}
