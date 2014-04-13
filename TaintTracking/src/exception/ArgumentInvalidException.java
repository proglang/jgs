package exception;

public class ArgumentInvalidException extends RuntimeException {

	private static final long serialVersionUID = -7654700779369025768L;

	public ArgumentInvalidException(String message) {
		super(message);
	}
	
	public ArgumentInvalidException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
