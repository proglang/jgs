package exception;

public class InvalidDimensionException extends RuntimeException {

	private static final long serialVersionUID = -9205443573390773179L;

	public InvalidDimensionException(String message) {
		super(message);
	}
	
	public InvalidDimensionException(String message, Throwable cause) {
		super(message, cause);
	}

}