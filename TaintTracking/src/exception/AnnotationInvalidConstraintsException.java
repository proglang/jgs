package exception;

public class AnnotationInvalidConstraintsException extends RuntimeException {

    private static final long serialVersionUID = 8017292929554865166L;

    public AnnotationInvalidConstraintsException(String message) {
        super(message);
    }

    public AnnotationInvalidConstraintsException(String message, Throwable cause) {
        super(message, cause);
    }

}