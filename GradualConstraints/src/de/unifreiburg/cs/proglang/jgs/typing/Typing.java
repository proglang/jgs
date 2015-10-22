package de.unifreiburg.cs.proglang.jgs.typing;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.util.NotImplemented;
import soot.Local;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Value;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EqExpr;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.JimpleValueSwitch;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.ParameterRef;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.ThisRef;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;

/**
 * A context for typing statements.
 * 
 * @author fennell
 *
 * @param <LevelT>
 *            The type of security levels.
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
                                   TypeVar pc) throws TypeError {
        Gen g = new Gen(env, pc);
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
     *
     */
    public static class Result<LevelT> {
        private final ConstraintSet<LevelT> constraints;
        private final Transition transition;

        Result(ConstraintSet<LevelT> constraints, Transition transition) {
            super();
            this.constraints = constraints;
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
                                      Transition transition) {
        return new Result<>(constraints, transition);
    }

    private Result<LevelT> makeResult() {
        return new Result<>(csets.empty(),
                            Transition.makeId(Environments.makeEmpty()));
    }

    /**
     * A statement switch that generates typing constraints.
     * 
     * @author Luminous Fennell
     *
     */
    public class Gen extends AbstractStmtSwitch {

        private final Environment env;
        private final TypeVar pc;

        public Gen(Environment env, TypeVar pc) {
            super();
            this.env = env;
            this.pc = pc;
        }

        private Result<LevelT> result;

        @Override
        public void caseAssignStmt(AssignStmt stmt) {

            // constraints (for now only local assignments)
            Set<Constraint<LevelT>> constraints = new HashSet<>();
            List<Var<?>> readVars = Var.getAll(stmt.getUseBoxes());

            TypeVar destTVar = tvars.fresh();
            CType<LevelT> destCType = CTypes.variable(destTVar);
            constraints.addAll(readVars.stream().map(v -> {
                CType<LevelT> tv = CTypes.variable(env.get(v));
                return cstrs.le(tv, destCType);
            }).collect(Collectors.toSet()));
            constraints.add(cstrs.le(CTypes.variable(this.pc), destCType));

            // transition (for now only local assignments)
            List<Var<?>> writeVars = Var.getAll(stmt.getDefBoxes());
            if (writeVars.size() != 1) {
                throw new TypingAssertionFailure(String.format("Assignment should have "
                                                               + "exactly one destination variable. "
                                                               + "Found: %s. Statement was: %s.",
                                                               writeVars.toString(),
                                                               stmt.toString()));
            }
            Var<?> writeVar = writeVars.get(0); // cannot fail now
            Environment fin = env.add(writeVar, destTVar);
            Transition transition = Transition.makeAtom(env, fin);

            // .. and the result
            this.result =
                makeResult(csets.fromCollection(constraints), transition);
        }

        @Override
        public void caseIdentityStmt(IdentityStmt stmt) {
            // TODO Auto-generated method stub
            super.caseIdentityStmt(stmt);
        }

        @Override
        public Result<LevelT> getResult() {
            return result;
        }

    }

}
