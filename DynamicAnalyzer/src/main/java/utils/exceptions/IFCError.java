package utils.exceptions;

public class IFCError extends RuntimeException {
	
	private static final long serialVersionUID = 6683567867045022307L;

	public IFCError(String message) {
		super(message);
		printStackTrace();
	}
		
}
