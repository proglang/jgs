package de.unifreiburg.cs.proglang.jgs.constraints;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains
        .UnknownSecurityLevelException;
import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser;
import org.junit.Test;
import scala.Option;

import java.util.Iterator;
import java.util.Optional;

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

    @Test
    public void testParser() {
        assertThat(types.typeParser().parse("LOW"), is(equalTo(Option.apply(TLOW))));
        assertThat(types.typeParser().parse("HIGH"), is(equalTo(Option.apply(THIGH))));
        assertThat(types.typeParser().parse("lkjas"), is(equalTo(Option.empty())));
        assertThat(types.typeParser().parse("pub"), is(equalTo(Option.apply(PUB))));
        assertThat(types.typeParser().parse("?"), is(equalTo(Option.apply(DYN))));
        // see that special types like ? and pub take precendence
        SecDomain<Level> alternativeDomain = new SecDomain<Level>() {
            @Override
            public Level bottom() {
                throw new RuntimeException("Not Implemented!");
            }

            @Override
            public Level top() {
                throw new RuntimeException("Not Implemented!");
            }

            @Override
            public Level lub(Level l1, Level l2) {
                throw new RuntimeException("Not Implemented!");
            }

            @Override
            public Level glb(Level l1, Level l2) {
                throw new RuntimeException("Not Implemented!");
            }

            @Override
            public boolean le(Level l1, Level l2) {
                throw new RuntimeException("Not Implemented!");
            }

            public AnnotationParser<Level> levelParser() {
                return s -> {
                    if (s.equals("?")) {
                        return Option.apply(HIGH);
                    } else {
                        return Option.empty();
                    }
                };
            }
            @Override
            public Level readLevel(String s) {
                if (s.equals("?")) {
                    return HIGH;
                } else {
                   throw new UnknownSecurityLevelException(s);
                }

            }

            @Override
            public Iterator<Level> enumerate() {
                throw new RuntimeException("Not Implemented!");
            }
        };
        TypeDomain<Level> alternativeTypeDomain = new TypeDomain<Level>(alternativeDomain);
        assertThat(alternativeTypeDomain.typeParser().parse("?"), is(equalTo(Option.apply(alternativeTypeDomain.dyn()))));
    }

}
