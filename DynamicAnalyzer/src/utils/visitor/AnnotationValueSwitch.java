package utils.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import utils.exceptions.InternalAnalyzerException;
import utils.logging.L1Logger;
import analyzer.level1.JimpleInjector;
import analyzer.level2.HandleStmt;
import soot.Local;
import soot.Value;
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
import soot.jimple.Expr;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
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
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.ThisRef;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;

public class AnnotationValueSwitch implements JimpleValueSwitch {
	
	Logger logger = L1Logger.getLogger();
	VisitorHelper vh = new VisitorHelper();
	
	protected enum StmtContext {UNDEF, INVOKE, ASSIGN, IDENTITY, RETURN, GOTZO, IF, SWITCH, THROW}
	protected StmtContext actualContext = StmtContext.UNDEF;

	@Override
	public void caseDoubleConstant(DoubleConstant v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseFloatConstant(FloatConstant v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseIntConstant(IntConstant v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseLongConstant(LongConstant v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseNullConstant(NullConstant v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseStringConstant(StringConstant v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseClassConstant(ClassConstant v) {
		// TODO Auto-generated method stub
		System.out.println(v);

	}

	@Override
	public void defaultCase(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseAddExpr(AddExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseAndExpr(AndExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseCmpExpr(CmpExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseCmpgExpr(CmpgExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseCmplExpr(CmplExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseDivExpr(DivExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseEqExpr(EqExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseNeExpr(NeExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseGeExpr(GeExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseGtExpr(GtExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseLeExpr(LeExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseLtExpr(LtExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseMulExpr(MulExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseOrExpr(OrExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseRemExpr(RemExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseShlExpr(ShlExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseShrExpr(ShrExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseUshrExpr(UshrExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseSubExpr(SubExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseXorExpr(XorExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {

		logger.severe("Invoke expression is of type InterfaceInvoke"); // TODO change to fine
		
	}

	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {

		logger.fine("Invoke expression is of type SpecialInvoke");
		logger.finest(v.toString());
		
		if (actualContext == StmtContext.INVOKE || actualContext == StmtContext.ASSIGN ) {
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
		
		
		if (actualContext == StmtContext.INVOKE || actualContext == StmtContext.ASSIGN ) {
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
		
		if (actualContext == StmtContext.INVOKE || actualContext == StmtContext.ASSIGN ) {
			Local[] args = vh.getArgumentsForInvokedMethod(v);
			JimpleInjector.storeArgumentLevels(args);
		} else {
			new InternalAnalyzerException("Unexpected Context for Invoke Expression");
		}
		
	}

	@Override
	public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {

		logger.severe("Invoke expression is of type DynamicInvoke"); // TODO change to fine

	}

	@Override
	public void caseCastExpr(CastExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseInstanceOfExpr(InstanceOfExpr v) {
		// TODO Auto-generated method stub

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
	}

	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseNewExpr(NewExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseLengthExpr(LengthExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseNegExpr(NegExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseArrayRef(ArrayRef v) {

		logger.finest("Array reference identified " + v.toString());
		// TODO Auto-generated method stub

	}

	@Override
	public void caseStaticFieldRef(StaticFieldRef v) {
		
		logger.finest("Static field reference identified " + v.toString());
		// TODO Auto-generated method stub
		
	}

	@Override
	public void caseInstanceFieldRef(InstanceFieldRef v) {
		
		logger.finest("Instance field reference identified " + v.toString());
		// TODO Auto-generated method stub

	}

	@Override
	public void caseParameterRef(ParameterRef v) {

		logger.finest("Parameter reference identified " + v.toString());
		// TODO Auto-generated method stub

	}

	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseThisRef(ThisRef v) {
		
		logger.finer("@This reference identified " + v.toString());

	}

	@Override
	public void caseLocal(Local l) {	

		logger.finest("Local identified " + l.toString());

	}


}
