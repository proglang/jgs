package constraints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exception.ConstraintUnsupportedException;

import logging.AnalysisLog;
import security.ILevel;
import security.ILevelMediator;
import soot.Local;
import soot.jimple.IfStmt;
import static constraints.ConstraintsUtils.*;
import static resource.Messages.getMsg;

public class Constraints {

	private final Set<IConstraint> constraintsSet = new HashSet<IConstraint>();

	private final AnalysisLog log;

	private final ILevelMediator mediator;

	private final Map<IfStmt, Set<IConstraint>> pcStack = new HashMap<IfStmt, Set<IConstraint>>();

	public Constraints(ILevelMediator mediator, AnalysisLog log) {
		this.mediator = mediator;
		this.log = log;
	}

	public Constraints(Set<IConstraint> constraints, Map<IfStmt, Set<IConstraint>> stack, ILevelMediator mediator, AnalysisLog log) {
		this.mediator = mediator;
		this.log = log;
		this.pcStack.putAll(stack);
		addAllSmart(constraints);
	}

	public void addSmart(IConstraint constraint) {
		if (isLEQConstraint(constraint)) {
			// add x ~ y iff x != y
			if (!constraint.getLhs().equals(constraint.getRhs())) {
				LEQConstraint leqConstraint = (LEQConstraint) constraint;
				// x <= bottom implies x == bottom and top <= x implies x == top
				// if (leqConstraint.getRhs().equals(mediator.getGreatestLowerBoundLevel())
				// || leqConstraint.getLhs().equals(mediator.getLeastUpperBoundLevel())) {
				// addConstraint(new LEQConstraint(leqConstraint.getRhs(), leqConstraint.getLhs()));
				// }
				addConstraint(leqConstraint);
			}
		} else {
			throw new ConstraintUnsupportedException(getMsg("exception.constraints.unknown_type", constraint.toString()));
		}
	}

	public void addAllSmart(Set<IConstraint> constraints) {
		for (IConstraint constraint : constraints) {
			addSmart(constraint);
		}
	}

	public boolean contains(IConstraint constraint) {
		return containsConstraint(constraintsSet, constraint);
	}

	public int size() {
		return constraintsSet.size();
	}

	private <C extends IConstraint> void addConstraint(C constraint) {
		constraintsSet.add(constraint);
	}

	public Set<IConstraint> getConstraintsSet() {
		Set<IConstraint> constraints = new HashSet<IConstraint>();
		constraints.addAll(constraintsSet);
		return constraints;
	}

	private void calculateTransitiveClosure() {
		int size = -1;
		while (size != constraintsSet.size()) {
			size = constraintsSet.size();
			Set<IConstraint> closur = new HashSet<IConstraint>();
			for (IConstraint constraintL : constraintsSet) {
				if (isLEQConstraint(constraintL)) {
					IConstraintComponent rhs = constraintL.getRhs();
					for (IConstraint constraintR : constraintsSet) {
						if (isLEQConstraint(constraintR)) {
							IConstraintComponent lhs = constraintR.getLhs();
							if (rhs.equals(lhs)) closur.add(new LEQConstraint(constraintL.getLhs(), constraintR.getRhs()));
						}
					}
				}
			}
			addAllSmart(closur);
		}
	}

	public Constraints getTransitiveClosure() {
		Constraints constraints = new Constraints(getConstraintsSet(), getProgramCounterStack(), mediator, log);
		constraints.calculateTransitiveClosure();
		return constraints;
	}

	private void removeConstraintsContaining(IConstraintComponent component) {
		calculateTransitiveClosure();
		for (IConstraint constraint : getConstraintsSet()) {
			if (constraint.containsComponent(component)) constraintsSet.remove(constraint);
		}
	}

	public boolean containsReturnReferenceFor(String signature) {
		return containsSetReturnReferenceFor(constraintsSet, signature);
	}

	public List<ILevel> getContainedLevels() {
		return getContainedLevelsOfSet(constraintsSet);
	}

