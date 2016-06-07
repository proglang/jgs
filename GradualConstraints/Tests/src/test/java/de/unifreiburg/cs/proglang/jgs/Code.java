package de.unifreiburg.cs.proglang.jgs;

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.*;
import de.unifreiburg.cs.proglang.jgs.typing.Environment;
import de.unifreiburg.cs.proglang.jgs.typing.Environments;
import de.unifreiburg.cs.proglang.jgs.util.Interop;
import org.apache.commons.lang3.tuple.Pair;
import soot.*;
import soot.jimple.*;
import soot.toolkits.graph.DirectedGraph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.TestDomain.*;
import static de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import static de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable.makeTable;
import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.*;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Some example "code" data for unit testing
 * <p>
 * Created by fennell on 11/16/15.
 */
public class Code {

    public final Jimple j;
    public final Local localX, localY, localZ;
    public final Local localO;
    public final Local localT;
    public final Local localThis;
    public final Var<?> varX, varY, varZ, varO, varT, varThis;
    public final TypeVars.TypeVar tvarX, tvarY, tvarZ, tvarO, tvarT, tvarThis;

    // classes and methods for tests
    public final SignatureTable<LowHigh.Level> signatures;
    public final SootClass testClass;
    public final SootMethod testCallee__int;
    public final SootField testLowField_int;
    public final SootField testHighField_int;
    public final SootField testStaticDynField_int;

    public final SootMethod testCallee_int_int__int;
    public final SootMethod ignoreSnd_int_int__int;
    public final SootMethod writeToLowReturn0_int__int;

    public final Environment init;
    public final FieldTable<Level> fields;
    public final SootMethod ignore0Low1ReturnHigh;

    private static SootClass makeFreshClass(String name) {
        // reset the testClass
        if (Scene.v().containsClass(name)) {
            Scene.v().removeClass(Scene.v().getSootClass(name));
        }
        SootClass result = new SootClass(name);
        Scene.v().addClass(result);
        return result;
    }

