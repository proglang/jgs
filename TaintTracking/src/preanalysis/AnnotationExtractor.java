package preanalysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.SootException.ExtractionException;
import exception.SootException.UnimplementedSwitchException;
import exception.SootException.UnpreparedEnvironmentException;

import logging.SecurityLogger;
import model.AnalyzedMethodEnvironment;
import model.ClassEnvironment;
import model.FieldEnvironment;
import model.MethodEnvironment;
import model.MethodEnvironment.MethodParameter;

import security.SecurityAnnotation;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.jimple.*;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.Chain;
import utils.SootUtils;

/**
 * <h1>Annotation Transformer</h1>
 * 
 * The class {@link AnnotationExtractor} acts on a {@link Body}. This class will
 * be used to calculate all {@link MethodEnvironment}s, {@link FieldEnvironment}
 * s and {@link AnalyzedMethodEnvironment}s. I.e. the Transformer stores for
 * every Method in the Body a {@link MethodEnvironment} and also for Field a
 * {@link FieldEnvironment}. For every directly processed Method an
 * {@link AnalyzedMethodEnvironment} will be stored. If an error occurs during
 * the transformation this will be indicated by a flag.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class AnnotationExtractor extends SceneTransformer {

	/**
	 * DOC
	 * 
	 * @author Thomas Vogel
	 * 
	 */
	public static class UsedObjectStore {

		/**
		 * DOC
		 */
		private final Map<SootMethod, MethodEnvironment> methods = new HashMap<SootMethod, MethodEnvironment>();
		/**
		 * DOC
		 */
		private final Map<SootClass, ClassEnvironment> classes = new HashMap<SootClass, ClassEnvironment>();
		/**
		 * DOC
		 */
		private final Map<SootField, FieldEnvironment> fields = new HashMap<SootField, FieldEnvironment>();

		/**
		 * DOC
		 * 
		 * @param sootMethod
		 * @return
		 * @throws UnpreparedEnvironmentException
		 */
		public MethodEnvironment getMethodEnvironment(SootMethod sootMethod)
				throws UnpreparedEnvironmentException {
			if (methods.containsKey(sootMethod)) {
				MethodEnvironment me = methods.get(sootMethod);
				return me;
			}
			// TODO: Exception message
			for (SootMethod method : methods.keySet()) {
				System.out.println(methods.get(method).getSootMethod().getSignature());
			}
			throw new UnpreparedEnvironmentException(
					"Method wasn't prepared for the analysis, i.e. the method environment doesn't exist.");
		}

		/**
		 * DOC
		 * 
		 * @param sootClass
		 * @return
		 * @throws UnpreparedEnvironmentException
		 */
		public ClassEnvironment getClassEnvironment(SootClass sootClass)
				throws UnpreparedEnvironmentException {
			if (classes.containsKey(sootClass)) {
				ClassEnvironment ce = classes.get(sootClass);
				return ce;
			}
			// TODO: Exception message
			throw new UnpreparedEnvironmentException(
					"Class wasn't prepared for the analysis, i.e. the class environment doesn't exist.");
		}

		/**
		 * DOC
		 * 
		 * @param sootField
		 * @return
		 * @throws UnpreparedEnvironmentException
		 */
		public FieldEnvironment getFieldEnvironment(SootField sootField)
				throws UnpreparedEnvironmentException {
			if (fields.containsKey(sootField)) {
				FieldEnvironment fe = fields.get(sootField);
				return fe;
			}
			// TODO: Exception message
			throw new UnpreparedEnvironmentException(
					"Field wasn't prepared for the analysis, i.e. the field environment doesn't exist.");
		}

		/**
		 * DOC
		 * 
		 * @param me
		 */
		private void addMethodEnvironment(MethodEnvironment me) {
			methods.put(me.getSootMethod(), me);
		}

		/**
		 * DOC
		 * 
		 * @param ce
		 */
		private void addClassEnvironment(ClassEnvironment ce) {
			classes.put(ce.getSootClass(), ce);
		}

		/**
		 * DOC
		 * 
		 * @param fe
		 */
		private void addFieldEnvironment(FieldEnvironment fe) {
			fields.put(fe.getSootField(), fe);
		}

		/**
		 * DOC
		 * 
		 * @param sootMethod
		 * @return
		 */
		private boolean containsMethod(SootMethod sootMethod) {
			return methods.containsKey(sootMethod);
		}

		/**
		 * DOC
		 * 
		 * @param sootClass
		 * @return
		 */
		private boolean containsClass(SootClass sootClass) {
			return classes.containsKey(sootClass);
		}

		/**
		 * DOC
		 * 
		 * @param sootField
		 * @return
		 */
		private boolean containsField(SootField sootField) {
			return fields.containsKey(sootField);
		}

	}

	/**
	 * DOC
	 */
	private JimpleValueSwitch valueSwitch = new JimpleValueSwitch() {

		/**
		 * DOC
		 * 
		 * @see soot.jimple.RefSwitch#caseThisRef(soot.jimple.ThisRef)
		 */
		@Override
		public void caseThisRef(ThisRef v) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.RefSwitch#caseStaticFieldRef(soot.jimple.StaticFieldRef)
		 */
		@Override
		public void caseStaticFieldRef(StaticFieldRef v) {
			addFieldEvironmentForField(v.getField());
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.RefSwitch#caseParameterRef(soot.jimple.ParameterRef)
		 */
		@Override
		public void caseParameterRef(ParameterRef v) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.RefSwitch#caseInstanceFieldRef(soot.jimple.InstanceFieldRef)
		 */
		@Override
		public void caseInstanceFieldRef(InstanceFieldRef v) {
			addFieldEvironmentForField(v.getField());
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.RefSwitch#caseCaughtExceptionRef(soot.jimple.CaughtExceptionRef)
		 */
		@Override
		public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.RefSwitch#caseArrayRef(soot.jimple.ArrayRef)
		 */
		@Override
		public void caseArrayRef(ArrayRef v) {
			v.getBase().apply(valueSwitch);
			v.getIndex().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseXorExpr(soot.jimple.XorExpr)
		 */
		@Override
		public void caseXorExpr(XorExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseVirtualInvokeExpr(soot.jimple.VirtualInvokeExpr)
		 */
		@Override
		public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
			addMethodEnvironmentForMethod(v.getMethod());
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseUshrExpr(soot.jimple.UshrExpr)
		 */
		@Override
		public void caseUshrExpr(UshrExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseSubExpr(soot.jimple.SubExpr)
		 */
		@Override
		public void caseSubExpr(SubExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseStaticInvokeExpr(soot.jimple.StaticInvokeExpr)
		 */
		@Override
		public void caseStaticInvokeExpr(StaticInvokeExpr v) {
			addMethodEnvironmentForMethod(v.getMethod());
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseSpecialInvokeExpr(soot.jimple.SpecialInvokeExpr)
		 */
		@Override
		public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
			addMethodEnvironmentForMethod(v.getMethod());
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseShrExpr(soot.jimple.ShrExpr)
		 */
		@Override
		public void caseShrExpr(ShrExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseShlExpr(soot.jimple.ShlExpr)
		 */
		@Override
		public void caseShlExpr(ShlExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseRemExpr(soot.jimple.RemExpr)
		 */
		@Override
		public void caseRemExpr(RemExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseOrExpr(soot.jimple.OrExpr)
		 */
		@Override
		public void caseOrExpr(OrExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseNewMultiArrayExpr(soot.jimple.NewMultiArrayExpr)
		 */
		@Override
		public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
			for (int i = 0; i < v.getSizeCount(); i++) {
				v.getSize(i).apply(valueSwitch);
			}
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseNewExpr(soot.jimple.NewExpr)
		 */
		@Override
		public void caseNewExpr(NewExpr v) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseNewArrayExpr(soot.jimple.NewArrayExpr)
		 */
		@Override
		public void caseNewArrayExpr(NewArrayExpr v) {
			v.getSize().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseNegExpr(soot.jimple.NegExpr)
		 */
		@Override
		public void caseNegExpr(NegExpr v) {
			v.getOp().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseNeExpr(soot.jimple.NeExpr)
		 */
		@Override
		public void caseNeExpr(NeExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseMulExpr(soot.jimple.MulExpr)
		 */
		@Override
		public void caseMulExpr(MulExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseLtExpr(soot.jimple.LtExpr)
		 */
		@Override
		public void caseLtExpr(LtExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseLengthExpr(soot.jimple.LengthExpr)
		 */
		@Override
		public void caseLengthExpr(LengthExpr v) {
			v.getOp().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseLeExpr(soot.jimple.LeExpr)
		 */
		@Override
		public void caseLeExpr(LeExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseInterfaceInvokeExpr(soot.jimple.InterfaceInvokeExpr)
		 */
		@Override
		public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
			addMethodEnvironmentForMethod(v.getMethod());
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseInstanceOfExpr(soot.jimple.InstanceOfExpr)
		 */
		@Override
		public void caseInstanceOfExpr(InstanceOfExpr v) {
			v.getOp().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseGtExpr(soot.jimple.GtExpr)
		 */
		@Override
		public void caseGtExpr(GtExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseGeExpr(soot.jimple.GeExpr)
		 */
		@Override
		public void caseGeExpr(GeExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseEqExpr(soot.jimple.EqExpr)
		 */
		@Override
		public void caseEqExpr(EqExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseDynamicInvokeExpr(soot.jimple.DynamicInvokeExpr)
		 */
		@Override
		public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
			addMethodEnvironmentForMethod(v.getMethod());
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseDivExpr(soot.jimple.DivExpr)
		 */
		@Override
		public void caseDivExpr(DivExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseCmplExpr(soot.jimple.CmplExpr)
		 */
		@Override
		public void caseCmplExpr(CmplExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseCmpgExpr(soot.jimple.CmpgExpr)
		 */
		@Override
		public void caseCmpgExpr(CmpgExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseCmpExpr(soot.jimple.CmpExpr)
		 */
		@Override
		public void caseCmpExpr(CmpExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseCastExpr(soot.jimple.CastExpr)
		 */
		@Override
		public void caseCastExpr(CastExpr v) {
			v.getOp().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseAndExpr(soot.jimple.AndExpr)
		 */
		@Override
		public void caseAndExpr(AndExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ExprSwitch#caseAddExpr(soot.jimple.AddExpr)
		 */
		@Override
		public void caseAddExpr(AddExpr v) {
			v.getOp1().apply(valueSwitch);
			v.getOp2().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ConstantSwitch#defaultCase(java.lang.Object)
		 */
		@Override
		public void defaultCase(Object object) {
			throw new UnimplementedSwitchException(
					"Stmt switch not implemented");

		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ConstantSwitch#caseStringConstant(soot.jimple.StringConstant)
		 */
		@Override
		public void caseStringConstant(StringConstant v) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ConstantSwitch#caseNullConstant(soot.jimple.NullConstant)
		 */
		@Override
		public void caseNullConstant(NullConstant v) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ConstantSwitch#caseLongConstant(soot.jimple.LongConstant)
		 */
		@Override
		public void caseLongConstant(LongConstant v) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ConstantSwitch#caseIntConstant(soot.jimple.IntConstant)
		 */
		@Override
		public void caseIntConstant(IntConstant v) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ConstantSwitch#caseFloatConstant(soot.jimple.FloatConstant)
		 */
		@Override
		public void caseFloatConstant(FloatConstant v) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ConstantSwitch#caseDoubleConstant(soot.jimple.DoubleConstant)
		 */
		@Override
		public void caseDoubleConstant(DoubleConstant v) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.ConstantSwitch#caseClassConstant(soot.jimple.ClassConstant)
		 */
		@Override
		public void caseClassConstant(ClassConstant v) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.JimpleValueSwitch#caseLocal(soot.Local)
		 */
		@Override
		public void caseLocal(Local l) {
		}

	};

	/**
	 * DOC
	 */
	private StmtSwitch stmtSwitch = new StmtSwitch() {

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseAssignStmt(soot.jimple.AssignStmt)
		 */
		@Override
		public void caseAssignStmt(AssignStmt stmt) {
			stmt.getLeftOp().apply(valueSwitch);
			stmt.getRightOp().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseBreakpointStmt(soot.jimple.BreakpointStmt)
		 */
		@Override
		public void caseBreakpointStmt(BreakpointStmt stmt) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseEnterMonitorStmt(soot.jimple.EnterMonitorStmt)
		 */
		@Override
		public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
			stmt.getOp().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseExitMonitorStmt(soot.jimple.ExitMonitorStmt)
		 */
		@Override
		public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
			stmt.getOp().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseGotoStmt(soot.jimple.GotoStmt)
		 */
		@Override
		public void caseGotoStmt(GotoStmt stmt) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseIdentityStmt(soot.jimple.IdentityStmt)
		 */
		@Override
		public void caseIdentityStmt(IdentityStmt stmt) {
			stmt.getLeftOp().apply(valueSwitch);
			stmt.getRightOp().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseIfStmt(soot.jimple.IfStmt)
		 */
		@Override
		public void caseIfStmt(IfStmt stmt) {
			stmt.getCondition().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseInvokeStmt(soot.jimple.InvokeStmt)
		 */
		@Override
		public void caseInvokeStmt(InvokeStmt stmt) {
			stmt.getInvokeExpr().apply(valueSwitch);
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseLookupSwitchStmt(soot.jimple.LookupSwitchStmt)
		 */
		@Override
		public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
			// TODO: Consider reaction
			// stmt.getKey().apply(valueSwitch);
			// for (int i = 0; i < stmt.getTargetCount(); i++) {
			// stmt.getTarget(i).apply(valueSwitch);
			// }
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseNopStmt(soot.jimple.NopStmt)
		 */
		@Override
		public void caseNopStmt(NopStmt stmt) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseRetStmt(soot.jimple.RetStmt)
		 */
		@Override
		public void caseRetStmt(RetStmt stmt) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseReturnStmt(soot.jimple.ReturnStmt)
		 */
		@Override
		public void caseReturnStmt(ReturnStmt stmt) {
			stmt.getOp().apply(valueSwitch);

		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseReturnVoidStmt(soot.jimple.ReturnVoidStmt)
		 */
		@Override
		public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseTableSwitchStmt(soot.jimple.TableSwitchStmt)
		 */
		@Override
		public void caseTableSwitchStmt(TableSwitchStmt stmt) {
			// TODO: Consider reaction
		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#caseThrowStmt(soot.jimple.ThrowStmt)
		 */
		@Override
		public void caseThrowStmt(ThrowStmt stmt) {
			// TODO: Consider reaction

		}

		/**
		 * DOC
		 * 
		 * @see soot.jimple.StmtSwitch#defaultCase(java.lang.Object)
		 */
		@Override
		public void defaultCase(Object obj) {
			throw new UnimplementedSwitchException(
					"Stmt switch not implemented");

		}
	};

	/**
	 * DOC
	 * 
	 * @author Thomas Vogel
	 * 
	 */
	private class AnnotationChecker extends
			ForwardFlowAnalysis<Unit, List<Unit>> {

		/**
		 * DOC
		 * 
		 * @param graph
		 */
		public AnnotationChecker(DirectedGraph<Unit> graph) {
			super(graph);
			doAnalysis();
		}

		/**
		 * DOC
		 * 
		 * @see soot.toolkits.scalar.AbstractFlowAnalysis#copy(java.lang.Object,
		 *      java.lang.Object)
		 */
		@Override
		protected void copy(List<Unit> source, List<Unit> dest) {

		}

		/**
		 * DOC
		 * 
		 * @see soot.toolkits.scalar.AbstractFlowAnalysis#entryInitialFlow()
		 */
		@Override
		protected List<Unit> entryInitialFlow() {
			return new ArrayList<Unit>();
		}

		/**
		 * DOC
		 * 
		 * @see soot.toolkits.scalar.FlowAnalysis#flowThrough(java.lang.Object,
		 *      java.lang.Object, java.lang.Object)
		 */
		@Override
		protected void flowThrough(List<Unit> in, Unit d, List<Unit> out) {
			d.apply(stmtSwitch);

		}

		/**
		 * DOC
		 * 
		 * @see soot.toolkits.scalar.AbstractFlowAnalysis#merge(java.lang.Object,
		 *      java.lang.Object, java.lang.Object)
		 */
		@Override
		protected void merge(List<Unit> in1, List<Unit> in2, List<Unit> out) {
		}

		/**
		 * DOC
		 * 
		 * @see soot.toolkits.scalar.AbstractFlowAnalysis#newInitialFlow()
		 */
		@Override
		protected List<Unit> newInitialFlow() {
			return new ArrayList<Unit>();
		}

	}

	/**
	 * DOC
	 */
	private boolean annotationValidity = true;
	/**
	 * DOC
	 */
	final private SecurityLogger log;
	/**
	 * DOC
	 */
	final private UsedObjectStore store = new UsedObjectStore();
	/**
	 * DOC
	 */
	final private SecurityAnnotation securityAnnotation;

	/**
	 * DOC
	 * 
	 * @param log
	 * @param securityAnnotation
	 */
	public AnnotationExtractor(SecurityLogger log,
			SecurityAnnotation securityAnnotation) {
		super();
		this.log = log;
		this.securityAnnotation = securityAnnotation;
	}

	/**
	 * DOC
	 * 
	 * @param sootClass
	 */
	private void addClassEnvironmentForClass(SootClass sootClass) {
		if (annotationValidity) {
			if (!store.containsClass(sootClass)) {
				ClassEnvironment ce = checkAndBuildClassEnvironment(sootClass);
				store.addClassEnvironment(ce);
			}
		}
	}

	/**
	 * DOC
	 * 
	 * @param sootField
	 */
	private void addFieldEvironmentForField(SootField sootField) {
		if (annotationValidity) {
			if (!store.containsField(sootField)) {
				FieldEnvironment fe = checkAndBuildFieldEnvironment(sootField);
				store.addFieldEnvironment(fe);
			}
		}
	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 */
	private void addMethodEnvironmentForMethod(SootMethod sootMethod) {
		addMethodEnvironmentForMethod(sootMethod, true);

	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @param overriden
	 */
	private void addMethodEnvironmentForMethod(SootMethod sootMethod,
			boolean overriden) {
		if (annotationValidity) {
			if (!store.containsMethod(sootMethod)) {
				MethodEnvironment me = checkAndBuildMethodEnvironment(sootMethod);
				store.addMethodEnvironment(me);
			}
			if (overriden && SootUtils.overridesMethod(sootMethod)) {
				List<SootMethod> methods = SootUtils
						.getOverridenMethods(sootMethod);
				if (methods.size() >= 1) {
					addMethodEnvironmentForMethod(methods.get(0), false);
				}
			}
		}

	}

	/**
	 * DOC
	 * 
	 * @param sootClass
	 * @return
	 */
	private ClassEnvironment checkAndBuildClassEnvironment(SootClass sootClass) {
		boolean isLibrary = sootClass.isJavaLibraryClass();
		boolean hasClassWriteEffectAnnotation = securityAnnotation
				.hasClassWriteEffectAnnotation(sootClass);
		boolean hasClassWriteEffects = securityAnnotation
				.hasClassWriteEffects(sootClass);
		List<String> classWriteEffects = new ArrayList<String>();
		if (!isLibrary) {
			if (hasClassWriteEffectAnnotation && hasClassWriteEffects) {
				try {
					classWriteEffects.addAll(securityAnnotation
							.extractClassEffects(sootClass));
				} catch (Exception e) {
					// TODO: Logging
					log.error(SootUtils.generateFileName(sootClass), 0,
							"Class with write effects that couldn't be extracted.");
					annotationValidity = false;
				}
			}
		} else {
			try {
				classWriteEffects.addAll(securityAnnotation
						.getLibraryClassWriteEffects(sootClass));
			} catch (Exception e) {
				// TODO: Logging
				log.error(SootUtils.generateFileName(sootClass), 0,
						"Library class with write effects that couldn't be looked up.");
				annotationValidity = false;
			}
		}
		ClassEnvironment ce = new ClassEnvironment(sootClass,
				classWriteEffects, log, securityAnnotation);
		return ce;
	}

	/**
	 * DOC
	 * 
	 * @param sootField
	 * @return
	 */
	private FieldEnvironment checkAndBuildFieldEnvironment(SootField sootField) {
		SootClass declaringClass = sootField.getDeclaringClass();
		boolean isLibrary = declaringClass.isJavaLibraryClass();
		String fieldSecurityLevel = null;
		boolean hasFieldSecurityAnnotation = securityAnnotation
				.hasFieldSecurityAnnotation(sootField);
		boolean hasFieldSecurityLevel = securityAnnotation
				.hasFieldSecurityLevel(sootField);
		if (!isLibrary) {
			if (hasFieldSecurityAnnotation && hasFieldSecurityLevel) {
				try {
					fieldSecurityLevel = securityAnnotation
							.extractFieldSecurityLevel(sootField);
				} catch (ExtractionException e) {
					// TODO: Logging
					log.error(SootUtils.generateFileName(declaringClass), 0,
							"Field with security level that can't be extracted.");
					annotationValidity = false;
				}
			} else {
				// TODO: Logging
				log.error(SootUtils.generateFileName(declaringClass), 0,
						"Field without a field security level.");
				annotationValidity = false;
			}
		} else {
			try {
				fieldSecurityLevel = securityAnnotation
						.getLibraryFieldSecurityLevel(sootField);
			} catch (Exception e) {
				// TODO: Logging
				log.error(SootUtils.generateFileName(declaringClass), 0,
						"Library field with security level that can't be looked up.");
				annotationValidity = false;
			}
		}
		List<String> classWriteEffects = new ArrayList<String>();
		addClassEnvironmentForClass(declaringClass);
		if (annotationValidity) {
			try {
				ClassEnvironment ce = store.getClassEnvironment(declaringClass);
				classWriteEffects.addAll(ce.getWriteEffects());
			} catch (UnpreparedEnvironmentException e) {
				// TODO: Logging
				log.error(SootUtils.generateFileName(declaringClass), 0,
						"Can't access the additional information of the class.");
				annotationValidity = false;
			}
		}
		FieldEnvironment fe = new FieldEnvironment(sootField,
				fieldSecurityLevel, classWriteEffects, log, securityAnnotation);
		return fe;
	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @return
	 */
	private MethodEnvironment checkAndBuildMethodEnvironment(
			SootMethod sootMethod) {
		SootClass declaringClass = sootMethod.getDeclaringClass();
		boolean isLibrary = sootMethod.isJavaLibraryMethod();
		boolean isIdFunction = securityAnnotation.isIdFunction(sootMethod);
		boolean isClinit = SootUtils.isClinitMethod(sootMethod);
		boolean isInit = SootUtils.isInitMethod(sootMethod);
		boolean isVoid = SootUtils.isVoidMethod(sootMethod);
		boolean isSootSecurityMethod = securityAnnotation
				.isMethodOfSootSecurityLevelClass(sootMethod);
		String returnSecurityLevel = null;
		int parameterCount = sootMethod.getParameterCount();
		List<MethodParameter> parameterSecurityLevel = new ArrayList<MethodParameter>();
		List<String> methodWriteEffects = new ArrayList<String>();
		List<String> classWriteEffects = new ArrayList<String>();
		if (!isLibrary) {
			boolean hasReturnSecurityAnnotation = securityAnnotation
					.hasReturnSecurityAnnotation(sootMethod);
			boolean hasReturnSecurityLevel = securityAnnotation
					.hasReturnSecurityLevel(sootMethod);
			boolean hasParameterSecurityAnnotation = securityAnnotation
					.hasParameterSecurityAnnotation(sootMethod);
			boolean hasParameterSecurityLevels = securityAnnotation
					.hasParameterSecurityLevels(sootMethod);
			boolean hasMethodWriteEffectAnnotation = securityAnnotation
					.hasMethodWriteEffectAnnotation(sootMethod);
			boolean hasMethodWriteEffects = securityAnnotation
					.hasMethodWriteEffects(sootMethod);
			if (!isVoid) {
				if (hasReturnSecurityAnnotation && hasReturnSecurityLevel) {
					try {
						returnSecurityLevel = securityAnnotation
								.extractReturnSecurityLevel(sootMethod);
					} catch (ExtractionException e) {
						// TODO: Logging
						log.error(SootUtils.generateFileName(sootMethod), 0,
								"Normal method with a return security level that can't be extracted.");
						annotationValidity = false;
					}
				} else {
					// TODO: Logging
					log.error(SootUtils.generateFileName(sootMethod), 0,
							"Normal method without a return security level.");
					annotationValidity = false;
				}
			} else {
				if (!hasReturnSecurityLevel && !hasReturnSecurityAnnotation) {
					// RENEW: change back to null
					returnSecurityLevel = SecurityAnnotation.VOID_LEVEL;
				} else {
					// TODO: Logging
					log.error(SootUtils.generateFileName(sootMethod), 0,
							"Void method with return security level.");
					annotationValidity = false;
				}
			}
			if (hasMethodWriteEffectAnnotation && hasMethodWriteEffects) {
				try {
					methodWriteEffects.addAll(securityAnnotation
							.extractMethodEffects(sootMethod));
				} catch (ExtractionException e) {
					// TODO: Logging
					log.error(SootUtils.generateFileName(sootMethod), 0,
							"Method with write effects that can't be extracted.");
					annotationValidity = false;
				}
			}
			if (parameterCount != 0) {
				if (hasParameterSecurityAnnotation
						&& hasParameterSecurityLevels) {
					List<String> parameterLevels = new ArrayList<String>();
					try {
						parameterLevels.addAll(securityAnnotation
								.extractParameterSecurityLevels(sootMethod));
					} catch (ExtractionException e) {
						// TODO: Logging
						log.error(SootUtils.generateFileName(sootMethod), 0,
								"Method with parameter security levels that can't be extracted.");
						annotationValidity = false;
					}
					if (parameterLevels.size() == parameterCount) {
						List<String> names = SootUtils
								.getParameterNames(sootMethod);
						for (int i = 0; i < parameterLevels.size(); i++) {
							Type type = sootMethod.getParameterType(i);
							String level = parameterLevels.get(i);
							String name = (parameterCount == names.size()) ? names
									.get(i) : "arg" + (i + 1);
							MethodParameter mp = new MethodParameter(i, name,
									type, level);
							parameterSecurityLevel.add(mp);
						}
					} else {
						// TODO: Logging
						log.error(SootUtils.generateFileName(sootMethod), 0,
								"Method with wrong count of parameter security levels.");
						annotationValidity = false;
					}
				} else if (isIdFunction && parameterCount == 1) {
					String level = securityAnnotation
							.getReturnSecurityLevelOfIdFunction(sootMethod);
					Type type = sootMethod.getParameterType(0);
					String name = "arg0";
					MethodParameter mp = new MethodParameter(0, name, type,
							level);
					parameterSecurityLevel.add(mp);
				} else {
					// TODO: Logging
					log.error(SootUtils.generateFileName(sootMethod), 0,
							"Method with required parameter security levels.");
					annotationValidity = false;
				}
			}
		} else {

			List<String> parameterLevels = new ArrayList<String>();
			try {
				parameterLevels.addAll(securityAnnotation
						.getLibraryParameterSecurityLevel(sootMethod));
			} catch (Exception e) {
				// TODO: Logging
				log.error(SootUtils.generateFileName(sootMethod), 0,
						"Library method with parameter security level that can't be looked up.");
				annotationValidity = false;
			}
			if (parameterLevels.size() == parameterCount) {
				List<String> names = SootUtils.getParameterNames(sootMethod);
				for (int i = 0; i < parameterLevels.size(); i++) {
					Type type = sootMethod.getParameterType(i);
					String level = parameterLevels.get(i);
					String name = (parameterCount == names.size()) ? names
							.get(i) : "arg" + (i + 1);
					MethodParameter mp = new MethodParameter(i, name, type,
							level);
					parameterSecurityLevel.add(mp);
				}
			} else {
				// TODO: Logging
				log.error(SootUtils.generateFileName(sootMethod), 0,
						"Method with wrong count of parameter security levels.");
				annotationValidity = false;
			}

			if (isVoid) {
				returnSecurityLevel = SecurityAnnotation.VOID_LEVEL;
			} else {
				try {
					returnSecurityLevel = securityAnnotation
							.getLibraryReturnSecurityLevel(sootMethod,
									parameterLevels);
				} catch (Exception e) {
					// TODO: Logging
					log.error(SootUtils.generateFileName(sootMethod), 0,
							"Library method with return security level that can't be looked up.");
					annotationValidity = false;
				}
			}
			try {
				methodWriteEffects.addAll(securityAnnotation
						.getLibraryWriteEffects(sootMethod));
			} catch (Exception e) {
				// TODO: Logging
				log.error(SootUtils.generateFileName(sootMethod), 0,
						"Library method with write effects that can't be looked up.");
				annotationValidity = false;
			}

		}
		addClassEnvironmentForClass(declaringClass);
		if (annotationValidity) {
			try {
				ClassEnvironment ce = store.getClassEnvironment(declaringClass);
				classWriteEffects.addAll(ce.getWriteEffects());
			} catch (UnpreparedEnvironmentException e) {
				// TODO: Logging
				log.error(SootUtils.generateFileName(declaringClass), 0,
						"Can't access the additional information of the class.");
				annotationValidity = false;
			}
		}
		MethodEnvironment methodEnvironment = new MethodEnvironment(sootMethod,
				isIdFunction, isClinit, isInit, isVoid, isSootSecurityMethod,
				parameterSecurityLevel, returnSecurityLevel,
				methodWriteEffects, classWriteEffects, log, securityAnnotation);
		return methodEnvironment;
	}

	/**
	 * DOC
	 * 
	 * @return
	 */
	public boolean checkReasonability() {
		if (annotationValidity) {
			for (SootClass sootClass : store.classes.keySet()) {
				ClassEnvironment ce = store.classes.get(sootClass);
				if (!ce.isLibrary()) {
					annotationValidity = annotationValidity
							&& ce.isReasonable();
				}
			}
		}
		if (annotationValidity) {
			for (SootField sootField : store.fields.keySet()) {
				FieldEnvironment fe = store.fields.get(sootField);
				if (!fe.isLibraryClass()) {
					annotationValidity = annotationValidity
							&& fe.isReasonable();
				}
			}
		}
		if (annotationValidity) {
			for (SootMethod sootMethod : store.methods.keySet()) {
				MethodEnvironment me = store.methods.get(sootMethod);
				if (!me.isLibraryMethod()) {
					annotationValidity = annotationValidity
							&& me.isReasonable();
				}
			}
		}
		return annotationValidity;
	}

	/**
	 * DOC
	 * 
	 * @return
	 */
	public UsedObjectStore getUsedObjectStore() {
		return store;
	}

	@Override
	protected void internalTransform(String phaseName, Map options) {
		Chain<SootClass> classes = Scene.v().getApplicationClasses();
		for (SootClass sootClass : classes) {
			addClassEnvironmentForClass(sootClass);
			for (SootField sootField : sootClass.getFields()) {
				addFieldEvironmentForField(sootField);
			}
			if (!SootUtils.containsStaticInitializer(sootClass.getMethods())) {
				SootUtils
						.generatedEmptyStaticInitializer(sootClass);
			}
			for (SootMethod sootMethod : sootClass.getMethods()) {
				UnitGraph graph = new BriefUnitGraph(sootMethod.retrieveActiveBody());
                sootMethod = graph.getBody().getMethod();
				if ((!securityAnnotation
						.isMethodOfSootSecurityLevelClass(sootMethod))
						|| securityAnnotation.isIdFunction(sootMethod)) {
					addMethodEnvironmentForMethod(sootMethod);
					if (sootMethod.hasActiveBody()) {
						for (Unit unit : sootMethod.getActiveBody().getUnits()) {
							unit.apply(stmtSwitch);
						}
					}
				}
			}

		}
	}

}
