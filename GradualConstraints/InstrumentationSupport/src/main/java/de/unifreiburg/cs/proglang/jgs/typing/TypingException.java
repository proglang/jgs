package de.unifreiburg.cs.proglang.jgs.typing;

/**
 * An Exception class for "user errors" during (security) type checking, e.g. syntax errors in casts and constraints.
 */
public class TypingException extends Exception {

    private static final long serialVersionUID = 1L;

    public TypingException(String arg0) {
        super(arg0);
    }

}
