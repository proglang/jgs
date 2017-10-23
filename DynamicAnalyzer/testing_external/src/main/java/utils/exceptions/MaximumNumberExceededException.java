package util.exceptions;

public class MaximumNumberExceededException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6638359207149281836L;

	/**
	 * @param message
	 */
	public MaximumNumberExceededException(String message) {
		super(message);
		printStackTrace();
		System.exit(0);
	}
}
