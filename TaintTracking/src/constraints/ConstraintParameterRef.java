package constraints;

public class ConstraintParameterRef implements IConstraintComponentVar {

	private final int parameterPos;
	private final String signature;

	protected final String getSignature() {
		return signature;
	}

	public ConstraintParameterRef(int parameterPos, String signature) {
		this.parameterPos = parameterPos;
		this.signature = signature;
	}

	public String toString() {
		return "PR[" + parameterPos + "]@" + signature;
	}

	protected int getParameterPos() {
		return parameterPos;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ConstraintParameterRef ref = (ConstraintParameterRef) obj;
    return parameterPos == ref.parameterPos && signature.equals(ref.signature);
	}
	
}
