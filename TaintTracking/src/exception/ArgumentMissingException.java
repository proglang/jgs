package exception;

public class ArgumentMissingException extends RuntimeException {

	private static final long serialVersionUID = -8857915538878310898L;

	public ArgumentMissingException(String message) {
		super(message);
	}
	
	public ArgumentMissingException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
