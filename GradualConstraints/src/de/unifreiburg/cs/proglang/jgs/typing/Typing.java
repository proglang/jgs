package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.CTypeDomain.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSetFactory;
import de.unifreiburg.cs.proglang.jgs.constraints.Transition;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.util.NotImplemented;
import soot.Local;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Stmt;

import de.unifreiburg.cs.proglang.jgs.constraints.Transitions;

/**
 * A context for typing statements.
 * 
 * @author fennell
 *
 * @param <LevelT>
 *            The type of security levels.
 */
public class Typing<LevelT> {

    final public CTypeDomain<LevelT> ctypes;
    final public ConstraintSetFactory<LevelT> csets;
    final public TypeDomain<LevelT> types;

    public Typing(CTypeDomain<LevelT> ctypes, ConstraintSetFactory<LevelT> csets, TypeDomain<LevelT> types) {
        super();
        this.ctypes = ctypes;
        this.csets = csets;
        this.types = types;
    }

    // TODO: how to deal with type errors?
    public Result generate(Stmt s) {
        Gen g = new Gen();
        s.apply(g);
        return g.getResult();
    }

    /**
     * The result of a typing derivation: a set of constraints and an "environment transition".
     * @author fennell
     *
     */
    public static class Result<LevelT> {
        private final ConstraintSet<LevelT> constraints;
        private final Transition transition;

        Result(ConstraintSet<LevelT> constraints,
                Transition transition) {
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

        public CType<Level> initialTypeVariableOf(Local local) {
            return this.transition.getInit().get(local);
        }

        public CType<Level> finalTypeVariableOf(Local local) {
            return this.transition.getFinal().get(local);
        }

        
    }

    private Result makeResult(ConstraintSet<LevelT> constraints,
                Transition transition) {
        return new Result(constraints, transition);
    }
    
    private Result makeResult() {
        return new Result(csets.empty(), Transitions.makeId(Environments.makeEmpty()));
    }


    /**
     * A statement switch that generates typing constraints.
     * 
     * @author Luminous Fennell
     *
     */
    public class Gen extends AbstractStmtSwitch {
        
        private Result result;

        @Override
        public void caseAssignStmt(AssignStmt stmt) {
            // TODO Auto-generated method stub
            super.caseAssignStmt(stmt);
        }

        @Override
        public void caseIdentityStmt(IdentityStmt stmt) {
            // TODO Auto-generated method stub
            super.caseIdentityStmt(stmt);
        }

        @Override
        public void defaultCase(Object obj) {
            Stmt s = (Stmt) obj;
            throw new NotImplemented(String.format("Constraints generation for statement %s",
                                                   s.toString()));
        }

        @Override
        public Result getResult() {
            throw new NotImplemented("Gen.getResult");
        }
        


    }

}
