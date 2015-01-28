package exception;

public class ExtractorException extends RuntimeException {

    private static final long serialVersionUID = -378410390661810997L;

    public ExtractorException(String message) {
        super(message);
    }

    public ExtractorException(String message, Throwable cause) {
        super(message, cause);
    }

}