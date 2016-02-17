package de.unifreiburg.cs.proglang.jgs.signatures.parse;

import de.unifreiburg.cs.proglang.jgs.constraints.Constraint;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.javafp.parsecj.*;
import org.junit.Before;
import org.junit.Test;
import soot.JastAddJ.Opt;

import java.util.Optional;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.literal;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.param;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.ret;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConstraintParserTest {

    private ConstraintParser<Level> parsers;

    @Before
    public void setUp() {
        parsers = new ConstraintParser<>(types.typeParser());
    }

    private <T> T parse(Parser<Character, T> parser, String input) throws Exception {
        return parser.parse(State.of(input)).getResult();
    }

    private <T> Matcher<Parser<Character, T>> parses(String input, T result) {
        return new TypeSafeMatcher<Parser<Character, T>>() {


            private Reply<Character, T> reply;
            private Optional<T> parseResult;

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format(" parses \"%s\" as ", input)).appendValue(result);
            }

            @Override
            protected boolean matchesSafely(Parser<Character, T> p) {
                reply = p.parse(State.of(input));
                try {
                    parseResult = Optional.of(reply.getResult());
                } catch (Exception e) {
                    parseResult = Optional.empty();
                }
                return parseResult.isPresent() && parseResult.get().equals(result);
            }

            @Override
            protected void describeMismatchSafely(Parser<Character, T> item, Description mismatchDescription) {
                if (parseResult.isPresent()) {
                   mismatchDescription.appendText(" parsed ").appendValue(parseResult.get());
                } else {
                    mismatchDescription.appendText(" failed with ").appendValue(reply.getMsg());
                }
            }
        };

    }

    @Test
    public void testTakeString() throws Exception {
        assertThat(parsers.takeString(Text.digit), parses("1234", "1234"));
    }

    @Test
    public void test() throws Exception {
        assertThat(parsers.constraintKindParser(), parses("<=", Constraint.Kind.LE));
        assertThat(parsers.constraintKindParser(), parses("~", Constraint.Kind.COMP));
        assertThat(parsers.symbolParser(), parses("HIGH", literal(THIGH)));
        assertThat(parsers.symbolParser(), parses("?", literal(DYN)));
        assertThat(parsers.symbolParser(), parses("@ret", ret()));
        assertThat(parsers.symbolParser(), parses("@1", param(1)));
        assertThat(parsers.constraintParser().parse(State.of("HIGH <= LOW")).getResult(),
                   is(equalTo(leS(literal(THIGH), literal(TLOW)))));
        assertThat(parsers.constraintParser().parse(State.of("? <= LOW")).getResult(),
                   is(equalTo(leS(literal(DYN), literal(TLOW)))));
        assertThat(parsers.constraintParser().parse(State.of("@1 <= LOW")).getResult(),
                   is(equalTo(leS(param(1), literal(TLOW)))));
        assertThat(parsers.constraintParser().parse(State.of("@ret <= @2")).getResult(),
                   is(equalTo(leS(ret(), param(2)))));
    }


}
