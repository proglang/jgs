package exception;

public class EnvironmentNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7739586508557685408L;

	public EnvironmentNotFoundException(String message) {
		super(message);
	}
	
	public EnvironmentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}