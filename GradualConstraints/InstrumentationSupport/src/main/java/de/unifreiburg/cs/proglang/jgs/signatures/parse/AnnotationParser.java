package de.unifreiburg.cs.proglang.jgs.signatures.parse;


import scala.Option;

/**
 * Simple interface for parsing source annotations.
 */
public interface AnnotationParser<T> {

    Option<T> parse(String input);
}
