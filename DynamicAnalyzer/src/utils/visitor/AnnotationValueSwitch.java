package utils.visitor;

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
import utils.exceptions.InternalAnalyzerException;
import utils.exceptions.NotSupportedStmtException;
import utils.logging.L1Logger;

import java.util.logging.Logger;

public class AnnotationValueSwitch implements JimpleValueSwitch {
	
	Logger logger = L1Logger.getLogger();
	VisitorHelper vh = new VisitorHelper();
	
	/*
	 * ???
	 * UNDEF 
	 * INVOKE 
	 * ASSIGNRIGHT 
	 * ASSIGNLEFT 
	 * IDENTITY 
	 * RETURN 
	 * GOTO 
	 * IF 
	 * SWITCH 
	 * THROW
	 */
	protected enum StmtContext { 
		UNDEF, INVOKE, ASSIGNRIGHT, ASSIGNLEFT, IDENTITY, 
		RETURN, GOTO, IF, SWITCH, THROW
	}
	
	protected StmtContext actualContext = StmtContext.UNDEF;
	
	/*
	 * Type of the right expression in an assign statement. Certain types need to
	 * be handled in a special way. Explanation:
	 * NOT This is used for arithmetic expressions. The locals in the expressions
	 * 		are already handled, so its not neccessary to treat the expression
	 *      in StmtSwitch
	 * NEW_ARRAY 
	 * NEW_UNDEF_OBJECT
	 * INVOKE_INTERAL_METHOD 
	 * INVOKE_EXTERNAL_METHOD 
	 * MAKE_HIGH ?????
	 * MAKE_LOW ?????
	 */
	protected enum RightElement {
		NOT, NEW_ARRAY, NEW_UNDEF_OBJECT,
		INVOKE_INTERAL_METHOD, INVOKE_EXTERNAL_METHOD,
		MAKE_HIGH, MAKE_LOW
	} 
	
	protected static RightElement rightElement = RightElement.NOT;
	
	/*
	 * The statement which called the AnnotationValueSwitch. This variable is set
	 * by AnnotationStmtSwitch.
	 */
	protected Stmt callingStmt;

	/**
	 * It is not neccessary to treat a constant.
	 * @param v a constant
	 */
	@Override
	public void caseDoubleConstant(DoubleConstant v) {
		logger.finest("DoubleConstant identified " + callingStmt.toString());
		rightElement = RightElement.NOT;
	}
	
	/**
	 * It is not neccessary to treat a constant.
	 * @param v a constant
	 */
	@Override
	public void caseFloatConstant(FloatConstant v) {
		logger.finest("FloatConstant identified " + callingStmt.toString());
		rightElement = RightElement.NOT;
	}

	/**
	 * It is not neccessary to treat a constant.
	 * @param v a constant
	 */
	@Override
	public void caseIntConstant(IntConstant v) {
		logger.finest("IntConstant identified " + callingStmt.toString());
		rightElement = RightElement.NOT;
	}

	/**
	 * It is not neccessary to treat a constant.
	 * @param v a constant
	 */
	@Override
	public void caseLongConstant(LongConstant v) {
		logger.finest("LongConstant identified " + callingStmt.toString());
		rightElement = RightElement.NOT;
	}

	/**
	 * It is not neccessary to treat a constant.
	 * @param v a constant
	 */
	@Override
	public void caseNullConstant(NullConstant v) {
		logger.finest("NullConstant identified " + callingStmt.toString());
		rightElement = RightElement.NOT;
	}

	/**
	 * It is not neccessary to treat a constant.
	 * @param v a constant
	 */
	@Override
	public void caseStringConstant(StringConstant v) {
		logger.finest("StringConstant identified " + callingStmt.toString());
		rightElement = RightElement.NOT;
	}

