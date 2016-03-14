package de.unifreiburg.cs.proglang.jgs.typing;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVarTags.TypeVarTag;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.CastsFromMapping;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.RhsSwitch;
import de.unifreiburg.cs.proglang.jgs.signatures.FieldTable;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import soot.*;
import soot.jimple.*;
import soot.toolkits.scalar.LocalDefs;
import sun.security.jca.GetInstance;

import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.literal;
import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.variable;
import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.*;
import static de.unifreiburg.cs.proglang.jgs.typing.BodyTypingResult.fromEnv;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

/**
 * A context for typing basic statements (assignments, method calls,
 * instantiations... everything except branching and sequencing).
 *
 * @param <LevelT> The type of security levels.
 * @author fennell
 */
public class BasicStatementTyping<LevelT> {

    final public ConstraintSetFactory<LevelT> csets;
    final public Constraints<LevelT> cstrs;
    final public TypeVars.MethodTypeVars tvars;


    final public SootMethod currentMethod;

    public BasicStatementTyping(ConstraintSetFactory<LevelT> csets,
                                TypeVars.MethodTypeVars tvars,
                                Constraints<LevelT> cstrs,
                                SootMethod currentMethod) {
        super();
        this.csets = csets;
        this.tvars = tvars;
        this.cstrs = cstrs;
        this.currentMethod = currentMethod;
    }

    public BodyTypingResult<LevelT> generate(Stmt s,
                                             LocalDefs localDefs,
                                             Environment env,
                                             Set<TypeVar> pc,
                                             SignatureTable<LevelT> signatures,
                                             FieldTable<LevelT> fields,
                                             Casts<LevelT> casts) throws TypingException {
        Gen g = new Gen(env, pc, signatures, fields, casts, localDefs);
        s.apply(g);
        // abort on any errors
        if (!g.getErrorMsg().isEmpty()) {
            throw new TypingException(
                    "There where errors during statement typing: \n"
                    + g.getErrorMsg()
                       .stream()
                       .reduce((s1, s2) -> s1 + "\n" + s2));
        }
        return g.getResult();
    }

