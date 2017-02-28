package de.unifreiburg.cs.proglang.jgs.signatures.parse;

import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintKind;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol$;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import scala.util.parsing.combinator.Parsers;

import java.util.Optional;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConstraintParserTest {

    private ConstraintParser<Level> parsers;

    @Before
    public void setUp() {
        parsers = new ConstraintParser<Level>(types);
    }

    private <T> T parse(Parsers.Parser<T> parser, String input) throws Exception {
        return (T)parsers.parseAll(parser, input).get();
    }

    private <T> Matcher<Parsers.Parser<T>> parses(String input, T result) {
        return new TypeSafeMatcher<Parsers.Parser<T>>() {


            private Parsers.ParseResult<T> reply;
            private Optional<T> parseResult;

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format(" parses \"%s\" as ", input)).appendValue(result);
            }

            @Override
            protected boolean matchesSafely(Parsers.Parser<T> p) {
                reply = parsers.parse(p, input);
                try {
                    parseResult = Optional.of(reply.get());
                } catch (Exception e) {
                    parseResult = Optional.empty();
                }
                return parseResult.isPresent() && parseResult.get().equals(result);
            }

            @Override
            protected void describeMismatchSafely(Parsers.Parser<T> item, Description mismatchDescription) {
                if (parseResult.isPresent()) {
                   mismatchDescription.appendText(" parsed ").appendValue(parseResult.get());
                } else {
                    mismatchDescription.appendText(" failed with ").appendValue(reply.toString());
                }
            }
        };

    }

    /*
    @Test
    public void testTakeString() throws Exception {
        assertThat(parsers.takeString(Text.digit), parses("1234", "1234"));
    }
    */

    @Test
    public void test() throws Exception {
        assertThat(parsers.constraintKindParser(), parses("<=", ConstraintKind.LE));
        assertThat(parsers.constraintKindParser(), parses("~", ConstraintKind.COMP));
        assertThat(parsers.symbolParser(), parses("HIGH", Symbol$.MODULE$.literal(THIGH)));
        assertThat(parsers.symbolParser(), parses("?", Symbol$.MODULE$.literal(DYN)));
        assertThat(parsers.symbolParser(), parses("@ret", Symbol$.MODULE$.ret()));
        assertThat(parsers.symbolParser(), parses("@1", Symbol$.MODULE$.param(1)));
        assertThat(parsers.parse(parsers.constraintParser(), "HIGH <= LOW").get(),
                   is(equalTo(leS(Symbol$.MODULE$.literal(THIGH), Symbol$.MODULE$.literal(TLOW)))));
        assertThat(parsers.parse(parsers.constraintParser(), "? <= LOW").get(),
                   is(equalTo(leS(Symbol$.MODULE$.literal(DYN), Symbol$.MODULE$.literal(TLOW)))));
        assertThat(parsers.parse(parsers.constraintParser(), ("@1 <= LOW")).get(),
                   is(equalTo(leS(Symbol$.MODULE$.param(1), Symbol$.MODULE$.literal(TLOW)))));
        assertThat(parsers.parse(parsers.constraintParser(), ("@ret <= @2")).get(),
                   is(equalTo(leS(Symbol$.MODULE$.ret(), Symbol$.MODULE$.param(2)))));
    }


}
