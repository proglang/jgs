package util.visitor;

import analyzer.level1.JimpleInjector;
import soot.Local;
import soot.SootMethod;
import soot.jimple.*;
import util.exceptions.InternalAnalyzerException;
import util.exceptions.NotSupportedStmtException;
import util.logging.L1Logger;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;


public class AnnotationValueSwitch implements JimpleValueSwitch {
	
	private static final Logger logger = Logger.getLogger(AnnotationValueSwitch.class.getName());

	private VisitorHelper vh = new VisitorHelper();

	public AnnotationValueSwitch(Stmt stmt, StmtContext cx) {
	    this.callingStmt = stmt;
	    this.actualContext = cx;
	}

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
	
	private StmtContext actualContext = StmtContext.UNDEF;
	
	/*
	 * Type of the right expression in an assign statement. Certain types need to
	 * be handled in a special way. Explanation:
	 * IGNORE This is used for arithmetic expressions. The locals in the expressions
	 * 		are already handled, so its not neccessary to treat the expression
	 *      in StmtSwitch
	 * NEW_ARRAY 
	 * NEW_UNDEF_OBJECT
	 * INVOKE_INTERAL_METHOD 
	 * INVOKE_EXTERNAL_METHOD 
	 * MAKE_HIGH ?????
	 * MAKE_LOW ?????
	 */
	protected enum RequiredActionForRHS {
		NEW_ARRAY, NEW_UNDEF_OBJECT,
		INVOKE_INTERAL_METHOD, INVOKE_EXTERNAL_METHOD, 
		SET_RETURN_LEVEL,
		MAKE_HIGH, MAKE_MEDIUM, MAKE_LOW,
		CAST
	} 
	
	private Set<RequiredActionForRHS> requiredActionForRHS =
			new HashSet<>();

	private void setRequiredActionForRHS(Optional<RequiredActionForRHS> maybeAction) {
		maybeAction.ifPresent(action -> requiredActionForRHS.add(action));
	}

	public Optional<RequiredActionForRHS> getRequiredActionForRHS() {
		if (requiredActionForRHS.size() > 1) {
			throw new IllegalStateException("More than one action was collected: "
											+ requiredActionForRHS.toString());
		}
		return requiredActionForRHS.stream().findFirst();
	}


	/*
	 * The statement which called the AnnotationValueSwitch. This variable is set
	 * by AnnotationStmtSwitch.
	 */
	private Stmt callingStmt;

	/**
	 * It is not neccessary to treat a constant.
	 * @param v a constant
	 */
	@Override
	public void caseDoubleConstant(DoubleConstant v) {
		logger.finest("DoubleConstant identified " + callingStmt.toString());
	}
	
	/**
	 * It is not neccessary to treat a constant.
	 * @param v a constant
	 */
	@Override
	public void caseFloatConstant(FloatConstant v) {
		logger.finest("FloatConstant identified " + callingStmt.toString());
	}

	/**
	 * It is not neccessary to treat a constant.
	 * @param v a constant
	 */
	@Override
	public void caseIntConstant(IntConstant v) {
		logger.finest("IntConstant identified " + callingStmt.toString());
	}

	/**
	 * It is not neccessary to treat a constant.
	 * @param v a constant
	 */
	@Override
	public void caseLongConstant(LongConstant v) {
		logger.finest("LongConstant identified " + callingStmt.toString());
	}

	/**
	 * It is not neccessary to treat a constant.
	 * @param v a constant
	 */
	@Override
	public void caseNullConstant(NullConstant v) {
		logger.finest("NullConstant identified " + callingStmt.toString());
	}

	/**
	 * It is not neccessary to treat a constant.
	 * @param v a constant
	 */
	@Override
	public void caseStringConstant(StringConstant v) {
		logger.finest("StringConstant identified " + callingStmt.toString());
	}

