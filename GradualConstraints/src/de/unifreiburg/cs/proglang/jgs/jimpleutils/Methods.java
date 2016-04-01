package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.Signature;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import de.unifreiburg.cs.proglang.jgs.signatures.parse.ConstraintParser;
import soot.SootMethod;
import soot.tagkit.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by fennell on 1/22/16.
 */
public class Methods {


    public static <Level> Stream<Symbol.Param<Level>> parameters(SootMethod method) {
        return IntStream.range(0, method.getParameterCount())
                        .mapToObj(pos -> Symbol.param(pos));
    }

    public static <Level> Map<Symbol.Param<Level>, TypeVar> symbolMapForMethod(TypeVars tvars, SootMethod method) {
        return Methods.<Level>parameters(method).collect(
                toMap(Function.identity(),
                      p -> tvars.param(Var.fromParam(p))));
    }

    public static <T> Stream<T> extractAnntotation(String type,
                                                   Function<AnnotationElem, T> extract,
                                                   Stream<Tag> tags) {
        Stream<AnnotationTag> matchingAnnotationTags =
                tags.filter(t -> t instanceof VisibilityAnnotationTag)
                    .flatMap(t -> ((VisibilityAnnotationTag) t)
                            .getAnnotations().stream()
                            .filter(a -> a.getType().equals(type)));

        return matchingAnnotationTags.flatMap(a -> {
            Stream.Builder<T> es = Stream.builder();
            IntStream.range(0, a.getNumElems()).forEach(
                    i -> {
                        AnnotationElem e = a.getElemAt(i);
                        es.add(extract.apply(e));
                    });
            return es.build();
        });
    }

    public static List<String> extractStringAnnotation(String type, Stream<Tag> tags) {
        Function<AnnotationElem, String> extract = e -> {
            IllegalArgumentException wrongType =
                    new IllegalArgumentException(
                            "Expected a string element but got: "
                            + e.toString());
            if (! (e instanceof AnnotationStringElem)) {
                throw wrongType;
            }
            return ((AnnotationStringElem) e).getValue();
        };
        return extractAnntotation(type, extract, tags).collect(Collectors.toList());
    }

    /**
     * Filter a stream of tags for Annotations and return the list of Strings
     * contained in them.
     *
     * @param type The type of the Annotation class to be extracted, as a
     *             mangled class name. Should contain a "value" field containing
     *             a String array.
     */
    public static List<List<String>> extractStringArrayAnnotation(String type, Stream<Tag> tags) {
        Function<AnnotationElem, List<String>> extract = e -> {
            IllegalArgumentException wrongType =
                    new IllegalArgumentException(
                            "Expected a string array Element but got: "
                            + e.toString());
            if (!(e instanceof AnnotationArrayElem)) {
                throw wrongType;
            }
            List<String> values =
                    ((AnnotationArrayElem) e).getValues().stream().map(
                            s -> {
                                if (!(s instanceof AnnotationStringElem)) {
                                    throw wrongType;
                                }
                                return ((AnnotationStringElem) s).getValue();
                            }

                    ).collect(toList());
            return values;
        };
        return extractAnntotation(type, extract, tags).collect(Collectors.toList());
    }

    public static <Level> Signature<Level> extractSignatureFromTags(
            ConstraintParser<Level> parser,
            List<Tag> tags) {
        List<AnnotationTag> atags =
                tags.stream()
                    .filter(t -> t instanceof AnnotationTag)
                    .map(t -> (AnnotationTag) t).collect(toList());
//        Stream<AnnotationTag> constraint =
//                atags.stream().filter(t -> t.getName().equals("Constraints"))

        throw new RuntimeException("Not implemented");
    }
}
