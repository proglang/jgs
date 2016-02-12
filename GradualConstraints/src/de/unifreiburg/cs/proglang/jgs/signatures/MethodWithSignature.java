package de.unifreiburg.cs.proglang.jgs.signatures;

import soot.SootMethod;

import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;

/**
 * A soot method with a security signature
 */
public class MethodWithSignature<Level> {

    public static <Level> MethodWithSignature<Level> make(SootMethod method, MethodSignatures.Signature<Level> signature) {
        return new MethodWithSignature<>(method, signature);
    }


    public final SootMethod method;
    public final MethodSignatures.Signature<Level> signature;


    private MethodWithSignature(SootMethod method, Signature<Level> signature) {
        this.method = method;
        this.signature = signature;
    }

}
