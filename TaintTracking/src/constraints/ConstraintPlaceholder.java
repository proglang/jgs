package constraints;

public class ConstraintPlaceholder implements IConstraintComponent {

	@Override
	public IConstraintComponent changeSignature(String signature) {
		return new ConstraintPlaceholder();
	}

	@Override
	public String toString() {
		return "#";
	}
	
}
