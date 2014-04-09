package constraints;

public class LEQConstraint extends AConstraint {

	public LEQConstraint(IConstraintComponent lhs, IConstraintComponent rhs) {
		super(lhs, rhs);
	}

	@Override
	public String toString() {
		return lhs.toString() + " <= " + rhs.toString();
	}
	
}
