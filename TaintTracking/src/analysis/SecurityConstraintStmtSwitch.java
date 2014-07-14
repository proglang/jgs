package analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.AnalyzedMethodEnvironment;
import soot.Value;
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
import constraints.ConstraintLocal;
import constraints.ConstraintPlaceholder;
import constraints.ConstraintProgramCounterRef;
import constraints.ConstraintReturnRef;
import constraints.ConstraintsSet;
import constraints.ConstraintsUtils;
import constraints.IConstraintComponent;
import constraints.IProgramCounterTrigger;
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
		SecurityConstraintValueWriteSwitch writeSwitch = getWriteSwitch(stmt.getLeftOp());
		SecurityConstraintValueReadSwitch readSwitch = getReadSwitch(stmt.getRightOp());
		handleReadAndWriteStmt(writeSwitch, readSwitch);

	}

	private void handleReadAndWriteStmt(SecurityConstraintValueWriteSwitch writeSwitch, SecurityConstraintValueReadSwitch readSwitch) {
		if (writeSwitch.isLocal()) {
			ConstraintLocal local = new ConstraintLocal(writeSwitch.getLocal());			
			addConstraints(generateConstraints(writeSwitch, readSwitch));
			restoreLocalForPlaceholder(local);
			removeAccessArtifacts(readSwitch);
		} else {
			addWriteEffects(writeSwitch);
			addConstraints(generateConstraints(writeSwitch, readSwitch));
			removeAccessArtifacts(readSwitch);
		}
	}

	private void restoreLocalForPlaceholder(ConstraintLocal local) {
		ConstraintPlaceholder placeholder = new ConstraintPlaceholder();
		getOut().removeConstraintsContaining(local);
		addConstraint(new LEQConstraint(placeholder, local));
		getOut().removeConstraintsContaining(placeholder);
	}

	private void addWriteEffects(SecurityConstraintValueWriteSwitch writeSwitch) {
		if (writeSwitch.isField()) {
			for (IConstraintComponent component : writeSwitch.getWriteComponents()) {
				if (ConstraintsUtils.isLevel(component))
					getOut().addWriteEffect(new LEQConstraint(new ConstraintProgramCounterRef(getAnalyzedSignature()), component));
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
		SecurityConstraintValueWriteSwitch writeSwitch = getWriteSwitch(stmt.getLeftOp());
		SecurityConstraintValueReadSwitch readSwitch = getReadSwitch(stmt.getRightOp());
		handleReadAndWriteStmt(writeSwitch, readSwitch);
		
	}

	@Override
	public void caseIfStmt(IfStmt stmt) {
		handleBranch(stmt.getCondition(), new IfProgramCounterTrigger(stmt));
	}

	@Override
	public void caseInvokeStmt(InvokeStmt stmt) {
		SecurityConstraintValueReadSwitch readSwitch = getReadSwitch(stmt.getInvokeExpr());
		addConstraints(readSwitch.getConstraints());
		removeAccessArtifacts(readSwitch);
	}

	@Override
	public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
		handleBranch(stmt.getKey(), new LookupSwitchProgramCounterTrigger(stmt));
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
		SecurityConstraintValueReadSwitch readSwitch = getReadSwitch(stmt.getOp());
		Set<LEQConstraint> constraints = new HashSet<LEQConstraint>(readSwitch.getConstraints());
		for (IConstraintComponent read : readSwitch.getReadComponents()) {
			constraints.add(new LEQConstraint(read, new ConstraintReturnRef(getAnalyzedSignature())));
		}
		addConstraints(constraints);
	}

	@Override
	public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
		returnStmt = stmt;
	}

	@Override
	public void caseTableSwitchStmt(TableSwitchStmt stmt) {
		handleBranch(stmt.getKey(), new TableSwitchProgramCounterTrigger(stmt));
	}

	private void handleBranch(Value condition, IProgramCounterTrigger programCounterTrigger) {
		SecurityConstraintValueReadSwitch readSwitch = getReadSwitch(condition);
		ConstraintsSet pcConstraints = new ConstraintsSet(getIn().getConstraintsSet(), getIn().getProgramCounter(),	getIn().getWriteEffects(), getMediator());
		for (IConstraintComponent component : readSwitch.getReadComponents()) {
			pcConstraints.add(new LEQConstraint(component, new ConstraintProgramCounterRef(getAnalyzedSignature())));
		}
		pcConstraints.removeConstraintsContainingLocal();
		getOut().addProgramCounterConstraints(programCounterTrigger, pcConstraints.getConstraintsSet());
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

	private Set<LEQConstraint> generateConstraints(SecurityConstraintValueWriteSwitch writeSwitch, SecurityConstraintValueReadSwitch readSwitch) {
		Set<LEQConstraint> constraints = new HashSet<LEQConstraint>();
		readSwitch.addReadComponents(writeSwitch.getReadComponents());
		readSwitch.addReadComponent(new ConstraintProgramCounterRef(getAnalyzedSignature()));
		constraints.addAll(readSwitch.getConstraints());		
		for (IConstraintComponent write : writeSwitch.getWriteComponents()) {
			for (IConstraintComponent read : readSwitch.getReadComponents()) {
				constraints.add(new LEQConstraint(read, write));
			}
		}
		return constraints;
	}

	private void removeAccessArtifacts(SecurityConstraintValueReadSwitch readSwitch) {
		List<String> signatures = new ArrayList<String>();
		if (readSwitch.isMethod()) signatures.add(readSwitch.getMethod().getSignature());
		if (readSwitch.usesStaticAccess()) signatures.add(readSwitch.getStaticClass().getName());
		getOut().removeConstraintsContainingReferencesFor(signatures);
	}

}
