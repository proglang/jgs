package exception;

/**
 * 
 * @author Thomas Vogel
 * @version 0.3
 */
public class SootException extends RuntimeException {

	/** */
	private static final long serialVersionUID = 6890539668900008627L;

	/**
	 * 
	 * @param msg
	 */
	public SootException(String msg) {
		super(msg);
	}
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class InvalidLevelException extends SootException {
		
		/**  */
		private static final long serialVersionUID = -3708643254878320457L;

		/**
		 * 
		 * @param msg
		 */
		public InvalidLevelException(String msg) {
			super(msg);
		}
	}
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class InvalidEquationException extends SootException {
		
		/**  */
		private static final long serialVersionUID = -6862104648688697647L;

		/**
		 * 
		 * @param msg
		 */
		public InvalidEquationException(String msg) {
			super(msg);
		}
		
	}
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class SwitchException extends SootException {
		
		/**  */
		private static final long serialVersionUID = -3807355719242345048L;
		
		/**
		 * 
		 * @param msg
		 */
		public SwitchException(String msg) {
			super(msg);
		}
		
	}
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class UnimplementedSwitchException extends SwitchException {
		
		/**  */
		private static final long serialVersionUID = -8829437176470838524L;
		
		/**
		 * 
		 * @param msg
		 */
		public UnimplementedSwitchException(String msg) {
			super(msg);
		}
		
	}
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class InvalidSwitchException extends SwitchException {
		
		/** */
		private static final long serialVersionUID = -690847736317969170L;

		/**
		 * 
		 * @param msg
		 */
		public InvalidSwitchException(String msg) {
			super(msg);
		}

	}
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class NoSecurityLevelException extends SootException {
		
		/**  */
		private static final long serialVersionUID = 1064471321911589298L;

		/**
		 * 
		 * @param msg
		 */
		public NoSecurityLevelException(String msg) {
			super(msg);
		}

	}
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class ProgramCounterException extends SootException {

		/** */
		private static final long serialVersionUID = 8230204683132903586L;

		/**
		 * 
		 * @param msg
		 */
		public ProgramCounterException(String msg) {
			super(msg);
		}
		
	}

}
