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
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * A table mapping methods to their security signatures.
 */
public class SignatureTable<Level> {

    /**
     * Create a new table from a map. The signatures in the map <code>signatureMap</code> are extended to be consistent, that is, each signature should mention exactly the parameters of the method and should mention ret exactly if the method has a return type
     *
     * @param signatureEntries
     * @param <Level>
     * @return
     */
    public static <Level> SignatureTable<Level> makeTable(Map<SootMethod, Signature<Level>> signatureMap) {
        // update the signatures such that they are consistent for their methods, i.e. add trivial constraints for parameters not mentioned
        Stream<Pair<SootMethod, Signature<Level>>> sigStream = signatureMap.entrySet().stream()
                .map(e -> Pair.of(e.getKey(), e.getValue()));
        Stream<Pair<SootMethod, Signature<Level>>> fixedSigStream = sigStream.map(e -> {
            SootMethod m = e.getKey();
            Set<Symbol.Param<Level>> requiredParams =
                    Methods.<Level>parameters(m).collect(toSet());
            Signature<Level> sig = e.getValue();
            Set<Symbol.Param<Level>> sigParams = sig.constraints.symbols()
                    .filter(s -> s instanceof Symbol.Param)
                    .map(s -> (Symbol.Param<Level>) s)
                    .collect(toSet());

            requiredParams.removeAll(sigParams);
            Stream<SigConstraint<Level>> missingSigConstraints = requiredParams.stream()
                    .map(p -> MethodSignatures.le(p, p));

            return Pair.of(m, sig.addConstraints(missingSigConstraints));
        });

            // TODO: check what happens in these cases.. are they unproblematic now?
            /*
            if (m.getReturnType().equals(VoidType.v()) && sig.constraints.symbols().collect(toSet()).contains(Symbol.ret())) {
                throw new IllegalArgumentException(String.format("Method signature of void method contains return symbol.\n method: %s\n sig: %s ", m, sig));
            }

            if (!m.getReturnType().equals(VoidType.v()) && !sig.constraints.symbols().collect(toSet()).contains(Symbol.ret())) {
                throw new IllegalArgumentException(String.format("No return symbol found in method signature.\n method: %s\n sig: %s", m, sig));
            }
            */

        return new SignatureTable<>(fixedSigStream.collect(toMap(Pair::getKey, Pair::getValue)));
    }

    private final Map<SootMethod, Signature<Level>> signatureMap;

    private SignatureTable(Map<SootMethod, Signature<Level>> signatureMap) {
        this.signatureMap = signatureMap;
    }

    public SignatureTable<Level> extendWith(SootMethod m, Stream<SigConstraint<Level>> constraints, Effects<Level> effects) {
        HashMap<SootMethod, Signature<Level>> freshTable = new HashMap<>(this.signatureMap);
        freshTable.put(m, makeSignature(MethodSignatures.signatureConstraints(constraints), effects));
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
