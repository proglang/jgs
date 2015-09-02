package de.unifreiburg.cs.proglang.jgs.constraints;

import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Stmt;

/**
 * A statement switch that generates typing constraints.
 * 
 * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
 *
 */
public class Generate extends AbstractStmtSwitch {

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
        throw new RuntimeException(String.format("Constraints generation not implementented for statement %s",
                                                 s.toString()));
    }

}