	/**
	 * It is not neccessary to treat a constant.
	 * @param v a constant
	 */
	@Override
	public void caseClassConstant(ClassConstant v) {
		logger.finest("ClassConstant identified " + callingStmt.toString());
		rightElement = RightElement.NOT;
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new InternalAnalyzerException();
		}
	}

	@Override
	public void defaultCase(Object object) {
		rightElement = RightElement.NOT;
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("defaultCase");
		}
	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseAddExpr(AddExpr v) {
		logger.finest("Add Expr identified " + callingStmt.toString());
		rightElement = RightElement.NOT;
	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseAndExpr(AndExpr v) {
		logger.finest("And Expr identified " + callingStmt.toString());
		rightElement = RightElement.NOT;
	}

	@Override
	public void caseCmpExpr(CmpExpr v) {
		rightElement = RightElement.NOT;
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("CmpExpr");
		}
	}

	@Override
	public void caseCmpgExpr(CmpgExpr v) {
		rightElement = RightElement.NOT;
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("CmpgExpr");
		}
	}

	@Override
	public void caseCmplExpr(CmplExpr v) {
		rightElement = RightElement.NOT;
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("CmplExpr");
		}
	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseDivExpr(DivExpr v) {
		logger.finest("Div Expr identified " + callingStmt.toString());
		rightElement = RightElement.NOT;
	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseEqExpr(EqExpr v) {
		logger.finest("Eq Expr identified " + callingStmt.toString());
		rightElement = RightElement.NOT;
	}

	@Override
	public void caseNeExpr(NeExpr v) {
		rightElement = RightElement.NOT;
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("NeExpr");
		}
	}

	@Override
	public void caseGeExpr(GeExpr v) {
		rightElement = RightElement.NOT;
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("GeExpr");
		}
	}

	@Override
	public void caseGtExpr(GtExpr v) {
		rightElement = RightElement.NOT;
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("GtExpr");
		}
	}

	@Override
	public void caseLeExpr(LeExpr v) {
		rightElement = RightElement.NOT;
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("LeExpr");
		}
	}

	@Override
	public void caseLtExpr(LtExpr v) {
		rightElement = RightElement.NOT;
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("LtExpr");
		}
	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseMulExpr(MulExpr v) {
		logger.finest("Mul Expr identified " + callingStmt.toString());
		rightElement = RightElement.NOT;

	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseOrExpr(OrExpr v) {
		logger.finest("Or Expr identified " + callingStmt.toString());
		rightElement = RightElement.NOT;

	}

	@Override
	public void caseRemExpr(RemExpr v) {
		rightElement = RightElement.NOT;
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("RemExpr");
		}
	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseShlExpr(ShlExpr v) {
		logger.finest("Shl Expr identified " + callingStmt.toString());
		rightElement = RightElement.NOT;

	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseShrExpr(ShrExpr v) {
		logger.finest("Shr Expr identified " + callingStmt.toString());
		rightElement = RightElement.NOT;

	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseUshrExpr(UshrExpr v) {
		logger.finest("Ushr Expr identified " + callingStmt.toString());
		rightElement = RightElement.NOT;
	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseSubExpr(SubExpr v) {
		logger.finest("Sub Expr identified " + callingStmt.toString());
		rightElement = RightElement.NOT;
	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseXorExpr(XorExpr v) {
		logger.finest("Xor Expr identified " + callingStmt.toString());
		rightElement = RightElement.NOT;
	}

	@Override
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
		rightElement = RightElement.NOT;
		logger.fine("Invoke expression is of type InterfaceInvoke");
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("InterfaceInvokeExpression");
		}
	}

	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
		logger.fine("Invoke expression is of type SpecialInvoke");
		logger.finest(v.toString());
		rightElement = RightElement.NOT;
	
		if (actualContext == StmtContext.INVOKE 
				|| actualContext == StmtContext.ASSIGNRIGHT ) {
			Local[] args = vh.getArgumentsForInvokedMethod(v);
			String method = v.getMethod().toString();

			if (ExternalClasses.methodMap.containsKey(method)) {
				logger.fine("Found an external class " + method);
				logger.fine("This class is treated in a special way");
				ExternalClasses.receiveCommand(method, callingStmt, args);
			} else {
				logger.fine("Found an external class " + method);
				logger.fine("This class is treated as an internal class");
				JimpleInjector.storeArgumentLevels(callingStmt, args);
			}
		} else {
			throw new InternalAnalyzerException(
					"Unexpected Context for Invoke Expression");
		}
	}

	@Override
	public void caseStaticInvokeExpr(StaticInvokeExpr v) {
		logger.fine("Invoke expression is of type StaticInvoke");
		logger.finest(v.toString());
		rightElement = RightElement.NOT;
	
		if (actualContext == StmtContext.INVOKE 
				|| actualContext == StmtContext.ASSIGNRIGHT ) {
			Local[] args = vh.getArgumentsForInvokedMethod(v);
			String method = v.getMethod().toString();

			if (ExternalClasses.methodMap.containsKey(method)) {
				logger.fine("Found an external class " + method);
				logger.fine("This class is treated in a special way");
				ExternalClasses.receiveCommand(method, callingStmt, args);
			} else {
				logger.fine("Found an external class " + method);
				logger.fine("This class is treated as an internal class");
				JimpleInjector.storeArgumentLevels(callingStmt, args);
			}
		} else {
			throw new InternalAnalyzerException(
					"Unexpected Context for Invoke Expression");
		}
	}

	@Override
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
		logger.fine("Invoke expression is of type VirtualInvoke");
		logger.finest(v.toString());
		rightElement = RightElement.NOT;
	
		if (actualContext == StmtContext.INVOKE 
				|| actualContext == StmtContext.ASSIGNRIGHT ) {
			Local[] args = vh.getArgumentsForInvokedMethod(v);
			String method = v.getMethod().toString();

			if (ExternalClasses.methodMap.containsKey(method)) {		// methodMap sind spezielle methoden, 
					// die nicht instrumentiert werden (beispiel: makeHigh)
				logger.fine("Found an external class " + method);
				logger.fine("This class is treated in a special way");
				ExternalClasses.receiveCommand(method, callingStmt, args);	
			} else {
				logger.fine("Found an external class " + method);
				logger.fine("This class is treated as an internal class");
				// JimpleInjector.pushToGlobalPC(LocalPC JOIN GlobalPC)
				JimpleInjector.storeArgumentLevels(callingStmt, args);	// this is where we could push a global pc
			}
		} else {
			throw new InternalAnalyzerException(
					"Unexpected Context for Invoke Expression");
		}		
	}

	@Override
	public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
		logger.fine("Invoke expression is of type DynamicInvoke");
		logger.finest(v.toString());
		rightElement = RightElement.NOT;
	
		if (actualContext == StmtContext.INVOKE 
				|| actualContext == StmtContext.ASSIGNRIGHT ) {
			Local[] args = vh.getArgumentsForInvokedMethod(v);
			String method = v.getMethod().toString();

			if (ExternalClasses.methodMap.containsKey(method)) {
				logger.fine("Found an external class " + method);
				logger.fine("This class is treated in a special way");
				ExternalClasses.receiveCommand(method, callingStmt, args);
			} else {
				logger.fine("Found an external class " + method);
				logger.fine("This class is treated as an internal class");
				JimpleInjector.storeArgumentLevels(callingStmt, args);
			}
		} else {
			throw new InternalAnalyzerException(
					"Unexpected Context for Invoke Expression");
		}
	}

	@Override
	public void caseCastExpr(CastExpr v) {
		rightElement = RightElement.NOT;
			
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			// new InternalAnalyzerException();
		}
	}

	@Override
	public void caseInstanceOfExpr(InstanceOfExpr v) {
		rightElement = RightElement.NOT;
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("InstanceOf");
		}
	}

	@Override
	public void caseNewArrayExpr(NewArrayExpr v) {
		rightElement = RightElement.NEW_ARRAY;
		logger.finest("New Array expression identified: " + callingStmt.toString());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
		// TODO
		}
	}

	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		rightElement = RightElement.NEW_ARRAY;
		logger.finest("New Multiarray expression identified: " + callingStmt.toString());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseNewExpr(NewExpr v) {
		rightElement = RightElement.NEW_UNDEF_OBJECT;
		logger.finest("NewExpr identified " + v.getBaseType());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			// new InternalAnalyzerException("new Expr");
			if (!ExternalClasses.classMap.contains(v.toString())) {
				// TODO Standardverfahren
			} else {
				// TODO special methods
			}
		}
	}

	
	/**
	 * It is supposed that it is not neccessay to treat the length expression since
	 * it always comes with the corresponding array and the level of the array (which is
	 * the same as its length level) will always be already treated.
	 * @param v length expression
	 */
	@Override
	public void caseLengthExpr(LengthExpr v) {
		rightElement = RightElement.NOT;
		logger.finest("LengthExpr identified: " + v.toString());
//		if (actualContext == StmtContext.ASSIGNRIGHT) {
//			// JimpleInjector.addLevelInAssignStmt(v, callingStmt);
//			System.out.println(v.getType());
//			System.out.println(v.getUseBoxes().get(0).toString());
//
//			new InternalAnalyzerException();
//		} else {
//			new InternalAnalyzerException();			
//		}
	}

	@Override
	public void caseNegExpr(NegExpr v) {
		rightElement = RightElement.NOT;
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("NegExpr");
		}
	}

	@Override
	public void caseArrayRef(ArrayRef v) {
		logger.finest("Array reference identified " + v.toString());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			JimpleInjector.addLevelInAssignStmt(v, callingStmt);
		} else if (actualContext == StmtContext.ASSIGNLEFT) {
			JimpleInjector.setLevelOfAssignStmt(v, callingStmt);
		} else {
			throw new InternalAnalyzerException();			
		}
	}

	@Override
	public void caseStaticFieldRef(StaticFieldRef v) {	
		rightElement = RightElement.NOT;
		logger.finest("Static field reference identified " + v.toString());
		System.out.println(		v.getField().getDeclaringClass());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			JimpleInjector.addLevelInAssignStmt(v, callingStmt);
		} else if (actualContext == StmtContext.ASSIGNLEFT) {
			JimpleInjector.setLevelOfAssignStmt(v, callingStmt);
		} else {
			new InternalAnalyzerException();			
		}
	}

	@Override
	public void caseInstanceFieldRef(InstanceFieldRef v) {	
		rightElement = RightElement.NOT;
		logger.finest("Instance field reference identified " + v.toString());	
		System.out.println("kh" + v.getBase().toString());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			JimpleInjector.addLevelInAssignStmt(v, callingStmt);
		} else if (actualContext == StmtContext.ASSIGNLEFT) {
			JimpleInjector.setLevelOfAssignStmt(v, callingStmt);
		} else {
			new InternalAnalyzerException();			
		}
	}

	@Override
	public void caseParameterRef(ParameterRef v) {
		logger.finest("Parameter reference identified " + v.toString());
		throw new NotSupportedStmtException("ParameterRef");
	}

	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		rightElement = RightElement.NOT;
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("CaughtExceptionRef");
		}
	}

	@Override
	public void caseThisRef(ThisRef v) {
		logger.finer("@This reference identified " + v.toString());
		throw new NotSupportedStmtException("ThisRef");
	}

	@Override
	public void caseLocal(Local l) {	
		logger.finest("Local identified " + l.toString());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			JimpleInjector.addLevelInAssignStmt(l, callingStmt);
		} else if (actualContext == StmtContext.ASSIGNLEFT) {
			JimpleInjector.setLevelOfAssignStmt(l, callingStmt);
		} else {
			new InternalAnalyzerException();			
		}
	}
}
