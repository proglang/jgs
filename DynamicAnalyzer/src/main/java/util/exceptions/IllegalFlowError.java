package util.exceptions;

/**
 * An exception thrown when an illegal information flow is detected.
 */
public class IllegalFlowError extends IFCError {
    public IllegalFlowError(String message) {
        super(message);
    }
}
