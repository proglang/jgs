package pattern;

import java.util.ArrayList;
import java.util.List;

import analysis.TaintTracking;

import model.AnalyzedMethodEnvironment;
import model.FieldEnvironment;
import model.LocalsMap;
import soot.Local;
import soot.SootField;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
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
import soot.jimple.UnopExpr;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import utils.SecurityMessages;
import utils.SootUtils;
import exception.SootException.NoSecurityLevelException;
import exception.SootException.UnimplementedSwitchException;

/**
 * <h1>Lookup switch for the {@link TaintTracking} analysis</h1>
 * 
 * The {@link LookupSwitch} extends the switch {@link TaintTrackingSwitch} and contains incoming and
 * outgoing locals map of a specific state in the progress of the method
 * {@link TaintTracking#flowThrough(LocalsMap, soot.Unit, LocalsMap)} as well as the environment of
 * the current analyzed method. The class provides for every possible value a method that looks up
 * the <em>security level</em> if it is possible. Also the methods check for security violations,
 * such as the <em>security level</em> of the method parameters, occurring <em>write effects</em>,
 * etc. If a level was looked up, it will be stored in {@link LookupSwitch#level}. <br / >
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
public class LookupSwitch extends TaintTrackingSwitch implements JimpleValueSwitch {

	/** The calculated <em>security level</em> of the applied value. */
	private String level = null;

	/**
	 * Constructor of a {@link LookupSwitch} that requires the current incoming and outgoing map of
	 * local variables as well as the environment of the current analyzed method. Note, there is a
	 * special case: An {@link IdentityStmt} can be the assignment of a this or a parameter
	 * reference to a local variable. Because of a validity check there are inconsistencies: Please
	 * use for {@link IdentityStmt} a {@link UpdateSwitch} where the level is set to {@code null}
	 * and the left hand side of the statement is added to the {@link UpdateSwitch} with the method
	 * {@link UpdateSwitch#setIdentityInformation(Value)}. If the identity statement is a assignment
	 * of a parameter reference please set also the parameter reference and the corresponding method
	 * parameter information.
	 * 
	 * @param analysisEnvironment
	 *            The environment of the method that is currently analyzed.
	 * @param in
	 *            Current incoming map of the local variables.
	 * @param out
	 *            Current outgoing map of the local variables.
	 */
	public LookupSwitch(AnalyzedMethodEnvironment analyzedMethodEnvironment, LocalsMap in,
			LocalsMap out) {
		super(analyzedMethodEnvironment, in, out);
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link AddExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseAddExpr(soot.jimple.AddExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseAddExpr(AddExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link AndExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseAndExpr(soot.jimple.AndExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseAndExpr(AndExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given array reference and stores the level in
	 * {@link LookupSwitch#level}. The resulting level is the strongest <em>security level</em> of
	 * the array level and the index level.
	 * 
	 * @param v
	 *            The array reference for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.RefSwitch#caseArrayRef(soot.jimple.ArrayRef)
	 */
	@Override
	public void caseArrayRef(ArrayRef v) {
		Value array = v.getBase();
		String arrayLevel = calculateLevel(array, v.toString());
		Value index = v.getIndex();
		String indexLevel = calculateLevel(index, v.toString());
		this.level = getMaxLevel(indexLevel, arrayLevel);
	}

	/**
	 * Looks up the <em>security level</em> for the given cast expression and stores the level in
	 * {@link LookupSwitch#level}.
	 * 
	 * @param v
	 *            The cast expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseCastExpr(soot.jimple.CastExpr)
	 */
	@Override
	public void caseCastExpr(CastExpr v) {
		Value value = v.getOp();
		handleOneValue(value, v);
	}

	/**
	 * The method should look up the <em>security level</em> of a {@link CaughtExceptionRef}, but it
	 * is not implemented how to look up the level of a caught exceptions reference.
	 * 
	 * @param v
	 *            The caught exception reference for which the <em>security level</em> should be
	 *            looked up.
	 * @see soot.jimple.RefSwitch#caseCaughtExceptionRef(soot.jimple.CaughtExceptionRef)
	 * @throws UnimplementedSwitchException
	 *             Method throws always this exception, because the method is not implemented.
	 */
	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase(
				"caught exception ref", "SecurityLevelLookupValueSwitch", v.toString(), getSrcLn()));
	}

	/**
	 * Looks up the <em>security level</em> for the given constant and stores the level in
	 * {@link LookupSwitch#level}. For a {@link ClassConstant} this is the weakest available
	 * <em>security level</em>.
	 * 
	 * @param v
	 *            The constant for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ConstantSwitch#caseClassConstant(soot.jimple.ClassConstant)
	 */
	@Override
	public void caseClassConstant(ClassConstant v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link CmpgExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseCmpExpr(soot.jimple.CmpExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseCmpExpr(CmpExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link CmpgExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseCmpgExpr(soot.jimple.CmpgExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseCmpgExpr(CmpgExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link CmplExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseCmplExpr(soot.jimple.CmplExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseCmplExpr(CmplExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link DivExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseDivExpr(soot.jimple.DivExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseDivExpr(DivExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given constant and stores the level in
	 * {@link LookupSwitch#level}. For a {@link DoubleConstant} this is the weakest available
	 * <em>security level</em>.
	 * 
	 * @param v
	 *            The constant for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ConstantSwitch#caseDoubleConstant(soot.jimple.DoubleConstant)
	 */
	@Override
	public void caseDoubleConstant(DoubleConstant v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * Looks up the <em>security level</em> of the given invoke expression with the type
	 * {@link DynamicInvokeExpr} and stores the resulting level in {@link LookupSwitch#level}. Also
	 * the parameter <em>security level</em> and the <em>write effects</em> will be handled.
	 * 
	 * @param v
	 *            The invoke expression, for which the level should be looked up.
	 * @see soot.jimple.ExprSwitch#caseDynamicInvokeExpr(soot.jimple.DynamicInvokeExpr)
	 * @see LookupSwitch#handleInvokeExpr(InvokeExpr)
	 */
	@Override
	public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
		handleInvokeExpr(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link EqExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseEqExpr(soot.jimple.EqExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseEqExpr(EqExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given constant and stores the level in
	 * {@link LookupSwitch#level}. For a {@link FloatConstant} this is the weakest available
	 * <em>security level</em>.
	 * 
	 * @param v
	 *            The constant for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ConstantSwitch#caseFloatConstant(soot.jimple.FloatConstant)
	 */
	@Override
	public void caseFloatConstant(FloatConstant v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link GeExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseGeExpr(soot.jimple.GeExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseGeExpr(GeExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link GtExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseGtExpr(soot.jimple.GtExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseGtExpr(GtExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> of the given field reference with the type
	 * {@link InstanceFieldRef} and stores the resulting level in {@link LookupSwitch#level}.
	 * Additionally, the base of the field will be checked and if the level of the base if stronger
	 * than the resulting <em>security level</em> of the field, then this base
	 * <em>security level</em> will be stored in {@link LookupSwitch#level}.
	 * 
	 * @param v
	 *            Instance field reference for which the <em>security level</em> should be
	 *            calculated.
	 * @see soot.jimple.RefSwitch#caseInstanceFieldRef(soot.jimple.InstanceFieldRef)
	 * @see LookupSwitch#handleFieldAccess(FieldRef)
	 * @see LookupSwitch#handleBase(Value, Value)
	 */
	@Override
	public void caseInstanceFieldRef(InstanceFieldRef v) {
		handleFieldAccess(v);
		Value baseValue = v.getBase();
		handleBase(baseValue, v);
	}

	/**
	 * Looks up the <em>security level</em> for the given instanceof expression and stores the level
	 * in {@link LookupSwitch#level}.
	 * 
	 * @param v
	 *            The instanceof expression for which the <em>security level</em> should be looked
	 *            up.
	 * @see soot.jimple.ExprSwitch#caseInstanceOfExpr(soot.jimple.InstanceOfExpr)
	 * @see LookupSwitch#handleOneValue(Value, Value)
	 */
	@Override
	public void caseInstanceOfExpr(InstanceOfExpr v) {
		Value value = v.getOp();
		handleOneValue(value, v);
	}

	/**
	 * Looks up the <em>security level</em> for the given constant and stores the level in
	 * {@link LookupSwitch#level}. For a {@link IntConstant} this is the weakest available
	 * <em>security level</em>.
	 * 
	 * @param v
	 *            The constant for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ConstantSwitch#caseIntConstant(soot.jimple.IntConstant)
	 */
	@Override
	public void caseIntConstant(IntConstant v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * Looks up the <em>security level</em> of the given invoke expression with the type
	 * {@link InterfaceInvokeExpr} and stores the resulting level in {@link LookupSwitch#level}.
	 * Also the parameter <em>security level</em> and the <em>write effects</em> will be handled.
	 * Additionally, the base of the invoke expression will be checked and if the level of the base
	 * if stronger than the resulting <em>security level</em> of the invoke expression, then this
	 * base <em>security level</em> will be stored in {@link LookupSwitch#level}.
	 * 
	 * @param v
	 *            The invoke expression, for which the level should be looked up.
	 * @see soot.jimple.ExprSwitch#caseInterfaceInvokeExpr(soot.jimple.InterfaceInvokeExpr)
	 * @see LookupSwitch#handleInvokeExpr(InvokeExpr)
	 * @see LookupSwitch#handleBase(Value, Value)
	 */
	@Override
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
		handleInvokeExpr(v);
		Value base = v.getBase();
		handleBase(base, v);
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link LeExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseLeExpr(soot.jimple.LeExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseLeExpr(LeExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given length expression and stores the level in
	 * {@link LookupSwitch#level}. The resulting level is the level of the array.
	 * 
	 * @param v
	 *            The length expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseLengthExpr(soot.jimple.LengthExpr)
	 * @see LookupSwitch#handleUnaryOperation(UnopExpr)
	 */
	@Override
	public void caseLengthExpr(LengthExpr v) {
		handleUnaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given local variable and stores the level in
	 * {@link LookupSwitch#level}. The <em>security level</em> of a {@link Local} can be determined
	 * with the help of the {@link LocalsMap}. This map stores all local variables and the
	 * corresponding <em>security levels</em>.
	 * 
	 * @param v
	 *            The local variable for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.JimpleValueSwitch#caseLocal(soot.Local)
	 * @see LocalsMap
	 */
	@Override
	public void caseLocal(Local l) {
		if (in.containsLocal(l)) {
			this.level = in.getLevelOfLocal(l);
		}
	}

	/**
	 * Looks up the <em>security level</em> for the given constant and stores the level in
	 * {@link LookupSwitch#level}. For a {@link LongConstant} this is the weakest available
	 * <em>security level</em>.
	 * 
	 * @param v
	 *            The constant for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ConstantSwitch#caseLongConstant(soot.jimple.LongConstant)
	 */
	@Override
	public void caseLongConstant(LongConstant v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link LtExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseLtExpr(soot.jimple.LtExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseLtExpr(LtExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link MulExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseMulExpr(soot.jimple.MulExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseMulExpr(MulExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link NeExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseNeExpr(soot.jimple.NeExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseNeExpr(NeExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given negation expression and stores the level
	 * in {@link LookupSwitch#level}.
	 * 
	 * @param v
	 *            The length negation for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseNegExpr(soot.jimple.NegExpr)
	 * @see LookupSwitch#handleUnaryOperation(UnopExpr)
	 */
	@Override
	public void caseNegExpr(NegExpr v) {
		handleUnaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given new expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link NewArrayExpr} this is the weakest available
	 * <em>security level</em>.
	 * 
	 * @param v
	 *            The new expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseNewArrayExpr(soot.jimple.NewArrayExpr)
	 */
	@Override
	public void caseNewArrayExpr(NewArrayExpr v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * Looks up the <em>security level</em> for the given new expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link NewExpr} this is the weakest available
	 * <em>security level</em>.
	 * 
	 * @param v
	 *            The new expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseNewExpr(soot.jimple.NewExpr)
	 */
	@Override
	public void caseNewExpr(NewExpr v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * Looks up the <em>security level</em> for the given new expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link NewMultiArrayExpr} this is the weakest available
	 * <em>security level</em>.
	 * 
	 * @param v
	 *            The new expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseNewMultiArrayExpr(soot.jimple.NewMultiArrayExpr)
	 */
	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * Looks up the <em>security level</em> for the given constant and stores the level in
	 * {@link LookupSwitch#level}. For a {@link NullConstant} this is the weakest available
	 * <em>security level</em>.
	 * 
	 * @param v
	 *            The constant for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ConstantSwitch#caseNullConstant(soot.jimple.NullConstant)
	 */
	@Override
	public void caseNullConstant(NullConstant v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link OrExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseOrExpr(soot.jimple.OrExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseOrExpr(OrExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * The method should look up the <em>security level</em> of a {@link ParameterRef}, but the look
	 * up of the level of a this reference is implemented in the {@link UpdateSwitch}.
	 * 
	 * @param v
	 *            The this reference for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.RefSwitch#caseParameterRef(ParameterRef)
	 * @throws UnimplementedSwitchException
	 *             Method throws always this exception, because this method may not be invoked.
	 */
	@Override
	public void caseParameterRef(ParameterRef v) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase(
				"parameter ref", "SecurityLevelLookupValueSwitch", v.toString(), getSrcLn()));
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link RemExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseRemExpr(soot.jimple.RemExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseRemExpr(RemExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link ShlExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseShlExpr(soot.jimple.ShlExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseShlExpr(ShlExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link ShrExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseShrExpr(soot.jimple.ShrExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseShrExpr(ShrExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> of the given invoke expression with the type
	 * {@link SpecialInvokeExpr} and stores the resulting level in {@link LookupSwitch#level}. Also
	 * the parameter <em>security level</em> and the <em>write effects</em> will be handled.
	 * Additionally, the base of the invoke expression will be checked and if the level of the base
	 * if stronger than the resulting <em>security level</em> of the invoke expression, then this
	 * base <em>security level</em> will be stored in {@link LookupSwitch#level}.
	 * 
	 * @param v
	 *            The invoke expression, for which the level should be looked up.
	 * @see soot.jimple.ExprSwitch#caseSpecialInvokeExpr(soot.jimple.SpecialInvokeExpr)
	 * @see LookupSwitch#handleInvokeExpr(InvokeExpr)
	 * @see LookupSwitch#handleBase(Value, Value)
	 */
	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
		handleInvokeExpr(v);
		Value base = v.getBase();
		handleBase(base, v);
	}

	/**
	 * Looks up the <em>security level</em> of the given field reference with the type
	 * {@link StaticFieldRef} and stores the resulting level in {@link LookupSwitch#level}.
	 * 
	 * @param v
	 *            Static field reference for which the <em>security level</em> should be calculated.
	 * @see soot.jimple.RefSwitch#caseStaticFieldRef(soot.jimple.StaticFieldRef)
	 * @see LookupSwitch#handleFieldAccess(FieldRef)
	 */
	@Override
	public void caseStaticFieldRef(StaticFieldRef v) {
		handleFieldAccess(v);
	}

	/**
	 * Looks up the <em>security level</em> of the given invoke expression with the type
	 * {@link StaticInvokeExpr} and stores the resulting level in {@link LookupSwitch#level}. Also
	 * the parameter <em>security level</em> and the <em>write effects</em> will be handled.
	 * 
	 * @param v
	 *            The invoke expression, for which the level should be looked up.
	 * @see soot.jimple.ExprSwitch#caseStaticInvokeExpr(soot.jimple.StaticInvokeExpr)
	 * @see LookupSwitch#handleInvokeExpr(InvokeExpr)
	 */
	@Override
	public void caseStaticInvokeExpr(StaticInvokeExpr v) {
		handleInvokeExpr(v);
	}

	/**
	 * Looks up the <em>security level</em> for the given constant and stores the level in
	 * {@link LookupSwitch#level}. For a {@link StringConstant} this is the weakest available
	 * <em>security level</em>.
	 * 
	 * @param v
	 *            The constant for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ConstantSwitch#caseStringConstant(soot.jimple.StringConstant)
	 */
	@Override
	public void caseStringConstant(StringConstant v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link SubExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseSubExpr(soot.jimple.SubExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseSubExpr(SubExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * The method should look up the <em>security level</em> of a {@link ThisRef}, but the look up
	 * of the level of a this reference is implemented in the {@link UpdateSwitch}.
	 * 
	 * @param v
	 *            The this reference for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.RefSwitch#caseThisRef(soot.jimple.ThisRef)
	 * @throws UnimplementedSwitchException
	 *             Method throws always this exception, because this method may not be invoked.
	 */
	@Override
	public void caseThisRef(ThisRef v) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("this ref",
				"SecurityLevelLookupValueSwitch", v.toString(), getSrcLn()));
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link UshrExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseUshrExpr(soot.jimple.UshrExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseUshrExpr(UshrExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Looks up the <em>security level</em> of the given invoke expression with the type
	 * {@link VirtualInvokeExpr} and stores the resulting level in {@link LookupSwitch#level}. Also
	 * the parameter <em>security level</em> and the <em>write effects</em> will be handled.
	 * Additionally, the base of the invoke expression will be checked and if the level of the base
	 * if stronger than the resulting <em>security level</em> of the invoke expression, then this
	 * base <em>security level</em> will be stored in {@link LookupSwitch#level}.
	 * 
	 * @param v
	 *            The invoke expression, for which the level should be looked up.
	 * @see soot.jimple.ExprSwitch#caseVirtualInvokeExpr(soot.jimple.VirtualInvokeExpr)
	 * @see LookupSwitch#handleInvokeExpr(InvokeExpr)
	 * @see LookupSwitch#handleBase(Value, Value)
	 */
	@Override
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
		handleInvokeExpr(v);
		Value base = v.getBase();
		handleBase(base, v);
	}

	/**
	 * Looks up the <em>security level</em> for the given binary expression and stores the level in
	 * {@link LookupSwitch#level}. For a {@link XorExpr} this is the strongest operand
	 * <em>security level</em> of the given binary expression.
	 * 
	 * @param v
	 *            The expression for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ExprSwitch#caseXorExpr(soot.jimple.XorExpr)
	 * @see LookupSwitch#handleBinaryOperation(BinopExpr)
	 */
	@Override
	public void caseXorExpr(XorExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * Default case: the method should look up the <em>security level</em> of an {@link Object}, but
	 * a level for an object could not be looked up.
	 * 
	 * @param object
	 *            The object for which the <em>security level</em> should be looked up.
	 * @see soot.jimple.ConstantSwitch#defaultCase(java.lang.Object)
	 * @throws UnimplementedSwitchException
	 *             Method throws always this exception, because this method may not be invoked.
	 */
	@Override
	public void defaultCase(Object object) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("default",
				"SecurityLevelLookupValueSwitch", object.toString(), getSrcLn()));
	}

	/**
	 * Returns the looked up <em>security level</em> of the applied value.
	 * 
	 * @return The calculated <em>security level</em>.
	 * @throws NoSecurityLevelException
	 *             If no <em>security level</em> could be determined, or if until now no level was
	 *             looked up.
	 */
	public String getLevel() throws NoSecurityLevelException {
		if (this.level == null) {
			throw new NoSecurityLevelException(SecurityMessages.accessToValueWithoutSecurityLevel(
					getSrcLn(), getMethodSignature()));
		}
		return level;

	}

	/**
	 * Method that handles the given value as a base and expects that the level
	 * {@link LookupSwitch#level} was calculated previously. If the calculated
	 * <em>security level</em> is weaker than the base level, the base level will be set to the
	 * calculated <em>security level</em>.
	 * 
	 * @param value
	 *            The value which is the base and for which the level should be calculated and
	 *            checked whether it is stronger than the existing level.
	 * @param containing
	 *            The value which encapsulates the base value for logging reasons.
	 */
	private void handleBase(Value value, Value containing) {
		String baseLevel = calculateLevel(value, containing.toString());
		this.level = getMaxLevel(level, baseLevel);
	}

	/**
	 * Method looks up the <em>security level</em> of both operands of the given binary operation
	 * and stores the strongest of these two levels in the variable {@link LookupSwitch#level}.
	 * 
	 * @param expr
	 *            The binary expression, for which the level should be looked up, i.e. the strongest
	 *            <em>security level</em> of the operands will be stored.
	 */
	private void handleBinaryOperation(BinopExpr expr) {
		List<String> levels = new ArrayList<String>();
		Value left = expr.getOp1();
		levels.add(calculateLevel(left, expr.toString()));
		Value right = expr.getOp2();
		levels.add(calculateLevel(right, expr.toString()));
		this.level = getMaxLevel(levels);
	}

	/**
	 * Method looks up the <em>security level</em> of the given field reference. Therefore it checks
	 * whether the level at the field is valid and set this <em>security level</em> to the
	 * calculated level {@link LookupSwitch#level}. If the field is a library field or the level is
	 * not valid the weakest available level is taken instead. Occurring exceptions are logged.
	 * 
	 * @param fieldRef
	 *            Field reference for which the <em>security level</em> should be calculated.
	 */
	private void handleFieldAccess(FieldRef fieldRef) {
		SootField sootField = fieldRef.getField();
		FieldEnvironment field = new FieldEnvironment(sootField, getLog(), getSecurityAnnotation());
		String fieldLevel = getWeakestSecurityLevel();
		if (!field.isLibraryClass()) {
			if (field.isFieldSecurityLevelValid()) {
				fieldLevel = field.getLevel();
			} else {
				logError(SecurityMessages.invalidFieldAnnotation(getMethodSignature(),
						SootUtils.generateFieldSignature(sootField, false, true, true), getSrcLn()));
			}
		} else {
			logWarning(SecurityMessages.accessOfLibraryField(getMethodSignature(),
					SootUtils.generateFieldSignature(sootField, false, true, true), getSrcLn()));
		}
		this.level = fieldLevel;
	}

	/**
	 * Method calculates the <em>security level</em> of the given invoke expression and stores
	 * levels in the variable {@link LookupSwitch#level}. I.e. the method checks also the
	 * <em>security levels</em> of the method parameters. If an error occurs during the check of the
	 * parameter the weakest available <em>security level</em> will be stored. If the invoke
	 * expression is a library method then the strongest level of the invoke expression arguments
	 * will be stored as resulting <em>security level</em>. Additionally, also the
	 * <em>write effects</em> of the invoked method will be checked.
	 * 
	 * @param invokeExpr
	 *            The invoke expression, for which the level should be looked up.
	 * @see TaintTrackingSwitch#calculateInvokeExprLevel(InvokeExpr, boolean)
	 */
	private void handleInvokeExpr(InvokeExpr invokeExpr) {
		this.level = calculateInvokeExprLevel(invokeExpr, true);
	}

	/**
	 * Looks up the <em>security level</em> of the given value and stores the resulting level in
	 * {@link LookupSwitch#level}. The second given value encapsulates the first given value and is
	 * required only for logging reasons.
	 * 
	 * @param value
	 *            The value for which the level should be calculated.
	 * @param containing
	 *            The value which encapsulates the first given value for logging reasons.
	 */
	private void handleOneValue(Value value, Value containing) {
		String valueLevel = calculateLevel(value, containing.toString());
		this.level = valueLevel;
	}

	/**
	 * Looks up the <em>security level</em> for the given unary expression and stores the level in
	 * {@link LookupSwitch#level}.
	 * 
	 * @param expr
	 *            The unary expression for which the <em>security level</em> should be looked up.
	 * @see LookupSwitch#handleOneValue(Value, Value)
	 */
	private void handleUnaryOperation(UnopExpr expr) {
		Value value = expr.getOp();
		handleOneValue(value, expr);
	}
}