package model;

import analysis.TaintTracking;
import logging.SecurityLogger;
import logging.SootLoggerLevel;
import security.LevelMediator;
import soot.SootField;
import soot.SootMethod;

/**
 * <h1>Base analysis environment</h1>
 * 
 * The {@link Environment} is the abstract base class for environments of different types. E.g. the
 * environment for {@link SootField} or for {@link SootMethod} which will be analyzed only
 * indirectly (e.g. reference to a {@link SootField} or the invoke of a {@link SootMethod} inside of
 * a method body, see {@link FieldEnvironment} and {@link MethodEnvironment}), or the environment
 * for a {@link SootMethod} which will be analyzed directly (the method which is main suspect of the
 * {@link TaintTracking} analysis, see {@link AnalyzedMethodEnvironment}). Each environment type
 * requires the a logger that allows to log informations of different {@link SootLoggerLevel} as
 * well as a security annotation object that provides the handling of <em>security level</em>.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see FieldEnvironment
 * @see MethodEnvironment
 * @see AnalyzedMethodEnvironment
 * @see LevelMediator
 * @see SecurityLogger
 */
public abstract class Environment {

	/** The logger that allows to log informations of different {@link SootLoggerLevel}. */
	private final SecurityLogger log;
	/** The security annotation object that provides the handling of <em>security level</em>. */
	private final LevelMediator mediator;

	/**
	 * Constructor of a {@link Environment} object that requires a logger in order to allow logging
	 * for this object as well as a security annotation object in order to provide the handling of
	 * <em>security levels</em>.
	 * 
	 * @param log
	 *            A {@link SecurityLogger} in order to allow logging for this object.
	 * @param mediator
	 *            A {@link LevelMediator} in order to provide the handling of
	 *            <em>security levels</em>.
	 */
	public Environment(SecurityLogger log, LevelMediator mediator) {
		super();
		this.mediator = mediator;
		this.log = log;
	}

	/**
	 * Returns the {@link SecurityLogger} of the {@link Environment#mediator} object.
	 * 
	 * @return The logger.
	 */
	public SecurityLogger getLog() {
		return log;
	}

	/**
	 * Returns the {@link LevelMediator} that allows the handling of <em>security level</em>.
	 * 
	 * @return The level mediator.
	 */
	public LevelMediator getLevelMediator() {
		return mediator;
	}

	/**
	 * Method returns the weakest available <em>security level</em> given by the security annotation
	 * object (see {@link Environment#mediator}).
	 * 
	 * @return The weakest available <em>security level</em>.
	 */
	public String getWeakestSecurityLevel() {
		return mediator.getWeakestSecurityLevel();
	}

}
