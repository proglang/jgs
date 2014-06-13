package security;

import constraints.IConstraintComponent;

public abstract class ALevel implements ILevel {

	public abstract boolean equals(Object obj);
	
	public abstract int hashCode();

	public abstract String getName();

	public abstract String toString();
	
	public IConstraintComponent changeSignature(String signature) {
		return this;
	}

}
