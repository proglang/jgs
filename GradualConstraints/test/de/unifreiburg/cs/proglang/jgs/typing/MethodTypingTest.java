package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Methods;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import org.junit.Test;
import soot.*;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.toolkits.graph.BriefUnitGraph;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.*;
import static java.util.Arrays.asList;
import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.*;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class MethodTypingTest {

    private static final Jimple j = Jimple.v();
    private static final TypeVars tvars = new TypeVars();
    private static final Code code = new Code(tvars);

    /*
    // a method with multiple return statements

    static int multipleReturns(int x, int y, int z, Object o) {
       if(o == o) {
         if (y == y) {
           return x;
         } else {
           return 0;
         }
       } else {
         return z;
       }
    }
     */

    @Test
    public void testMultipleReturns() throws TypingException {
        Unit returnX = j.newReturnStmt(code.localX);
        Unit returnZ = j.newReturnStmt(code.localZ);
        Unit ifY = j.newIfStmt(j.newEqExpr(code.localY, code.localY), returnX);
        Unit ifO = j.newIfStmt(j.newEqExpr(code.localO, code.localO), ifY);
        Unit returnZero = j.newReturnStmt(IntConstant.v(0));

        IntType i = IntType.v();

        SootMethod m = code.makeMethod(Modifier.STATIC, "multipleReturns", asList(code.localX, code.localY, code.localZ, code.localO), IntType.v(),
                asList(ifO,
                        returnZ,
                        ifY,
                        returnZero,
                        returnX
                ));
        List<Param<Level>> ps = methodParameters(m);
        MethodSignatures.Signature<Level> sig =
                makeSignature(signatureConstraints(asList(
                        leS(ps.get(0), ret()),
                        leS(ps.get(1), ret()),
                        leS(ps.get(2), ret()),
                        leS(ps.get(2), ps.get(2)),
                        leS(ps.get(3), ret()))),
                        emptyEffect());
        SignatureTable<Level> signatures = code.signatures.extendWith(singletonMap(m, sig));
        ConstraintSetFactory<Level> csets = new NaiveConstraintsFactory<>(types);
        MethodTyping mtyping = new MethodTyping<Level>(tvars, csets, cstrs, casts);
        MethodTyping.Result<Level> r = mtyping.check(signatures, m);

        //TODO: make a junit-Matcher for these results
        assertThat("No success: " + r.refinementError.toString() + "\n Effects:" + r.missedEffects.toString(), r.isSuccess(), is(true));

        sig = makeSignature(signatureConstraints(asList(
                leS(ps.get(0), ret()),
                leS(ps.get(1), ret()),
                leS(ps.get(2), ps.get(2)),
                leS(ps.get(3), ret()))),
                emptyEffect());
        signatures = code.signatures.extendWith(singletonMap(m, sig));

        assertThat("Second parameter is gone", methodParameters(m), hasItem(ps.get(2)));


        MethodBodyTyping mbtyping = new MethodBodyTyping(tvars, csets, cstrs, casts, signatures);
        Map<Param<Level>, TypeVars.TypeVar> paramMapping = Methods.symbolMapForMethod(tvars,m);
        BodyTypingResult rb = mbtyping.generateResult(new BriefUnitGraph(m.getActiveBody()), tvars.topLevelContext(), Environments.forParamMap(tvars, paramMapping));
        ConstraintSet<Level> cs = rb.getConstraints();
        r = mtyping.check(signatures, m);


        Map<Symbol<Level>, TypeVars.TypeVar> symbolMapping = new HashMap<>(paramMapping);
        symbolMapping.put(Symbol.ret(), tvars.ret());

        ConstraintSet<Level> sigConstraints = csets.fromCollection(sig.constraints.toTypingConstraints(symbolMapping).collect(toList()));


        Assignment<Level> ass =
                Assignments.builder(tvars.ret(), TLOW)
                        .add(tvars.param(Var.fromParam(ps.get(2))), DYN)
                        .build();
        ConstraintSet<Level> appliedSig = makeNaive(sigConstraints.apply(ass).collect(Collectors.toSet()));

        ConstraintSet<Level> appliedCs = makeNaive(cs.apply(ass).collect(Collectors.toSet()));

        assertThat("sig unsat for param2->L, ret->?", appliedSig.isSat(types), is(true));
        assertThat("cs sat for param2->L, ret->?", appliedCs.isSat(types), is(false));
        assertThat("sig does subsume constraints", sigConstraints.subsumes(cs), is(false));

        //TODO: make a junit-Matcher for these results
        assertThat(String.format("Should not succeed, constraints: \nSIG: %s\n CONSTRAINTS: %s", sigConstraints.toString().replace(",", ",\n"), rb.getConstraints().toString().replace(",", ",\n")), r.isSuccess(), is(false));
    }


/*

    SootMethod m = new SootMethod("test", Collections.emptyList(), VoidType.v());
    Body b = j.newBody(m);
    b.getLocals().addAll(asList(code.localO, code.localX, code.localY, code.localZ));
    b.getUnits().addAll(asList(
            ifO,
                        returnZ,
                        ifY,
                        returnZero,
                        returnX
                        ));

    DirectedGraph<Unit> g = new BriefUnitGraph(b);
    */
}
