package constraints;

import static constraints.ConstraintsUtils.isLevel;
import static constraints.ConstraintsUtils.isLocal;
import static constraints.ConstraintsUtils.isArrayReference;
import static constraints.ConstraintsUtils.isParameterReference;
import static constraints.ConstraintsUtils.isProgramCounterReference;
import static constraints.ConstraintsUtils.isReturnReference;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import security.ILevel;

public final class LEQConstraint {

	private final IComponent lhs;

	private final IComponent rhs;

	public LEQConstraint(IComponent lhs, IComponent rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public final boolean containsComponent(IComponent component) {
		return lhs.equals(component) || rhs.equals(component);
	}
	
	public final boolean containsComponentInclBase(IComponent component) {
		if (isArrayReference(lhs)) {
			ComponentArrayRef car = (ComponentArrayRef) lhs;
			if (car.getBase().equals(component)) return true;
		}
		if (isArrayReference(rhs)) {
			ComponentArrayRef car = (ComponentArrayRef) rhs;
			if (car.getBase().equals(component)) return true;
		}
		return lhs.equals(component) || rhs.equals(component);
	}

	public final boolean containsReturnReferenceFor(String signature) {
		return isReturnReference(lhs, signature) || isReturnReference(rhs, signature);
	}

	public final boolean containsReturnReference() {
		return isReturnReference(lhs) || isReturnReference(rhs);
	}
	
	public final boolean containsReturnReferenceInclBaseFor(String signature) {
		if (isArrayReference(lhs)) {
			ComponentArrayRef car = (ComponentArrayRef) lhs;
			if (isReturnReference(car.getBase(), signature)) return true;
		}
		if (isArrayReference(rhs)) {
			ComponentArrayRef car = (ComponentArrayRef) rhs;
			if (isReturnReference(car.getBase(), signature)) return true;
		}
		return isReturnReference(lhs, signature) || isReturnReference(rhs, signature);
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
	
	public final boolean containsParameterReferenceInclBaseFor(String signature) {
		if (isArrayReference(lhs)) {
			ComponentArrayRef car = (ComponentArrayRef) lhs;
			if (isParameterReference(car.getBase(), signature)) return true;
		}
		if (isArrayReference(rhs)) {
			ComponentArrayRef car = (ComponentArrayRef) rhs;
			if (isParameterReference(car.getBase(), signature)) return true;
		}
		return isParameterReference(lhs, signature) || isParameterReference(rhs, signature);
	}

	public final boolean containsParameterReference() {
		return isParameterReference(lhs) || isParameterReference(rhs);
	}

	public final boolean containsLocal() {
		if (isArrayReference(lhs)) {
			ComponentArrayRef car = (ComponentArrayRef) lhs;
			if (isLocal(car.getBase())) return true;
		}
		if (isArrayReference(rhs)) {
			ComponentArrayRef car = (ComponentArrayRef) rhs;
			if (isLocal(car.getBase())) return true;
		}
		return isLocal(lhs) || isLocal(rhs);
	}

	public boolean containsGeneratedLocal() {
		if (isLocal(lhs)) {
			if (((ComponentLocal) lhs).isGeneratedLocal()) return true;
		}
		if (isLocal(rhs)) {
			if (((ComponentLocal) rhs).isGeneratedLocal()) return true;
		}
		if (isArrayReference(lhs)) {
			ComponentArrayRef car = (ComponentArrayRef) lhs;
			if (isLocal(car.getBase())) {
				ComponentLocal l = (ComponentLocal) car.getBase();
				if (l.isGeneratedLocal()) return true;
			}
		}
		if (isArrayReference(rhs)) {
			ComponentArrayRef car = (ComponentArrayRef) rhs;
			if (isLocal(car.getBase())) {
				ComponentLocal l = (ComponentLocal) car.getBase();
				if (l.isGeneratedLocal()) return true;
			}
		}
		return false;
	}

	public final Set<IComponent> getInvalidParameterReferencesFor(String signature, int count, List<Integer> dimension) {
		Set<IComponent> invalid = new HashSet<IComponent>();
		if (isParameterReference(lhs)) {
			ComponentParameterRef paramRef = (ComponentParameterRef) lhs;
			if (!paramRef.getSignature().equals(signature) || paramRef.getParameterPos() >= count) {
				invalid.add(paramRef);
			}
		} else if (isArrayReference(lhs)) {
			ComponentArrayRef arrayRef = (ComponentArrayRef) lhs;
			if (isParameterReference(arrayRef.getBase())) {
				ComponentParameterRef paramRef = (ComponentParameterRef) arrayRef.getBase();
				if (!paramRef.getSignature().equals(signature) || paramRef.getParameterPos() >= count) {
					invalid.add(arrayRef);
				} else {
					if (arrayRef.getDimesion() > dimension.get(paramRef.getParameterPos())) {
						invalid.add(arrayRef);
					}
				}
			}			
		}
		if (isParameterReference(rhs)) {
			ComponentParameterRef paramRef = (ComponentParameterRef) rhs;
			if (!paramRef.getSignature().equals(signature) || paramRef.getParameterPos() >= count) {
				invalid.add(paramRef);
			}
		} else if (isArrayReference(rhs)) {
			ComponentArrayRef arrayRef = (ComponentArrayRef) rhs;
			if (isParameterReference(arrayRef.getBase())) {
				ComponentParameterRef paramRef = (ComponentParameterRef) arrayRef.getBase();
				if (!paramRef.getSignature().equals(signature) || paramRef.getParameterPos() >= count) {
					invalid.add(arrayRef);
				} else {
					if (arrayRef.getDimesion() > dimension.get(paramRef.getParameterPos())) {
						invalid.add(arrayRef);
					}
				}
			}			
		}
		return invalid;
	}

	public final Set<IComponent> getInvalidReturnReferencesFor(String signature, int dimension) {
		Set<IComponent> invalid = new HashSet<IComponent>();
		if (isReturnReference(lhs)) {
			ComponentReturnRef returnRef = (ComponentReturnRef) lhs;
			if (!returnRef.getSignature().equals(signature)) {
				invalid.add(returnRef);
			}
		} else if (isArrayReference(lhs)) {
			ComponentArrayRef arrayRef = (ComponentArrayRef) lhs;
			if (isReturnReference(arrayRef.getBase())) {
				ComponentReturnRef returnRef = (ComponentReturnRef) arrayRef.getBase();				
				if (!returnRef.getSignature().equals(signature)) {
					invalid.add(arrayRef);
				} else {
					if (arrayRef.getDimesion() > dimension) {
						invalid.add(arrayRef);
					}
				}				
			}
		}
		if (isReturnReference(rhs)) {
			ComponentReturnRef returnRef = (ComponentReturnRef) rhs;
			if (!returnRef.getSignature().equals(signature)) {
				invalid.add(returnRef);
			}
		} else if (isArrayReference(rhs)) {
			ComponentArrayRef arrayRef = (ComponentArrayRef) rhs;
			if (isReturnReference(arrayRef.getBase())) {
				ComponentReturnRef returnRef = (ComponentReturnRef) arrayRef.getBase();				
				if (!returnRef.getSignature().equals(signature)) {
					invalid.add(arrayRef);
				} else {
					if (arrayRef.getDimesion() > dimension) {
						invalid.add(arrayRef);
					}
				}				
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

	public final IComponent getLhs() {
		return lhs;
	}

	public final IComponent getRhs() {
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
