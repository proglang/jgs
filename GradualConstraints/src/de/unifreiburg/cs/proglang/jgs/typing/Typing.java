package de.unifreiburg.cs.proglang.jgs.typing;

import java.util.Collections;
import java.util.Set;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.Transition;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.util.NotImplemented;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Stmt;

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
    final public TypeDomain<LevelT> types;

    public Typing(CTypeDomain<LevelT> ctypes, TypeDomain<LevelT> types) {
        super();
        this.ctypes = ctypes;
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
    public class Result {
        private final Set<CTypeDomain<LevelT>.CType> constraints;
        private final Transition transition;

        public Result(Set<CTypeDomain<LevelT>.CType> constraints,
                Transition transition) {
            super();
            this.constraints = constraints;
            this.transition = transition;
        }
        
        public Set<CTypeDomain<LevelT>.CType> getConstraints() {
            return Collections.unmodifiableSet(this.constraints);
        }

        public Transition getTransition() {
            return transition;
        }
        
    }

    /**
     * A statement switch that generates typing constraints.
     * 
     * @author Luminous Fennell
     *
     */
    public class Gen extends AbstractStmtSwitch {
        

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
