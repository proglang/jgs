package analysis;

import java.util.HashSet;
import java.util.Set;

import model.AnalyzedMethodEnvironment;
import model.ClassEnvironment;
import model.FieldEnvironment;
import model.MethodEnvironment;
import soot.Local;
import soot.SootClass;
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
import constraints.Constraints;
import constraints.IConstraint;
import constraints.IConstraintComponent;
import constraints.LEQConstraint;
import extractor.UsedObjectStore;

public class SecurityConstraintValueSwitch extends SecurityConstraintSwitch implements JimpleValueSwitch {

	private final Set<IConstraintComponent> constraintComponents = new HashSet<IConstraintComponent>();
	private final Set<IConstraint> constraints = new HashSet<IConstraint>();
	private Local local = null;
	private SootMethod invokedMethod = null;
	private String staticAccessSignature = null;
	private boolean isField = false;

	protected SecurityConstraintValueSwitch(AnalyzedMethodEnvironment methodEnvironment, UsedObjectStore store, Constraints in,
			Constraints out) {
		super(methodEnvironment, store, in, out);
	}

	@Override
	public void caseDoubleConstant(DoubleConstant v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseFloatConstant(FloatConstant v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseIntConstant(IntConstant v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseLongConstant(LongConstant v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseNullConstant(NullConstant v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseStringConstant(StringConstant v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseClassConstant(ClassConstant v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void defaultCase(Object object) {
		throwUnknownObjectException(object);
	}

	@Override
	public void caseAddExpr(AddExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	private void handleBinaryExpr(Value op1, Value op2) {
		SecurityConstraintValueSwitch op1Switch = getNewValueSwitch();
		SecurityConstraintValueSwitch op2Switch = getNewValueSwitch();
		op1.apply(op1Switch);
		op2.apply(op2Switch);
		addAllComponents(op1Switch.getConstraintComponents());
		addAllComponents(op2Switch.getConstraintComponents());
		addAllConstraints(op1Switch.getInnerConstraints());
		addAllConstraints(op2Switch.getInnerConstraints());
	}

	@Override
	public void caseAndExpr(AndExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
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
	public void caseEqExpr(EqExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseNeExpr(NeExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
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
	public void caseLeExpr(LeExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
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
	public void caseOrExpr(OrExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
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
	public void caseUshrExpr(UshrExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseSubExpr(SubExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseXorExpr(XorExpr v) {
		handleBinaryExpr(v.getOp1(), v.getOp2());
	}

	@Override
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
		handleBase(v.getBase());
		handleInvoke(v);
	}

	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
		handleBase(v.getBase());
		handleInvoke(v);
	}

	@Override
	public void caseStaticInvokeExpr(StaticInvokeExpr v) {
		handleStatic(v.getMethod().getDeclaringClass());
		handleInvoke(v);
		addConstraint(new LEQConstraint(new ConstraintProgramCounterRef(getSootMethod().getSignature()), new ConstraintProgramCounterRef(
				v.getMethod().getDeclaringClass().getName())));
	}

	@Override
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
		handleBase(v.getBase());
		handleInvoke(v);
	}

	@Override
	public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
		handleStatic(v.getMethod().getDeclaringClass());		
		handleInvoke(v);
		addConstraint(new LEQConstraint(new ConstraintProgramCounterRef(getSootMethod().getSignature()), new ConstraintProgramCounterRef(
				v.getMethod().getDeclaringClass().getName())));
	}

	private void handleBase(Value base) {
		SecurityConstraintValueSwitch baseSwitch = getNewValueSwitch();
		base.apply(baseSwitch);
		addAllComponents(baseSwitch.getConstraintComponents());
		addAllConstraints(baseSwitch.getInnerConstraints());
	}

	private void handleStatic(SootClass sootClass) {
		ClassEnvironment ce = store.getClassEnvironment(sootClass);
		addAllConstraints(ce.getSignatureContraints());
		
		staticAccessSignature = sootClass.getName();
	}

	private void handleInvoke(InvokeExpr invokeExpr) {
		invokedMethod = invokeExpr.getMethod();
		String invokedSignature = invokedMethod.getSignature();
		MethodEnvironment me = store.getMethodEnvironment(invokedMethod);
		// FIXME: Recursive call?
		addAllConstraints(me.getSignatureContraints());
		for (int i = 0; i < invokeExpr.getArgCount(); i++) {
			Value arg = invokeExpr.getArg(i);
			SecurityConstraintValueSwitch argSwitch = getNewValueSwitch();
			arg.apply(argSwitch);
			for (IConstraintComponent comp : argSwitch.getConstraintComponents()) {
				addConstraint(new LEQConstraint(comp, new ConstraintParameterRef(i, invokedSignature)));
			}
			addAllConstraints(argSwitch.getInnerConstraints());
		}
		addConstraint(new LEQConstraint(new ConstraintProgramCounterRef(getSootMethod().getSignature()), new ConstraintProgramCounterRef(
				invokedSignature)));
		
		addComponent(new ConstraintReturnRef(invokedSignature));
	}

	@Override
	public void caseCastExpr(CastExpr v) {
		Value arg = v.getOp();
		SecurityConstraintValueSwitch argSwitch = getNewValueSwitch();
		arg.apply(argSwitch);
		addAllComponents(argSwitch.constraintComponents);
		addAllConstraints(argSwitch.getInnerConstraints());
	}

	@Override
	public void caseInstanceOfExpr(InstanceOfExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseNewArrayExpr(NewArrayExpr v) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void caseStaticFieldRef(StaticFieldRef v) {
		handleStatic(v.getField().getDeclaringClass());
		isField = true;
		FieldEnvironment fe = store.getFieldEnvironment(v.getField());
		addComponent(fe.getLevel());
		addConstraint(new LEQConstraint(new ConstraintProgramCounterRef(getSootMethod().getSignature()), new ConstraintProgramCounterRef(v
				.getField().getDeclaringClass().getName())));
	}

	@Override
	public void caseInstanceFieldRef(InstanceFieldRef v) {
		isField = true;
		handleBase(v.getBase());
		FieldEnvironment fe = store.getFieldEnvironment(v.getField());
		addComponent(fe.getLevel());
	}

	@Override
	public void caseParameterRef(ParameterRef v) {
		int position = v.getIndex();
		String signature = getSootMethod().getSignature();
		addComponent(new ConstraintParameterRef(position, signature));
	}

	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		throwNotImplementedException(v.getClass(), v.toString());
	}

	@Override
	public void caseThisRef(ThisRef v) {
		addComponent(getWeakestSecurityLevel());
	}

	@Override
	public void caseLocal(Local l) {
		this.local = l;
		addComponent(new ConstraintLocal(l));
	}

	public Set<IConstraintComponent> getConstraintComponents() {
		return constraintComponents;
	}

	public Set<IConstraint> getInnerConstraints() {
		return constraints;
	}

	private void addComponent(IConstraintComponent component) {
		constraintComponents.add(component);
	}

	private void addAllComponents(Set<IConstraintComponent> set) {
		constraintComponents.addAll(set);
	}

	private void addConstraint(IConstraint constraint) {
		constraints.add(constraint);
	}

	private void addAllConstraints(Set<IConstraint> set) {
		constraints.addAll(set);
	}

	public boolean isLocal() {
		return local != null;
	}

	public Local getLocal() {
		if (!isLocal()) throw new NullPointerException("Boooo"); // FIXME: Error
		return local;
	}

	public boolean isInvokeExpr() {
		return invokedMethod != null;
	}

	public SootMethod getInvokeMethod() {
		if (!isInvokeExpr()) throw new NullPointerException("Boooo"); // FIXME: Error
		return invokedMethod;
	}

	public boolean hasStaticAccess() {
		return staticAccessSignature != null;
	}

	public String getStaticAccessSignature() {
		if (!hasStaticAccess()) throw new NullPointerException("Boooo"); // FIXME: Error
		return staticAccessSignature;
	}
	
	public boolean isField() {
		return isField;
	}

}
