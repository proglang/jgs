package de.unifreiburg.cs.proglang.jgs.constraints;

/**
 * Exception thrown when the sting representation of a security type cannot be parsed.
 */
public class UnknownTypeException extends RuntimeException {
    public UnknownTypeException(String s) {
        super(String.format("Error parsing string %s to a type", s));
    }
}
