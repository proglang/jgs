package visitor;

import java.util.List;
import java.util.logging.Logger;

import logging.L1Logger;
import analyzer.level1.JimpleInjector;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.Constant;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FieldRef;
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
	
	AnnotationValueSwitch valueSwitch = new AnnotationValueSwitch();
	Logger logger = L1Logger.getLogger();

	@Override
	public void caseBreakpointStmt(BreakpointStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseInvokeStmt(InvokeStmt stmt) {
		logger.info("Invoke Statement identified");
		// es hat
		System.out.println(stmt.getUseAndDefBoxes());
		System.out.println(stmt.getInvokeExpr());
		System.out.println(stmt.getInvokeExpr().getArgs());
		List<Value> list = stmt.getInvokeExpr().getArgs();
		for (Value e : list) {
			System.out.println(e.getType());
		}
	}

	@Override
	public void caseAssignStmt(AssignStmt stmt) {
		Unit unit = stmt;
		System.out.println(unit.getDefBoxes().toString());
		
		Value leftValue = unit.getDefBoxes().get(0).getValue();


		System.out.println(unit.getUseBoxes().toString());
		

		int numOfArgs = unit.getUseBoxes().size();
		
		if(leftValue instanceof Local) {
			Local left = (Local) leftValue;

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
	}

	@Override
	public void caseIdentityStmt(IdentityStmt stmt) {
		// TODO hier sind Parameter und this-Referenzen
		System.out.println("Identity Stmt: "+ stmt.getUseBoxes().toString());	
		System.out.println(stmt.getRightOp().getType());
		stmt.getRightOp().apply(valueSwitch);
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
		System.out.println(stmt.getUseBoxes().toString());
		Value val = stmt.getUseBoxes().get(0).getValue();
		if (val instanceof Constant) {
			JimpleInjector.returnConstant();
		} else if (val instanceof Local) {
			JimpleInjector.returnLocal((Local) val);
		}

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
