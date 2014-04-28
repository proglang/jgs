package exception;

public class AnalysisTypeException extends RuntimeException {

	private static final long serialVersionUID = 6683567867045022307L;

	public AnalysisTypeException(String message) {
		super(message);
	}
	
	public AnalysisTypeException(String message, Throwable cause) {
		super(message, cause);
	}

}