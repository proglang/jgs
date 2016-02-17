package de.unifreiburg.cs.proglang.jgs.signatures.parse;

import java.util.Optional;

/**
 * Simple interface for parsing source annotations.
 */
@FunctionalInterface
public interface AnnotationParser<T> {

    Optional<T> parse(String input);
}
