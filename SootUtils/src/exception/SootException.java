package exception;

import java.io.Serializable;

import soot.Local;
import soot.jimple.Constant;
import soot.jimple.JimpleValueSwitch;
import soot.util.Switch;

/**
 * <h1>Soot exception</h1>
 * 
 * The {@link SootException} is a {@link RuntimeException} and represents an exception which occurs
 * during the analysis. Such an exception can have various reason, e.g. invalid
 * <em>security levels</em>, invalid level equations or erroneous {@link Switch} processes.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.3
 * @see LevelException
 * @see SecurityLevelException
 * @see SwitchException
 */
public abstract class SootException extends RuntimeException {

	/**
	 * <h1>Invalid <em>security level</em> equation exception</h1>
	 * 
	 * The {@link InvalidEquationException} is a {@link LevelException} and indicates that a
	 * specific <em>security level</em> equation is invalid. E.g. if the bracketing of a {@code max}
	 * or {@code min} operator is not correct.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class InvalidEquationException extends LevelException {

		/**
		 * Version number, which is used during deserialization to verify that the sender and
		 * receiver of a serialized object have loaded classes for that object that are compatible
		 * with respect to serialization (see {@link Serializable}).
		 */
		private static final long serialVersionUID = -6862104648688697647L;

		/**
		 * Constructor of an exception which indicates that a specific <em>security level</em>
		 * equation is invalid and which is described by the given message.
		 * 
		 * @param msg
		 *            A detailed description of the exception.
		 */
		public InvalidEquationException(String msg) {
			super(msg);
		}

	}

	/**
	 * <h1>Invalid <em>security level</em> exception</h1>
	 * 
	 * The {@link InvalidLevelException} is a {@link LevelException} and indicates that there exists
	 * an invalid <em>security level</em>. E.g. if a comparison of <em>security levels</em>
	 * processes an incompatible or unknown level.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class InvalidLevelException extends LevelException {

		/**
		 * Version number, which is used during deserialization to verify that the sender and
		 * receiver of a serialized object have loaded classes for that object that are compatible
		 * with respect to serialization (see {@link Serializable}).
		 */
		private static final long serialVersionUID = -3708643254878320457L;

		/**
		 * Constructor for an exception which indicates that there exists an invalid
		 * <em>security level</em> and which is described by the given message.
		 * 
		 * @param msg
		 *            A detailed description of the exception.
		 */
		public InvalidLevelException(String msg) {
			super(msg);
		}
	}

	/**
	 * <h1>Switch not applicable exception</h1>
	 * 
	 * The {@link InvalidSwitchException} is a {@link SwitchException} and indicates that the use of
	 * the switch for the object currently handled is not permitted. E.g. a
	 * {@link JimpleValueSwitch} which updates the <em>security level</em> of the value can not be
	 * applied to a {@link Constant} value, because this value has always the same level.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class InvalidSwitchException extends SwitchException {

		/**
		 * Version number, which is used during deserialization to verify that the sender and
		 * receiver of a serialized object have loaded classes for that object that are compatible
		 * with respect to serialization (see {@link Serializable}).
		 */
		private static final long serialVersionUID = -690847736317969170L;

		/**
		 * Constructor for an exception which indicates that the use of the switch for the object
		 * currently handled is not permitted and which is described by the given message.
		 * 
		 * @param msg
		 *            A detailed description of the exception.
		 */
		public InvalidSwitchException(String msg) {
			super(msg);
		}

	}

	/**
	 * <h1><em>Security level</em> exception</h1>
	 * 
	 * The {@link LevelException} will be thrown in the case of an erroneous handling with
	 * <em>security levels</em>. This exception is used as a abstract class for multiple exceptions
	 * that can occur while handling with <em>security levels</em>. E.g. there are exceptions which
	 * can occur during the lookup of the level of a local variable, during the calculation of a
	 * <em>security level</em> equation or if there is an invalid <em>security level</em>.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 * @see InvalidLevelException
	 * @see InvalidEquationException
	 * @see NoSecurityLevelException
	 */
	public static abstract class LevelException extends SootException {

		/**
		 * Version number, which is used during deserialization to verify that the sender and
		 * receiver of a serialized object have loaded classes for that object that are compatible
		 * with respect to serialization (see {@link Serializable}).
		 */
		private static final long serialVersionUID = -7162746759528941864L;

		/**
		 * Constructor of an exception which indicates that the analysis handles with an erroneous
		 * <em>security levels</em> and which is described by the given message.
		 * 
		 * @param msg
		 *            A detailed description of the exception.
		 */
		public LevelException(String msg) {
			super(msg);
		}

	}

	/**
	 * <h1>No <em>security level</em> for value exception</h1>
	 * 
	 * The {@link NoSecurityLevelException} is a {@link LevelException} and indicates that for a
	 * specific {@link Local} no <em>security level</em> can be determined. I.e. the model which
	 * stores the local variables doesn't contain this local variable, thus no
	 * <em>security level</em> can be found.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class NoSecurityLevelException extends LevelException {

		/**
		 * Version number, which is used during deserialization to verify that the sender and
		 * receiver of a serialized object have loaded classes for that object that are compatible
		 * with respect to serialization (see {@link Serializable}).
		 */
		private static final long serialVersionUID = 1064471321911589298L;

		/**
		 * Constructor for an exception which indicates that for a specific {@link Local} no
		 * <em>security level</em> can be determined and which is described by the given message.
		 * 
		 * @param msg
		 *            A detailed description of the exception.
		 */
		public NoSecurityLevelException(String msg) {
			super(msg);
		}

	}

	/**
	 * <h1><em>Security level</em> implementation exception</h1>
	 * 
	 * The {@link SecurityLevelException} indicates that the implementation of the
	 * {@code SecurityLevel} class can't be checked correctly. E.g. the implementation can't be
	 * found or there are issues with the compilation.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class SecurityLevelException extends SootException {

		/**
		 * Version number, which is used during deserialization to verify that the sender and
		 * receiver of a serialized object have loaded classes for that object that are compatible
		 * with respect to serialization (see {@link Serializable}).
		 */
		private static final long serialVersionUID = 6923594123071071122L;

		/**
		 * Constructor of an exception which indicates that the implementation of the
		 * {@code SecurityLevel} class can't be checked correctly and which is described by the
		 * given message.
		 * 
		 * @param msg
		 *            A detailed description of the exception.
		 */
		public SecurityLevelException(String msg) {
			super(msg);
		}

	}

	/**
	 * <h1>Switch Exception</h1>
	 * 
	 * The {@link SwitchException} will be thrown in the case of an erroneous {@link Switch}
	 * execution. This exception is used as a abstract class for two exceptions that can occur while
	 * using a {@link Switch}. One of these indicates that the used switch has no implementation for
	 * the currently handled object type. The second exception indicates that the use of the switch
	 * for the object currently used is not permitted.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 * @see UnimplementedSwitchException
	 * @see InvalidSwitchException
	 */
	public static abstract class SwitchException extends SootException {

		/**
		 * Version number, which is used during deserialization to verify that the sender and
		 * receiver of a serialized object have loaded classes for that object that are compatible
		 * with respect to serialization (see {@link Serializable}).
		 */
		private static final long serialVersionUID = -3807355719242345048L;

		/**
		 * Constructor for an exception which indicates that an error occurred during the execution
		 * of a {@link Switch} and which is described by the given message.
		 * 
		 * @param msg
		 *            A detailed description of the exception.
		 */
		public SwitchException(String msg) {
			super(msg);
		}

	}

	/**
	 * <h1>Switch not implemented for this type exception</h1>
	 * 
	 * The {@link UnimplementedSwitchException} is a {@link SwitchException} and indicates that the
	 * used switch has no implementation for the currently handled object type.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class UnimplementedSwitchException extends SwitchException {

		/**
		 * Version number, which is used during deserialization to verify that the sender and
		 * receiver of a serialized object have loaded classes for that object that are compatible
		 * with respect to serialization (see {@link Serializable}).
		 */
		private static final long serialVersionUID = -8829437176470838524L;

		/**
		 * Constructor for an exception which indicates that the used switch has no implementation
		 * for the currently handled object type and which is described by the given message.
		 * 
		 * @param msg
		 *            A detailed description of the exception.
		 */
		public UnimplementedSwitchException(String msg) {
			super(msg);
		}

	}

	/**
	 * Version number, which is used during deserialization to verify that the sender and receiver
	 * of a serialized object have loaded classes for that object that are compatible with respect
	 * to serialization (see {@link Serializable}).
	 */
	private static final long serialVersionUID = 6890539668900008627L;

	/**
	 * Constructor of an exception which indicates that an exception occurred during the analysis
	 * and which is described by the given message.
	 * 
	 * @param msg
	 *            A detailed description of the exception.
	 */
	public SootException(String msg) {
		super(msg);
	}

}
