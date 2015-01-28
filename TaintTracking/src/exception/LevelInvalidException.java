package exception;

public class LevelInvalidException extends RuntimeException {

    private static final long serialVersionUID = -7735549001785567852L;

    public LevelInvalidException(String message) {
        super(message);
    }

    public LevelInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

}