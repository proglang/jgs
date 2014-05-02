package test;

import java.util.ArrayList;
import java.util.List;

import security.ILevelMediator;
import constraints.IConstraintComponent;
import constraints.LEQConstraint;

public class LEQConstraintsSet extends AConstraintsSet<LEQConstraint> {

	public LEQConstraintsSet(ILevelMediator mediator) {
		super(mediator);
	}

	@Override
	public boolean add(LEQConstraint constraint) {
		IConstraintComponent left = constraint.getLhs();
		IConstraintComponent right = constraint.getRhs();
		if (!left.equals(right)) {
			boolean res = false;
			if (right.equals(mediator.getGreatestLowerBoundLevel()) || left.equals(mediator.getLeastUpperBoundLevel())) {
				res |= add(new LEQConstraint(right, left));
			}
			return res | internalAdd(constraint);
		}
		return false;
	}

	private boolean internalAdd(LEQConstraint constraint) {
		if (!constraints.contains(constraint) && constraints.add(constraint)) {
			closureAdd(constraint);
			return true;
		}
		return false;
	}

	private boolean closureAdd(LEQConstraint constraint) {
		IConstraintComponent left = constraint.getLhs();
		IConstraintComponent right = constraint.getRhs();
		mapAdd(left, right);
		boolean res = false;
		for (IConstraintComponent rightComponent : saveLeftSideGet(right)) {
			LEQConstraint additional = new LEQConstraint(left, rightComponent);
			if (!constraints.contains(additional) && !saveLeftSideGet(left).contains(right) && !saveRightSideGet(right).contains(left)) {
				res |= closureAdd(additional);
			}
		}
		return res;
	}

	private void mapAdd(IConstraintComponent left, IConstraintComponent right) {
		saveLeftSideGet(left).add(right);
		saveRightSideGet(right).add(left);
	}

	private List<IConstraintComponent> saveLeftSideGet(IConstraintComponent left) {
		if (!leftSide.containsKey(left)) {
			leftSide.put(left, new ArrayList<IConstraintComponent>());
		}
		return leftSide.get(left);
	}

	private List<IConstraintComponent> saveRightSideGet(IConstraintComponent right) {
		if (!rightSide.containsKey(right)) {
			rightSide.put(right, new ArrayList<IConstraintComponent>());
		}
		return rightSide.get(right);
	}

	@Override
	public boolean remove(Object obj) {
		if (constraints.remove(obj)) {
			LEQConstraint constraint = (LEQConstraint) obj;
			IConstraintComponent left = constraint.getLhs();
			IConstraintComponent right = constraint.getRhs();
			saveLeftSideGet(left).remove(right);
			saveRightSideGet(right).remove(left);
			return true;
		}
		return false;
	}

	public List<LEQConstraint> getTransitiveClosure() {
		List<LEQConstraint> closure = new ArrayList<LEQConstraint>();
		for (IConstraintComponent left : leftSide.keySet()) {
			for (IConstraintComponent right : leftSide.get(left)) {
				closure.add(new LEQConstraint(left, right));
			}
		}
		return closure;
	}

	@Override
	protected boolean consitentRemove(Object obj) {
		if (remove(obj)) {
			recalculateTransitiveClosure();
			return true;
		}
		return false;
	}

	@Override
	protected void recalculateTransitiveClosure() {
		List<LEQConstraint> oldConstraints = getConstraintsList();
		clear();
		for (LEQConstraint constraint : oldConstraints) {
			add(constraint);
		}
	}

}
