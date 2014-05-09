package analysis;

import static utils.AnalysisUtils.generateFileName;
import static utils.AnalysisUtils.getSignatureOfMethod;

import java.util.List;

import logging.AnalysisLog;
import model.AnalyzedMethodEnvironment;
import security.ILevel;
import security.ILevelMediator;
import soot.SootClass;
import soot.SootMethod;
import utils.AnalysisUtils;
import extractor.UsedObjectStore;

/**
 * <h1>Base switch for the {@link SecurityLevelAnalysis} analysis</h1>
 * 
 * The {@link SecuritySwitch} is the base class for switches which are used for the {@link SecurityLevelAnalysis} analysis. The base
 * switch contains the incoming and outgoing locals map of a specific
 * {@link SecurityLevelAnalysis#flowThrough(LocalsMap, soot.Unit, LocalsMap)} state as well as the environment of the current analyzed
 * method. The class provides multiple methods which allows to log, to compare <em>security level</em>, to check <em>write effect</em> and
 * also to initialize the lookup and the update of <em>security level</em>. <br />
 * The class {@link SecuritySwitch} is the parent class of the following:
 * <ul>
 * <li>{@link SecurityLevelStmtSwitch}: switch that processes statements - depending on the statement the <em>security levels</em> will be
 * calculated or updated (with the help of other switches) for components of the statements and also the statements will be checked for
 * security violations.</li>
 * <li>{@link SecurityLevelReadValueSwitch}: switch that processes values - depending on the value, the <em>security level</em> will be
 * looked up for the value and also stored in the switch. If necessary the switch checks for security violations, too (e.g. for method
 * invocations the parameter <em>security level</em>).</li>
 * <li>{@link SecurityLevelWriteValueSwitch}: switch that processes values - depending on the value, the <em>security level</em> of this
 * value will be updated by the given level. The switch checks for security violations, too (e.g. occurring <em>write effects</em> or
 * invalid assignments).</li>
 * </ul>
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * 
 * @see LocalsMap
 * @see SecurityLevelAnalysis
 * @see AnalyzedMethodEnvironment
 */
abstract public class SecuritySwitch<U> {

	/** Current analyzed method environment. */
	private final AnalyzedMethodEnvironment analyzedMethodEnvironment;
	/** Current incoming map of the local variables. */
	private final U in;
	/** Current outgoing map of the local variables. */
	private final U out;
	/**
	 * DOC
	 */
	private final UsedObjectStore store;

	/**
	 * Constructor of a {@link SecuritySwitch} that requires the current incoming and outgoing map of local variables as well as the
	 * environment of the current analyzed method.
	 * 
	 * @param analysisEnvironment
	 *          The environment of the method that is currently analyzed.
	 * @param in
	 *          Current incoming map of the local variables.
	 * @param out
	 *          Current outgoing map of the local variables.
	 */
	protected SecuritySwitch(AnalyzedMethodEnvironment methodEnvironment, UsedObjectStore store, U in, U out) {
		this.analyzedMethodEnvironment = methodEnvironment;
		this.store = store;
		this.out = out;
		this.in = in;
	}

	/**
	 * Returns the file name of the class which declares the analyzed method. The resulting file name can differ from the real file name, e.g.
	 * for nested classes (see {@link AnalysisUtils#generateFileName(SootClass)}) and the file name do not contain the file suffix.
	 * 
	 * @return The file name of the class which declares the analyzed method.
	 */
	protected String getFileName() {
		return generateFileName(analyzedMethodEnvironment.getSootMethod());
	}

	/**
	 * Returns the {@link LevelMediator} that allows the handling of <em>security level</em>.
	 * 
	 * @return The security annotation object.
	 */
	protected ILevelMediator getMediator() {
		return analyzedMethodEnvironment.getLevelMediator();
	}

	/**
	 * Returns the {@link AnalysisLog} of the {@link AnalyzedMethodEnvironment#mediator} object.
	 * 
	 * @return The logger.
	 */
	protected AnalysisLog getLog() {
		return analyzedMethodEnvironment.getLog();
	}
	
	protected final AnalyzedMethodEnvironment getAnalyzedEnvironment() {
		return analyzedMethodEnvironment;
	}

	/**
	 * Returns the strongest <em>security level</em> of the given levels. If one of the two given levels is 'void' (
	 * {@link LevelMediator#VOID_LEVEL} ), the resulting level is also 'void'. If one of the both given levels is an invalid
	 * <em>security level</em> the method will log this exception and will return the weakest available <em>security level</em> (see
	 * {@link SecuritySwitch#getWeakestSecurityLevel()}).
	 * 
	 * @param level1
	 *          First level which should be compared.
	 * @param level2
	 *          Second level which should be compared.
	 * @return If none of the given level is 'void' and invalid, the strongest level of the both given levels will be returned. 'void' will be
	 *         returned, if one of the given levels is 'void' ( {@link LevelMediator#VOID_LEVEL}).
	 */
	protected ILevel getMaxLevel(ILevel level1, ILevel level2) {
		return getMediator().getLeastUpperBoundLevelOf(level1, level2);
	}

	/**
	 * Returns the strongest <em>security level</em> of the given level list. If one of the given levels is invalid or 'void' (
	 * {@link LevelMediator#VOID_LEVEL}), the method will log this exception and will return the weakest available <em>security level</em>
	 * (see {@link SecuritySwitch#getWeakestSecurityLevel()}).
	 * 
	 * @param levels
	 *          List of <em>security levels</em> for which the strongest contained level should be returned.
	 * @return If all given levels of the list are valid, the strongest <em>security level</em> contained by the given list will be returned.
	 *         Otherwise the the weakest available <em>security level</em> will be returned.
	 */
	protected ILevel getMaxLevel(List<ILevel> levels) {
		return getMediator().getLeastUpperBoundLevelOf(levels);
	}

