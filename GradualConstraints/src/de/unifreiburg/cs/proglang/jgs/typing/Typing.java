package de.unifreiburg.cs.proglang.jgs.typing;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes;
import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.Constraint;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSetFactory;
import de.unifreiburg.cs.proglang.jgs.constraints.Constraints;
import de.unifreiburg.cs.proglang.jgs.constraints.Transition;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.RhsSwitch;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import soot.*;
import soot.jimple.*;
import soot.util.Switch;

import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import static java.util.stream.Collectors.toList;

/**
 * A context for typing statements.
 *
 * @param <LevelT> The type of security levels.
 * @author fennell
 */
public class Typing<LevelT> {

    final public ConstraintSetFactory<LevelT> csets;
    final public Constraints<LevelT> cstrs;
    final public TypeDomain<LevelT> types;
    final public TypeVars tvars;

    public Typing(ConstraintSetFactory<LevelT> csets,
                  TypeDomain<LevelT> types,
                  TypeVars tvars,
                  Constraints<LevelT> cstrs) {
        super();
        this.csets = csets;
        this.types = types;
        this.tvars = tvars;
        this.cstrs = cstrs;
    }

    public Result<LevelT> generate(Stmt s,
                                   Environment env,
                                   TypeVar pc,
                                   SignatureTable<LevelT> signatures) throws
                                                                      TypeError {
        Gen g = new Gen(env, pc, signatures);
        s.apply(g);
        return g.getResult();
    }

    // TODO: is this really a type error? What is in the case where we have
    // unresolvable constraints.. how does this differ from TypeError?
    public static class TypeError extends Exception {

        private static final long serialVersionUID = 1L;

        public TypeError(String arg0) {
            super(arg0);
        }

    }

    /**
     * The result of a typing derivation: a set of constraints and an
     * "environment transition".
     *
     * @author fennell
     */
    public static class Result<LevelT> {
        private final ConstraintSet<LevelT> constraints;
        private final Effects<LevelT> effects;
        private final Transition transition;

        Result(ConstraintSet<LevelT> constraints,
               Effects<LevelT> effects,
               Transition transition) {
            super();
            this.constraints = constraints;
            this.effects = effects;
            this.transition = transition;
        }

        public ConstraintSet<LevelT> getConstraints() {
            return this.constraints;
        }

        public Transition getTransition() {
            return transition;
        }

        public TypeVar initialTypeVariableOf(Var<?> local) {
            return this.transition.getInit().get(local);
        }

        public TypeVar finalTypeVariableOf(Var<?> local) {
            return this.transition.getFinal().get(local);
        }

    }

    private Result<LevelT> makeResult(ConstraintSet<LevelT> constraints,
                                      Transition transition,
                                      Effects<LevelT> effects) {
        return new Result<>(constraints, effects, transition);
    }

    private Result<LevelT> makeResult() {
        return new Result<>(csets.empty(),
                            emptyEffect(),
                            Transition.makeId(Environments.makeEmpty()));
    }

    /**
     * A statement switch that generates typing constraints.
     *
     * @author Luminous Fennell
     */
    public class Gen extends AbstractStmtSwitch {

        private final Environment env;
        private final TypeVar pc;
        private final SignatureTable<LevelT> signatures;

        public Gen(Environment env, TypeVar pc, SignatureTable signatures) {
            super();
            this.env = env;
            this.pc = pc;
            this.signatures = signatures;
        }

        private Result<LevelT> result;

        private Effects<LevelT> extractEffects(Value rhs) {
            RhsSwitch effectCases = new RhsSwitch() {
                @Override public void caseLocalExpr(Collection<Value> atoms) {
                    setResult(emptyEffect());
                }

                @Override public void caseCall(SootMethod m,
                                               Optional<Var<?>> thisPtr,
                                               List<Var<?>> args) {
                    setResult(getSignature(m).effects);

                }

                @Override public void caseGetField(FieldRef field,
                                                   Optional<Var<?>> thisPtr) {
                    setResult(emptyEffect());
                }
            };
            return (Effects<LevelT>) effectCases.getResult();
        }

        private Signature<LevelT> getSignature(SootMethod m) {
            return signatures.get(m)
                             .orElseThrow(() -> new TypingAssertionFailure(
                                     "No signature found for method "
                                     + m.toString()));
        }

