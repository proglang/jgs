package security;

import constraints.IConstraintComponent;

public interface ILevel extends IConstraintComponent {

	public boolean equals(Object obj);

	public String getName();

	public String toString();

}
