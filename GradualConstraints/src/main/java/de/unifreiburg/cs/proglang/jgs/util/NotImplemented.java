package de.unifreiburg.cs.proglang.jgs.util;

public class NotImplemented extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NotImplemented(String message) {
        super("NOT IMPLEMENTED: " + message);
    }
    

}
