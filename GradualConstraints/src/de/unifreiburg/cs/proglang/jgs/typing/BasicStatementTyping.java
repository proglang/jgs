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
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.RhsSwitch;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import soot.SootMethod;
import soot.Value;
import soot.jimple.*;

import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.variable;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

/**
 * A context for typing basic statements (assignments, method calls, instantiations... everything except branching and sequencing).
 *
 * @param <LevelT> The type of security levels.
 * @author fennell
 */
public class BasicStatementTyping<LevelT> {

    final public ConstraintSetFactory<LevelT> csets;
    final public Constraints<LevelT> cstrs;
    final public TypeDomain<LevelT> types;
    final public TypeVars tvars;

    public BasicStatementTyping(ConstraintSetFactory<LevelT> csets,
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
                                   Set<TypeVar> pc,
                                   SignatureTable<LevelT> signatures,
                                   Casts<LevelT> casts) throws TypeError {
        return generate(s, env, pc, signatures, casts, Optional.<TypeVar>empty());
    }
    public Result<LevelT> generate(Stmt s,
                                   Environment env,
                                   Set<TypeVar> pc,
                                   SignatureTable<LevelT> signatures,
                                   Casts<LevelT> casts,
                                   Optional<TypeVar> newPc) throws TypeError {
        Gen g = new Gen(env, pc, signatures, casts, newPc);
        s.apply(g);
        // abort on any errors
        if (!g.getErrorMsg().isEmpty()) {
            throw new TypeError("There where errors during statement typing: \n"
                    + g.getErrorMsg()
                    .stream()
                    .reduce((s1, s2) -> s1 + "\n" + s2));
        }
        return g.getResult();
    }

    private Result<LevelT> makeResult(ConstraintSet<LevelT> constraints,
                                      Environment env,
                                      Effects<LevelT> effects) {
        return new Result<>(constraints, effects, env);
    }

    //

    /**
     * A statement switch that generates typing constraints.
     *
     * @author Luminous Fennell
     */
    public class Gen extends AbstractStmtSwitch {

        private final Environment env;
        private final Set<TypeVar> pcs;
        private final SignatureTable<LevelT> signatures;
        private final Casts<LevelT> casts;
        private final Optional<TypeVar> newPc;

        public List<String> getErrorMsg() {
            return unmodifiableList(errorMsg);
        }

        private final ArrayList<String> errorMsg;

        public Gen(Environment env,
                   Set<TypeVar> pc,
                   SignatureTable signatures,
                   Casts<LevelT> casts, Optional<TypeVar> newPc) {
            super();
            this.env = env;
            this.pcs = pc;
            this.signatures = signatures;
            this.casts = casts;
            this.errorMsg = new ArrayList<>();
            this.newPc = newPc;
        }

        private Result<LevelT> result;

        private Effects<LevelT> extractEffects(Value rhs) {
            RhsSwitch<LevelT> effectCases = new RhsSwitch<LevelT>(casts) {
                @Override
                public void caseLocalExpr(Collection<Value> atoms) {
                    setResult(emptyEffect());
                }

                @Override
                public void caseCall(SootMethod m,
                                     Optional<Var<?>> thisPtr,
                                     List<Var<?>> args) {
                    setResult(getSignature(m).effects);

                }

                @Override
                public void caseGetField(FieldRef field,
                                         Optional<Var<?>> thisPtr) {
                    setResult(emptyEffect());
                }

                @Override
                public void caseCast(Casts.Cast<LevelT> cast) {
                    setResult(emptyEffect());
                }

            };
            rhs.apply(effectCases);
            return (Effects<LevelT>) effectCases.getResult();
        }

        private Signature<LevelT> getSignature(SootMethod m) {
            return signatures.get(m)
                    .orElseThrow(() -> new TypingAssertionFailure(
                            "No signature found for method "
                                    + m.toString()));
        }

