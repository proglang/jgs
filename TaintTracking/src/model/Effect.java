package model;

/**
 * <h1>Side effect to a <em>security level</em></h1>
 * 
 * The {@link Effect} represents a calculated effect to a <em>security level</em> which occurs at a
 * specific source line number inside of a analyzed method. Each effect has also a cause that
 * indicates why this effect has occurred, or from where this effect was inherited.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 * @see Cause
 */
public class Effect {

	/** The reason that indicates why this effect has occurred. */
	private final Cause cause;
	/** The effected <em>security level</em> of this effect. */
	private final String effected;
	/** The source line number at which this effect occurs. */
	private final long srcLn;

	/**
	 * Constructor of an {@link Effect} that requires the effected <em>security level</em>, the
	 * source line number at which the effect occurs and also a cause why this effect occurs.
	 * 
	 * @param effected
	 *            <em>Security level</em> that is effected.
	 * @param srcLn
	 *            Source line where the effect occurs.
	 * @param cause
	 *            Subclass of {@link Cause} which specifies the reason of this effect.
	 */
	public Effect(String effected, long srcLn, Cause cause) {
		super();
		this.effected = effected;
		this.srcLn = srcLn;
		this.cause = cause;
	}

	/**
	 * Returns a subclass of {@link Cause} which represents the reason of the effect.
	 * 
	 * @return The cause of the effect.
	 */
	public Cause getCause() {
		return this.cause;
	}

	/**
	 * Returns the effected <em>security level</em>.
	 * 
	 * @return The effected <em>security level</em>.
	 */
	public String getEffected() {
		return this.effected;
	}

	/**
	 * Returns the source line number where the effect occurs.
	 * 
	 * @return The source line number where the effect occurs.
	 */
	public long getSrcLn() {
		return this.srcLn;
	}

}