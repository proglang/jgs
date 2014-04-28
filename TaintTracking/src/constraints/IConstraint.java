package constraints;

import java.util.List;

import security.ILevel;

public interface IConstraint {

	public boolean containsComponent(IConstraintComponent component);

	public IConstraintComponent getLhs();

	public IConstraintComponent getRhs();

	public String toString();

	public boolean equals(Object object);

	public boolean containsReturnReferenceFor(String signature);

	public List<ILevel> getContainedLevel();

	public boolean containsParameterReferenceFor(String signature, int position);

	public boolean containsParameterReference();

	public List<ConstraintParameterRef> getInvalidParameterReferencesFor(String signature, int count);

	public List<ConstraintReturnRef> getInvalidReturnReferencesFor(String signature);

	public boolean containsProgramCounterReference();

	public boolean containsReturnReference();

}
