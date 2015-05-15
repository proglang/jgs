package visitor;

import analyzer.level1.JimpleInjector;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
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
		Unit unit = stmt;
		System.out.println(unit.getDefBoxes().toString());
		
		Value leftValue = unit.getDefBoxes().get(0).getValue();
		Local left = (Local) leftValue;
		

		System.out.println(unit.getUseBoxes().toString());
		

		int numOfArgs = unit.getUseBoxes().size();
		


			if (numOfArgs == 1) {
				Value rightValue = unit.getUseBoxes().get(0).getValue();

				
				if (rightValue instanceof Local) {
			    System.out.println("Local on the right side");

			    Local right = (Local) rightValue;
			    JimpleInjector.assignLocalToLocal(left, right, stmt);
				} else {
					JimpleInjector.assignConstantToLocal(left, stmt); 
				    // TODO: kann man hier was mit dem ValueSwitch machen? 
					// ZB rausfinden, welchen Typ die Argumente habe
					} 
			} else if (numOfArgs == 3) {
				System.out.println("2 Locals on the right side");
				

				Value rightValue1 = unit.getUseBoxes().get(0).getValue();
				Local right1 = (Local) rightValue1;
				
				Value rightValue2 = unit.getUseBoxes().get(1).getValue();
				Local right2 = (Local) rightValue2;
				
				JimpleInjector.assignLocalsToLocal(left, right1, right2, stmt);
			}
			

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
