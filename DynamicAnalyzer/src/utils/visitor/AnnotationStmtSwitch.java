package visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import exceptions.InternalAnalyzerException;
import logging.L1Logger;
import analyzer.level1.JimpleInjector;
import soot.Local;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.ClassConstant;
import soot.jimple.Constant;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FieldRef;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import visitor.AnnotationValueSwitch.Stmt;

public class AnnotationStmtSwitch implements StmtSwitch {
	
	AnnotationValueSwitch valueSwitch = new AnnotationValueSwitch();
	Logger logger = L1Logger.getLogger();

	
	ArrayList<Value> DefBox = new ArrayList<Value>();
	

	@Override
	public void caseBreakpointStmt(BreakpointStmt stmt) {
		logger.severe("> > > Breakpoint statement identified < < <"); // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseInvokeStmt(InvokeStmt stmt) {
		
		InvokeStmt iStmt = stmt;
		valueSwitch.actualContext = Stmt.INVOKE;
		
		logger.fine(" > > > Invoke Statement identified < < <");
		
		InvokeExpr invokeExpr = iStmt.getInvokeExpr();
		
		invokeExpr.apply(valueSwitch);

		// TODO das ist eher interessant bei AssignStmt
		logger.finer("Method has return type: " + invokeExpr.getType());
		
		valueSwitch.actualContext = Stmt.UNDEF;
	}

	@Override
	public void caseAssignStmt(AssignStmt stmt) {
		
		valueSwitch.actualContext = Stmt.ASSIGN;
		AssignStmt aStmt = stmt;
		
		logger.fine("\n > > > ASSIGN STATEMENT identified < < <" );
		logger.finer(" > > > left side: " + aStmt.getDefBoxes().toString() + " < < <");
		logger.finer(" > > > right side: " + aStmt.getUseBoxes().toString() + " < < <");
		
		// Das ist im Moment nur zum Ausgeben, was vorkommt
		// Vielleicht kann man das aber noch irgendwie verwenden
		for (int i = 0; i < aStmt.getDefBoxes().size(); i++) {
			aStmt.getDefBoxes().get(i).getValue().apply(valueSwitch);
		}
		for (int i = 0; i < aStmt.getUseBoxes().size(); i++) {
			aStmt.getUseBoxes().get(i).getValue().apply(valueSwitch);
		}

		

		int numOfArgs = aStmt.getUseBoxes().size();
		
		logger.finer("Number of arguments :" + numOfArgs);
		
		Value leftValue = aStmt.getDefBoxes().get(0).getValue();
		leftValue.apply(valueSwitch);
		

		if(leftValue instanceof Local) {
		logger.finer("Left value is a Local");
			Local left = (Local) leftValue;

			if (numOfArgs == 1) {
				Value rightValue = aStmt.getUseBoxes().get(0).getValue();

				
				if (rightValue instanceof Local) {
			    logger.finer("Local on the right side");

			    Local right = (Local) rightValue;
			    JimpleInjector.assignLocalToLocal(left, right, aStmt);
				} else if (rightValue instanceof ClassConstant) { // TODO wieder entfernen
					ClassConstant cc = (ClassConstant) rightValue;
					System.out.println(rightValue);
				} else 
				{
					JimpleInjector.assignConstantToLocal(left, aStmt); 
				    // TODO: kann man hier was mit dem ValueSwitch machen? 
					// ZB rausfinden, welchen Typ die Argumente habe
					} 
				
			} else if (numOfArgs == 2) {
				logger.finer("Field or Array on the right side");
				Value rightValue = aStmt.getUseBoxes().get(1).getValue();
				if (rightValue instanceof NewArrayExpr) {
					System.out.println("new Array created");
					int length = Integer.parseInt(aStmt.getUseBoxes().get(0).getValue().toString());
					JimpleInjector.addArrayToObjectMap((Local) leftValue,  aStmt);
				}
				
				
			} else if (numOfArgs == 3) {
				logger.finer("2 Locals on the right side");
				

				Value rightValue1 = aStmt.getUseBoxes().get(0).getValue();
				
				Value rightValue2 = aStmt.getUseBoxes().get(1).getValue();
				
				if (!(rightValue1 instanceof Local) || !(rightValue2 instanceof Local)) {
					new InternalAnalyzerException("Expected Local as "
							+ "argument but got unknown type instead");
				}
				
				Local right1 = (Local) rightValue1;
				Local right2 = (Local) rightValue2;
				
				JimpleInjector.assignLocalsToLocal(left, right1, right2, aStmt);
			} else {
				System.out.println(numOfArgs);
				new InternalAnalyzerException("unexpected number of "
						+ "arguments in assign statement");
			}
			
		} else if (leftValue instanceof InstanceFieldRef) {
			logger.finer("Left value is an instance field");
			

			System.out.println(((InstanceFieldRef) leftValue).getBase());
			Local base = (Local) ((InstanceFieldRef) leftValue).getBase();
			System.out.println(((InstanceFieldRef) leftValue).getField().getDeclaration());
			
		} else if (leftValue instanceof StaticFieldRef ) {
			logger.finer("Left value is a static field");
			

			System.out.println(((StaticFieldRef) leftValue).getField().getDeclaringClass());

			if(leftValue.getUseBoxes().size() > 0) {
				logger.finer(" Declaring object " + leftValue.getUseBoxes().toString());
				logger.finer("Class " + leftValue.getClass());
				// TODO its unclear how to access r0 
			}
		}
		
		valueSwitch.actualContext = Stmt.UNDEF;
	}

	@Override
	public void caseIdentityStmt(IdentityStmt stmt) {
		
		valueSwitch.actualContext = Stmt.IDENTITY;
		
		IdentityStmt iStmt = stmt;
		
		logger.fine(" > > > Identity statement identified < < <");
		// TODO hier sind Parameter und this-Referenzen
		System.out.println("Identity Stmt: "+ stmt.getUseBoxes().toString());	
		System.out.println(stmt.getRightOp().getType());
		stmt.getRightOp().apply(valueSwitch);
		
		valueSwitch.actualContext = Stmt.UNDEF;
	}

	@Override
	public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
		logger.severe(" > > > Enter monitor statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
		logger.severe(" > > > Exit monitor statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseGotoStmt(GotoStmt stmt) {
		logger.severe(" > > > Goto statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseIfStmt(IfStmt stmt) {
		logger.fine(" > > > If statement identified < < <");  
		
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
		logger.severe(" > > > Lookup switch statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseNopStmt(NopStmt stmt) {
		logger.severe(" > > > Nop statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseRetStmt(RetStmt stmt) {
		logger.severe(" > > > Ret statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseReturnStmt(ReturnStmt stmt) {
		
		ReturnStmt rStmt = stmt;
		
		logger.fine(" > > > Return statement identified < < <");
		
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
		logger.fine(" > > > Return void statement identified < < <");
	}

	@Override
	public void caseTableSwitchStmt(TableSwitchStmt stmt) {
		logger.severe(" > > > Table switch statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void caseThrowStmt(ThrowStmt stmt) {
		logger.severe(" > > > Throw statement identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

	@Override
	public void defaultCase(Object obj) {
		logger.severe(" > > > Default case of statements identified < < <");  // TODO Change to fine
		// TODO Auto-generated method stub

	}

}
