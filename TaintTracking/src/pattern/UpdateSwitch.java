package pattern;

import analysis.TaintTracking;
import model.AnalyzedMethodEnvironment;
import model.FieldEnvironment;
import model.LocalsMap;
import model.MethodEnvironment.MethodParameter;
import soot.Local;
import soot.SootClass;
import soot.SootField;
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
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.IdentityStmt;
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
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.ThisRef;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import utils.SecurityMessages;
import utils.SootUtils;
import exception.SootException.InvalidSwitchException;
import exception.SootException.UnimplementedSwitchException;

/**
 * <h1>Update switch for the {@link TaintTracking} analysis</h1>
 * 
 * The {@link UpdateSwitch} extends the switch {@link TaintTrackingSwitch} and contains the
 * <em>security level</em> to which the level of the value should be updated, the incoming and
 * outgoing locals map of a specific state in the progress of the method
 * {@link TaintTracking#flowThrough(LocalsMap, soot.Unit, LocalsMap)} as well as the environment of
 * the current analyzed method. The class provides for every possible value a method that updates
 * the <em>security level</em> if it is possible. Also the methods check for security violations,
 * such as the <em>security level</em> of the method parameters, occurring <em>write effects</em>,
 * invalid assignments, etc. If a level will be stored, also the program counter is considered. <br / >
 * 
 * Note, there is a special case: An {@link IdentityStmt} can be the assignment of a this or a
 * parameter reference to a local variable. Because of a validity check there are inconsistencies:
 * Please use for {@link IdentityStmt} a {@link UpdateSwitch} where the level is set to {@code null}
 * and the left hand side of the statement is added to the {@link UpdateSwitch} with the method
 * {@link UpdateSwitch#setIdentityInformation(Value)}. If the identity statement is a assignment of
 * a parameter reference please set also the parameter reference and the corresponding method
 * parameter information.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see TaintTrackingSwitch
 */
public class UpdateSwitch extends TaintTrackingSwitch implements JimpleValueSwitch {

	/** The <em>security level</em> to which the level of the value should be updated. */
	private final String level;
	/**
	 * Method parameter information for which should be checked if the local variable that should be
	 * updated corresponds to this parameter information. This special case occurs only for the
	 * update of local variables (see {@link UpdateSwitch#caseLocal(Local)}).
	 */
	private MethodParameter methodParameter = null;
	/**
	 * Parameter reference for which should be checked if the local variable that should be updated
	 * corresponds to this parameter reference. This special case occurs only for the update of
	 * local variables (see {@link UpdateSwitch#caseLocal(Local)}).
	 */
	private ParameterRef parameterRef = null;
	/** The value that should be updated in the special case for parameter and this references. */
	private Value value = null;

	/**
	 * Constructor of a {@link UpdateSwitch} that requires the current incoming and outgoing map of
	 * local variables as well as the environment of the current analyzed method. Also the
	 * <em>security level</em> to which the level of the applied value should be updated. Note,
	 * there is a special case: An {@link IdentityStmt} can be the assignment of a this or a
	 * parameter reference to a local variable. Because of a validity check there are
	 * inconsistencies: Please use for {@link IdentityStmt} a {@link UpdateSwitch} where the level
	 * is set to {@code null} and the left hand side of the statement is added to the
	 * {@link UpdateSwitch} with the method {@link UpdateSwitch#setIdentityInformation(Value)}. If
	 * the identity statement is a assignment of a parameter reference please set also the parameter
	 * reference and the corresponding method parameter information.
	 * 
	 * @param analysisEnvironment
	 *            The environment of the method that is currently analyzed.
	 * @param in
	 *            Current incoming map of the local variables.
	 * @param out
	 *            Current outgoing map of the local variables.
	 * @param level
	 *            <em>Security level</em> to which the level of the applied value will be updated.
	 */
	public UpdateSwitch(AnalyzedMethodEnvironment analyzedMethodEnvironment, LocalsMap in,
			LocalsMap out, String level) {
		super(analyzedMethodEnvironment, in, out);
		this.level = level;
	}

