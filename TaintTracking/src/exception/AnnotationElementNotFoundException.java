package exception;

public class AnnotationElementNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7091381420460205533L;

	public AnnotationElementNotFoundException(String message) {
		super(message);
	}
	
	public AnnotationElementNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}