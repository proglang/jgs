package security;

import constraints.IConstraintComponentConstant;

public interface ILevel extends IConstraintComponentConstant {

	public boolean equals(Object obj);
	
	public int hashCode();

	public String getName();

	public String toString();

}
