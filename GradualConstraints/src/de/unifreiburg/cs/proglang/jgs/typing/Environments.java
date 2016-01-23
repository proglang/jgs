package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import soot.IntType;
import soot.SootMethod;
import soot.Type;
import sun.security.pkcs11.wrapper.Functions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;

/**
 * Companion class to <code>Environment</code>
 * @author fennell
 *
 */
public class Environments {
    
    public static Environment makeEmpty() {
        return new Environment(Collections.emptyMap());
    }

    public static Environment fromMap(Map<Var<?>, TypeVars.TypeVar> m) {
        return new Environment(m);
    }


    public static <Level> Environment forParamMap(TypeVars tvars, Map<Symbol.Param<Level>, TypeVar> symbolMapping) {
        Function<Map.Entry<Symbol.Param<Level>, TypeVar>, Var<?>> getKey = e -> Var.fromParam(e.getKey());
        Stream<Map.Entry<Symbol.Param<Level>, TypeVar>> s = symbolMapping.entrySet().stream();
        Map<Var<?>, TypeVar> m = s.collect(Collectors.toMap(getKey, Map.Entry::getValue));
       return fromMap(m);
    }
}
