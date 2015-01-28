package exception;

public class ConstraintUnsupportedException extends RuntimeException {

    private static final long serialVersionUID = 6683567867045022307L;

    public ConstraintUnsupportedException(String message) {
        super(message);
    }

    public ConstraintUnsupportedException(String message, Throwable cause) {
        super(message, cause);
    }

}