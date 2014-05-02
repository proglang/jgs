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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((signature == null) ? 0 : signature.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ConstraintReturnRef other = (ConstraintReturnRef) obj;
		if (signature == null) {
			if (other.signature != null) return false;
		} else if (!signature.equals(other.signature)) return false;
		return true;
	}
	
}
