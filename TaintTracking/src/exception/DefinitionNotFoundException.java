package exception;

public class DefinitionNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -2683417491780289085L;

	public DefinitionNotFoundException(String message) {
		super(message);
	}
	
	public DefinitionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
