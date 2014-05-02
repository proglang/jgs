package constraints;

import java.util.ArrayList;
import java.util.List;

import exception.ConstraintUnsupportedException;

import logging.AnalysisLog;
import security.ILevel;
import security.ILevelMediator;
import soot.Local;
import static constraints.ConstraintsUtils.*;
import static resource.Messages.getMsg;

public class Constraints {

	private final List<IConstraint> constraintsSet = new ArrayList<IConstraint>();

	private final AnalysisLog log;

	private final ILevelMediator mediator;

	public Constraints(ILevelMediator mediator, AnalysisLog log) {
		this.mediator = mediator;
		this.log = log;
	}

	public Constraints(List<IConstraint> constraints, ILevelMediator mediator, AnalysisLog log) {
		this.mediator = mediator;
		this.log = log;
		addAllSmart(constraints);
	}

	public void addSmart(IConstraint constraint) {
		if (isLEQConstraint(constraint)) {
			// add x ~ y iff x != y
			if (!constraint.getLhs().equals(constraint.getRhs())) {
				LEQConstraint leqConstraint = (LEQConstraint) constraint;
				// x <= bottom implies x == bottom and top <= x implies x == top
//				if (leqConstraint.getRhs().equals(mediator.getGreatestLowerBoundLevel())
//						|| leqConstraint.getLhs().equals(mediator.getLeastUpperBoundLevel())) {
//					addConstraint(new LEQConstraint(leqConstraint.getRhs(), leqConstraint.getLhs()));
//				}
				addConstraint(leqConstraint);
			}
		} else {
			throw new ConstraintUnsupportedException(getMsg("exception.constraints.unknown_type", constraint.toString()));
		}
	}

	public void addAllSmart(List<IConstraint> constraints) {
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
		if (!constraintsSet.contains(constraint)) constraintsSet.add(constraint);
	}

	public List<IConstraint> getConstraintsList() {
		List<IConstraint> constraints = new ArrayList<IConstraint>();
		constraints.addAll(constraintsSet);
		return constraints;
	}

	private void calculateTransitiveClosure() {
		int size = -1;
		while (size != constraintsSet.size()) {
			size = constraintsSet.size();
			List<IConstraint> closur = new ArrayList<IConstraint>();
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
		Constraints constraints = new Constraints(getConstraintsList(), mediator, log);
		constraints.calculateTransitiveClosure();
		return constraints;
	}

	private void removeConstraintsContaining(IConstraintComponent component) {
		calculateTransitiveClosure();
		for (IConstraint constraint : getConstraintsList()) {
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
		StringBuilder sb = new StringBuilder("Constraints: {\n");
		for (int i = 0; i < constraintsSet.size(); i++) {
			if (i != 0) sb.append(", \n");
			sb.append("\t" + constraintsSet.get(i).toString());
		}
		sb.append("}");
		return sb.toString();
	}

	public void removeConstraintsContainingReferencesFor(String signature) {
		calculateTransitiveClosure();
		for (IConstraint constraint : getConstraintsList()) {
			if (constraint.containsReturnReferenceFor(signature) || constraint.containsParameterReferenceFor(signature)) {
				constraintsSet.remove(constraint);
			}
		}
	}
	
	public void removeConstraintsContainingLocal() {
		for (IConstraint constraint : getConstraintsList()) {
			if (constraint.containsLocal()) constraintsSet.remove(constraint);
		}
	}

	public void removeConstraintsContaining(Local local) {
		removeConstraintsContaining(new ConstraintLocal(local));
	}
	
	public List<IConstraint> checkForConsistency(List<IConstraint> sConstraints) {
		List<IConstraint> missing = new ArrayList<IConstraint>();
		for (IConstraint constraint : getConstraintsList()) {
			if (! sConstraints.contains(constraint)) {
				if (!(isLevel(constraint.getLhs()) && isLevel(constraint.getRhs()))) {
					missing.add(constraint);
				}				
			}
		}
		return missing;
	}
}
