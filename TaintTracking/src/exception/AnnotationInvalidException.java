package exception;

public class AnnotationInvalidException extends RuntimeException {

    private static final long serialVersionUID = -4473410060226074476L;

    public AnnotationInvalidException(String message) {
        super(message);
    }

    public AnnotationInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

}