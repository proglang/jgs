package constraints;

import security.ILevel;

public abstract class AConstraint implements IConstraint {

	public static boolean isLevel(IConstraintComponent component) {
		return component instanceof ILevel;
	}
	public static boolean isParameterRef(IConstraintComponent component) {
		return component instanceof ConstraintParameterRef;
	}

	public static boolean isReturnRef(IConstraintComponent component) {
		return component instanceof ConstraintReturnRef;
	}

	protected final IConstraintComponent lhs;

	protected final IConstraintComponent rhs;

	public AConstraint(IConstraintComponent lhs, IConstraintComponent rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public final boolean containsLevel() {
		return isLevel(lhs) || isLevel(rhs);
	}

	public final boolean containsParameterRef() {
		return isParameterRef(lhs) || isParameterRef(rhs);
	}

	public final boolean containsReturnRef() {
		return isReturnRef(lhs) || isReturnRef(rhs);
	}

	public final IConstraintComponent getLhs() {
		return lhs;
	}

	public final IConstraintComponent getRhs() {
		return rhs;
	}

	public abstract String toString();

}
