package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import soot.SootMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * Created by fennell on 1/22/16.
 */
public class Methods {


    public static <Level> Stream<Symbol.Param<Level>> parameters(SootMethod method) {
        return IntStream.range(0, method.getParameterCount())
                .mapToObj(pos -> Symbol.param(method.getParameterType(pos), pos));
    }

    public static <Level> Map<Symbol.Param<Level>, TypeVar> symbolMapForMethod(TypeVars tvars, SootMethod method) {
        return Methods.<Level>parameters(method).collect(
                toMap(Function.identity(),
                        p -> tvars.param(Var.fromParam(p))));
    }
}
