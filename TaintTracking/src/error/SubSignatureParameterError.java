package error;

import constraints.LEQConstraint;

public class SubSignatureParameterError extends ASubSignatureError {

    private final int position;

    public SubSignatureParameterError(LEQConstraint constraint, int position) {
        super(constraint);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

}
