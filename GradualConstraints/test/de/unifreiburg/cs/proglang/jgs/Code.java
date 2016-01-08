package de.unifreiburg.cs.proglang.jgs;

import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import de.unifreiburg.cs.proglang.jgs.typing.Environment;
import de.unifreiburg.cs.proglang.jgs.typing.Environments;
import soot.*;
import soot.jimple.GotoStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.toolkits.graph.DirectedGraph;

import java.util.*;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable.*;
import static java.util.Collections.*;
import static java.util.Arrays.asList;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.*;

/**
 * Some example "code" data for unit testing
 * <p>
 * Created by fennell on 11/16/15.
 */
public class Code {

    public final Jimple j;
    public final Local localX, localY, localZ;
    public final Local localO;
    public final Var<?> varX, varY, varZ, varO;
    public final TypeVars.TypeVar tvarX, tvarY, tvarZ, tvarO;

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
        this.localO = j.newLocal("o", RefType.v());
        this.varX = Var.fromLocal(localX);
        this.varY = Var.fromLocal(localY);
        this.varZ = Var.fromLocal(localZ);
        this.varO = Var.fromLocal(localO);
        this.tvarX = tvars.testParam(varX, "");
        this.tvarY = tvars.testParam(varY, "");
        this.tvarZ = tvars.testParam(varZ,"");
        this.tvarO = tvars.testParam(varO, "");

        this.init = Environments.makeEmpty()
                .add(varX, tvarX)
                .add(varY, tvarY)
                .add(varZ, tvarZ)
                .add(varO, tvarO)
        ;

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

    public abstract class AdHocUnitGraph implements DirectedGraph<Unit> {
        protected abstract List<Unit> getUnits();

        @Override
        public final int size() {
            return getUnits().size();
        }

        @Override
        public final Iterator<Unit> iterator() {
            return getUnits().iterator();
        }

    }

    public class LoopWithReflexivePostdom extends AdHocUnitGraph {
        /*
        *     +-----------+
        *     v           |
        *  -> if -> exit  |
        *     +---------> n
        * */

        public final Unit nExit = j.newReturnStmt(IntConstant.v(42));
        public final Unit nIf = j.newIfStmt(j.newEqExpr(IntConstant.v(0), IntConstant.v(0)), nExit);
        public final Unit n = j.newGotoStmt(nIf);

        @Override
        protected List<Unit> getUnits() {
            return Arrays.asList(nIf, n, nExit);
        }

        @Override
        public List<Unit> getHeads() {
            return Collections.singletonList(nIf);
        }

        @Override
        public List<Unit> getTails() {
            return Collections.singletonList(nExit);
        }

        @Override
        public List<Unit> getPredsOf(Unit unit) {
            if (unit.equals(nIf)) {
                return Collections.emptyList();
            } else if (unit.equals(nExit)) {
                return Collections.singletonList(nIf);
            } else if (unit.equals(n)) {
                return Collections.singletonList(nIf);
            } else {
                throw new RuntimeException("UNEXPECTED CASE");
            }
        }

        @Override
        public List<Unit> getSuccsOf(Unit unit) {

            if (unit.equals(nIf)) {
                return asList(nExit, n);
            } else if (unit.equals(nExit)) {
                return Collections.emptyList();
            } else if (unit.equals(n)) {
                return Collections.singletonList(nIf);
            } else {
                throw new RuntimeException("UNEXPECTED CASE");
            }
        }

    }

    public class LoopWhereIfIsExitNode extends AdHocUnitGraph {
        /*
        *     +------+----+
        *     v      |    |
        *  -> if -> n2    |
        *     +---------> n1
        * */

        public final Unit n1 = j.newGotoStmt((Unit)null);
        public final Unit n2 = j.newGotoStmt((Unit)null);
        public final Unit nIf = j.newIfStmt(j.newEqExpr(IntConstant.v(0), IntConstant.v(0)), n1);

        public LoopWhereIfIsExitNode() {
            ((GotoStmt)n1).setTarget(nIf);
            ((GotoStmt)n2).setTarget(nIf);
        }

        @Override
        protected List<Unit> getUnits() {
            return Arrays.asList(nIf, n1, n2);
        }

        @Override
        public List<Unit> getHeads() {
            return Collections.singletonList(nIf);
        }

        @Override
        public List<Unit> getTails() {
            return Collections.singletonList(nIf);
        }

        @Override
        public List<Unit> getPredsOf(Unit unit) {
            if (unit.equals(nIf)) {
                return Collections.emptyList();
            } else if (unit.equals(n1)) {
                return Collections.singletonList(nIf);
            } else if (unit.equals(n2)) {
                return Collections.singletonList(nIf);
            } else {
                throw new RuntimeException("UNEXPECTED CASE");
            }
        }

        @Override
        public List<Unit> getSuccsOf(Unit unit) {

            if (unit.equals(nIf)) {
                return asList(n1, n2);
            } else if (unit.equals(n1)) {
                return Collections.singletonList(nIf);
            } else if (unit.equals(n2)) {
                return Collections.singletonList(nIf);
            } else {
                throw new RuntimeException("UNEXPECTED CASE");
            }
        }
    }

    /*
    *  -> if -> nExit
    * */
    public class TrivialIf extends AdHocUnitGraph {

        public final Unit nExit = j.newReturnStmt(IntConstant.v(42));
        public final Unit nIf = j.newIfStmt(j.newEqExpr(IntConstant.v(0), IntConstant.v(0)), nExit);

        @Override
        protected List<Unit> getUnits() {
            return asList(nIf, nExit);
        }

        @Override
        public List<Unit> getHeads() {
            return singletonList(nIf);
        }

        @Override
        public List<Unit> getTails() {
            return singletonList(nExit);
        }

        @Override
        public List<Unit> getPredsOf(Unit s) {
            if (s.equals(nIf)) {
                return emptyList();
            } else if (s.equals(nExit)) {
                return singletonList(nIf);
            } else {
                throw new RuntimeException("UNEXPECTED CASE");
            }
        }

        @Override
        public List<Unit> getSuccsOf(Unit s) {
            if (s.equals(nIf)) {
                return singletonList(nExit);
            } else if (s.equals(nExit)) {
                return emptyList();
            } else {
                throw new RuntimeException("UNEXPECTED CASE");
            }
        }
    }

}
