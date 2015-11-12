package de.unifreiburg.cs.proglang.jgs.signatures;

import soot.SootMethod;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;

import java.util.Map;
import java.util.Optional;

/**
 * Created by fennell on 11/5/15.
 */
public class SignatureTable<Level> {

    public static <Level> SignatureTable<Level> makeTable(Map<SootMethod, Signature<Level>> signatureMap) {
        return new SignatureTable<>(signatureMap);
    }

    private final Map<SootMethod, Signature<Level>> signatureMap;

    private SignatureTable(Map<SootMethod, Signature<Level>> signatureMap) {
        this.signatureMap = signatureMap;
    }

    public Optional<Signature<Level>> get(SootMethod m) {
        return Optional.<Signature<Level>>ofNullable(signatureMap.get(m));
    }
}
