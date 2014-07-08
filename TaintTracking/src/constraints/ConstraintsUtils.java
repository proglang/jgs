package constraints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import security.ILevel;
import error.ISubSignatureError;
import error.SubSignatureParameterError;
import error.SubSignatureProgramCounterError;
import error.SubSignatureReturnError;

public class ConstraintsUtils {

	public static boolean containsSetParameterReference(Collection<LEQConstraint> constraints) {
		for (LEQConstraint constraint : constraints) {
			if (constraint.containsParameterReference()) return true;
		}
		return false;
	}

	public static boolean containsSetParameterReferenceFor(Collection<LEQConstraint> constraints, String signature, int position) {
		for (LEQConstraint constraint : constraints) {
			if (constraint.containsParameterReferenceFor(signature, position)) return true;
		}
		return false;
	}

	public static boolean containsSetProgramCounterReference(Collection<LEQConstraint> constraints) {
		for (LEQConstraint constraint : constraints) {
			if (constraint.containsProgramCounterReference()) return true;
		}
		return false;
	}

	public static boolean containsSetReturnReference(Collection<LEQConstraint> constraints) {
		for (LEQConstraint constraint : constraints) {
			if (constraint.containsReturnReference()) return true;
		}
		return false;
	}

	public static boolean containsSetReturnReferenceFor(Collection<LEQConstraint> constraints, String signature) {
		for (LEQConstraint constraint : constraints) {
			if (constraint.containsReturnReferenceFor(signature)) return true;
		}
		return false;
	}

	public static Set<ILevel> getContainedLevelsOfSet(Collection<LEQConstraint> constraints) {
		Set<ILevel> levels = new HashSet<ILevel>();
		for (LEQConstraint constraint : constraints) {
			levels.addAll(constraint.getContainedLevel());
		}
		return levels;
	}

	public static List<ConstraintParameterRef> getInvalidParameterReferencesOfSet(Collection<LEQConstraint> constraints, String signature,
			int count) {
		List<ConstraintParameterRef> invalid = new ArrayList<ConstraintParameterRef>();
		for (LEQConstraint constraint : constraints) {
			invalid.addAll(constraint.getInvalidParameterReferencesFor(signature, count));
		}
		return invalid;
	}

	public static List<ConstraintReturnRef> getInvalidReturnReferencesOfSet(Collection<LEQConstraint> constraints, String signature) {
		List<ConstraintReturnRef> invalid = new ArrayList<ConstraintReturnRef>();
		for (LEQConstraint constraint : constraints) {
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

	public static boolean isParameterReference(IConstraintComponent component, int position) {
		if (isParameterReference(component)) {
			ConstraintParameterRef paramRef = (ConstraintParameterRef) component;
			return paramRef.getParameterPos() == position;
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

	public static boolean isLocal(IConstraintComponent component) {
		return component instanceof ConstraintLocal;
	}

	public static Set<LEQConstraint> changeAllComponentsSignature(String newSignature, Set<LEQConstraint> constraints) {
		Set<LEQConstraint> result = new HashSet<LEQConstraint>();
		for (LEQConstraint constraint : constraints) {
			result.add(constraint.changeAllComponentsSignature(newSignature));
		}
		return result;
	}

	public static String constraintsAsString(Set<LEQConstraint> constraints) {
		StringBuilder sb = new StringBuilder("{ ");
		int count = 0;
		for (LEQConstraint constraint : constraints) {
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

	public static Set<LEQConstraint> getConstraintsContaining(Set<LEQConstraint> constraints, IConstraintComponent component) {
		Set<LEQConstraint> result = new HashSet<LEQConstraint>();
		for (LEQConstraint constraint : constraints) {
			if (constraint.containsComponent(component)) {
				result.add(constraint);
			}
		}
		return result;
	}

	private static LEQConstraint changeSignatureOf(LEQConstraint constraint, String signature) {
		IConstraintComponent left = constraint.getLhs();
		IConstraintComponent right = constraint.getRhs();
		if (left instanceof IConstraintComponentVar && right instanceof IConstraintComponentVar) {
			IConstraintComponentVar leftVar = (IConstraintComponentVar) left;
			IConstraintComponentVar rightVar = (IConstraintComponentVar) right;
			return new LEQConstraint(changeSignatureOf(leftVar, signature), changeSignatureOf(rightVar, signature));
		} else if (left instanceof IConstraintComponentVar) {
			IConstraintComponentVar leftVar = (IConstraintComponentVar) left;
			return new LEQConstraint(changeSignatureOf(leftVar, signature), right);
		} else if (right instanceof IConstraintComponentVar) {
			IConstraintComponentVar rightVar = (IConstraintComponentVar) right;
			return new LEQConstraint(left, changeSignatureOf(rightVar, signature));
		}
		return new LEQConstraint(left, right);
	}

	private static IConstraintComponentVar changeSignatureOf(IConstraintComponentVar component, String signature) {
		if (isProgramCounterReference(component)) {
			return new ConstraintProgramCounterRef(signature);
		} else if (isParameterReference(component)) {
			ConstraintParameterRef paramRef = (ConstraintParameterRef) component;
			return new ConstraintParameterRef(paramRef.getParameterPos(), signature);
		} else if (isReturnReference(component)) {
			return new ConstraintReturnRef(signature);
		}
		return component;
	}

	public static List<ISubSignatureError> isSubSignature(Set<LEQConstraint> mPlus, String mSignature, Set<LEQConstraint> _m) {
		List<ISubSignatureError> errors = new ArrayList<ISubSignatureError>();
		for (LEQConstraint constraint : _m) {
			if (isReturnReference(constraint.getRhs())) {
				if (!mPlus.contains(changeSignatureOf(constraint, mSignature))) {
					errors.add(new SubSignatureReturnError(constraint));
				}
			}
			if (isParameterReference(constraint.getLhs())) {
				ConstraintParameterRef paramRef = (ConstraintParameterRef) constraint.getLhs();
				if (!mPlus.contains(changeSignatureOf(constraint, mSignature))) {
					errors.add(new SubSignatureParameterError(constraint, paramRef.getParameterPos()));
				}
			}
			if (isProgramCounterReference(constraint.getLhs())) {
				if (!mPlus.contains(changeSignatureOf(constraint, mSignature))) {
					if (!mPlus.contains(changeSignatureOf(constraint, mSignature))) {
						errors.add(new SubSignatureProgramCounterError(constraint));
					}
				}
			}
		}
		return errors;
	}

}
