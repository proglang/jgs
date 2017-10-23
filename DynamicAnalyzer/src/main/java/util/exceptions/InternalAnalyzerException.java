package util.exceptions;

public class InternalAnalyzerException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String globalMessage = "Oops. Seems that you've found an "
			+ "internal problem. Please help us to improve our product and send us "
			+ "an email with the ?? stack trace?? to ...@...";
	
	/**
	 * @param message 
	 */
	public InternalAnalyzerException(String message) {
		super(message);
		printMessage();
	}

	public InternalAnalyzerException(String message, Throwable cause) {
		super(message, cause);
	}
		
	/**
	 * 
	 */
	public InternalAnalyzerException() {
		super(globalMessage);
		printMessage();
		printStackTrace();
		System.exit(0);
	}

	public void printMessage() {
		printStackTrace();
	}

}
