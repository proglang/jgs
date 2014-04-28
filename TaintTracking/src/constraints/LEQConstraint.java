package constraints;

public class LEQConstraint extends AConstraint {

	public LEQConstraint(IConstraintComponent lhs, IConstraintComponent rhs) {
		super(lhs, rhs);
	}

	@Override
	public String toString() {
		return lhs.toString() + " <= " + rhs.toString();
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) return true;
		if (object == null) return false;
		if (getClass() != object.getClass()) return false;
		LEQConstraint cons = (LEQConstraint) object;
		return getLhs().equals(cons.getLhs()) && getRhs().equals(cons.getRhs());
	}

}
