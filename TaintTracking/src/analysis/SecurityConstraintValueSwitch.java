package analysis;

import java.util.HashSet;
import java.util.Set;

import model.AnalyzedMethodEnvironment;
import model.ClassEnvironment;
import model.FieldEnvironment;
import model.MethodEnvironment;
import soot.ArrayType;
import soot.Local;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
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
import constraints.ConstraintLocal;
import constraints.ConstraintParameterRef;
import constraints.ConstraintProgramCounterRef;
import constraints.ConstraintReturnRef;
import constraints.ConstraintsSet;
import constraints.IConstraintComponent;
import constraints.LEQConstraint;
import exception.CastInvalidException;
import extractor.UsedObjectStore;

public class SecurityConstraintValueSwitch extends SecurityConstraintSwitch implements JimpleValueSwitch {

	private final Set<IConstraintComponent> constraintComponents = new HashSet<IConstraintComponent>();
	private final Set<LEQConstraint> constraints = new HashSet<LEQConstraint>();

	private SootField field = null;
	private ConstraintLocal local = null;
	private SootMethod method = null;
	private SootClass staticClass = null;

	protected SecurityConstraintValueSwitch(AnalyzedMethodEnvironment methodEnvironment, UsedObjectStore store, ConstraintsSet in,
			ConstraintsSet out) {
		super(methodEnvironment, store, in, out);
	}

