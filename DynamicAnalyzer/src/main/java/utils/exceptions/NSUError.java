package utils.exceptions;

/**
 * An exception thrown when a violation of the NSU policy is detected.
 */
public class NSUError extends IFCError {
    public NSUError(String message) {
        super(message);
    }
}
