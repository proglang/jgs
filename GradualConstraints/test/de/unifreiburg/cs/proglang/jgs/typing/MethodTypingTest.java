package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.Code;
import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import org.junit.Before;
import org.junit.Test;
import soot.*;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;

import java.util.List;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.*;
import static java.util.Arrays.asList;
import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.*;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Tests that illustrate type-checking of individual methods against their signatures. The assertions for expected
 * success or failure are "compliesTo" and "violates", respectively.
 * <p>
 * Less-than-or-equal constraints are created with the statically imported "leS" method.
 * <p>
 * The "code" object (see class <code>de.unifreiburg.cs.proglang.jgs.Code</code>) contains some common helper
 * definitions (local variables, fields, abstract methods).
 */
public class MethodTypingTest {

    private Jimple j;
    private TypeVars tvars;
    private Code code;
    private ConstraintSetFactory<Level> csets;

    @Before
    public void setUp() {
        this.j = Jimple.v();
        this.tvars = new TypeVars();
        this.code = new Code(tvars);
        this.csets = new NaiveConstraintsFactory<>(types);

    }

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
    public SootMethod makeMultipleReturns() {
        Unit returnX = j.newReturnStmt(code.localX);
        Unit returnZ = j.newReturnStmt(code.localZ);
        Unit ifY = j.newIfStmt(j.newEqExpr(code.localY, code.localY), returnX);
        Unit ifO = j.newIfStmt(j.newEqExpr(code.localO, code.localO), ifY);
        Unit returnZero = j.newReturnStmt(IntConstant.v(0));

        IntType i = IntType.v();

        SootMethod m = code.makeMethod(Modifier.STATIC, "multipleReturns", asList(code.localX, code.localY, code.localZ, code.localO),
                IntType.v(),
                asList(ifO,
                        returnZ,
                        ifY,
                        returnZero,
                        returnX
                ));
        return m;
    }


    /*
    valid signature:
    constraints:
      param0 <= ret
      param1 <= ret
      param2 <= ret
      param3 <= ret
     no effects
     */
    @Test
    public void testMultipleReturns_validSig() throws TypingException {
        SootMethod m = makeMultipleReturns();
        List<Param<Level>> ps = methodParameters(m);
        Stream<SigConstraint<Level>> sigConstraints = Stream.of(
                leS(ps.get(0), ret()),
                leS(ps.get(1), ret()),
                leS(ps.get(2), ret()),
                leS(ps.get(2), ps.get(2)),
                leS(ps.get(3), ret()));
        SignatureTable<Level> signatures = code.signatures.extendWith(m, sigConstraints, emptyEffect());
        MethodTyping.Result<Level> r = mtyping.check(tvars, signatures, code.fields, m);

        //TODO: make a junit-Matcher for these results
        assertThat("No success: " + r.refinementCheckResult.toString() + "\n Effects:" + r.missedEffects.toString(), r.isSuccess(), is(true));
    }

    /*
    invalid signature (constraint for param2 is missing):
    constraints:
      param0 <= ret
      param1 <= ret
      param3 <= ret
     no effects
     */
    @Test
    public void testMultipleReturns_invalidSig() throws TypingException {
        SootMethod m = makeMultipleReturns();
        List<Param<Level>> ps = methodParameters(m);
        SignatureTable<Level> signatures = code.signatures.extendWith(m,
                Stream.of(
                        leS(ps.get(0), ret()),
                        leS(ps.get(1), ret()),
                        leS(ps.get(3), ret())),
                emptyEffect());

        MethodTyping.Result r = mtyping.check(tvars, signatures, code.fields, m);

        //TODO: make a junit-Matcher for these results
        assertThat(String.format("Should not succeed, constraints: \nSIG: %s\n CONSTRAINTS: %s", r.refinementCheckResult.signature.toString().replace(",", ",\n"),
                r.refinementCheckResult.concrete.toString().replace(",", ",\n")), r.isSuccess(), is(false));
    }


    // A method that uses "this"
    /*
        int methodUsingThis(int i) {
            return i + this.testDynamicField;
        }
     */
    public SootMethod makeMethodUsingThis() {
        // TODO: check what Soot does with nested expressions
        Unit getDynField = j.newAssignStmt(code.localY, j.newStaticFieldRef(code.testDynField_int.makeRef()));
        Unit add = j.newAssignStmt(code.localY, j.newAddExpr(code.localX, code.localY));
        Unit exit = j.newReturnStmt(code.localY);

        return code.makeMethod(0, "methodUsingThis", singletonList(code.localX), IntType.v(), asList(
                getDynField,
                add,
                exit
        ));
    }

    // Valid signature
    /*
    constraints:
      param0 <= ret, ? <= ret

    no effects

     */
    @Test
    public void testMethodUsingThis() throws TypingException {
        SootMethod m = makeMethodUsingThis();
        List<Param<Level>> ps = methodParameters(m);
        SignatureTable<Level> signatures = code.signatures.extendWith(m,
                Stream.of(
                        leS(ps.get(0), ret()),
                        leS(Symbol.literal(DYN), ret())
                ),
                emptyEffect());
        assertThat(m, compliesTo(tvars, signatures, code.fields));
    }

    // Invalid signature
    /*
    constraints:
       ? <= ret
    no effects
     */
    @Test
    public void testMethodUsingThis_invalid() throws TypingException {
        SootMethod m = makeMethodUsingThis();
        List<Param<Level>> ps = methodParameters(m);
        SignatureTable<Level> signatures = code.signatures.extendWith(m,
                Stream.of(
                        leS(Symbol.literal(DYN), ret())
                ),
                emptyEffect());
        assertThat(m, violates(tvars, signatures, code.fields));
    }
}
