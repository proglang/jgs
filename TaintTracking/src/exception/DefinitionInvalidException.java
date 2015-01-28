package exception;

public class DefinitionInvalidException extends RuntimeException {

    private static final long serialVersionUID = 4396762179692576572L;

    public DefinitionInvalidException(String message) {
        super(message);
    }

    public DefinitionInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

}