    public Code(TypeVars tvars) {


        this.j = Jimple.v();

        this.localX = j.newLocal("x", IntType.v());
        this.localY = j.newLocal("y", IntType.v());
        this.localZ = j.newLocal("z", IntType.v());
        this.localO = j.newLocal("o", RefType.v("java.lang.Object"));
        this.localT = j.newLocal("t", RefType.v("TestClass"));
        this.localThis = j.newLocal("this", RefType.v("TestClass"));
        this.varX = Var.fromLocal(localX);
        this.varY = Var.fromLocal(localY);
        this.varZ = Var.fromLocal(localZ);
        this.varO = Var.fromLocal(localO);
        this.varT = Var.fromLocal(localT);
        this.varThis = Var.fromLocal(localThis);
        this.tvarX = tvars.testParam(varX, "");
        this.tvarY = tvars.testParam(varY, "");
        this.tvarZ = tvars.testParam(varZ, "");
        this.tvarO = tvars.testParam(varO, "");
        this.tvarT = tvars.testParam(varT, "");
        this.tvarThis = tvars.testParam(varThis, "");

        this.init = Environments.makeEmpty()
                .add(varX, tvarX)
                .add(varY, tvarY)
                .add(varZ, tvarZ)
                .add(varO, tvarO)
                .add(varT, tvarT)
                .add(varThis, tvarThis)
        ;

        this.testClass = makeFreshClass("TestClass");

        Map<SootField, TypeDomain.Type<Level>> fieldMap = new HashMap<>();

        this.testLowField_int = new SootField("testLowField", IntType.v(), 0);
        testClass.addField(this.testLowField_int);
        fieldMap.put(testLowField_int, TLOW);
        this.testHighField_int = new SootField("testHighField", IntType.v(), 0);
        testClass.addField(this.testHighField_int);
        fieldMap.put(testHighField_int, THIGH);
        this.testStaticDynField_int = new SootField("testStaticDynField", IntType.v(), Modifier.STATIC);
        testClass.addField(this.testStaticDynField_int);
        fieldMap.put(testStaticDynField_int, DYN);

        // freeze field map
        this.fields = new FieldTable<>(fieldMap);

        Map<SootMethod, Signature<Level>> sigMap = new HashMap<>();
        Symbol.Param<LowHigh.Level> param_x = param(0);
        Symbol.Param<LowHigh.Level> param_y = param(1);

        // Method:
        // int testCallee()
        //    with {} effect {}
        this.testCallee__int = new SootMethod("testCallee",
                Collections.emptyList(),
                IntType.v(), Modifier.ABSTRACT);
        this.testClass.addMethod(testCallee__int);
        sigMap.put(this.testCallee__int,
                makeSignature(this.testCallee__int.getParameterCount(),
                              Stream.of(leS(Symbol.literal(PUB), ret())).collect(toList()),
                              Effects.emptyEffect()));

        // Method:
        // int testCallee_int_int__int (int, int)
        //   with {@param1 <= @ret, @param2 <= @ret} effect {}
        this.testCallee_int_int__int = new SootMethod("testCallee",
                asList(IntType.v(),
                        IntType.v()),
                IntType.v(), Modifier.ABSTRACT);
        this.testClass.addMethod(testCallee_int_int__int);
        Stream<SigConstraint<Level>> sigCstrs =
                Stream.of(leS(param_x, ret()),
                        leS(param_y, ret()));
        sigMap.put(this.testCallee_int_int__int,
                makeSignature(this.testCallee_int_int__int.getParameterCount(),
                              sigCstrs.collect(toList()),
                              Effects.emptyEffect()));

        // Method:
        // int ignoreSnd(int, int)
        //   with { @param1 <= @ret } effect {}
        this.ignoreSnd_int_int__int = new SootMethod("ignoreSnd",
                asList(IntType.v(),
                        IntType.v()),
                IntType.v(), Modifier.ABSTRACT);
        this.testClass.addMethod(ignoreSnd_int_int__int);
        sigCstrs = Stream.of(leS(param_x, ret()), leS(param_y, param_y));
        sigMap.put(this.ignoreSnd_int_int__int,
                makeSignature(this.ignoreSnd_int_int__int.getParameterCount(),
                              sigCstrs.collect(toList()), Effects.emptyEffect()));

        // Method:
        // int writeToLowReturn0(int)
        //   with { @param0 <= LOW } effect { LOW }
        this.writeToLowReturn0_int__int = new SootMethod("writeToLow",
                singletonList(IntType.v()),
                IntType.v(), Modifier.ABSTRACT);
        this.testClass.addMethod(this.writeToLowReturn0_int__int);
        sigCstrs = Stream.of((leS(param_x, literal(TLOW))), leS(ret(), ret()));
        sigMap.put(this.writeToLowReturn0_int__int,
                makeSignature(this.writeToLowReturn0_int__int.getParameterCount(),
                              sigCstrs.collect(toList()), Effects.makeEffects(TLOW)));


        // Method:
        // int ignore0Low1ReturnHigh(int, int)
        //   with { @param1 <= LOW, HIGH <= ret } effect {}
        this.ignore0Low1ReturnHigh = new SootMethod("ignore0Low1ReturnHigh",
                asList(IntType.v(), IntType.v()),
                IntType.v(), Modifier.ABSTRACT);
        this.testClass.addMethod(this.ignore0Low1ReturnHigh);
        sigCstrs = Stream.of(leS(param(1), literal(TLOW)), leS(literal(THIGH), ret()));
        sigMap.put(this.ignore0Low1ReturnHigh, makeSignature(this.ignore0Low1ReturnHigh.getParameterCount(),
                                                             sigCstrs.collect(toList()), Effects.emptyEffect()));

        // freeze signatures
        this.signatures = makeTable(sigMap);
    }


    /**
     * Create a method conveniently. The method is added to the class "TestClass". Parameters can be given as an
     * (positional) array of local variables (the "identity statements", required by Soot to map parameters to local
     * variables, are inserted automatically)
     */
    public SootMethod makeMethod(int modifier, String name, List<Local> params, soot.Type retType, List<Unit> bodyStmts) {
        SootMethod m = new SootMethod(name, params.stream().map(Local::getType).collect(toList()), retType, modifier);
        this.testClass.addMethod(m);
        Body body = Jimple.v().newBody(m);
        m.setActiveBody(body);

        // set the statements for the body.. first the identity statements, then the bodyStmts
        if (!m.isStatic()) {
            body.getLocals().add(localThis);
            body.getUnits().add(Jimple.v().newIdentityStmt(localThis, Jimple.v().newThisRef(testClass.getType())));
        }
        IntStream.range(0, params.size()).forEach(pos -> {
            Local l = params.get(pos);
            ParameterRef pr = Jimple.v().newParameterRef(l.getType(), pos);
            body.getUnits().add(Jimple.v().newIdentityStmt(l, pr));
        });
        body.getUnits().addAll(bodyStmts);

        // set the locals for the body
        Set<Local> locals = Stream.concat(
                params.stream(),
                body.getUseAndDefBoxes().stream()
                        .filter(b -> b.getValue() instanceof Local)
                        .map(b -> (Local) b.getValue())
        ).collect(toSet());
        locals.removeAll(body.getLocals());
        body.getLocals().addAll(locals);

        return m;
    }




