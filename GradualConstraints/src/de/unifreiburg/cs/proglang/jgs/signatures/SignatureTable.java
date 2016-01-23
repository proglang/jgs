package de.unifreiburg.cs.proglang.jgs.signatures;

import de.unifreiburg.cs.proglang.jgs.jimpleutils.Methods;
import de.unifreiburg.cs.proglang.jgs.typing.MethodTyping;
import soot.SootMethod;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import soot.VoidType;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * A table mapping methods to their security signatures.
 */
public class SignatureTable<Level> {

    /**
     * Create a new table from a map. The map has to be consistent, that is, each signature should mention exactly the parameters of the method and should mention ret exactly if the method has a return type
     *
     * @param signatureMap
     * @param <Level>
     * @return
     */
    public static <Level> SignatureTable<Level> makeTable(Map<SootMethod, Signature<Level>> signatureMap) {
        // check the map for consistency
        for (Map.Entry<SootMethod, Signature<Level>> e : signatureMap.entrySet()) {
            SootMethod m = e.getKey();
            Set<Symbol.Param<Level>> requiredParams =
                            Methods.<Level>parameters(m).collect(toSet());
            Signature<Level> sig = e.getValue();
            Set<Symbol.Param<Level>> sigParams = sig.constraints.symbols()
                    .filter(s -> s instanceof Symbol.Param)
                    .map(s -> (Symbol.Param<Level>) s)
                    .collect(toSet());

            if (!requiredParams.equals(sigParams)) {
                throw new IllegalArgumentException(String.format("Parameters of method signature does not match actual method parameters. \n" +
                        "method: %s \n " +
                        "signature params: %s \n" +
                        " method params: %s", m, sigParams, requiredParams));
            }

            if (m.getReturnType().equals(VoidType.v()) && sig.constraints.symbols().collect(toSet()).contains(Symbol.ret())) {
                throw new IllegalArgumentException(String.format("Method signature of void method contains return symbol.\n method: %s\n sig: %s ", m, sig));
            }

            if (!m.getReturnType().equals(VoidType.v()) && ! sig.constraints.symbols().collect(toSet()).contains(Symbol.ret())) {
                throw new IllegalArgumentException(String.format("No return symbol found in method signature.\n method: %s\n sig: %s", m, sig));
            }

        }
        return new SignatureTable<>(signatureMap);
    }

    private final Map<SootMethod, Signature<Level>> signatureMap;

    private SignatureTable(Map<SootMethod, Signature<Level>> signatureMap) {
        this.signatureMap = signatureMap;
    }

    public SignatureTable<Level> extendWith(Map<SootMethod, Signature<Level>> signatureMap) {
        Map<SootMethod, Signature<Level>> fresh = new HashMap<>(this.signatureMap);
        fresh.putAll(signatureMap);
        return makeTable(fresh);
    }

    public Optional<Signature<Level>> get(SootMethod m) {
        return Optional.<Signature<Level>>ofNullable(signatureMap.get(m));
    }

}