	/**
	 * The method should update the <em>security level</em> of a {@link AddExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseAddExpr(soot.jimple.AddExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseAddExpr(AddExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link AndExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseAndExpr(soot.jimple.AndExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseAndExpr(AndExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of an array reference, more precisely,
	 * the level is not changed, however, it is checked whether the assignment is possible. In this
	 * case, the level of the array, the level of the index and the level of the right hand side,
	 * which may be affected by the program counter, will be considered. In addition, a
	 * <em>write effect</em> is added and also checked, because an assignment to an array occurs.
	 * Note, that arrays may contain only values with the weakest available <em>security level</em>.
	 * Occurring exceptions are logged.
	 * 
	 * @param v
	 *            The array reference for which the <em>security level</em> should be updated.
	 * @see soot.jimple.RefSwitch#caseArrayRef(soot.jimple.ArrayRef)
	 */
	@Override
	public void caseArrayRef(ArrayRef v) {
		String rightLevel = takePCintoAccount(level);
		Value array = v.getBase();
		String arrayLevel = calculateLevel(array, v.toString());
		addWriteEffectCausedByArrayAssign(arrayLevel, v);
		Value index = v.getIndex();
		String indexLevel = calculateLevel(index, v.toString());
		if (!isEqualLevel(rightLevel, getWeakestSecurityLevel())) {
			// Only weakest security level array elements
			logSecurity(SecurityMessages.onlyLowArrayElements(getMethodSignature(), getSrcLn(),
					rightLevel, getWeakestSecurityLevel()));
		}
		if (!isWeakerOrEqualLevel(indexLevel, arrayLevel)) {
			logSecurity(SecurityMessages.assignmentToArrayWithStrongerIndex(getMethodSignature(),
					getSrcLn(), arrayLevel, indexLevel));
		}
		if (!isWeakerOrEqualLevel(rightLevel, arrayLevel)) {
			logSecurity(SecurityMessages.assignmentToWeakerArray(getMethodSignature(), getSrcLn(),
					arrayLevel, rightLevel));
		}
	}

