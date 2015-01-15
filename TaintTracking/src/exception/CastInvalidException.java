package exception;

public class CastInvalidException extends RuntimeException {

	private static final long serialVersionUID = 6683567867045022307L;

	public CastInvalidException(String message) {
		super(message);
	}
	
	public CastInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

}