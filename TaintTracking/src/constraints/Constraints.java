package constraints;

import java.util.HashSet;
import java.util.Set;

import security.ILevel;
import static constraints.AConstraint.*;

public class Constraints {
	
	private final Set<IConstraint> constraints = new HashSet<IConstraint>();
	
	public Constraints() {
		
	}

	public void add(IConstraint constraint) {
		constraints.add(constraint);		
	}
	
	public int size() {
		return constraints.size();
	}
	
	public boolean contains(IConstraint constraint) {
		return constraints.contains(constraint);
	}
	
	public Set<IConstraint> getConstraintsIncludingParameter() {
		Set<IConstraint> result = new HashSet<IConstraint>();
		for (IConstraint constraint : constraints) {
			if (constraint.containsParameterRef()) {
				result.add(constraint);
			}
		}
		return result;
	}
	
	public Set<IConstraint> getConstraintsIncludingReturn() {
		Set<IConstraint> result = new HashSet<IConstraint>();
		for (IConstraint constraint : constraints) {
			if (constraint.containsReturnRef()) {
				result.add(constraint);
			}
		}
		return result;
	}
	
	public boolean containsReturnRef() {
		for (IConstraint constraint : constraints) {
			if (constraint.containsReturnRef()) return true;
		}
		return false;
	}
	
	public boolean containsParameterRef() {
		for (IConstraint constraint : constraints) {
			if (constraint.containsParameterRef()) return true;
		}
		return false;
	}
	
	public Set<ConstraintParameterRef> getContainedParameterRefs() {
		Set<ConstraintParameterRef> result = new HashSet<ConstraintParameterRef>();		
		for (IConstraint constraint : constraints) {
			if (constraint.containsParameterRef()) {
				if (isParameterRef(constraint.getLhs())) {
					ConstraintParameterRef lhs = (ConstraintParameterRef) constraint.getLhs();
					result.add(lhs);
				}
				if (isParameterRef(constraint.getRhs())) {
					ConstraintParameterRef rhs = (ConstraintParameterRef) constraint.getRhs();
					result.add(rhs);
				}
			}
		}		
		return result;
	}
	
	public Set<ILevel> getContainedLevels() {
		Set<ILevel> result = new HashSet<ILevel>();		
		for (IConstraint constraint : constraints) {
			if (constraint.containsLevel()) {
				if (isLevel(constraint.getLhs())) {
					ILevel lhs = (ILevel) constraint.getLhs();
					result.add(lhs);
				}
				if (isLevel(constraint.getRhs())) {
					ILevel rhs = (ILevel) constraint.getRhs();
					result.add(rhs);
				}
			}
		}		
		return result;
	}
	
	public int highestParameterRefNumber(){
		int no = -1;
		for (ConstraintParameterRef paramRef : getContainedParameterRefs()) {
			if (no < paramRef.getParameterPos()) no = paramRef.getParameterPos();
		}
		return no;
	}

	public Set<IConstraint> getConstraints() {
		Set<IConstraint> result = new HashSet<IConstraint>();
		result.addAll(constraints);
		return result;
	}
}
