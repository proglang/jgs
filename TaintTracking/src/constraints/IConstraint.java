package constraints;

import java.util.Set;

import security.ILevel;

public interface IConstraint {

	public boolean containsComponent(IConstraintComponent component);

	public boolean containsLocal();
	
	public boolean containsGeneratedLocal();

	public boolean containsParameterReference();

	public boolean containsParameterReferenceFor(String signature);

	public boolean containsParameterReferenceFor(String signature, int position);

	public boolean containsProgramCounterReference();

	public boolean containsProgramCounterReferenceFor(String signature);

	public boolean containsReturnReference();

	public boolean containsReturnReferenceFor(String signature);

	public boolean equals(Object object);

	public Set<ILevel> getContainedLevel();

	public Set<ConstraintParameterRef> getInvalidParameterReferencesFor(String signature, int count);

	public Set<ConstraintReturnRef> getInvalidReturnReferencesFor(String signature);

	public IConstraintComponent getLhs();

	public IConstraintComponent getRhs();

	public int hashCode();

	public String toString();

	public IConstraint changeAllComponentsSignature(String signature);

}