    private BodyTypingResult<LevelT> makeResult(ConstraintSet<LevelT> constraints,
                                                Environment env,
                                                Effects<LevelT> effects,
                                                TagMap<LevelT> tags) {
        return new BodyTypingResult<>(constraints, effects, env, tags);
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
        private final FieldTable<LevelT> fields;
        private final Casts<LevelT> casts;
        private final LocalDefs localDefs;

        public List<String> getErrorMsg() {
            return unmodifiableList(errorMsg);
        }

        private final ArrayList<String> errorMsg;

        public Gen(Environment env,
                   Set<TypeVar> pc,
                   SignatureTable<LevelT> signatures,
                   FieldTable<LevelT> fields, Casts<LevelT> casts, LocalDefs localDefs) {
            super();
            this.env = env;
            this.pcs = pc;
            this.signatures = signatures;
            this.fields = fields;
            this.casts = casts;
            this.errorMsg = new ArrayList<>();
            this.localDefs = localDefs;
        }

        private BodyTypingResult<LevelT> result;

        private Effects<LevelT> extractEffects(Value rhs) {
            RhsSwitch<LevelT> effectCases = new RhsSwitch<LevelT>(casts) {
                @Override
                public void caseLocalExpr(Collection<Value> atoms) {
                    setResult(emptyEffect());
                }

                @Override
                public void caseCall(SootMethod m,
                                     Optional<Var<?>> thisPtr,
                                     List<Optional<Var<?>>> args) {
                    setResult(getSignature(m).effects);

                }

                @Override
                public void caseGetField(FieldRef field,
                                         Optional<Var<?>> thisPtr) {
                    setResult(emptyEffect());
                }

                @Override
                public void caseCast(Casts.ValueCast<LevelT> cast) {
                    setResult(emptyEffect());
                }

                @Override
                public void caseNew(RefType type) {
                    setResult(emptyEffect());
                }

                @Override
                public void caseConstant(Value v) {
                    setResult(emptyEffect());
                }

                @Override
                public void defaultCase(Object v) {
                    throw new RuntimeException(
                            "Effect extraction not implemented for case: "
                            + v.toString());
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

        private TypeDomain.Type<LevelT> getFieldType(SootField f) {

            return fields.get(f)
                         .orElseThrow(() ->
                                              new TypingAssertionFailure(
                                                      "No field type found for field "
                                                      + f.toString()));

        }

        private class ExprSwitch extends RhsSwitch<LevelT> {

            private final Function<CType<LevelT>, Constraint<LevelT>> leDest;
            private final Function<Var<?>, CType<LevelT>> toCType;
            private final Stream.Builder<Constraint<LevelT>> constraints;
            private final TypeVar destTVar;
            private final CType<LevelT> destCType;

            private TagMap<LevelT> tags = TagMap.empty();

            public ExprSwitch(Casts<LevelT> casts, Function<CType<LevelT>, Constraint<LevelT>> leDest, Function<Var<?>, CType<LevelT>> toCType, Stream.Builder<Constraint<LevelT>> constraints, TypeVar destTVar, CType<LevelT> destCType) {
                super(casts);
                this.leDest = leDest;
                this.toCType = toCType;
                this.constraints = constraints;
                this.destTVar = destTVar;
                this.destCType = destCType;
            }

            public TagMap<LevelT> getTags() {
                return tags;
            }


            @Override
            public void caseLocalExpr(Collection<Value> atoms) {
                Var.getAllFromValues(atoms)
                   .map(toCType.andThen(leDest))
                   .forEach(constraints);
            }

            /* for method calls:
               - [x] add return as lower bound to dest
             */
            @Override
            public void caseCall(SootMethod m,
                                 Optional<Var<?>> thisPtr,
                                 List<Optional<Var<?>>> args) {
                // check parameter count
                int argCount = args.size();
                int paramterCount = m.getParameterCount();
                if (argCount != paramterCount) {
                    throw new RuntimeException(String.format(
                            "Argument count (%d) does not "
                            + "equal parameter count (%d): %s",
                            argCount,
                            paramterCount,
                            m.toString()));
                }

                // Get signature, if possible
                Signature<LevelT> sig = getSignature(m);

                // - [x] instantiate the signature with the parameters and destination variable and add corresponding constraints
                Map<Symbol<LevelT>, CType<LevelT>> instantiation =
                        new HashMap<>();
                List<Optional<TypeVar>> argTypes =
                        args.stream().map(mv -> mv.map(env::get)).collect(toList());
                IntStream.range(0, argCount).forEach(i -> {
                    Optional<TypeVar> mat = argTypes.get(i);
                    mat.ifPresent(at -> {
                        instantiation.put(Symbol.param(i), variable(at));
                    });
                    if (!mat.isPresent()) {
                        instantiation.put(Symbol.param(i), literal(cstrs.types.pub()));
                    }
                }); // <- params

                instantiation.put(Symbol.ret(), variable(destTVar)); // <- return
                Map<Constraint<LevelT>, TypeVarTag> tagMap = new HashMap<>();
                sig.constraints.stream().forEach(sc -> {
                                                     Constraint<LevelT> c = sc.toTypingConstraint(instantiation);
                                                     Stream<TypeVarTag> params =
                                                             sc.symbols().filter(s -> s instanceof Symbol.Param)
                                                               .map(s -> new TypeVarTags.MethodArg(m, ((Symbol.Param<LevelT>) s).position));
                                                     Stream<TypeVarTag> rets =
                                                             sc.symbols().filter(s -> s instanceof Symbol.Return)
                                                               .map(s -> new TypeVarTags.MethodReturn(m));
                                                     constraints.add(c);
                                                     Stream.concat(params, rets).forEach(t -> tagMap.put(c, t));
                                                 }
                );

                tags = TagMap.of(tagMap);

                // - [x] add thisPtr as lower bound to dest
                thisPtr.map(toCType.andThen(leDest)).ifPresent(constraints);

                // - [x] add effect as upper bound to each pcs
                sig.effects.stream().forEach(t -> {
                    pcs.forEach(pc -> {
                        constraints.add(cstrs.le(variable(pc),
                                                 literal(t)));
                    });
                });
            }

            @Override
            public void caseGetField(FieldRef field,
                                     Optional<Var<?>> thisPtr) {

                Constraint<LevelT> destC =
                        leDest.apply(literal(getFieldType(field.getField())));
                tags =
                        TagMap.of(destC, new TypeVarTags.Field(field.getField()));
                Stream.concat(
                        Stream.of(destC),
                        thisPtr.isPresent()
                        ? Stream.of(leDest.apply(variable(env.get(thisPtr.get()))))
                        : Stream.empty()
                ).forEach(constraints);
            }

            @Override
            public void caseCast(Casts.ValueCast<LevelT> cast) {
                if (!compatible(cast.sourceType, cast.destType)) {
                    errorMsg.add(String.format(
                            "Source type %s cannot be converted to destination type %s.",
                            cast.sourceType,
                            cast.destType));
                    return;
                }
                // add source and dest type
                Optional<Constraint<LevelT>> mcstr = cast.value.map(
                        v -> cstrs.le(toCType.apply(v), literal(cast.sourceType)));

                mcstr.ifPresent(constraints);

                Constraint<LevelT> destC =
                        cstrs.le(literal(cast.destType), destCType);
                constraints.add(destC);

                CastsFromMapping.Conversion<LevelT> conv =
                        new CastsFromMapping.Conversion<LevelT>(cast.sourceType, cast.destType);
                TypeVarTag tag = new TypeVarTags.Cast(conv);
                tags = TagMap.of(destC,
                                 tag)
                             .addAll(mcstr.map((Constraint<LevelT> c) -> TagMap.of(c, tag)).orElse(TagMap.empty()));
            }

            @Override
            public void caseNew(RefType type) {
                noRestrictions();
            }

            @Override
            public void caseConstant(Value v) {
                // do nothing
            }

        }

        private void caseLocalDefinition(Local writeVar, DefinitionStmt stmt) {
            //constraints and errors
            Stream.Builder<Constraint<LevelT>> constraints = Stream.builder();

            // Type variable (and constraint-type) for destinations
            TypeVar destTVar = tvars.forLocal(Var.fromLocal(writeVar), stmt);
            CType<LevelT> destCType = variable(destTVar);

            // Utility functions
            Function<CType<LevelT>, Constraint<LevelT>> leDest =
                    (CType<LevelT> ct) -> cstrs.le(ct, destCType);
            Function<Var<?>, CType<LevelT>> toCType =
                    v -> variable(env.get(v));

            // get constraints from rhs..
            Value rhs = stmt.getRightOp();
            ExprSwitch sw =
                    new ExprSwitch(casts, leDest, toCType, constraints, destTVar, destCType);
            rhs.apply(sw);

            // finally the pcs flow into the destination
            this.pcs.forEach(pc -> {
                constraints.add(leDest.apply(variable(pc)));
            });

            // transition
            Environment fin = env.add(Var.fromLocal(writeVar), destTVar);

            // .. and the result
            this.result = makeResult(csets.fromCollection(constraints.build()
                                                                     .collect(
                                                                             toList())),
                                     fin,
                                     extractEffects(rhs),
                                     sw.getTags());

        }

        // TODO: shares quite a lot of code with caseLocalDefinition
        private void caseFieldDefinition(FieldRef fieldRef, DefinitionStmt stmt) {
            SootField field = fieldRef.getField();

            //constraints and errors
            Stream.Builder<Constraint<LevelT>> constraints = Stream.builder();

            TypeDomain.Type<LevelT> fieldType = getFieldType(field);
            Function<CType<LevelT>, Constraint<LevelT>> leDest =
                    c -> cstrs.le(c, CTypes.literal(fieldType));
            // get reads from lhs.. they are definitively flowing into the destination
            Var.getAllFromValueBoxes((Collection<ValueBox>) stmt.getLeftOp().getUseBoxes())
               .map((Var<?> v) -> Constraints.<LevelT>le(CTypes.variable(env.get(v)), CTypes.literal(fieldType)))
               .forEach(constraints);

            TagMap<LevelT> tags;
            // the right hand side should only be a local
            if ((stmt.getRightOp() instanceof Local)) {
                // .. and it flows into the field
                Local rhs = (Local) stmt.getRightOp();
                Constraint<LevelT> cstr =
                        leDest.apply(CTypes.variable(env.get(Var.fromLocal(rhs))));
                constraints.add(cstr);
                // add a tag for the field
                tags = TagMap.of(cstr, new TypeVarTags.Field(field));
            } else if (stmt.getRightOp() instanceof Constant) {
                // do nothing
                tags = TagMap.empty();
            } else {
                throw new TypingAssertionFailure(
                        "Only field updates of the form \"x.F = y\" of \"x.F = c\" are supported. Found "
                        + stmt.toString());
            }

            // also the context (pc) flows into the destination
            pcs.stream().forEach(v -> constraints.add(leDest.apply(CTypes.variable(v))));

            // it remains to define the effect
            Effects<LevelT> effects;
            if (
                // if in a constructor and updating a "this" field, we do not have an effect
                    currentMethod.getName().equals("<init>") && fieldRef instanceof InstanceFieldRef
                    ) {

                Value base = ((InstanceFieldRef) fieldRef).getBase();
                if (base instanceof ThisRef) {
                    effects = emptyEffect();
                } else if (base instanceof Local){
                    List<Unit> baseDefs = localDefs.getDefsOfAt((Local) base, stmt);
                    if (baseDefs.size() == 1) {
                        Stmt baseDef = (Stmt)baseDefs.get(0);
                        if (baseDef instanceof IdentityStmt && ((IdentityStmt)baseDef).getRightOp() instanceof ThisRef) {
                           effects = emptyEffect();
                        } else {
                            effects =
                                    MethodSignatures.<LevelT>emptyEffect().add(fieldType);
                        }
                    } else {
                        effects =
                                MethodSignatures.<LevelT>emptyEffect().add(fieldType);
                    }
                } else {
                    // regular case
                    effects = MethodSignatures.<LevelT>emptyEffect().add(fieldType);
                }


            } else if(
                //  or we are in a static initializer that writes to a static field of it's class
                    currentMethod.getName().equals("<clinit>")
                    && fieldRef instanceof StaticFieldRef
                    && field.getDeclaringClass().equals(currentMethod.getDeclaringClass())
                    ) {
                effects = MethodSignatures.<LevelT>emptyEffect();
            } else {
                // regular case
                effects = MethodSignatures.<LevelT>emptyEffect().add(fieldType);
            }

            this.result =
                    makeResult(csets.fromCollection(constraints.build().collect(toList())), env, effects, tags);
        }


        private void caseDefinitionStmt(DefinitionStmt stmt) {
            Value lhs = stmt.getLeftOp();
            if (lhs instanceof Local) {
                caseLocalDefinition(((Local) lhs), stmt);
            } else if (lhs instanceof FieldRef) {
                caseFieldDefinition(((FieldRef) lhs), stmt);
            } else {
                throw new TypingAssertionFailure(
                        "Extracting write locations for statement "
                        + stmt.toString() + " is not implemented!");
            }
        }

        @Override
        public void caseAssignStmt(AssignStmt stmt) {
            caseDefinitionStmt(stmt);
        }


        private boolean compatible(TypeDomain.Type<LevelT> sourceType,
                                   TypeDomain.Type<LevelT> destType) {
            return cstrs.types.dyn().equals(destType) ^ cstrs.types.dyn()
                                                                   .equals(sourceType);
        }

        private void noRestrictions() {
            this.result = fromEnv(csets, env);
        }

        @Override
        public void caseIdentityStmt(IdentityStmt stmt) {
            this.caseDefinitionStmt(stmt);
        }


        @Override
        public void caseNopStmt(NopStmt stmt) {
            noRestrictions();
        }


        @Override
        public void caseGotoStmt(GotoStmt stmt) {
            noRestrictions();
        }

        @Override
        public void caseReturnStmt(ReturnStmt stmt) {
            if (stmt.getOp() instanceof Local) {
                Var<?> r = Var.fromLocal((Local) stmt.getOp());
                this.result = makeResult(csets.fromCollection(
                        Stream.concat(
                                Stream.of(Constraints.<LevelT>le(variable(env.get(r)), variable(tvars.ret()))),
                                this.pcs.stream().map(pcVar -> Constraints.<LevelT>le(variable(pcVar), variable(tvars.ret()))))
                              .collect(Collectors.<Constraint<LevelT>>toList()))
                        , env, emptyEffect(), TagMap.empty());
            } else if (stmt.getOp() instanceof Constant) {
                noRestrictions();
            } else {
                throw new RuntimeException("Did not expect to return a "
                                           + stmt.getOp().getClass());
            }
        }

        @Override
        public void caseInvokeStmt(InvokeStmt stmt) {
            Value e = stmt.getInvokeExpr();
            Stream.Builder<Constraint<LevelT>> constraints = Stream.builder();

            // TODO: refactor the expression switch s.t. this dummy is no longer required
            Local dummy = Jimple.v().newLocal("DUMMY_FOR_INVOKE_STMT", null);
            TypeVar destTVar = tvars.forLocal(Var.fromLocal(dummy), stmt);
            CType<LevelT> destCType = variable(destTVar);
            Function<CType<LevelT>, Constraint<LevelT>> leDest =
                    t -> cstrs.le(t, destCType);
            Function<Var<?>, CType<LevelT>> toCType = v -> variable(env.get(v));
            ExprSwitch sw =
                    new ExprSwitch(casts, leDest, toCType, constraints, destTVar
                            , destCType);
            e.apply(sw);

            this.result =
                    makeResult(csets.fromCollection(constraints.build().collect(toList())), env, extractEffects(e), sw.getTags());
        }

        @Override
        public void caseIfStmt(IfStmt stmt) {
            // note that this is the case where an if statement only has a single successor. I.e. it degenerates to a noop.
            noRestrictions();
        }

        @Override
        public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
            noRestrictions();
        }

        @Override
        public void defaultCase(Object obj) {
            throw new RuntimeException("Case not implemented: " + obj);
        }

        @Override
        public BodyTypingResult<LevelT> getResult() {
            return result;
        }

    }

}
