package constraints;

import static constraints.ConstraintsUtils.isLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import security.ILevel;
import security.ILevelMediator;

public class LEQConstraintsSet extends AConstraintsSet<LEQConstraint> {

	public LEQConstraintsSet(ILevelMediator mediator) {
		super(mediator);
	}

	public LEQConstraintsSet(Set<LEQConstraint> constraints, ILevelMediator mediator) {
		super(constraints, mediator);
	}

	@Override
	public boolean add(LEQConstraint constraint) {
		IConstraintComponent left = constraint.getLhs();
		IConstraintComponent right = constraint.getRhs();
		if (!left.equals(right)) {
			if (right.equals(mediator.getGreatestLowerBoundLevel()) || left.equals(mediator.getLeastUpperBoundLevel())) {
				LEQConstraint bound = new LEQConstraint(right, left);
				if (! contains(bound)) add(bound);
			}
			return super.add(constraint);
		}
		return false;
	}

	@Override
	public boolean remove(Object obj) {
		return super.remove(obj);
	}

	@Override
	public Set<LEQConstraint> getTransitiveClosureSet() {
		LEQConstraintsSet constraints = new LEQConstraintsSet(getConstraintsSet(), mediator);
		constraints.calculateTransitiveClosure();
		return constraints.getConstraintsSet();
	}

	@Override
	protected boolean transitivClosureMergeCondition(LEQConstraint leftConstraint, LEQConstraint rightConstraint) {
		return leftConstraint.getRhs().equals(rightConstraint.getLhs()) && !leftConstraint.getLhs().equals(rightConstraint.getRhs());
	}

	@Override
	protected LEQConstraint transitivClosureMerge(LEQConstraint leftConstraint, LEQConstraint rightConstraint) {
		return new LEQConstraint(leftConstraint.getLhs(), rightConstraint.getRhs());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		LEQConstraintsSet other = (LEQConstraintsSet) obj;
		if (getConstraintsSet() == null) {
			if (other.getConstraintsSet() != null) return false;
		} else if (!getConstraintsSet().equals(other.getConstraintsSet())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getConstraintsSet() == null) ? 0 : getConstraintsSet().hashCode());
		return result;
	}

	public Set<LEQConstraint> getInequality() {
		Set<LEQConstraint> inequal = new HashSet<LEQConstraint>();
		for (LEQConstraint constraint : getConstraintsSet()) {
			IConstraintComponent left = constraint.getLhs();
			IConstraintComponent right = constraint.getRhs();
			if (isLevel(left) && isLevel(right)) {
				ILevel l1 = (ILevel) left;
				ILevel l2 = (ILevel) right;
				if (!mediator.isWeaker(l1, l2)) {
					inequal.add(constraint);
				}
			}
		}
		return inequal;
	}

	public List<String> toBoundaryStrings() {
		List<String> result = new ArrayList<String>();		
		Map<IConstraintComponentVar, Set<ILevel>> lower = new HashMap<IConstraintComponentVar, Set<ILevel>>();
		Map<IConstraintComponentVar, Set<ILevel>> upper = new HashMap<IConstraintComponentVar, Set<ILevel>>();
		Set<IConstraintComponentVar> vars = new HashSet<IConstraintComponentVar>();
		for (LEQConstraint constraint : getConstraintsSet()) {
			IConstraintComponent left = constraint.getLhs();
			IConstraintComponent right = constraint.getRhs();
			if (isLevel(left) && right instanceof IConstraintComponentVar) {
				ILevel level = (ILevel) left;
				IConstraintComponentVar var = (IConstraintComponentVar) right;
				if (! lower.containsKey(var)) lower.put(var, new HashSet<ILevel>());
				if (! upper.containsKey(var)) upper.put(var, new HashSet<ILevel>());
				vars.add(var);
				lower.get(var).add(level);
			}
			if (isLevel(right) && left instanceof IConstraintComponentVar) {
				ILevel level = (ILevel) right;				
				IConstraintComponentVar var = (IConstraintComponentVar) left;
				if (! lower.containsKey(var)) lower.put(var, new HashSet<ILevel>());
				if (! upper.containsKey(var)) upper.put(var, new HashSet<ILevel>());
				vars.add(var);
				upper.get(var).add(level);	
			}
		}
		for (IConstraintComponentVar var : vars) {
			String string = "";
			Set<ILevel> lowerLevels = lower.get(var);
			Set<ILevel> upperLevels = upper.get(var); 
			try {
				if (lowerLevels.size() > 0) string += mediator.getLeastUpperBoundLevelOf(new ArrayList<ILevel>(lowerLevels)).toString() + " <= ";
				string += var.toString();
				if (upperLevels.size() > 0) string += " <= " + mediator.getGreatestLowerBoundLevelOf(new ArrayList<ILevel>(upperLevels)).toString();
				result.add(string);
			} catch (Exception e) {
				System.err.println("error for " + var.toString() + " up: " + upperLevels.size() + " | low: " + lowerLevels.size());
			}
			
		}
		return result;
	}

}
