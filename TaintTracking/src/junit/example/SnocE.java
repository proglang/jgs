package junit.example;

import constraints.AConstraint;
import constraints.IConstraintComponent;

public class SnocE extends AConstraint {

	public SnocE(IConstraintComponent lhs, IConstraintComponent rhs) {
		super(lhs, rhs);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
		result = prime * result + ((rhs == null) ? 0 : rhs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		SnocE other = (SnocE) obj;
		if (lhs == null) {
			if (other.lhs != null) return false;
		} else if (!lhs.equals(other.lhs)) return false;
		if (rhs == null) {
			if (other.rhs != null) return false;
		} else if (!rhs.equals(other.rhs)) return false;
		return true;
	}

	@Override
	public String toString() {
		return lhs.toString() + " = " + rhs.toString();
	}

}
