package analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.AnalyzedMethodEnvironment;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NopStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import constraints.ConstraintPlaceholder;
import constraints.ConstraintProgramCounterRef;
import constraints.ConstraintReturnRef;
import constraints.ConstraintsSet;
import constraints.ConstraintsUtils;
import constraints.IConstraintComponent;
import constraints.IfProgramCounterTrigger;
import constraints.LEQConstraint;
import constraints.LookupSwitchProgramCounterTrigger;
import constraints.TableSwitchProgramCounterTrigger;
import extractor.UsedObjectStore;

public class SecurityConstraintStmtSwitch extends SecurityConstraintSwitch implements StmtSwitch {

	private Stmt returnStmt = null;

	protected SecurityConstraintStmtSwitch(AnalyzedMethodEnvironment methodEnvironment, UsedObjectStore store, ConstraintsSet in,
			ConstraintsSet out) {
		super(methodEnvironment, store, in, out);
	}

	@Override
	public void caseAssignStmt(AssignStmt stmt) {
		SecurityConstraintValueSwitch switchLhs = getValueSwitch(stmt.getLeftOp());
		SecurityConstraintValueSwitch switchRhs = getValueSwitch(stmt.getRightOp());
		if (switchLhs.isLocal()) {
			ConstraintPlaceholder placeholder = new ConstraintPlaceholder(getAnalyzedSignature());
			Set<IConstraintComponent> pComponents = new HashSet<IConstraintComponent>(Arrays.asList(new IConstraintComponent[] { placeholder }));
			addConstraints(generateConstraints(considerWriteEffect(switchLhs, switchRhs.getComponents()), pComponents));
			removeLocalAssignArtifacts(switchLhs);
			addConstraint(new LEQConstraint(placeholder, switchLhs.getConstraintLocal()));
			removePlaceholder(placeholder);
			removeAccessArtifacts(switchRhs);	
		} else {
			addWriteEffects(switchLhs);
			addConstraints(generateConstraints(considerWriteEffect(switchLhs, switchRhs.getComponents()), switchLhs.getComponents()));
			addConstraints(switchRhs.getConstraints());
			removeAccessArtifacts(switchRhs);		
		}
		
		
		
	}

	private void removePlaceholder(ConstraintPlaceholder placeholder) {
		getOut().removeConstraintsContaining(placeholder);		
	}

	private void addWriteEffects(SecurityConstraintValueSwitch switchLhs) {
		if (switchLhs.isField()) {
			for (IConstraintComponent component : switchLhs.getComponents()) {
				if (ConstraintsUtils.isLevel(component))	getOut().addWriteEffect(new LEQConstraint(new ConstraintProgramCounterRef(getAnalyzedSignature()), component));
			}			
		}
	}

	@Override
	public void caseBreakpointStmt(BreakpointStmt stmt) {
		throwNotImplementedException(stmt.getClass(), stmt.toString());
	}

