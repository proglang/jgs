package exception;

public class AnalysisException extends RuntimeException {

    private static final long serialVersionUID = 6683567867045022307L;

    public AnalysisException(String message) {
        super(message);
    }

    public AnalysisException(String message, Throwable cause) {
        super(message, cause);
    }

}