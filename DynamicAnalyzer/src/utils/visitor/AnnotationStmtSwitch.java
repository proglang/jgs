package utils.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import utils.exceptions.InternalAnalyzerException;
import utils.logging.L1Logger;
import utils.visitor.AnnotationValueSwitch.StmtContext;
import analyzer.level1.JimpleInjector;
import soot.Local;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.Constant;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeExpr;
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

	
	ArrayList<Value> DefBox = new ArrayList<Value>();
	

	@Override
	public void caseBreakpointStmt(BreakpointStmt stmt) {
		logger.severe("\n > > > Breakpoint statement identified < < <"); // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseInvokeStmt(InvokeStmt stmt) {
		
		InvokeStmt iStmt = stmt;
		valueSwitch.actualContext = StmtContext.INVOKE;
		
		logger.fine("\n > > > Invoke Statement identified < < <");
		
		InvokeExpr invokeExpr = iStmt.getInvokeExpr();
		
		invokeExpr.apply(valueSwitch);

		// TODO das ist eher interessant bei AssignStmt
		logger.finer("Method has return type: " + invokeExpr.getType());
		
		valueSwitch.actualContext = StmtContext.UNDEF;
	}

	@Override
	public void caseAssignStmt(AssignStmt stmt) {

		AssignStmt aStmt = stmt;
		valueSwitch.callingStmt = aStmt;
		
		valueSwitch.actualContext = StmtContext.ASSIGNRIGHT;
		
		
		if (aStmt.getDefBoxes().size() != 1) {
			new InternalAnalyzerException("Unexpected number of elements on the left "
					+ "side of assign statement");
		}
		
		logger.fine("\n > > > ASSIGN STATEMENT identified < < <" );
		logger.finer(" > > > left side: " + aStmt.getDefBoxes().toString() + " < < <");
		logger.finer(" > > > right side: " + aStmt.getUseBoxes().toString() + " < < <");

		for (int i = 0; i < aStmt.getUseBoxes().size(); i++) {
			aStmt.getUseBoxes().get(i).getValue().apply(valueSwitch);
		}
		
		valueSwitch.actualContext = StmtContext.ASSIGNLEFT;
		
		switch(valueSwitch.rightElement) {
			case NOT: break;
			case NEW_ARRAY: 
				System.out.println(stmt.getLeftOp());
				JimpleInjector.addArrayToObjectMap((Local) stmt.getLeftOp(), stmt);
				break;
			case NEW_UNDEF_OBJECT: break;
			default: new InternalAnalyzerException("Unexpected newExprContext");
		}
		

		aStmt.getDefBoxes().get(0).getValue().apply(valueSwitch);

		valueSwitch.actualContext = StmtContext.UNDEF;
	}

	@Override
	public void caseIdentityStmt(IdentityStmt stmt) {
		
		valueSwitch.actualContext = StmtContext.IDENTITY;
		
		logger.fine("\n > > > Identity statement identified < < <");
		// TODO hier sind Parameter und this-Referenzen
		System.out.println("Identity Stmt: "+ stmt.getUseBoxes().toString());	
		System.out.println(stmt.getRightOp().getType());
		stmt.getRightOp().apply(valueSwitch);
		
		valueSwitch.actualContext = StmtContext.UNDEF;
	}

	@Override
	public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
		logger.severe("\n > > > Enter monitor statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
		logger.severe("\n > > > Exit monitor statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseGotoStmt(GotoStmt stmt) {
		logger.severe("\n > > > Goto statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseIfStmt(IfStmt stmt) {
		logger.fine("\n > > > If statement identified < < <");  
		
		System.out.println(stmt.getUseAndDefBoxes());
		List<ValueBox> valueList = stmt.getUseBoxes();
		ArrayList<Local> localList = new ArrayList<Local>();
		for (ValueBox v : valueList) {
			
			Value val = v.getValue();
			
			if (val instanceof Local) {
				localList.add((Local) val );
				System.out.println(val);
			}
		}
		
		int localListLength = localList.size();
		
		Local[] arguments = new Local[localListLength];
		
		for (int i = 0; i < localListLength; i++) {
			arguments[i] = localList.get(i);
		}

		JimpleInjector.checkCondition(stmt, arguments);

	}

	@Override
	public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
		logger.severe("\n > > > Lookup switch statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseNopStmt(NopStmt stmt) {
		logger.severe("\n > > > Nop statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseRetStmt(RetStmt stmt) {
		logger.severe("\n > > > Ret statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseReturnStmt(ReturnStmt stmt) {
		
		logger.fine("\n > > > Return statement identified < < <");
		
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
		logger.fine("\n > > > Return void statement identified < < <");
	}

	@Override
	public void caseTableSwitchStmt(TableSwitchStmt stmt) {
		logger.severe("\n > > > Table switch statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseThrowStmt(ThrowStmt stmt) {
		logger.severe("\n > > > Throw statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void defaultCase(Object obj) {
		logger.severe("\n > > > Default case of statements identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}
}