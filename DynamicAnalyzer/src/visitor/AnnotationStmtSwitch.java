package visitor;

import analyzer.level1.JimpleInjector;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NopStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;

public class AnnotationStmtSwitch implements StmtSwitch {

	@Override
	public void caseBreakpointStmt(BreakpointStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseInvokeStmt(InvokeStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseAssignStmt(AssignStmt stmt) {
		// TODO: Unterscheiden, was das Ziel ist und was die Quelle.
		// Ziel: Field/Local
		// Quelle: Expr, Local, Field, Constant
		Unit unit = stmt;
		JimpleInjector.invokeHandleStmtUnit(unit, stmt.getDefBoxes(), stmt.getUseBoxes());

	}

	@Override
	public void caseIdentityStmt(IdentityStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseGotoStmt(GotoStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseIfStmt(IfStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseNopStmt(NopStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseRetStmt(RetStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseReturnStmt(ReturnStmt stmt) {
		// TODO Update return Level in Local Map

	}

	@Override
	public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseTableSwitchStmt(TableSwitchStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseThrowStmt(ThrowStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void defaultCase(Object obj) {
		// TODO Auto-generated method stub

	}

}