        @Override
        public void caseAssignStmt(AssignStmt stmt) {
            // get the variables that we write to
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


            //constraints and errors
            Stream.Builder<Constraint<LevelT>> constraints = Stream.builder();

            // Type variable (and constraint-type) for destinations
            TypeVar destTVar = tvars.fresh(writeVar.toString());
            CType<LevelT> destCType = variable(destTVar);

            // Utility functions
            Function<CType<LevelT>, Constraint<LevelT>> leDest =
                    ct -> cstrs.le(ct, destCType);
            Function<Var<?>, CType<LevelT>> toCType =
                    v -> variable(env.get(v));

            // get reads from lhs.. they are definitively flowing into the destination
            Var.getAllFromValueBoxes(stmt.getLeftOp().getUseBoxes())
                    .map(toCType.andThen(leDest))
                    .forEach(constraints);

            // get constraints from rhs..
            Value rhs = stmt.getRightOp();
            rhs.apply(new RhsSwitch<LevelT>(casts) {

                // for local expressions all use boxes flow into the destination
                @Override
                public void caseLocalExpr(Collection<Value> atoms) {
                    Var.getAllFromValues(atoms)
                            .map(toCType.andThen(leDest))
                            .forEach(constraints);
                }

                /* for method calls:
                   - [ ] add return as lower bound to dest
                 */
                @Override
                public void caseCall(SootMethod m,
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

                    // - [x] add effect as upper bound to each pcs
                    sig.effects.stream().forEach(t -> {
                        pcs.forEach(pc -> {
                            constraints.add(cstrs.le(variable(pc),
                                    CTypes.literal(t)));
                        });
                    });
                }

                @Override
                public void caseGetField(FieldRef field,
                                         Optional<Var<?>> thisPtr) {
                    throw new RuntimeException("NOT IMPLEMENTED");
                }

                @Override
                public void caseCast(Casts.Cast<LevelT> cast) {
                    if (!compatible(cast.sourceType, cast.destType)) {
                        errorMsg.add(String.format(
                                "Source type %s cannot be converted to destination type %s.",
                                cast.sourceType,
                                cast.destType));
                        return;
                    }
                    asList(cstrs.le(toCType.apply(cast.value),
                                    CTypes.literal(cast.sourceType)),
                            cstrs.le(CTypes.literal(cast.destType),
                                    destCType)).forEach(constraints);
                }

            });

            // finally the pcs flow into the destination
            this.pcs.forEach(pc -> {
                constraints.add(leDest.apply(variable(pc)));
            });

            // transition (for now only local assignments)
            Environment fin = env.add(writeVar, destTVar);

            // .. and the result
            this.result = makeResult(csets.fromCollection(constraints.build()
                            .collect(
                                    toList())),
                    fin,
                    extractEffects(rhs));
        }


        private boolean compatible(TypeDomain.Type<LevelT> sourceType,
                                   TypeDomain.Type<LevelT> destType) {
            return types.dyn().equals(destType) ^ types.dyn()
                    .equals(sourceType);
        }

        @Override
        public void caseIdentityStmt(IdentityStmt stmt) {
            super.caseIdentityStmt(stmt);
        }

        @Override
        public void caseIfStmt(IfStmt stmt) {

            Stream.Builder<Constraint<LevelT>> cs = Stream.builder();

            if (!newPc.isPresent()) {
                throw new RuntimeException("If case without assigning a new pc");
            }

            CType<LevelT> nPc = CTypes.variable(newPc.get());
            // add new pc as upper bound to old pc
            this.pcs.forEach(pc -> {
                cs.add(cstrs.le(variable(pc), nPc));
            });

            Var.getAllFromValueBoxes(stmt.getUseBoxes()).forEach(v ->
                    cs.add(Constraints.le(variable(env.get(v)), nPc)));

            ConstraintSet<LevelT> cset = csets.fromCollection(cs.build().collect(toList()));
            Result<LevelT> result = makeResult(cset, env, MethodSignatures.<LevelT>emptyEffect());

            this.result = result;
        }

        private Result<LevelT> trivialCase() {
            return makeResult(csets.empty(), env, emptyEffect());
        }

        @Override
        public void caseNopStmt(NopStmt stmt) {
            this.result = trivialCase();
        }

        @Override
        public void caseGotoStmt(GotoStmt stmt) {
            // nothing interesting here
            this.result = trivialCase();
        }

        @Override
        public void defaultCase(Object obj) {
            throw new RuntimeException("Case not implemented!");
        }

        @Override
        public Result<LevelT> getResult() {
            return result;
        }

    }

}