    /**
     * Conveniently create a class deriving from Code.testClass
     */
    public Pair<SootClass,SignatureTable<Level>> makeDerivedClass(String name, SootClass superClass, List<MethodWithSignature<Level>> methods) {
        SootClass result = makeFreshClass(name);
        result.setSuperclass(superClass);
        SignatureTable<Level> newSignatures = this.signatures;
        for (MethodWithSignature<Level> m : methods) {
            result.addMethod(m.method);
            newSignatures = newSignatures.extendWith(m.method, Interop.asJavaStream(m.signature.constraints.stream()).collect(Collectors.toList()), m.signature.effects);
        }

        return Pair.of(result, newSignatures);
    }

    public abstract class AdHocUnitGraph implements DirectedGraph<Unit> {
        protected abstract Collection<Unit> getUnits();

        @Override
        public final int size() {
            return getUnits().size();
        }

        @Override
        public final Iterator<Unit> iterator() {
            return getUnits().iterator();
        }

    }

    public class AdHocForwardUnitGraph extends AdHocUnitGraph {

        private Map<Unit, Set<Unit>> forwardEdges;

        protected AdHocForwardUnitGraph() {
        }

        protected void setForwardEdges(Map<Unit, Set<Unit>> forwardEdges) {
            this.forwardEdges = forwardEdges;
        }

        @Override
        public final List<Unit> getSuccsOf(Unit s) {
            if (this.forwardEdges.containsKey(s)) {
                return new ArrayList<>(this.forwardEdges.get(s));
            } else {
                return Collections.emptyList();
            }
        }

        @Override
        public final List<Unit> getHeads() {
            return this.getUnits().stream().filter(s -> this.getPredsOf(s).isEmpty()).collect(toList());
        }

        @Override
        public final List<Unit> getTails() {
            return this.getUnits().stream().filter(s -> this.getSuccsOf(s).isEmpty()).collect(toList());
        }

        @Override
        public final List<Unit> getPredsOf(Unit s) {
            return this.forwardEdges.entrySet().stream()
                    .filter(e -> e.getValue().contains(s))
                    .map(Map.Entry::getKey).collect(toList());
        }

        @Override
        protected final Collection<Unit> getUnits() {
            Set<Unit> result = new HashSet<>();
            result.addAll(this.forwardEdges.keySet());
            this.forwardEdges.values().forEach(result::addAll);
            return result;
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

        public final Unit n1 = j.newGotoStmt((Unit) null);
        public final Unit n2 = j.newGotoStmt((Unit) null);
        public final Unit nIf = j.newIfStmt(j.newEqExpr(IntConstant.v(0), IntConstant.v(0)), n1);

        public LoopWhereIfIsExitNode() {
            ((GotoStmt) n1).setTarget(nIf);
            ((GotoStmt) n2).setTarget(nIf);
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


    private Pair<Unit, Set<Unit>> makeEdge(Unit s) {
        return Pair.of(s, emptySet());
    }

    private Pair<Unit, Set<Unit>> makeEdge(Unit s, Unit... succs) {
        return Pair.of(s, new HashSet<>(asList(succs)));
    }

    @SafeVarargs
    private final Map<Unit, Set<Unit>> makeEdges(Pair<Unit, Set<Unit>> firstEntry, Pair<Unit, Set<Unit>>... restEntries) {
        return Stream.concat(Stream.of(firstEntry), Stream.of(restEntries)).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }


    public class MultipleReturns extends AdHocForwardUnitGraph {
        public final Unit returnX = j.newReturnStmt(localX);
        public final Unit returnZ = j.newReturnStmt(localZ);
        public final Unit ifY = j.newIfStmt(j.newEqExpr(localY, localY), returnX);
        public final Unit ifO = j.newIfStmt(j.newEqExpr(localO, localO), ifY);
        public final Unit returnZero = j.newReturnStmt(IntConstant.v(0));

        public MultipleReturns() {
            setForwardEdges(
                    makeEdges(
                            makeEdge(ifO, ifY, returnZ),
                            makeEdge(ifY, returnX, returnZero)
                    ));
        }
    }
}
