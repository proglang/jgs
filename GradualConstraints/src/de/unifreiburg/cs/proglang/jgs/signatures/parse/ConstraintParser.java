package de.unifreiburg.cs.proglang.jgs.signatures.parse;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.SigConstraint;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser;
import de.unifreiburg.cs.proglang.jgs.util.Interop;
import org.javafp.parsecj.Combinators;
import org.javafp.parsecj.Parser;

import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.makeSigConstraint;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.literal;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.param;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.ret;
import static org.javafp.parsecj.Combinators.*;
import static org.javafp.parsecj.Text.*;

public class ConstraintParser<Level> {

    public final AnnotationParser<TypeDomain.Type<Level>> typeParser;

    public ConstraintParser(AnnotationParser<TypeDomain.Type<Level>> typeParser) {
        this.typeParser = typeParser;
    }

    static <T> Parser<Character, String> takeString(Parser<Character, ?> charClass) {
        return charClass.many1().bind(cs -> {
            StringBuilder sb = new StringBuilder();
            cs.stream().forEach(sb::append);
            return retn(sb.toString());
        });
    }

    static <T> Parser<Character, T> fromAnnotationParser(AnnotationParser<T> p) {
        return takeString(satisfy(c -> ! Character.isSpaceChar(c))).bind(
                s -> Interop.asJavaOptional(p.parse(s))
                              .map(t -> Combinators.<Character, T>retn(t))
                              .orElse(fail()));
    }

    public Parser<Character, Symbol<Level>> symbolParser() {
        Parser<Character, Symbol<Level>> retParser =
                string("ret").label("return").then(retn(ret()));
        Parser<Character, Symbol<Level>> paramParser =
                (takeString(digit).bind(s -> {
                    Parser<Character, Symbol<Level>> result;
                    try {
                        result = retn(param(Integer.parseInt(s)));
                    } catch (NumberFormatException e) {
                        result = fail();
                    }
                    return result.label("param");
                }));
        Parser<Character, Symbol<Level>> literalParser =
                fromAnnotationParser(typeParser)
                        .bind(t -> retn(literal(t)));
        Parser<Character, Symbol<Level>> retOrParamParser =
                string("@").then(retParser.or(paramParser));


        return (retOrParamParser).or(literalParser);
    }

    public Parser<Character, ConstraintKind> constraintKindParser() {
        return string("<=").then(retn(ConstraintKind.LE))
                           .or(string("~").then(retn(ConstraintKind.COMP)));
    }

    /**
     * Parse a stream of tokens to a constraint. Syntax:
     * <p>
     * <symbol> <constraint-kind> <symbol>.
     */
    public Parser<Character, SigConstraint<Level>> constraintParser() {
        return (space.many()).then(symbolParser().bind(
                sym1 -> space.many().then(constraintKindParser().bind(
                        k -> space.many().then(symbolParser().bind(
                                sym2 -> Combinators.<Character>eof()
                                        .then(retn(makeSigConstraint(sym1, sym2, k))
                                        )))))));
    }

}
