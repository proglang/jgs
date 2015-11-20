package de.unifreiburg.cs.proglang.jgs;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import de.unifreiburg.cs.proglang.jgs.typing.Environment;
import de.unifreiburg.cs.proglang.jgs.typing.Environments;
import soot.*;
import soot.jimple.Jimple;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable.*;
import static java.util.Collections.*;
import static java.util.Arrays.asList;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.*;

/**
 *
 * Some example "code" data for unit testing
 *
 * Created by fennell on 11/16/15.
 */
public class Code {

    public final Jimple j;
    public final Local localX, localY, localZ;
    public final Var<?> varX, varY, varZ;
    public final TypeVars.TypeVar tvarX, tvarY, tvarZ;

    // classes and methods for tests
    public final SignatureTable<LowHigh.Level> signatures;
    public final SootClass testClass;
    public final SootMethod testCallee__int;
    public final SootField testLowField_int;

    public final SootMethod testCallee_int_int__int;
    public final SootMethod ignoreSnd_int_int__int;
    public final SootMethod writeToLowReturn0_int__int;

    public final Environment init;

    public Code(TypeVars tvars) {
        this.j = Jimple.v();

        this.localX = j.newLocal("x", IntType.v());
        this.localY = j.newLocal("y", IntType.v());
        this.localZ = j.newLocal("z", IntType.v());
        this.varX = Var.fromLocal(localX);
        this.varY = Var.fromLocal(localY);
        this.varZ = Var.fromLocal(localZ);
        this.tvarX = tvars.fresh();
        this.tvarY = tvars.fresh();
        this.tvarZ = tvars.fresh();

        this.init = Environments.makeEmpty()
                .add(varX, tvarX)
                .add(varY, tvarY)
                .add(varZ, tvarZ);

        this.testClass = new SootClass("TestClass");
        this.testLowField_int = new SootField("testLowField", IntType.v());
        testClass.addField(this.testLowField_int);

        Map<SootMethod, MethodSignatures.Signature<LowHigh.Level>> sigMap = new HashMap<>();
        Symbol.Param<LowHigh.Level> param_x = param(IntType.v(), 0);
        Symbol.Param<LowHigh.Level> param_y = param(IntType.v(), 1);

        // Method:
        this.testCallee__int = new SootMethod("testCallee",
                Collections.emptyList(),
                IntType.v());
        this.testClass.addMethod(testCallee__int);
        sigMap.put(this.testCallee__int,
                makeSignature(signatureConstraints(singleton(leS(
                        Symbol.literal(PUB),
                        ret()))), emptyEffect()));

        // Method:
        this.testCallee_int_int__int = new SootMethod("testCallee",
                asList(IntType.v(),
                        IntType.v()),
                IntType.v());
        this.testClass.addMethod(testCallee_int_int__int);
        SigConstraintSet<Level> sigCstrs =
                signatureConstraints(asList(leS(param_x, ret()),
                        leS(param_y, ret())));
        sigMap.put(this.testCallee_int_int__int,
                makeSignature(sigCstrs, emptyEffect()));

        // Method:
        this.ignoreSnd_int_int__int = new SootMethod("ignoreSnd",
                asList(IntType.v(),
                        IntType.v()),
                IntType.v());
        this.testClass.addMethod(ignoreSnd_int_int__int);
        sigCstrs = signatureConstraints(asList(leS(param_x, ret())));
        sigMap.put(this.ignoreSnd_int_int__int,
                makeSignature(sigCstrs, emptyEffect()));

        //Method:
        this.writeToLowReturn0_int__int = new SootMethod("writeToLow",
                singletonList(IntType.v()),
                IntType.v());
        this.testClass.addMethod(this.writeToLowReturn0_int__int);
        sigCstrs = signatureConstraints(asList((leS(param_x, literal(TLOW)))));
        sigMap.put(this.writeToLowReturn0_int__int,
                makeSignature(sigCstrs, effects(TLOW)));

        // freeze signatures
        this.signatures = makeTable(sigMap);
    }

}
