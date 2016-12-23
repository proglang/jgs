package de.unifreiburg.cs.proglang.jgs.constraints.secdomains;

/**
 * An exception to be thrown when encountering unknown string encodings of security levels.
 * Created by fennell on 12/23/16.
 */
public class UnknownSecurityLevelException extends RuntimeException {

    public UnknownSecurityLevelException(String encodedLevel) {
        super(String.format("Error parsing string %s to a level", encodedLevel));
    }
}
