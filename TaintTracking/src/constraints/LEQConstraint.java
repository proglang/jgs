package constraints;

import static constraints.ConstraintsUtils.isLevel;
import static constraints.ConstraintsUtils.isLocal;
import static constraints.ConstraintsUtils.isParameterReference;
import static constraints.ConstraintsUtils.isProgramCounterReference;
import static constraints.ConstraintsUtils.isReturnReference;

import java.util.HashSet;
import java.util.Set;

import security.ILevel;

public final class LEQConstraint {

	private final IConstraintComponent lhs;

	private final IConstraintComponent rhs;

	public LEQConstraint(IConstraintComponent lhs, IConstraintComponent rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public final boolean containsComponent(IConstraintComponent component) {
		return lhs.equals(component) || rhs.equals(component);
	}

	public final boolean containsReturnReferenceFor(String signature) {
		return isReturnReference(lhs, signature) || isReturnReference(rhs, signature);
	}

	public final boolean containsReturnReference() {
		return isReturnReference(lhs) || isReturnReference(rhs);
	}

	public final Set<ILevel> getContainedLevel() {
		Set<ILevel> levels = new HashSet<ILevel>();
		if (isLevel(lhs)) levels.add((ILevel) lhs);
		if (isLevel(rhs)) levels.add((ILevel) rhs);
		return levels;
	}

	public final boolean containsParameterReferenceFor(String signature, int position) {
		return isParameterReference(lhs, signature, position) || isParameterReference(rhs, signature, position);
	}

	public final boolean containsParameterReferenceFor(String signature) {
		return isParameterReference(lhs, signature) || isParameterReference(rhs, signature);
	}

	public final boolean containsParameterReference() {
		return isParameterReference(lhs) || isParameterReference(rhs);
	}

	public final boolean containsLocal() {
		return isLocal(lhs) || isLocal(rhs);
	}

	public boolean containsGeneratedLocal() {
		if (isLocal(lhs)) {
			if (((ConstraintLocal) lhs).isGeneratedLocal()) return true;
		}
		if (isLocal(rhs)) {
			if (((ConstraintLocal) rhs).isGeneratedLocal()) return true;
		}
		return false;
	}

	public final Set<ConstraintParameterRef> getInvalidParameterReferencesFor(String signature, int count) {
		Set<ConstraintParameterRef> invalid = new HashSet<ConstraintParameterRef>();
		if (isParameterReference(lhs)) {
			ConstraintParameterRef paramRef = (ConstraintParameterRef) lhs;
			if (!paramRef.getSignature().equals(signature) || paramRef.getParameterPos() >= count) {
				invalid.add(paramRef);
			}
		}
		if (isParameterReference(rhs)) {
			ConstraintParameterRef paramRef = (ConstraintParameterRef) rhs;
			if (!paramRef.getSignature().equals(signature) || paramRef.getParameterPos() >= count) {
				invalid.add(paramRef);
			}
		}
		return invalid;
	}

	public final Set<ConstraintReturnRef> getInvalidReturnReferencesFor(String signature) {
		Set<ConstraintReturnRef> invalid = new HashSet<ConstraintReturnRef>();
		if (isReturnReference(lhs)) {
			ConstraintReturnRef returnRef = (ConstraintReturnRef) lhs;
			if (!returnRef.getSignature().equals(signature)) {
				invalid.add(returnRef);
			}
		}
		if (isReturnReference(rhs)) {
			ConstraintReturnRef returnRef = (ConstraintReturnRef) rhs;
			if (!returnRef.getSignature().equals(signature)) {
				invalid.add(returnRef);
			}
		}
		return invalid;
	}

	public final boolean containsProgramCounterReference() {
		return isProgramCounterReference(lhs) || isProgramCounterReference(rhs);
	}

	public final boolean containsProgramCounterReferenceFor(String signature) {
		return isProgramCounterReference(lhs, signature) || isProgramCounterReference(rhs, signature);
	}

	public final IConstraintComponent getLhs() {
		return lhs;
	}

	public final IConstraintComponent getRhs() {
		return rhs;
	}

	@Override
	public final String toString() {
		return lhs.toString() + " <= " + rhs.toString();
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
		result = prime * result + ((rhs == null) ? 0 : rhs.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		LEQConstraint other = (LEQConstraint) obj;
		if (lhs == null) {
			if (other.lhs != null) return false;
		} else if (!lhs.equals(other.lhs)) return false;
		if (rhs == null) {
			if (other.rhs != null) return false;
		} else if (!rhs.equals(other.rhs)) return false;
		return true;
	}

	public final LEQConstraint changeAllComponentsSignature(String signature) {
		return new LEQConstraint(getLhs().changeSignature(signature), getRhs().changeSignature(signature));
	}

}
