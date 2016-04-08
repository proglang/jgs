package de.unifreiburg.cs.proglang.jgs.signatures;

import scala.Option;
import soot.SootMethod;

import java.util.*;

import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.makeSignature;

/**
 * A table mapping methods to their security signatures.
 */
public class SignatureTable<Level> {

    /**
     * Create a new table from a map.
     */
    public static <Level> SignatureTable<Level> makeTable(Map<SootMethod, Signature<Level>> signatureMap) {
        return new SignatureTable<>(new HashMap<>(signatureMap));
    }

    private final Map<SootMethod, Signature<Level>> signatureMap;

    private SignatureTable(Map<SootMethod, Signature<Level>> signatureMap) {
        this.signatureMap = signatureMap;
    }

    public SignatureTable<Level> extendWith(SootMethod m, Collection<SigConstraint<Level>> constraints, Effects<Level> effects) {
        HashMap<SootMethod, Signature<Level>> freshTable =
                new HashMap<>(this.signatureMap);
        freshTable.put(m, makeSignature(m.getParameterCount(), constraints, effects));
        return makeTable(freshTable);
    }

    @Override
    public String toString() {
        return this.signatureMap.toString();
    }

    public Option<Signature<Level>> get(SootMethod m) {
        return Option.apply(signatureMap.get(m));
    }

}
