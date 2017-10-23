package util.exceptions;

/**
 * Exception which is thrown if a statement occurs in the code which is not
 * supported by the analyzer.
 * @author koenigr
 */
public class NotSupportedStmtException extends RuntimeException {
	private static final long serialVersionUID = -3912857162841002393L;

	public NotSupportedStmtException(String message) {
		super(message);
		printStackTrace();
		System.exit(0);
	}


}
