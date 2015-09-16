package utils.visitor;

import java.util.logging.Logger;

import utils.exceptions.InternalAnalyzerException;
import utils.logging.L1Logger;
import analyzer.level1.JimpleInjector;
import soot.Local;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
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

public class AnnotationValueSwitch implements JimpleValueSwitch {
	
	Logger logger = L1Logger.getLogger();
	VisitorHelper vh = new VisitorHelper();
	
	protected enum StmtContext {UNDEF, INVOKE, ASSIGNRIGHT, ASSIGNLEFT, IDENTITY, RETURN, GOTO, IF, SWITCH, THROW}
	protected StmtContext actualContext = StmtContext.UNDEF;
	protected Stmt callingStmt;

	@Override
	public void caseDoubleConstant(DoubleConstant v) {		
	}

	@Override
	public void caseFloatConstant(FloatConstant v) {
	}

	@Override
	public void caseIntConstant(IntConstant v) {
	}

	@Override
	public void caseLongConstant(LongConstant v) {
	}

	@Override
	public void caseNullConstant(NullConstant v) {
	}

	@Override
	public void caseStringConstant(StringConstant v) {
	}

	@Override
	public void caseClassConstant(ClassConstant v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
		System.out.println(v);

	}

	@Override
	public void defaultCase(Object object) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseAddExpr(AddExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseAndExpr(AndExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseCmpExpr(CmpExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseCmpgExpr(CmpgExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseCmplExpr(CmplExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseDivExpr(DivExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseEqExpr(EqExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseNeExpr(NeExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseGeExpr(GeExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseGtExpr(GtExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseLeExpr(LeExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseLtExpr(LtExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseMulExpr(MulExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseOrExpr(OrExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseRemExpr(RemExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseShlExpr(ShlExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseShrExpr(ShrExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseUshrExpr(UshrExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseSubExpr(SubExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseXorExpr(XorExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {

		logger.severe("Invoke expression is of type InterfaceInvoke"); // TODO change to fine
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {

		logger.fine("Invoke expression is of type SpecialInvoke");
		logger.finest(v.toString());
		
		if (actualContext == StmtContext.INVOKE || actualContext == StmtContext.ASSIGNRIGHT ) {
			Local[] args = vh.getArgumentsForInvokedMethod(v);
			JimpleInjector.storeArgumentLevels(args);
		} else {
			new InternalAnalyzerException("Unexpected Context for Invoke Expression");
		}

	}

	@Override
	public void caseStaticInvokeExpr(StaticInvokeExpr v) {

		logger.fine("Invoke expression is of type StaticInvoke");
		logger.finest(v.toString());	
		
		
		if (actualContext == StmtContext.INVOKE || actualContext == StmtContext.ASSIGNRIGHT ) {
			Local[] args = vh.getArgumentsForInvokedMethod(v);
			JimpleInjector.storeArgumentLevels(args);
		} else {
			new InternalAnalyzerException("Unexpected Context for Invoke Expression");
		}

	}

	@Override
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {

		logger.fine("Invoke expression is of type VirtualInvoke");
		logger.finest(v.toString());
		
		if (actualContext == StmtContext.INVOKE || actualContext == StmtContext.ASSIGNRIGHT ) {
			Local[] args = vh.getArgumentsForInvokedMethod(v);
			JimpleInjector.storeArgumentLevels(args);
		} else {
			new InternalAnalyzerException("Unexpected Context for Invoke Expression");
		}
		
	}

	@Override
	public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {

		logger.severe("Invoke expression is of type DynamicInvoke"); // TODO change to fine
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseCastExpr(CastExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseInstanceOfExpr(InstanceOfExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseNewArrayExpr(NewArrayExpr v) {
		logger.finest("New Array expression identified");
		System.out.println(v.getBaseType());
		System.out.println(v.getClass());
		System.out.println(v.getSize());
		System.out.println(v.getSizeBox());
		System.out.println(v.getType());
		System.out.println(v.getUseBoxes());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseNewExpr(NewExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			//new InternalAnalyzerException("new Expr");
		}
	}

	@Override
	public void caseLengthExpr(LengthExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseNegExpr(NegExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseArrayRef(ArrayRef v) {
		logger.finest("Array reference identified " + v.toString());
		
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			JimpleInjector.addLevelInAssignStmt(v);
		} else if (actualContext == StmtContext.ASSIGNLEFT) {
			JimpleInjector.setLevelOfAssignStmt(v, callingStmt);
		} else {
			new InternalAnalyzerException();			
		}
	}

	@Override
	public void caseStaticFieldRef(StaticFieldRef v) {	
		logger.finest("Static field reference identified " + v.toString());
		System.out.println(		v.getField().getDeclaringClass());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			JimpleInjector.addLevelInAssignStmt(v);
		} else if (actualContext == StmtContext.ASSIGNLEFT) {
			JimpleInjector.setLevelOfAssignStmt(v, callingStmt);
		} else {
			new InternalAnalyzerException();			
		}
	}

	@Override
	public void caseInstanceFieldRef(InstanceFieldRef v) {	
		logger.finest("Instance field reference identified " + v.toString());	
		System.out.println("kh" + v.getBase().toString());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			JimpleInjector.addLevelInAssignStmt(v);
		} else if (actualContext == StmtContext.ASSIGNLEFT) {
			JimpleInjector.setLevelOfAssignStmt(v, callingStmt);
		} else {
			new InternalAnalyzerException();			
		}
	}

	@Override
	public void caseParameterRef(ParameterRef v) {

		logger.finest("Parameter reference identified " + v.toString());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseThisRef(ThisRef v) {
		
		logger.finer("@This reference identified " + v.toString());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseLocal(Local l) {	
		logger.finest("Local identified " + l.toString());
		
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			JimpleInjector.addLevelInAssignStmt(l);
		} else if (actualContext == StmtContext.ASSIGNLEFT) {
			JimpleInjector.setLevelOfAssignStmt(l, callingStmt);
		} else {
			new InternalAnalyzerException();			
		}
	}


}