	public boolean containsParameterReferenceFor(String signature, int position) {
		return containsSetParameterReferenceFor(constraintsSet, signature, position);
	}

	public boolean containsProgramCounterReferenceFor() {
		return containsSetProgramCounterReference(constraintsSet);
	}

	public void clear() {
		constraintsSet.clear();
		pcStack.clear();
	}

	public List<IConstraint> getInequality() {
		List<IConstraint> inconsistent = new ArrayList<IConstraint>();
		for (IConstraint constraint : constraintsSet) {
			if (isLEQConstraint(constraint)) {
				IConstraintComponent left = constraint.getLhs();
				IConstraintComponent right = constraint.getRhs();
				if (isLevel(left) && isLevel(right)) {
					ILevel l1 = (ILevel) left;
					ILevel l2 = (ILevel) right;
					if (!mediator.isWeaker(l1, l2)) {
						inconsistent.add(constraint);
					}
				}
			}
		}
		return inconsistent;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("{ ");
		boolean tail = false;
		for (IConstraint constraint : constraintsSet) {
			if (tail) {
				sb.append(", ");
			} else {
				tail = true;
			}
			sb.append(constraint.toString());
		}
		sb.append(" }");
		return sb.toString();
	}

	public void removeConstraintsContainingReferencesFor(String signature) {
		calculateTransitiveClosure();
		for (IConstraint constraint : getConstraintsSet()) {
			if (constraint.containsReturnReferenceFor(signature) || constraint.containsParameterReferenceFor(signature)
					|| constraint.containsProgramCounterReferenceFor(signature)) {
				constraintsSet.remove(constraint);
			}
		}
	}

	public void removeConstraintsContainingLocal() {
		for (IConstraint constraint : getConstraintsSet()) {
			if (constraint.containsLocal()) constraintsSet.remove(constraint);
		}
	}

	public void removeConstraintsContaining(Local local) {
		removeConstraintsContaining(new ConstraintLocal(local));
	}

	public Set<IConstraint> checkForConsistency(Set<IConstraint> sConstraints) {
		Set<IConstraint> missing = new HashSet<IConstraint>();
		for (IConstraint constraint : getConstraintsSet()) {
			if (!sConstraints.contains(constraint)) {
				if (!(isLevel(constraint.getLhs()) && isLevel(constraint.getRhs()))) {
					missing.add(constraint);
				}
			}
		}
		return missing;
	}

	public Map<IfStmt, Set<IConstraint>> getProgramCounterStack() {
		return new HashMap<IfStmt, Set<IConstraint>>(pcStack);
	}

	public Set<IConstraint> getAllPCConstraints() {
		Set<IConstraint> constraints = new HashSet<IConstraint>();
		for (IfStmt stmt : pcStack.keySet()) {
			constraints.addAll(pcStack.get(stmt));
		}
		return constraints;
	}

	public Set<IfStmt> getProgramCounterStmt() {
		return pcStack.keySet();
	}

	public boolean addProgramCounterConstraints(IfStmt stmt, Set<IConstraint> constraints) {
		return null == pcStack.put(stmt, constraints);
	}

	public void addAllProgramCounterConstraints(Map<IfStmt, Set<IConstraint>> map) {
		pcStack.putAll(map);
	}

	public boolean removeProgramCounterConstraintsFor(IfStmt stmt) {
		return null != pcStack.remove(stmt);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((constraintsSet == null) ? 0 : constraintsSet.hashCode());
		result = prime * result + ((pcStack == null) ? 0 : pcStack.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Constraints other = (Constraints) obj;
		if (constraintsSet == null) {
			if (other.constraintsSet != null) return false;
		} else if (!constraintsSet.equals(other.constraintsSet)) return false;
		if (pcStack == null) {
			if (other.pcStack != null) return false;
		} else if (!pcStack.equals(other.pcStack)) return false;
		return true;
	}

}
