package constraints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import security.ILevel;

public class ConstraintsUtils {

	public static boolean containsSetParameterReference(Collection<IConstraint> constraints) {
		for (IConstraint constraint : constraints) {
			if (constraint.containsParameterReference()) return true;
		}
		return false;
	}

	public static boolean containsSetParameterReferenceFor(Collection<IConstraint> constraints, String signature, int position) {
		for (IConstraint constraint : constraints) {
			if (constraint.containsParameterReferenceFor(signature, position)) return true;
		}
		return false;
	}

	public static boolean containsSetProgramCounterReference(Collection<IConstraint> constraints) {
		for (IConstraint constraint : constraints) {
			if (constraint.containsProgramCounterReference()) return true;
		}
		return false;
	}

	public static boolean containsSetReturnReference(Collection<IConstraint> constraints) {
		for (IConstraint constraint : constraints) {
			if (constraint.containsReturnReference()) return true;
		}
		return false;
	}
	
	public static boolean containsSetReturnReferenceFor(Collection<IConstraint> constraints, String signature) {
		for (IConstraint constraint : constraints) {
			if (constraint.containsReturnReferenceFor(signature)) return true;
		}
		return false;
	}

	public static Set<ILevel> getContainedLevelsOfSet(Collection<IConstraint> constraints) {
		Set<ILevel> levels = new HashSet<ILevel>();
		for (IConstraint constraint : constraints) {
			levels.addAll(constraint.getContainedLevel());
		}
		return levels;
	}

	public static List<ConstraintParameterRef> getInvalidParameterReferencesOfSet(Collection<IConstraint> constraints, String signature,
			int count) {
		List<ConstraintParameterRef> invalid = new ArrayList<ConstraintParameterRef>();
		for (IConstraint constraint : constraints) {
			invalid.addAll(constraint.getInvalidParameterReferencesFor(signature, count));
		}
		return invalid;
	}

	public static List<ConstraintReturnRef> getInvalidReturnReferencesOfSet(Collection<IConstraint> constraints, String signature) {
		List<ConstraintReturnRef> invalid = new ArrayList<ConstraintReturnRef>();
		for (IConstraint constraint : constraints) {
			invalid.addAll(constraint.getInvalidReturnReferencesFor(signature));
		}
		return invalid;
	}

	public static boolean isLevel(IConstraintComponent component) {
		return component instanceof ILevel;
	}
	
	public static boolean isParameterReference(IConstraintComponent component) {
		return component instanceof ConstraintParameterRef;
	}

	public static boolean isParameterReference(IConstraintComponent component, String signature) {
		if (component instanceof ConstraintParameterRef) {
			ConstraintParameterRef paramRef = (ConstraintParameterRef) component;
			return paramRef.getSignature().equals(signature);
		}
		return false;
	}

	public static boolean isParameterReference(IConstraintComponent component, String signature, int position) {
		if (isParameterReference(component)) {
			ConstraintParameterRef paramRef = (ConstraintParameterRef) component;
			return paramRef.getSignature().equals(signature) && paramRef.getParameterPos() == position;
		}
		return false;
	}

	public static boolean isProgramCounterReference(IConstraintComponent component) {
		return component instanceof ConstraintProgramCounterRef;
	}

	public static boolean isProgramCounterReference(IConstraintComponent component, String signature) {
		if (component instanceof ConstraintProgramCounterRef) {
			ConstraintProgramCounterRef pc = (ConstraintProgramCounterRef) component;
			return pc.getSignature().equals(signature);
		}
		return false;
	}

	public static boolean isReturnReference(IConstraintComponent component) {
		return component instanceof ConstraintReturnRef;
	}

	public static boolean isReturnReference(IConstraintComponent component, String signature) {
		if (component instanceof ConstraintReturnRef) {
			ConstraintReturnRef returnRef = (ConstraintReturnRef) component;
			return returnRef.getSignature().equals(signature);
		}
		return false;
	}

	public static boolean isLEQConstraint(IConstraint constraint) {
		return constraint instanceof LEQConstraint;
	}

	public static boolean isLocal(IConstraintComponent component) {
		return component instanceof ConstraintLocal;
	}
	
