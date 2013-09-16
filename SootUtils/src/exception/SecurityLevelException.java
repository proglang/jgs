package exception;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class SecurityLevelException extends RuntimeException {

	/**  */
	private static final long serialVersionUID = 6923594123071071122L;
	
	/**
	 * 
	 * @param msg
	 */
	public SecurityLevelException(String msg) {
		super(msg);
	}

}
