package exception;

public class SootException extends Exception {

	private static final long serialVersionUID = 6890539668900008627L;

	public SootException(String msg) {
		super(msg);
	}
	
	public static class InvalidLevelException extends SootException {
		
		private static final long serialVersionUID = -3708643254878320457L;

		public InvalidLevelException(String msg) {
			super(msg);
		}
	}
	
	public static class InvalidEquationException extends SootException {
		
		private static final long serialVersionUID = -6862104648688697647L;

		public InvalidEquationException(String msg) {
			super(msg);
		}
		
	}

}
