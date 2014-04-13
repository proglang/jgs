package exception;

public class OperationInvalidException extends RuntimeException {

	private static final long serialVersionUID = 5563372669005197540L;

	public OperationInvalidException(String message) {
		super(message);
	}
	
	public OperationInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

}