        @Override public void caseAssignStmt(AssignStmt stmt) {

            //constraints and effects
            Stream.Builder<Constraint<LevelT>> constraints = Stream.builder();

            // Type variable (and constraint-type) for destinations
            TypeVar destTVar = tvars.fresh();
            CType<LevelT> destCType = CTypes.variable(destTVar);

            // Utility functions
            Function<CType<LevelT>, Constraint<LevelT>> leDest =
                    ct -> cstrs.le(ct, destCType);
            Function<Var<?>, CType<LevelT>> toCType =
                    v -> CTypes.variable(env.get(v));

            // get reads from lhs.. they are definitively flowing into the destination
            Var.getAllFromValueBoxes(stmt.getLeftOp().getUseBoxes())
               .map(toCType.andThen(leDest))
               .forEach(constraints);

            // get constraints from rhs..
            Value rhs = stmt.getRightOp();
            rhs.apply(new RhsSwitch() {

                // for local expressions all use boxes flow into the destination
                @Override public void caseLocalExpr(Collection<Value> atoms) {
                    Var.getAllFromValues(atoms)
                       .map(toCType.andThen(leDest))
                       .forEach(constraints);
                }

                /* for method calls:
                   - [ ] add return as lower bound to dest
                 */
                @Override public void caseCall(SootMethod m,
                                               Optional<Var<?>> thisPtr,
                                               List<Var<?>> args) {
                    // check parameter count
                    int argCount = args.size();
                    int paramterCount = m.getParameterCount();
                    if (argCount != paramterCount) {
                        throw new TypingAssertionFailure(String.format(
                                "Argument count (%d) does not "
                                + "equal parameter count (%d): %s",
                                argCount,
                                paramterCount,
                                m.toString()));
                    }

                    // Get signature, if possible
                    Signature<LevelT> sig = getSignature(m);

                    // - [x] instantiate the signature with the parameters and destination variable and add corresponding constraints
                    Map<Symbol<LevelT>, TypeVar> instantiation =
                            new HashMap<>();
                    List<TypeVar> argTypes =
                            args.stream().map(env::get).collect(toList());
                    IntStream.range(0, argCount).forEach(i -> {
                        TypeVar at = argTypes.get(i);
                        soot.Type pt = m.getParameterType(i);
                        instantiation.put(Symbol.param(pt, i), at);
                    }); // <- params
                    instantiation.put(Symbol.ret(), destTVar); // <- return
                    sig.constraints.toTypingConstraints(instantiation)
                                   .forEach(constraints);

                    // - [x] add thisPtr as lower bound to dest
                    thisPtr.map(toCType.andThen(leDest)).ifPresent(constraints);

                    // - [x] add effect as upper bound to pc
                    sig.effects.stream().forEach(t -> {
                        constraints.add(cstrs.le(CTypes.variable(pc),
                                                 CTypes.literal(t)));
                    });
                }

                @Override public void caseGetField(FieldRef field,
                                                   Optional<Var<?>> thisPtr) {
                    throw new RuntimeException("NOT IMPLEMENTED");
                }
            });

            // finally the pc flows into the destination
            constraints.add(leDest.apply(CTypes.variable(this.pc)));

            // transition (for now only local assignments)
            List<Var<?>> writeVars =
                    Var.getAllFromValueBoxes(stmt.getDefBoxes())
                       .collect(toList());
            if (writeVars.size() != 1) {
                throw new TypingAssertionFailure(String.format(
                        "Assignment should have "
                        + "exactly one destination variable. "
                        + "Found: %s. Statement was: %s.",
                        writeVars.toString(),
                        stmt.toString()));
            }
            Var<?> writeVar = writeVars.get(0); // cannot fail now
            Environment fin = env.add(writeVar, destTVar);
            Transition transition = Transition.makeAtom(env, fin);

            // .. and the result
            this.result = makeResult(csets.fromCollection(constraints.build()
                                                                     .collect(
                                                                             toList())),
                                     transition,
                                     extractEffects(rhs));
        }

        @Override public void caseIdentityStmt(IdentityStmt stmt) {
            // TODO Auto-generated method stub
            super.caseIdentityStmt(stmt);
        }

        @Override public Result<LevelT> getResult() {
            return result;
        }

    }

}
