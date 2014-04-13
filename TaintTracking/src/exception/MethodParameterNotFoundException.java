package exception;

public class MethodParameterNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -9205443573390773179L;

	public MethodParameterNotFoundException(String message) {
		super(message);
	}
	
	public MethodParameterNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}