	/**
	 * The method should update the <em>security level</em> of an expression with type
	 * {@link CastExpr}, but it is not possible to update the level of such an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseCastExpr(soot.jimple.CastExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseCastExpr(CastExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link CaughtExceptionRef}, but it
	 * is not implemented how to update the level of a caught exceptions reference.
	 * 
	 * @param v
	 *            The caught exceptions reference for which the <em>security level</em> should be
	 *            updated.
	 * @see soot.jimple.RefSwitch#caseCaughtExceptionRef(soot.jimple.CaughtExceptionRef)
	 * @throws UnimplementedSwitchException
	 *             Method throws always this exception, because the method is not implemented.
	 */
	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase(
				"caught exception ref", "SecurityLevelUpdateSwitch", v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link ClassConstant}, but it is
	 * not possible to update the level of a constant.
	 * 
	 * @param v
	 *            The constant for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ConstantSwitch#caseClassConstant(soot.jimple.ClassConstant)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseClassConstant(ClassConstant v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("constant",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link CmpExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseCmpExpr(soot.jimple.CmpExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseCmpExpr(CmpExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link CmpgExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseCmpgExpr(soot.jimple.CmpgExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseCmpgExpr(CmpgExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link CmplExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseCmplExpr(soot.jimple.CmplExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseCmplExpr(CmplExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link DivExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseDivExpr(soot.jimple.DivExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseDivExpr(DivExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link DoubleConstant}, but it is
	 * not possible to update the level of a constant.
	 * 
	 * @param v
	 *            The constant for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ConstantSwitch#caseDoubleConstant(soot.jimple.DoubleConstant)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseDoubleConstant(DoubleConstant v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("constant",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of an invoke expression with type
	 * {@link DynamicInvokeExpr}, but it is not possible to update the level of an invoke
	 * expression.
	 * 
	 * @param v
	 *            The invoke expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseDynamicInvokeExpr(soot.jimple.DynamicInvokeExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("invocation",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link EqExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseEqExpr(soot.jimple.EqExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseEqExpr(EqExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link FloatConstant}, but it is
	 * not possible to update the level of a constant.
	 * 
	 * @param v
	 *            The constant for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ConstantSwitch#caseFloatConstant(soot.jimple.FloatConstant)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseFloatConstant(FloatConstant v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("constant",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link GeExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseGeExpr(soot.jimple.GeExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseGeExpr(GeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link GtExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseGtExpr(soot.jimple.GtExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseGtExpr(GtExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * Method that checks for <em>security level</em> violations as well as for
	 * <em>write effects</em> which may occur by the assignment to the given instance field. Note,
	 * that static as well as instance fields are handled in the same way (see
	 * {@link UpdateSwitch#handleFieldRef(FieldRef)}).
	 * 
	 * @param v
	 *            Instance field which is assigned and for which the <em>security level</em>
	 *            violations as well as the <em>write effects</em> should be checked.
	 * @see soot.jimple.RefSwitch#caseInstanceFieldRef(soot.jimple.InstanceFieldRef)
	 * @see UpdateSwitch#handleFieldRef(FieldRef)
	 */
	@Override
	public void caseInstanceFieldRef(InstanceFieldRef v) {
		handleFieldRef(v);
	}

	/**
	 * The method should update the <em>security level</em> of an expression with type
	 * {@link InstanceOfExpr}, but it is not possible to update the level of such an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseInstanceOfExpr(soot.jimple.InstanceOfExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseInstanceOfExpr(InstanceOfExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of an {@link IntConstant}, but it is not
	 * possible to update the level of a constant.
	 * 
	 * @param v
	 *            The constant for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ConstantSwitch#caseIntConstant(soot.jimple.IntConstant)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseIntConstant(IntConstant v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("constant",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of an invoke expression with type
	 * {@link InterfaceInvokeExpr}, but it is not possible to update the level of an invoke
	 * expression.
	 * 
	 * @param v
	 *            The invoke expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseInterfaceInvokeExpr(soot.jimple.InterfaceInvokeExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link LeExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseLeExpr(soot.jimple.LeExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseLeExpr(LeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of an expression with type
	 * {@link LengthExpr}, but it is not possible to update the level of such an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseLengthExpr(soot.jimple.LengthExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseLengthExpr(LengthExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * Method that handles the update of <em>security level</em> of a local variable, more
	 * precisely, the level is changed in the outgoing locals map. Therefore, the level of the local
	 * and the level of the right hand side, which may be affected by the program counter, will be
	 * considered. A special case here is the update of a local variable which is a parameter
	 * variable: here the name and the type of the parameter reference and the corresponding local
	 * variable will be checked for equality. This requires that the parameter reference as well as
	 * method parameter information are given in the {@link UpdateSwitch} instance (see
	 * {@link UpdateSwitch#setParameterInformation(ParameterRef, MethodParameter)}). Note, that
	 * occurring exceptions are logged.
	 * 
	 * @param v
	 *            The local variable for which the <em>security level</em> should be updated.
	 * @see soot.jimple.JimpleValueSwitch#caseLocal(soot.Local)
	 */
	@Override
	public void caseLocal(Local l) {
		String localLevel = getWeakestSecurityLevel();
		String valueLevel = level;
		if (in.containsLocal(l)) {
			localLevel = in.getLevelOfLocal(l);
		} else {
			logError(SecurityMessages.localNotFoundUpdate(getMethodSignature(), getSrcLn(),
					l.getName(), valueLevel));
		}
		if (parameterRef != null && methodParameter != null) {
			if (!(methodParameter.getPosition() >= 0
					&& methodParameter.getName().equals(l.getName()) && methodParameter.getType()
					.equals(parameterRef.getType()))) {
				logError(SecurityMessages.noParameterMatch(getMethodSignature(), getSrcLn(),
						methodParameter.getName(), l.getName(), valueLevel));
			}
		}
		valueLevel = takePCintoAccount(valueLevel);
		if (isWeakerLevel(valueLevel, localLevel)) {
			logWarning(SecurityMessages.weakenLocalVariable(getMethodSignature(), getSrcLn(),
					l.getName(), localLevel, valueLevel));
		}
		if (!out.update(l, valueLevel)) {
			logError(SecurityMessages.localNotFoundUpdate(getMethodSignature(), getSrcLn(),
					l.getName(), valueLevel));
		}
	}

	/**
	 * The method should update the <em>security level</em> of a {@link LongConstant}, but it is not
	 * possible to update the level of a constant.
	 * 
	 * @param v
	 *            The constant for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ConstantSwitch#caseLongConstant(soot.jimple.LongConstant)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseLongConstant(LongConstant v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("constant",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link LtExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseLtExpr(soot.jimple.LtExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseLtExpr(LtExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link MulExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseMulExpr(soot.jimple.MulExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseMulExpr(MulExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link NeExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseNeExpr(soot.jimple.NeExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseNeExpr(NeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of an expression with type
	 * {@link NegExpr}, but it is not possible to update the level of such an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseNegExpr(soot.jimple.NegExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseNegExpr(NegExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of an expression with type
	 * {@link NewArrayExpr}, but it is not possible to update the level of such an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseNewArrayExpr(soot.jimple.NewArrayExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseNewArrayExpr(NewArrayExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of an expression with type
	 * {@link NewExpr}, but it is not possible to update the level of such an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseNewExpr(soot.jimple.NewExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseNewExpr(NewExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of an expression with type
	 * {@link NewMultiArrayExpr}, but it is not possible to update the level of such an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseNewMultiArrayExpr(soot.jimple.NewMultiArrayExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link NullConstant}, but it is not
	 * possible to update the level of a constant.
	 * 
	 * @param v
	 *            The constant for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ConstantSwitch#caseNullConstant(soot.jimple.NullConstant)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseNullConstant(NullConstant v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("constant",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link OrExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseOrExpr(soot.jimple.OrExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseOrExpr(OrExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * If {@link UpdateSwitch#value} is set and the {@link UpdateSwitch#level} is {@code null}, then
	 * the <em>security level</em> of the contained value will be updated to the level of the given
	 * parameter level. Also it will be checked whether the handle assignment is valid, i.e. whether
	 * the name and the type of the local variable that is assigned corresponds to the name and type
	 * of the parameter.
	 * 
	 * @param v
	 *            Parameter reference that is assigned to the contained value
	 *            {@link UpdateSwitch#value} for which the <em>security level</em> should be updated
	 *            to the level of the parameter.
	 * @see soot.jimple.RefSwitch#caseParameterRef(soot.jimple.ParameterRef)
	 * @throws UnimplementedSwitchException
	 *             If {@link UpdateSwitch#value} is not set and the {@link UpdateSwitch#level} is
	 *             not {@code null}, because the contained value requires information of the given
	 *             parameter reference for updating the <em>security level</em>.
	 */
	@Override
	public void caseParameterRef(ParameterRef v) {
		if (level == null && value != null) {
			updateParameterLevel(value, v);
		} else {
			throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase(
					"parameter ref", "SecurityLevelUpdateSwitch", v.toString(), getSrcLn()));
		}
	}

	/**
	 * The method should update the <em>security level</em> of a {@link RemExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseRemExpr(soot.jimple.RemExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseRemExpr(RemExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link ShlExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseShlExpr(soot.jimple.ShlExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseShlExpr(ShlExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link ShrExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseShrExpr(soot.jimple.ShrExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseShrExpr(ShrExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of an invoke expression with type
	 * {@link SpecialInvokeExpr}, but it is not possible to update the level of an invoke
	 * expression.
	 * 
	 * @param v
	 *            The invoke expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseSpecialInvokeExpr(soot.jimple.SpecialInvokeExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("invocation",
				v.toString(), getSrcLn()));
	}

	/**
	 * Method that checks for <em>security level</em> violations as well as for
	 * <em>write effects</em> which may occur by the assignment to the given static field. Note,
	 * that static as well as instance fields are handled in the same way (see
	 * {@link UpdateSwitch#handleFieldRef(FieldRef)}).
	 * 
	 * @param v
	 *            Static field which is assigned and for which the <em>security level</em>
	 *            violations as well as the <em>write effects</em> should be checked.
	 * @see soot.jimple.RefSwitch#caseStaticFieldRef(soot.jimple.StaticFieldRef)
	 * @see UpdateSwitch#handleFieldRef(FieldRef)
	 */
	@Override
	public void caseStaticFieldRef(StaticFieldRef v) {
		handleFieldRef(v);
	}

	/**
	 * The method should update the <em>security level</em> of an invoke expression with type
	 * {@link StaticInvokeExpr}, but it is not possible to update the level of an invoke expression.
	 * 
	 * @param v
	 *            The invoke expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseStaticInvokeExpr(soot.jimple.StaticInvokeExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseStaticInvokeExpr(StaticInvokeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("invocation",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link StringConstant}, but it is
	 * not possible to update the level of a constant.
	 * 
	 * @param v
	 *            The constant for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ConstantSwitch#caseStringConstant(soot.jimple.StringConstant)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseStringConstant(StringConstant v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("constant",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link SubExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseSubExpr(soot.jimple.SubExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseSubExpr(SubExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * If {@link UpdateSwitch#value} is set and the {@link UpdateSwitch#level} is {@code null}, then
	 * the <em>security level</em> of the contained value will be updated to the weakest available
	 * <em>security level</em> because {@code this} has always the weakest <em>security level</em>.
	 * 
	 * @param v
	 *            The this reference which has always the weakest available <em>security level</em>.
	 *            The level of the contained value will be updated to this <em>security level</em>.
	 * @see soot.jimple.RefSwitch#caseThisRef(soot.jimple.ThisRef)
	 * @throws UnimplementedSwitchException
	 *             If {@link UpdateSwitch#value} is not set and the {@link UpdateSwitch#level} is
	 *             not {@code null}, because the contained value requires information of the given
	 *             this reference for updating the <em>security level</em>.
	 */
	@Override
	public void caseThisRef(ThisRef v) {
		if (level == null && value != null) {
			String level = getWeakestSecurityLevel();
			updateLevel(value, level, v.toString());
		} else {
			throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase(
					"this ref", "SecurityLevelUpdateSwitch", v.toString(), getSrcLn()));
		}
	}

	/**
	 * The method should update the <em>security level</em> of a {@link UshrExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseUshrExpr(soot.jimple.UshrExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseUshrExpr(UshrExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of an invoke expression with type
	 * {@link VirtualInvokeExpr}, but it is not possible to update the level of an invoke
	 * expression.
	 * 
	 * @param v
	 *            The invoke expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseVirtualInvokeExpr(soot.jimple.VirtualInvokeExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("invocation",
				v.toString(), getSrcLn()));
	}

	/**
	 * The method should update the <em>security level</em> of a {@link XorExpr}, but it is not
	 * possible to update the level of an expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ExprSwitch#caseXorExpr(soot.jimple.XorExpr)
	 * @throws InvalidSwitchException
	 *             Always, because the update is not possible.
	 */
	@Override
	public void caseXorExpr(XorExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression",
				v.toString(), getSrcLn()));
	}

	/**
	 * Default case: the method should update the <em>security level</em> of an {@link Object}, but
	 * it is not determined how to update the level of an object.
	 * 
	 * @param object
	 *            The object for which the <em>security level</em> should be updated.
	 * @see soot.jimple.ConstantSwitch#defaultCase(java.lang.Object)
	 * @throws UnimplementedSwitchException
	 *             Method throws always this exception, because this method may not be invoked.
	 */
	@Override
	public void defaultCase(Object object) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("default",
				"SecurityLevelUpdateSwitch", object.toString(), getSrcLn()));
	}

	/**
	 * Sets the value that should be updated in the special case for parameter and this references
	 * to the given value.
	 * 
	 * @param value
	 *            The value which should be updated in the special case for parameter and this
	 *            references.
	 * @see UpdateSwitch#caseParameterRef(ParameterRef)
	 * @see UpdateSwitch#caseThisRef(ThisRef)
	 */
	public void setIdentityInformation(Value value) {
		this.value = value;
	}

	/**
	 * Sets the information of the method parameter, including the <em>security level</em>, which
	 * are required to update the level of the contained value.
	 * 
	 * @param parameterRef
	 *            The parameter reference that points to the parameter which is assigned to a local
	 *            variable.
	 * @param methodParameter
	 *            The method parameter information that corresponds to the given parameter reference
	 *            and that contains the <em>security level</em> of the parameter to which the level
	 *            of the contained value should be updated.
	 */
	public void setParameterInformation(ParameterRef parameterRef, MethodParameter methodParameter) {
		this.parameterRef = parameterRef;
		this.methodParameter = methodParameter;
	}

	/**
	 * Method that handles the update of <em>security level</em> of a field, more precisely, the
	 * level is not changed, however, it is checked whether the assignment is possible. Therefore,
	 * the level of the field and the level of the right hand side, which may be affected by the
	 * program counter, will be considered. In addition, a <em>write effect</em> is added and also
	 * checked, because an assignment to a field occurs. If the field is static, also the class
	 * <em>write effects</em> of the class which declares the field will be added and checked.
	 * Occurring exceptions are logged.
	 * 
	 * @param fieldRef
	 *            Field which is assigned and for which the <em>security level</em> violations as
	 *            well as the <em>write effects</em> should be checked.
	 */
	private void handleFieldRef(FieldRef fieldRef) {
		String leftLevel = getWeakestSecurityLevel();
		String rightLevel = takePCintoAccount(level);
		SootField sootField = fieldRef.getField();
		FieldEnvironment field = new FieldEnvironment(sootField, getLog(), getSecurityAnnotation());
		String fieldSignature = SootUtils.generateFieldSignature(sootField, false, true, true);
		if (!field.isLibraryClass()) {
			if (field.isFieldSecurityLevelValid()) {
				leftLevel = field.getLevel();
				// SIDE-EFFECTS: |----->
				addWriteEffectCausedByAssign(leftLevel, sootField);
				// SIDE-EFFECTS: Check class write effects for static field
				if (field.isStatic()) {
					SootClass sootClass = field.getSootClass();
					String classSignature = SootUtils.generateClassSignature(sootClass, false);
					if (!field.isLibraryClass()) {
						if (field.areClassWriteEffectsValid()) {
							for (String effected : field.getExpectedClassWriteEffects()) {
								addWriteEffectCausedByClass(effected, sootClass);
							}
						} else {
							logError(SecurityMessages.invalidClassWriteEffectUsingClass(
									getMethodSignature(), getSrcLn(), classSignature));
						}
					} else {
						logWarning(SecurityMessages.usingLibraryClassNoClassWriteEffect(
								getMethodSignature(), getSrcLn(), classSignature));
					}
				}
				// <-----| SIDE-EFFECTS
			} else {
				logError(SecurityMessages.invalidFieldAnnotation(getMethodSignature(),
						fieldSignature, getSrcLn()));
				logError(SecurityMessages.invalidWriteEffect(getMethodSignature(), getSrcLn(),
						field.getLevel()));
			}
		} else {
			logWarning(SecurityMessages.assignmentToLibraryField(getMethodSignature(),
					fieldSignature, getSrcLn()));
			logWarning(SecurityMessages.usingLibraryFieldNoWriteEffect(getMethodSignature(),
					getSrcLn(), fieldSignature));
		}
		if (!isWeakerOrEqualLevel(rightLevel, leftLevel)) {
			logSecurity(SecurityMessages.assignmentToWeakerField(getMethodSignature(),
					fieldSignature, getSrcLn(), leftLevel, rightLevel));
		}
	}

}
