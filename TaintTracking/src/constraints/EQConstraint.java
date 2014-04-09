package constraints;

public class EQConstraint extends AConstraint {

	public EQConstraint(IConstraintComponent lhs, IConstraintComponent rhs) {
		super(lhs, rhs);
	}
	
	@Override
	public String toString() {
		return lhs.toString() + " = " + rhs.toString();
	}

}
