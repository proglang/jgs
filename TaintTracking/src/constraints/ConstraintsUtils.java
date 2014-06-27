package constraints;

import static resource.Messages.getMsg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import security.ILevel;
import exception.ConstraintUnsupportedException;

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

	public static Set<IConstraint> getConstraintsContaining(Set<IConstraint> constraints, IConstraintComponent component) {
		Set<IConstraint> result = new HashSet<IConstraint>();
		for (IConstraint constraint : constraints) {
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

	public static Set<IConstraint> checkReturn(Set<IConstraint> mPlus, String mSignature, Set<IConstraint> _m) {
		Set<IConstraint> missing = new HashSet<IConstraint>();
		Set<IConstraint> _MLowerBoundConstraintsForReturn = getLowerBoundConstraintsOfReturn(_m);
		for (IConstraint _MLowerBoundConstraintForReturn : _MLowerBoundConstraintsForReturn) {
			if (isLEQConstraint(_MLowerBoundConstraintForReturn)) {
				LEQConstraint leq = (LEQConstraint) _MLowerBoundConstraintForReturn;
				if (!mPlus.contains(changeSignatureOf(leq, mSignature))) {
					missing.add(leq);
				}
			} else {
				throw new ConstraintUnsupportedException(getMsg("exception.constraints.unknown_type", _MLowerBoundConstraintForReturn.toString()));
			}
		}
		return missing;
	}

	public static Set<IConstraint> checkParameter(int parameterPosition, Set<IConstraint> mPlus, String mSignature, Set<IConstraint> _m) {
		Set<IConstraint> missing = new HashSet<IConstraint>();
		Set<IConstraint> _MUpperBoundConstraintsForParameter = getUpperBoundConstraintsOfParameter(parameterPosition, _m);
		for (IConstraint _MUpperBoundConstraintForParameter : _MUpperBoundConstraintsForParameter) {
			if (isLEQConstraint(_MUpperBoundConstraintForParameter)) {
				LEQConstraint leq = (LEQConstraint) _MUpperBoundConstraintForParameter;
				if (!mPlus.contains(changeSignatureOf(leq, mSignature))) {
					missing.add(leq);
				}
			} else {
				throw new ConstraintUnsupportedException(
						getMsg("exception.constraints.unknown_type", _MUpperBoundConstraintForParameter.toString()));
			}
		}
		return missing;
	}

	public static Set<IConstraint> checkPC(Set<IConstraint> mPlus, String mSignature, Set<IConstraint> _m) {
		Set<IConstraint> missing = new HashSet<IConstraint>();
		Set<IConstraint> _MUpperBoundConstraintsForPC = getUpperBoundConstraintsOfProgramCounter(_m);
		for (IConstraint _MUpperBoundConstraintForPC : _MUpperBoundConstraintsForPC) {
			if (isLEQConstraint(_MUpperBoundConstraintForPC)) {
				LEQConstraint leq = (LEQConstraint) _MUpperBoundConstraintForPC;
				if (!mPlus.contains(changeSignatureOf(leq, mSignature))) {
					missing.add(leq);
				}
			} else {
				throw new ConstraintUnsupportedException(getMsg("exception.constraints.unknown_type", _MUpperBoundConstraintForPC.toString()));
			}
		}
		return missing;
	}

	private static Set<IConstraint> getLowerBoundConstraintsOfReturn(Set<IConstraint> constraints) {
		Set<IConstraint> lowerBoundConstraints = new HashSet<IConstraint>();
		for (IConstraint constraint : constraints) {
			if (isLEQConstraint(constraint)) {
				if (isReturnReference(constraint.getRhs())) {
					lowerBoundConstraints.add(constraint);
				}
			} else {
				throw new ConstraintUnsupportedException(getMsg("exception.constraints.unknown_type", constraint.toString()));
			}
		}
		return lowerBoundConstraints;
	}

	private static Set<IConstraint> getUpperBoundConstraintsOfParameter(int parameterPosition, Set<IConstraint> constraints) {
		Set<IConstraint> upperBoundConstraints = new HashSet<IConstraint>();
		for (IConstraint constraint : constraints) {
			if (isLEQConstraint(constraint)) {
				if (isParameterReference(constraint.getLhs(), parameterPosition)) {
					upperBoundConstraints.add(constraint);
				}
			} else {
				throw new ConstraintUnsupportedException(getMsg("exception.constraints.unknown_type", constraint.toString()));
			}
		}
		return upperBoundConstraints;
	}

	private static Set<IConstraint> getUpperBoundConstraintsOfProgramCounter(Set<IConstraint> constraints) {
		Set<IConstraint> upperBoundConstraints = new HashSet<IConstraint>();
		for (IConstraint constraint : constraints) {
			if (isLEQConstraint(constraint)) {
				if (isProgramCounterReference(constraint.getLhs())) {
					upperBoundConstraints.add(constraint);
				}
			} else {
				throw new ConstraintUnsupportedException(getMsg("exception.constraints.unknown_type", constraint.toString()));
			}
		}
		return upperBoundConstraints;
	}

}
