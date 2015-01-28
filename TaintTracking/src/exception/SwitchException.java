package exception;

public class SwitchException extends RuntimeException {

    private static final long serialVersionUID = -5661280236038965380L;

    public SwitchException(String message) {
        super(message);
    }

    public SwitchException(String message, Throwable cause) {
        super(message, cause);
    }

}