	/**
	 * Returns the readable method signature of the analyzed method. The returned signature contains the visibility of the method, the package
	 * name and also the types of the parameters and the return type.
	 * 
	 * @return The readable method signature of the analyzed method.
	 */
	protected String getSignatureOfAnalyzedMethod() {
		return getSignatureOfMethod(getAnalyzedMethod());
	}

	/**
	 * Returns the weakest <em>security level</em> of the given levels. If one of the two given levels is 'void' (
	 * {@link LevelMediator#VOID_LEVEL} ), the resulting level is also 'void'. If one of the both given levels is an invalid
	 * <em>security level</em> the method will log this exception and will return the weakest available <em>security level</em> (see
	 * {@link SecuritySwitch#getWeakestSecurityLevel()}).
	 * 
	 * @param level1
	 *          First level which should be compared.
	 * @param level2
	 *          Second level which should be compared.
	 * @return If none of the given level is 'void' and invalid, the weakest level of the both given levels will be returned. 'void' will be
	 *         returned, if one of the given levels is 'void' ( {@link LevelMediator#VOID_LEVEL}).
	 */
	protected ILevel getMinLevel(ILevel level1, ILevel level2) {
		return getMediator().getGreatestLowerBoundLevelOf(level1, level2);
	}

	/**
	 * The method returns the current analyzed {@link SootMethod}.
	 * 
	 * @return The analyzed method.
	 */
	protected SootMethod getAnalyzedMethod() {
		return analyzedMethodEnvironment.getSootMethod();
	}

	/**
	 * Method that returns the source line number of the currently handled statement of the analyzed method. Note, that immediately after the
	 * initialization of the environment and before the start of flow analysis, the method will return {@code 0}.
	 * 
	 * @return The source line number of the current handled statement of the analyzed method or if this wasn't set {@code 0}.
	 */
	protected long getSourceLine() {
		return analyzedMethodEnvironment.getSrcLn();
	}

	/**
	 * Method returns the weakest available <em>security level</em> (see {@link Environment#getLevelMediator()#getWeakestSecurityLevel()}).
	 * 
	 * @return The weakest available <em>security level</em>.
	 */
	protected ILevel getWeakestSecurityLevel() {
		return analyzedMethodEnvironment.getWeakestSecurityLevel();
	}

	protected UsedObjectStore getStore() {
		return store;
	}

	protected final U getIn() {
		return in;
	}

	protected final U getOut() {
		return out;
	}

	/**
	 * Checks whether the first given level is equals to the second given level. If one of the both given levels is an invalid
	 * <em>security level</em> the method will log this exception and will return {@code false}.
	 * 
	 * @param level1
	 *          Level for which should be checked whether it is equals to the second given level.
	 * @param level2
	 *          Level for which should be checked whether it is equals to the first given level.
	 * @return {@code true} if the first given level is equals to the second <em>security level</em> , {@code false} otherwise.
	 */
	protected boolean isEqualLevel(ILevel level1, ILevel level2) {
		return level1.equals(level2);
	}

	/**
	 * Checks whether the first given level is weaker than the second given level. If one of the both given levels is an invalid
	 * <em>security level</em> the method will log this exception and will return {@code false}.
	 * 
	 * @param level1
	 *          Level for which should be checked whether it is weaker than the second given level.
	 * @param level2
	 *          Level for which should be checked whether it is equals or stronger than the first given level.
	 * @return {@code true} if the first given level is weaker than the second <em>security level</em>, {@code false} otherwise.
	 */
	protected boolean isWeakerLevel(ILevel level1, ILevel level2) {
		return isWeakerOrEqualLevel(level1, level2) && !isEqualLevel(level1, level2);
	}

	/**
	 * Checks whether the first given level is weaker or equals than the second given level. If one of the both given levels is an invalid
	 * <em>security level</em> the method will log this exception and will return {@code false}.
	 * 
	 * @param level1
	 *          Level for which should be checked whether it is weaker or equals than the second given level.
	 * @param level2
	 *          Level for which should be checked whether it is stronger than the first given level.
	 * @return {@code true} if the first given level is weaker or equals than the second <em>security level</em>, {@code false} otherwise.
	 */
	protected boolean isWeakerOrEqualLevel(ILevel level1, ILevel level2) {
		return getMediator().isWeakerOrEquals(level1, level2);
	}

	/**
	 * Logs the given message as a security violation. The file name is created by the analyzed {@link SootMethod}, which the environment
	 * stores, the source line number is the line number of current handled statement.
	 * 
	 * @param msg
	 *          Message that should be printed as a security violation.
	 * @see AnalysisLog#security(String, long, String)
	 */
	protected void logSecurity(String msg) {
		getLog().security(getFileName(), getSourceLine(), msg);
	}

	/**
	 * Logs the given message as a warning. The file name is created by the analyzed {@link SootMethod}, which the environment stores, the
	 * source line number is the line number of current handled statement.
	 * 
	 * @param msg
	 *          Message that should be printed as a warning.
	 * @see AnalysisLog#warning(String, long, String)
	 */
	protected void logWarning(String msg) {
		getLog().warning(getFileName(), getSourceLine(), msg);
	}
	
	/**
	 * DOC
	 * 
	 * @param msg
	 */
	protected void logEffect(String msg) {
		getLog().effect(getFileName(), getSourceLine(), msg);
	}

}