package exception;

public class LevelNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 8494469164373290349L;

    public LevelNotFoundException(String message) {
        super(message);
    }

    public LevelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}