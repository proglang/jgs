package constraints;

public interface IConstraint {

	public boolean containsLevel();

	public boolean containsParameterRef();

	public boolean containsReturnRef();

	public IConstraintComponent getLhs();

	public IConstraintComponent getRhs();

	public String toString();

}
