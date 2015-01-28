package error;

import constraints.LEQConstraint;

public abstract class ASubSignatureError implements ISubSignatureError {

    private final LEQConstraint constraint;

    protected ASubSignatureError(LEQConstraint constraint) {
        this.constraint = constraint;
    }

    public LEQConstraint getConstraint() {
        return constraint;
    }

}
