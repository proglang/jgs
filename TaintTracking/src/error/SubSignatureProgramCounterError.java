package error;

import constraints.LEQConstraint;

public class SubSignatureProgramCounterError extends ASubSignatureError {

	public SubSignatureProgramCounterError(LEQConstraint constraint) {
		super(constraint);
	}

}