	/**
	 * It is not neccessary to treat a constant.
	 * @param v a constant
	 */
	@Override
	public void caseClassConstant(ClassConstant v) {
		logger.finest("ClassConstant identified " + callingStmt.toString());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new InternalAnalyzerException();
		}
	}

	@Override
	public void caseMethodHandle(MethodHandle methodHandle) {
		throw new NotSupportedStmtException("method handle");
	}

	@Override
	public void defaultCase(Object object) {
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
	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseAndExpr(AndExpr v) {
		logger.finest("And Expr identified " + callingStmt.toString());
	}

	@Override
	public void caseCmpExpr(CmpExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("CmpExpr");
		}
	}

	@Override
	public void caseCmpgExpr(CmpgExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("CmpgExpr");
		}
	}

	@Override
	public void caseCmplExpr(CmplExpr v) {
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
	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseEqExpr(EqExpr v) {
		logger.finest("Eq Expr identified " + callingStmt.toString());
	}

	@Override
	public void caseNeExpr(NeExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("NeExpr");
		}
	}

	@Override
	public void caseGeExpr(GeExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("GeExpr");
		}
	}

	@Override
	public void caseGtExpr(GtExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("GtExpr");
		}
	}

	@Override
	public void caseLeExpr(LeExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("LeExpr");
		}
	}

	@Override
	public void caseLtExpr(LtExpr v) {
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

	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseOrExpr(OrExpr v) {
		logger.finest("Or Expr identified " + callingStmt.toString());

	}

	@Override
	public void caseRemExpr(RemExpr v) {
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

	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseShrExpr(ShrExpr v) {
		logger.finest("Shr Expr identified " + callingStmt.toString());

	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseUshrExpr(UshrExpr v) {
		logger.finest("Ushr Expr identified " + callingStmt.toString());
	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseSubExpr(SubExpr v) {
		logger.finest("Sub Expr identified " + callingStmt.toString());
	}

	/**
	 * It is not neccessary to treat arithmetic expressions. The SecurityLevels
	 * of this expressions are treated at an other place.
	 * @param v an arithmetic expression
	 */
	@Override
	public void caseXorExpr(XorExpr v) {
		logger.finest("Xor Expr identified " + callingStmt.toString());
	}

	@Override
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
		logger.fine("Invoke expression is of type InterfaceInvoke");
		/*
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("InterfaceInvokeExpression");
		}
		*/
		caseMethodInvokation(v);
	}

	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
		logger.fine("Invoke expression is of type SpecialInvoke");
		caseMethodInvokation(v);
	}

	@Override
	public void caseStaticInvokeExpr(StaticInvokeExpr v) {
		logger.fine("Invoke expression is of type StaticInvoke");
		caseMethodInvokation(v);
	}

	@Override
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
		logger.fine("Invoke expression is of type VirtualInvoke");
		caseMethodInvokation(v);
	}

	@Override
	public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
		logger.fine("Invoke expression is of type DynamicInvoke");
		caseMethodInvokation(v);
	}

	@Override
	public void caseCastExpr(CastExpr v) {

		if (actualContext == StmtContext.ASSIGNRIGHT) {
			// new InternalAnalyzerException();
		}
	}

	@Override
	public void caseInstanceOfExpr(InstanceOfExpr v) {
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("InstanceOf");
		}
	}

	@Override
	@Deprecated
	public void caseNewArrayExpr(NewArrayExpr v) {
		setRequiredActionForRHS(Optional.of(RequiredActionForRHS.NEW_ARRAY));
		logger.finest("New Array expression identified: " + callingStmt.toString());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
		// TODO
		}
	}

	@Override
	@Deprecated
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		setRequiredActionForRHS(Optional.of(RequiredActionForRHS.NEW_ARRAY));
		logger.finest("New Multiarray expression identified: " + callingStmt.toString());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			new InternalAnalyzerException();
		}
	}

	@Override
	public void caseNewExpr(NewExpr v) {
		setRequiredActionForRHS(Optional.of(RequiredActionForRHS.NEW_UNDEF_OBJECT));
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
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			throw new NotSupportedStmtException("NegExpr");
		}
	}

	@Override
	@Deprecated
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
	@Deprecated
	public void caseStaticFieldRef(StaticFieldRef v) {
		logger.finest("Static field reference identified " + v.toString());
		logger.finest(		v.getField().getDeclaringClass().toString());
		if (actualContext == StmtContext.ASSIGNRIGHT) {
			JimpleInjector.addLevelInAssignStmt(v, callingStmt);
		} else if (actualContext == StmtContext.ASSIGNLEFT) {
			JimpleInjector.setLevelOfAssignStmt(v, callingStmt);
		} else {
			new InternalAnalyzerException();			
		}
	}

	@Override
	@Deprecated
	public void caseInstanceFieldRef(InstanceFieldRef v) {
		logger.finest("Instance field reference identified " + v.toString());
		logger.finest("kh" + v.getBase().toString());
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
	@Deprecated
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
	
	/**
	 * Method to handly any invoke statement
	 * @param v
	 */
	private void caseMethodInvokation(InvokeExpr v) {

	    // TODO: this log message surely is confusing
		logger.finest(v.toString());

		if (actualContext == StmtContext.INVOKE
				|| actualContext == StmtContext.ASSIGNRIGHT ) {
			Local[] args = vh.getArgumentsForInvokedMethod(v);
			SootMethod method = v.getMethod();

			if (ExternalClasses.isSpecialMethod(method)) {
				logger.fine("Found special method: " + method);
				setRequiredActionForRHS(ExternalClasses.instrumentSpecialMethod(method, callingStmt, args));

			} else {

				if (v.getMethod().getDeclaringClass().isLibraryClass()) {
					// method is not instrumented and we have no special treatment for it... 
					logger.severe("====== IGNORING UNKNOWN LIBRARY METHOD " + method + " =======");
                    // TODO: why don't we exit here? (otherwise we are not ignoring the library method)
				}

				logger.fine("Found an external class " + method);
				logger.fine("This class is treated as an internal class");
				// JimpleInjector.pushToGlobalPC(LocalPC JOIN GlobalPC)

				setRequiredActionForRHS(Optional.of(RequiredActionForRHS.SET_RETURN_LEVEL));

				// aber überall noch mal checken ob nirgendwo das right element
				// überschrieben wird, d.h. ob das hier eine eindeutige 
				// positioin ist
				JimpleInjector.storeArgumentLevels(callingStmt, args);	// this is where we could push a global pc
			}

		} else {
			throw new InternalAnalyzerException(
					"Unexpected Context for Invoke Expression");
		}		
	}
}


