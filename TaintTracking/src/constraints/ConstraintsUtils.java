package constraints;

import java.util.ArrayList;
import java.util.List;

import security.ILevel;

public class ConstraintsUtils {

	protected static boolean isLEQConstraint(IConstraint constraint) {
		return constraint instanceof LEQConstraint;
	}

	protected static boolean isLocal(IConstraintComponent component) {
		return component instanceof ConstraintLocal;
	}

	protected static boolean containsConstraint(List<IConstraint> constraints, IConstraint constraint) {
		return constraints.contains(constraint);
	}

	public static boolean isReturnReference(IConstraintComponent component, String signature) {
		if (component instanceof ConstraintReturnRef) {
			ConstraintReturnRef returnRef = (ConstraintReturnRef) component;
			return returnRef.getSignature().equals(signature);
		}
		return false;
	}

	public static boolean containsSetReturnReferenceFor(List<IConstraint> constraints, String signature) {
		for (IConstraint constraint : constraints) {
			if (constraint.containsReturnReferenceFor(signature)) return true;
		}
		return false;
	}

	public static boolean isLevel(IConstraintComponent component) {
		return component instanceof ILevel;
	}

	public static List<ILevel> getContainedLevelsOfSet(List<IConstraint> constraints) {
		List<ILevel> levels = new ArrayList<ILevel>();
		for (IConstraint constraint : constraints) {
			levels.addAll(constraint.getContainedLevel());
		}
		return levels;
	}

	public static boolean containsSetParameterReferenceFor(List<IConstraint> constraints, String signature, int position) {
		for (IConstraint constraint : constraints) {
			if (constraint.containsParameterReferenceFor(signature, position)) return true;
		}
		return false;
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

	public static boolean isProgramCounterReference(IConstraintComponent component) {
		return component instanceof ConstraintProgramCounterRef;
	}

	public static boolean isReturnReference(IConstraintComponent component) {
		return component instanceof ConstraintReturnRef;
	}

	public static boolean isParameterReference(IConstraintComponent component, String signature, int position) {
		if (isParameterReference(component)) {
			ConstraintParameterRef paramRef = (ConstraintParameterRef) component;
			return paramRef.getSignature().equals(signature) && paramRef.getParameterPos() == position;
		}
		return false;
	}

	public static List<ConstraintParameterRef> getInvalidParameterReferencesOfSet(List<IConstraint> constraints, String signature, int count) {
		List<ConstraintParameterRef> invalid = new ArrayList<ConstraintParameterRef>();
		for (IConstraint constraint : constraints) {
			invalid.addAll(constraint.getInvalidParameterReferencesFor(signature, count));
		}
		return invalid;
	}

	public static List<ConstraintReturnRef> getInvalidReturnReferencesOfSet(List<IConstraint> constraints, String signature) {
		List<ConstraintReturnRef> invalid = new ArrayList<ConstraintReturnRef>();
		for (IConstraint constraint : constraints) {
			invalid.addAll(constraint.getInvalidReturnReferencesFor(signature));
		}
		return invalid;
	}

	public static boolean containsSetProgramCounterReference(List<IConstraint> constraints) {
		for (IConstraint constraint : constraints) {
			if (constraint.containsProgramCounterReference()) return true;
		}
		return false;
	}

	public static boolean containsSetParameterReference(List<IConstraint> constraints) {
		for (IConstraint constraint : constraints) {
			if (constraint.containsParameterReference()) return true;
		}
		return false;
	}

	public static boolean containsSetReturnReference(List<IConstraint> constraints) {
		for (IConstraint constraint : constraints) {
			if (constraint.containsReturnReference()) return true;
		}
		return false;
	}

}
