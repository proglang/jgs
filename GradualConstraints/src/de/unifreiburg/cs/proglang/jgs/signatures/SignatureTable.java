package de.unifreiburg.cs.proglang.jgs.signatures;

import de.unifreiburg.cs.proglang.jgs.jimpleutils.Methods;
import de.unifreiburg.cs.proglang.jgs.typing.MethodTyping;
import org.apache.commons.lang3.tuple.Pair;
import soot.SootMethod;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import soot.VoidType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.makeSignature;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * A table mapping methods to their security signatures.
 */
public class SignatureTable<Level> {

    /**
     * Create a new table from a map. The signatures in the map
     * <code>signatureMap</code> are extended to be consistent, that is, each
     * signature should mention exactly the parameters of the method and should
     * mention ret exactly if the method has a return type
     */
    public static <Level> SignatureTable<Level> makeTable(Map<SootMethod, Signature<Level>> signatureMap) {
        // update the signatures such that they are consistent for their methods, i.e. add trivial constraints for parameters not mentioned
        Stream<Pair<SootMethod, Signature<Level>>> sigStream =
                signatureMap.entrySet().stream()
                            .map(e -> Pair.of(e.getKey(), e.getValue()));
        return new SignatureTable<>(sigStream.collect(toMap(Pair::getKey, Pair::getValue)));
    }

    private final Map<SootMethod, Signature<Level>> signatureMap;

    private SignatureTable(Map<SootMethod, Signature<Level>> signatureMap) {
        this.signatureMap = signatureMap;
    }

    public SignatureTable<Level> extendWith(SootMethod m, Stream<SigConstraint<Level>> constraints, Effects<Level> effects) {
        HashMap<SootMethod, Signature<Level>> freshTable =
                new HashMap<>(this.signatureMap);
        freshTable.put(m, makeSignature(m.getParameterCount(), constraints.collect(toList()), effects));
        return makeTable(freshTable);
    }

    @Override
    public String toString() {
        return this.signatureMap.toString();
    }

    public Optional<Signature<Level>> get(SootMethod m) {
        return Optional.<Signature<Level>>ofNullable(signatureMap.get(m));
    }

}
