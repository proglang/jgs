package exception;

public class ProgramCounterException extends RuntimeException {

    private static final long serialVersionUID = -858752050580242983L;

    public ProgramCounterException(String message) {
        super(message);
    }

    public ProgramCounterException(String message, Throwable cause) {
        super(message, cause);
    }

}