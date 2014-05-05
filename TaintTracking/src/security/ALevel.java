package security;

public abstract class ALevel implements ILevel {

	public abstract boolean equals(Object obj);
	
	public abstract int hashCode();

	public abstract String getName();

	public abstract String toString();

}