	@Override
	public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
		throwNotImplementedException(stmt.getClass(), stmt.toString());
	}

	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
		throwNotImplementedException(stmt.getClass(), stmt.toString());
	}

	@Override
	public void caseGotoStmt(GotoStmt stmt) {}

	@Override
	public void caseIdentityStmt(IdentityStmt stmt) {
		SecurityConstraintValueSwitch lhsValueSwitch = getValueSwitch(stmt.getLeftOp());
		SecurityConstraintValueSwitch rhsValueSwitch = getValueSwitch(stmt.getRightOp());
		addConstraints(generateConstraints(rhsValueSwitch.getComponents(), lhsValueSwitch.getComponents()));
	}

	@Override
	public void caseIfStmt(IfStmt stmt) {
		SecurityConstraintValueSwitch valueSwitch = getValueSwitch(stmt.getCondition());
		ConstraintsSet tempConstraints = new ConstraintsSet(getIn().getConstraintsSet(), getIn().getProgramCounter(), getIn().getWriteEffects(), getMediator());
		for (IConstraintComponent component : valueSwitch.getComponents()) {
			tempConstraints.add(new LEQConstraint(component, new ConstraintProgramCounterRef(getAnalyzedSignature())));
		}
		ConstraintsSet pcSet = tempConstraints.getTransitiveClosure();
		pcSet.removeConstraintsContainingLocal();
		getOut().addProgramCounterConstraints(new IfProgramCounterTrigger(stmt), pcSet.getConstraintsSet());
	}

	@Override
	public void caseInvokeStmt(InvokeStmt stmt) {
		SecurityConstraintValueSwitch invokeExprSwitch = getValueSwitch(stmt.getInvokeExpr());
		addConstraints(invokeExprSwitch.getConstraints());
		removeAccessArtifacts(invokeExprSwitch);
	}

	@Override
	public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
		SecurityConstraintValueSwitch valueSwitch = getValueSwitch(stmt.getKey());
		ConstraintsSet tempConstraints = new ConstraintsSet(getIn().getConstraintsSet(), getIn().getProgramCounter(), getIn().getWriteEffects(), getMediator());
		for (IConstraintComponent component : valueSwitch.getComponents()) {
			tempConstraints.add(new LEQConstraint(component, new ConstraintProgramCounterRef(getAnalyzedSignature())));
		}
		ConstraintsSet pcSet = tempConstraints.getTransitiveClosure();
		pcSet.removeConstraintsContainingLocal();
		getOut().addProgramCounterConstraints(new LookupSwitchProgramCounterTrigger(stmt),
				pcSet.getConstraintsSet());
	}

	@Override
	public void caseNopStmt(NopStmt stmt) {}

	@Override
	public void caseRetStmt(RetStmt stmt) {
		throwNotImplementedException(stmt.getClass(), stmt.toString());
	}

	@Override
	public void caseReturnStmt(ReturnStmt stmt) {
		returnStmt = stmt;
		SecurityConstraintValueSwitch valueSwitch = getValueSwitch(stmt.getOp());
		Set<IConstraintComponent> rComponents = new HashSet<IConstraintComponent>(
				Arrays.asList(new IConstraintComponent[] { new ConstraintReturnRef(getAnalyzedSignature()) }));
		addConstraints(generateConstraints(valueSwitch.getComponents(), rComponents));
	}

	@Override
	public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
		returnStmt = stmt;
	}

	@Override
	public void caseTableSwitchStmt(TableSwitchStmt stmt) {
		SecurityConstraintValueSwitch valueSwitch = getValueSwitch(stmt.getKey());
		ConstraintsSet tempConstraints = new ConstraintsSet(getIn().getConstraintsSet(), getIn().getProgramCounter(), getIn().getWriteEffects(), getMediator());
		for (IConstraintComponent component : valueSwitch.getComponents()) {
			tempConstraints.add(new LEQConstraint(component, new ConstraintProgramCounterRef(getAnalyzedSignature())));
		}
		ConstraintsSet pcSet = tempConstraints.getTransitiveClosure();
		pcSet.removeConstraintsContainingLocal();
		getOut().addProgramCounterConstraints(new TableSwitchProgramCounterTrigger(stmt),
				pcSet.getConstraintsSet());
	}

	@Override
	public void caseThrowStmt(ThrowStmt stmt) {
		throwNotImplementedException(stmt.getClass(), stmt.toString());
	}

	@Override
	public void defaultCase(Object object) {
		throwUnknownObjectException(object);
	}

	public boolean isReturnStmt() {
		return returnStmt != null;
	}

	private void addConstraints(Set<LEQConstraint> constraints) {
		getOut().addAll(constraints);
	}
	
	private void addConstraint(LEQConstraint constraint) {
		getOut().add(constraint);
	}

	private Set<IConstraintComponent> considerWriteEffect(SecurityConstraintValueSwitch switchLhs, Set<IConstraintComponent> rComponents) {
		Set<IConstraintComponent> components = new HashSet<IConstraintComponent>(rComponents);
		if (switchLhs.isField() || switchLhs.isLocal()) {
			components.add(new ConstraintProgramCounterRef(getAnalyzedSignature()));
		}
		return components;
	}

	private Set<LEQConstraint> generateConstraints(Set<IConstraintComponent> leftComponents, Set<IConstraintComponent> rightComponents) {
		Set<LEQConstraint> constraits = new HashSet<LEQConstraint>();
		for (IConstraintComponent left : leftComponents) { 
			for (IConstraintComponent right : rightComponents) {
				constraits.add(new LEQConstraint(left, right));
			}
		}
		return constraits;
	}

	private void removeAccessArtifacts(SecurityConstraintValueSwitch valueSwitch) {
		List<String> signatures = new ArrayList<String>();
		if (valueSwitch.isMethod()) signatures.add(valueSwitch.getMethod().getSignature());
		if (valueSwitch.hasStaticAccess()) signatures.add(valueSwitch.getStaticAccess().getName());
		getOut().removeConstraintsContainingReferencesFor(signatures);
	}

	private void removeLocalAssignArtifacts(SecurityConstraintValueSwitch valueSwitch) {
		if (valueSwitch.isLocal()) {
			getOut().removeConstraintsContaining(valueSwitch.getConstraintLocal());
		}
	}

}
