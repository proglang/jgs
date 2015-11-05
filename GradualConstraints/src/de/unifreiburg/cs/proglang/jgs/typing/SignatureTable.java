package de.unifreiburg.cs.proglang.jgs.typing;

import soot.SootMethod;
import de.unifreiburg.cs.proglang.jgs.typing.MethodSignatures.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by fennell on 11/5/15.
 */
public class SignatureTable<Level> {

    public static <Level> SignatureTable<Level> makeTable(Map<SootMethod, SigConstraintSet<Level>> signatureMap) {
        return new SignatureTable<>(signatureMap);
    }

    private final Map<SootMethod, SigConstraintSet<Level>> signatureMap;

    private SignatureTable(Map<SootMethod, SigConstraintSet<Level>> signatureMap) {
        this.signatureMap = signatureMap;
    }
}