	public static Set<IConstraint> changeAllComponentsSignature(String newSignature, Set<IConstraint> constraints) {
		Set<IConstraint> result = new HashSet<IConstraint>();
		for (IConstraint constraint : constraints) {
			result.add(constraint.changeAllComponentsSignature(newSignature));
		}
		return result;
	}
	
	public static String constraintsAsString(Set<IConstraint> constraints) {
		StringBuilder sb = new StringBuilder("{ ");
		int count = 0;
		for (IConstraint constraint : constraints) {
			if (0 != count++) sb.append(", ");
			sb.append(constraint.toString());
		}
		sb.append(" }");
		return sb.toString();
	}
	
	public static String levelsAsString(Set<ILevel> levels) {
		StringBuilder sb = new StringBuilder("{ ");
		int count = 0;
		for (ILevel level : levels) {
			if (0 != count++) sb.append(", ");
			sb.append(level.toString());
		}
		sb.append(" }");
		return sb.toString();
	}

	public static Set<IConstraint> getConstraintsContaining(Set<IConstraint> constraints,	IConstraintComponent component) {
		Set<IConstraint> result = new HashSet<IConstraint>();
		for (IConstraint constraint : constraints) {
			if (constraint.containsComponent(component)) {
				result.add(constraint);
			}
		}
		return result;
	}
	
	public static Set<ILevel> getLevelsOfRightSideWithLeftSideReturn(Set<IConstraint> constraints, String signature) {
		Set<ILevel> result = new HashSet<ILevel>();
		for (IConstraint constraint : constraints) {
			if (isReturnReference(constraint.getLhs(), signature) && isLevel(constraint.getRhs())) {
				ILevel level = (ILevel) constraint.getRhs();
				result.add(level);
			}
		}
		return result;
	}
	
	public static Set<ILevel> getLevelsOfLeftSideWithRightSideReturn(Set<IConstraint> constraints, String signature) {
		Set<ILevel> result = new HashSet<ILevel>();
		for (IConstraint constraint : constraints) {
			if (isReturnReference(constraint.getRhs(), signature) && isLevel(constraint.getLhs())) {
				ILevel level = (ILevel) constraint.getLhs();
				result.add(level);
			}
		}
		return result;
	}
	
	public static Set<ILevel> getLevelsOfRightSideWithLeftSideParameter(Set<IConstraint> constraints, String signature, int position) {
		Set<ILevel> result = new HashSet<ILevel>();
		for (IConstraint constraint : constraints) {
			if (isParameterReference(constraint.getLhs(), signature, position) && isLevel(constraint.getRhs())) {
				ILevel level = (ILevel) constraint.getRhs();
				result.add(level);
			}
		}
		return result;
	}
	
	public static Set<ILevel> getLevelsOfLeftSideWithRightSideParameter(Set<IConstraint> constraints, String signature, int position) {
		Set<ILevel> result = new HashSet<ILevel>();
		for (IConstraint constraint : constraints) {
			if (isParameterReference(constraint.getRhs(), signature, position) && isLevel(constraint.getLhs())) {
				ILevel level = (ILevel) constraint.getLhs();
				result.add(level);
			}
		}
		return result;
	}
	
	public static Set<ILevel> getLevelsOfRightSideWithLeftSidePC(Set<IConstraint> constraints, String signature) {
		Set<ILevel> result = new HashSet<ILevel>();
		for (IConstraint constraint : constraints) {
			if (isProgramCounterReference(constraint.getLhs(), signature) && isLevel(constraint.getRhs())) {
				ILevel level = (ILevel) constraint.getRhs();
				result.add(level);
			}
		}
		return result;
	}
	
	public static Set<ILevel> getLevelsOfLeftSideWithRightSidePC(Set<IConstraint> constraints, String signature) {
		Set<ILevel> result = new HashSet<ILevel>();
		for (IConstraint constraint : constraints) {
			if (isProgramCounterReference(constraint.getRhs(), signature) && isLevel(constraint.getLhs())) {
				ILevel level = (ILevel) constraint.getLhs();
				result.add(level);
			}
		}
		return result;
	}
	
}
