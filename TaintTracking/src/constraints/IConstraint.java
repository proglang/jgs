package constraints;

public interface IConstraint {

	public IConstraintComponent getLhs();
	public IConstraintComponent getRhs();
	
	public boolean containsReturnRef();
	public boolean containsParameterRef();
	public boolean containsLevel();
	
	public String toString();
	
}
