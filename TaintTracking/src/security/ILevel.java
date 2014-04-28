package security;

import constraints.IConstraintComponentConstant;

public interface ILevel extends IConstraintComponentConstant {

	public boolean equals(Object obj);

	public String getName();

	public String toString();

}
