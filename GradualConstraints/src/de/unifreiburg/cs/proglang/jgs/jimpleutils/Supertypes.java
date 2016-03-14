package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;

import java.sql.Ref;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Supertypes {

    /**
     * @return The supertypes of {@code sootClass}. Traverses the supertype DAG in preorder. The stream may contain
     * duplicates.
     */
    //TODO-performance: could avoid traversing classes multiple times (but how large can the hierarchies be?)
    public static Stream<SootClass> enumerate(SootClass sootClass) {
        List<SootClass> immediateSuperTypes =
                Stream.concat(
                  sootClass.hasSuperclass()
                  ? Stream.of(sootClass.getSuperclass())
                  : Stream.empty(),
                sootClass.getInterfaces().stream()).collect(toList());
        return Stream.concat(immediateSuperTypes.stream(), immediateSuperTypes.stream().flatMap(Supertypes::enumerate));
    }

    /**
     * @return The methods that m1 overrides.
     */
    public static Stream<SootMethod> findOverridden(SootMethod m1) {
        SootClass c1 = m1.getDeclaringClass();
        return Supertypes.enumerate(c1)
                .flatMap(c -> c.getMethods().stream())
                .filter(m -> overrides(m1, m));
    }

    /**
     * @return true if {@code m1} overrides {@code m2}.
     */
    public static boolean overrides(SootMethod m1, SootMethod m2) {
        // ignore static methods and constructors
        if (m1.isStatic() || m2.isStatic()) { return false; }
        if (m1.getName().equals("<init>") || m2.getName().equals("<init>")) { return false; }
        if (m1.getName().equals(m2.getName())
                && subTypeOf(m1.getDeclaringClass(), m2.getDeclaringClass())
                && m1.getParameterTypes().equals(m2.getParameterTypes())) {
            // it remains to check the return type
            Type rt1 = m1.getReturnType();
            Type rt2 = m2.getReturnType();
            if (rt1 instanceof RefType && rt2 instanceof RefType) {
                return subTypeOf(((RefType)rt1).getSootClass(), ((RefType)rt2).getSootClass());
            } else {
               return rt1.equals(rt2);
            }
        } else {
            return false;
        }
    }

    /**
     * @return true if {@code c1} is a subtype of {@code c2}
     */
    public static boolean subTypeOf(SootClass c1, SootClass c2) {
        return Supertypes.enumerate(c1).filter(sc1 -> sc1.equals(c2)).findAny().isPresent();
    }
}
