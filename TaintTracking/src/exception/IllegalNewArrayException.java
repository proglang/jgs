package exception;

public class IllegalNewArrayException extends RuntimeException {

	private static final long serialVersionUID = -378410390661810997L;

	public IllegalNewArrayException(String message) {
		super(message);
	}
	
	public IllegalNewArrayException(String message, Throwable cause) {
		super(message, cause);
	}

}