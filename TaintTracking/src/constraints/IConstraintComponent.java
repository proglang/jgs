package constraints;

public interface IConstraintComponent {

	public String toString();
	
	public boolean equals(Object object);
	
	public int hashCode();

	public IConstraintComponent changeSignature(String signature);

}
