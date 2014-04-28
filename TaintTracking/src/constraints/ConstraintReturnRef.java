package constraints;

public class ConstraintReturnRef implements IConstraintComponentVar {

	private final String signature;
	
	public ConstraintReturnRef(String signature) {
		this.signature = signature;
	}

	protected final String getSignature() {
		return signature;
	}

	public String toString() {
		return "RR@" + signature;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ConstraintReturnRef ref = (ConstraintReturnRef) obj;
    return signature.equals(ref.signature);
	}
	
}
