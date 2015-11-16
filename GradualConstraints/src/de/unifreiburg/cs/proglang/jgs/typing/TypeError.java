package de.unifreiburg.cs.proglang.jgs.typing;

/**
 * Created by fennell on 11/15/15.
 */ // TODO: is this really a type error? What is in the case where we have
// unresolvable constraints.. how does this differ from TypeError?
public class TypeError extends Exception {

    private static final long serialVersionUID = 1L;

    public TypeError(String arg0) {
        super(arg0);
    }

}