	@Override
	public void caseAddExpr(AddExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseAndExpr(AndExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseArrayRef(ArrayRef v) {	
		// TODO Auto-generated method stub
	}

	@Override
	public void caseCastExpr(CastExpr v) {
		if (v.getCastType() instanceof ArrayType) {
			// FIXME... Deny Cast from something into array
			throw new CastInvalidException("Try to cast something into an Array: this is not allowed");
		}
		handleUnaryExpr(v.getOp());
	}

	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		throwNotImplementedException(v.getClass(), v.toString());
	}

	@Override
	public void caseClassConstant(ClassConstant v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseCmpExpr(CmpExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseCmpgExpr(CmpgExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseCmplExpr(CmplExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseDivExpr(DivExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseDoubleConstant(DoubleConstant v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
		SootClass sootClass = v.getMethod().getDeclaringClass();
		handleStatic(sootClass);
		handleInvoke(v);
		addProgramCounterConstraint(sootClass.getName());
	}

	@Override
	public void caseEqExpr(EqExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseFloatConstant(FloatConstant v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseGeExpr(GeExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseGtExpr(GtExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseInstanceFieldRef(InstanceFieldRef v) {
		handleBase(v.getBase());
		handleField(v.getField());
	}

	@Override
	public void caseInstanceOfExpr(InstanceOfExpr v) {
		handleUnaryExpr(v.getOp());
	}

	@Override
	public void caseIntConstant(IntConstant v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
		handleBase(v.getBase());
		handleInvoke(v);
	}

	@Override
	public void caseLeExpr(LeExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseLengthExpr(LengthExpr v) {
		handleUnaryExpr(v.getOp());
	}

	@Override
	public void caseLocal(Local l) {
		addComponent(local = new ConstraintLocal(l));
	}

	@Override
	public void caseLongConstant(LongConstant v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseLtExpr(LtExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseMulExpr(MulExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseNeExpr(NeExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseNegExpr(NegExpr v) {
		handleUnaryExpr(v.getOp());
	}

	@Override
	public void caseNewArrayExpr(NewArrayExpr v) {
		// TODO Auto-generated method stub
	}

	@Override
	public void caseNewExpr(NewExpr v) {
		// TODO Auto-generated method stub
	}

	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		// TODO Auto-generated method stub
	}

	@Override
	public void caseNullConstant(NullConstant v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseOrExpr(OrExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseParameterRef(ParameterRef v) {
		addComponent(new ConstraintParameterRef(v.getIndex(), getAnalyzedSignature()));
	}

	@Override
	public void caseRemExpr(RemExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseShlExpr(ShlExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseShrExpr(ShrExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
		handleBase(v.getBase());
		handleInvoke(v);
	}

	@Override
	public void caseStaticFieldRef(StaticFieldRef v) {
		SootClass sootClass = v.getField().getDeclaringClass();
		handleStatic(sootClass);
		handleField(v.getField());
		addProgramCounterConstraint(sootClass.getName());
	}

	@Override
	public void caseStaticInvokeExpr(StaticInvokeExpr v) {
		SootClass sootClass = v.getMethod().getDeclaringClass();
		handleStatic(sootClass);
		handleInvoke(v);
		addProgramCounterConstraint(sootClass.getName());
	}

	@Override
	public void caseStringConstant(StringConstant v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseSubExpr(SubExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseThisRef(ThisRef v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseUshrExpr(UshrExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
		handleBase(v.getBase());
		handleInvoke(v);
	}

	@Override
	public void caseXorExpr(XorExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void defaultCase(Object object) {
		throwUnknownObjectException(object);
	}

	public Set<IConstraintComponent> getComponents() {
		return constraintComponents;
	}

	public Set<LEQConstraint> getConstraints() {
		return constraints;
	}

	public boolean hasStaticAccess() {
		return staticClass != null;
	}

	public boolean isField() {
		return field != null;
	}

	public boolean isLocal() {
		return local != null;
	}

	public boolean isMethod() {
		return method != null;
	}

	private void addAllComponents(Set<IConstraintComponent> set) {
		constraintComponents.addAll(set);
	}

	private void addAllConstraints(Set<LEQConstraint> set) {
		constraints.addAll(set);
	}

	private void addComponent(IConstraintComponent component) {
		constraintComponents.add(component);
	}

	private void addConstraint(LEQConstraint constraint) {
		constraints.add(constraint);
	}

	private void addParameterConstraints(Set<IConstraintComponent> leftComponents, String signature, int position) {
		IConstraintComponent right = new ConstraintParameterRef(position, signature);
		for (IConstraintComponent left : leftComponents) {
			addConstraint(new LEQConstraint(left, right));
		}
	}

	private void addProgramCounterConstraint(String signature) {
		addConstraint(new LEQConstraint(new ConstraintProgramCounterRef(getAnalyzedSignature()), new ConstraintProgramCounterRef(signature)));
	}

	private void handleBase(Value base) {
		SecurityConstraintValueSwitch baseSwitch = getValueSwitch(base);
		addAllComponents(baseSwitch.getComponents());
		addAllConstraints(baseSwitch.getConstraints());
	}

	private void handleBinaryExpr(Value op1, Value op2) {
		SecurityConstraintValueSwitch switchOp1 = getValueSwitch(op1);
		SecurityConstraintValueSwitch switchOp2 = getValueSwitch(op2);
		addAllComponents(switchOp1.getComponents());
		addAllComponents(switchOp2.getComponents());
		addAllConstraints(switchOp1.getConstraints());
		addAllConstraints(switchOp2.getConstraints());
	}

	private void handleField(SootField sootField) {
		FieldEnvironment fe = getStore().getFieldEnvironment(field = sootField);
		addComponent(fe.getLevel());
	}

	private void handleInvoke(InvokeExpr invokeExpr) {
		method = invokeExpr.getMethod();
		String signature = method.getSignature();
		MethodEnvironment me = getStore().getMethodEnvironment(method);
		addAllConstraints(me.getSignatureContraints());
		for (int i = 0; i < invokeExpr.getArgCount(); i++) {
			SecurityConstraintValueSwitch argSwitch = getValueSwitch(invokeExpr.getArg(i));
			addParameterConstraints(argSwitch.getComponents(), signature, i);
			addAllConstraints(argSwitch.getConstraints());
		}
		addProgramCounterConstraint(signature);
		addComponent(new ConstraintReturnRef(signature));
	}

	private void handleStatic(SootClass sootClass) {
		ClassEnvironment ce = getStore().getClassEnvironment(staticClass = sootClass);
		addAllConstraints(ce.getSignatureContraints());
	}

	private void handleUnaryExpr(Value op) {
		SecurityConstraintValueSwitch switchOp = getValueSwitch(op);
		addAllComponents(switchOp.getComponents());
		addAllConstraints(switchOp.getConstraints());
	}

	protected SootField getField() {
		return field;
	}

	protected ConstraintLocal getConstraintLocal() {
		return local;
	}

	protected SootMethod getMethod() {
		return method;
	}

	protected SootClass getStaticAccess() {
		return staticClass;
	}

}
