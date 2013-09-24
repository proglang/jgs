package model;

/**
 * The effect class represents a calculated effect to a class/object which occurs at a specific
 * source line. Each effect has also a cause why this effect has occurred, or from where this
 * effect was inherited.
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class Effect {

	private long srcLn;
	private String effected;
	private Cause cause;

	/**
	 * Constructor of an effect that requires the class of the effected object, the source line
	 * number on which the effect occurs and also a cause.
	 * 
	 * @param effected
	 *            Class of the effected object as String.
	 * @param srcLn
	 *            Source line where the effect occurs.
	 * @param cause
	 *            Subtype of cause which specifies the reason of this effect.
	 */
	public Effect(String effected, long srcLn, Cause cause) {
		super();
		this.effected = effected;
		this.srcLn = srcLn;
		this.cause = cause;
	}

	/**
	 * Returns the source line number where the effect occurs.
	 * 
	 * @return The source line number where the effect occurs.
	 */
	public long getSrcLn() {
		return this.srcLn;
	}

	/**
	 * Returns the effected class name as String.
	 * 
	 * @return The effected class.
	 */
	public String getEffected() {
		return this.effected;
	}

	/**
	 * Returns a subtype of cause which represents the reason of the effect.
	 * 
	 * @return The cause of the effect.
	 */
	public Cause getCause() {
		return this.cause;
	}

}