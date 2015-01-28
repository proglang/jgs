package exception;

public class AnnotationExtractionException extends RuntimeException {

    private static final long serialVersionUID = 3576959428546532862L;

    public AnnotationExtractionException(String message) {
        super(message);
    }

    public AnnotationExtractionException(String message, Throwable cause) {
        super(message, cause);
    }

}