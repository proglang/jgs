package error;

import constraints.LEQConstraint;

public class SubSignatureReturnError extends ASubSignatureError {

    public SubSignatureReturnError(LEQConstraint constraint) {
        super(constraint);
    }

}
