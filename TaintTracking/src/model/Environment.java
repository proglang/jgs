package model;

import logging.AnalysisLog;
import logging.AnalysisLogLevel;
import security.ILevel;
import security.ILevelMediator;
import soot.SootField;
import soot.SootMethod;
import analysis.SecurityTypeAnalysis;

/**
 * <h1>Base analysis environment</h1>
 * 
 * The {@link Environment} is the abstract base class for environments of different types. E.g. the environment for {@link SootField} or for
 * {@link SootMethod} which will be analyzed only indirectly (e.g. reference to a {@link SootField} or the invoke of a {@link SootMethod}
 * inside of a method body, see {@link FieldEnvironment} and {@link MethodEnvironment}), or the environment for a {@link SootMethod} which
 * will be analyzed directly (the method which is main suspect of the {@link SecurityTypeAnalysis} analysis, see
 * {@link AnalyzedMethodEnvironment}). Each environment type requires the a logger that allows to log informations of different
 * {@link AnalysisLogLevel} as well as a security annotation object that provides the handling of <em>security level</em>.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see FieldEnvironment
 * @see MethodEnvironment
 * @see AnalyzedMethodEnvironment
 * @see LevelMediator
 * @see AnalysisLog
 */
public abstract class Environment {

	/** The logger that allows to log informations of different {@link AnalysisLogLevel}. */
	private final AnalysisLog log;
	/** The security annotation object that provides the handling of <em>security level</em>. */
	private final ILevelMediator mediator;

	/**
	 * Constructor of a {@link Environment} object that requires a logger in order to allow logging for this object as well as a security
	 * annotation object in order to provide the handling of <em>security levels</em>.
	 * 
	 * @param log
	 *          A {@link AnalysisLog} in order to allow logging for this object.
	 * @param mediator
	 *          A {@link ILevelMediator} in order to provide the handling of <em>security levels</em>.
	 */
	protected Environment(AnalysisLog log, ILevelMediator mediator) {
		super();
		this.mediator = mediator;
		this.log = log;
	}

	/**
	 * Returns the {@link LevelMediator} that allows the handling of <em>security level</em>.
	 * 
	 * @return The level mediator.
	 */
	public ILevelMediator getLevelMediator() {
		return mediator;
	}

	/**
	 * Returns the {@link AnalysisLog} of the {@link Environment#mediator} object.
	 * 
	 * @return The logger.
	 */
	public AnalysisLog getLog() {
		return log;
	}

	/**
	 * Method returns the weakest available <em>security level</em> given by the security annotation object (see {@link Environment#mediator}
	 * ).
	 * 
	 * @return The weakest available <em>security level</em>.
	 */
	public ILevel getWeakestSecurityLevel() {
		return mediator.getGreatestLowerBoundLevel();
	}

}
