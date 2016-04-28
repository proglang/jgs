package utils.exceptions;

public class IllegalFlowException extends RuntimeException {
	
	private static final long serialVersionUID = 6683567867045022307L;

	public IllegalFlowException(String message) {
		super(message);
		printStackTrace();
	}

	public IllegalFlowException(String message, Throwable cause) {
		super(message, cause);
		printStackTrace();
	}
		
}
