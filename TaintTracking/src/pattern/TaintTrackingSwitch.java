package pattern;

import java.util.ArrayList;
import java.util.List;

import analysis.TaintTracking;

import logging.SecurityLogger;
import model.AnalyzedMethodEnvironment;
import model.Environment;
import model.LocalsMap;
import model.MethodEnvironment;
import model.MethodEnvironment.MethodParameter;
import security.LevelEquation;
import security.LevelEquationVisitor.LevelEquationEvaluationVisitor;
import security.SecurityAnnotation;
import soot.Local;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.InvokeExpr;
import soot.jimple.ParameterRef;
import utils.SecurityMessages;
import utils.SootUtils;
import exception.SootException.InvalidLevelException;
import exception.SootException.NoSecurityLevelException;
import exception.SootException.SwitchException;

/**
 * <h1>Base switch for the {@link TaintTracking} analysis</h1>
 * 
 * The {@link TaintTrackingSwitch} is the base class for switches which are used for the
 * {@link TaintTracking} analysis. The base switch contains the incoming and outgoing locals map of
 * a specific {@link TaintTracking#flowThrough(LocalsMap, soot.Unit, LocalsMap)} state as well as
 * the environment of the current analyzed method. The class provides multiple methods which allows
 * to log, to compare <em>security level</em>, to check <em>write effect</em> and also to initialize
 * the lookup and the update of <em>security level</em>. <br />
 * The class {@link TaintTrackingSwitch} is the parent class of the following:
 * <ul>
 * <li>{@link StatementSwitch}: switch that processes statements - depending on the statement the
 * <em>security levels</em> will be calculated or updated (with the help of other switches) for
 * components of the statements and also the statements will be checked for security violations.</li>
 * <li>{@link LookupSwitch}: switch that processes values - depending on the value, the
 * <em>security level</em> will be looked up for the value and also stored in the switch. If
 * necessary the switch checks for security violations, too (e.g. for method invocations the
 * parameter <em>security level</em>).</li>
 * <li>{@link UpdateSwitch}: switch that processes values - depending on the value, the
 * <em>security level</em> of this value will be updated by the given level. The switch checks for
 * security violations, too (e.g. occurring <em>write effects</em> or invalid assignments).</li>
 * </ul>
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * 
 * @see LocalsMap
 * @see TaintTracking
 * @see AnalyzedMethodEnvironment
 */
abstract public class TaintTrackingSwitch {

	/** Current analyzed method environment. */
	protected final AnalyzedMethodEnvironment analyzedMethodEnvironment;
	/** Current incoming map of the local variables. */
	protected final LocalsMap in;
	/** Current outgoing map of the local variables. */
	protected final LocalsMap out;

	/**
	 * Constructor of a {@link TaintTrackingSwitch} that requires the current incoming and outgoing
	 * map of local variables as well as the environment of the current analyzed method.
	 * 
	 * @param analysisEnvironment
	 *            The environment of the method that is currently analyzed.
	 * @param in
	 *            Current incoming map of the local variables.
	 * @param out
	 *            Current outgoing map of the local variables.
	 */
	public TaintTrackingSwitch(AnalyzedMethodEnvironment methodEnvironment, LocalsMap in,
			LocalsMap out) {
		this.analyzedMethodEnvironment = methodEnvironment;
		this.out = out;
		this.in = in;
	}

	/**
	 * Logs the given message as an error. The file name is created by the analyzed
	 * {@link SootMethod}, which the environment stores, the source line number is the line number
	 * of current handled statement.
	 * 
	 * @param msg
	 *            Message that should be printed as an error.
	 * @see SecurityLogger#error(String, long, String)
	 */
	public void logError(String msg) {
		getLog().error(getFileName(), getSrcLn(), msg);
	}

	/**
	 * Logs the given message as well as the {@link Exception} as an exception. The file name is
	 * created by the analyzed {@link SootMethod}, which the environment stores, the source line
	 * number is the line number of current handled statement.
	 * 
	 * @param msg
	 *            Message that should be printed as an exception.
	 * @param e
	 *            The exception which is the reason for the given exception message.
	 * @see SecurityLogger#exception(String, long, String, Throwable)
	 */
	public void logException(String msg, Exception e) {
		getLog().exception(getFileName(), getSrcLn(), msg, e);
	}

	/**
	 * Logs the given message as a security violation. The file name is created by the analyzed
	 * {@link SootMethod}, which the environment stores, the source line number is the line number
	 * of current handled statement.
	 * 
	 * @param msg
	 *            Message that should be printed as a security violation.
	 * @see SecurityLogger#security(String, long, String)
	 */
	public void logSecurity(String msg) {
		getLog().security(getFileName(), getSrcLn(), msg);
	}

	/**
	 * Logs the given message as a warning. The file name is created by the analyzed
	 * {@link SootMethod}, which the environment stores, the source line number is the line number
	 * of current handled statement.
	 * 
	 * @param msg
	 *            Message that should be printed as a warning.
	 * @see SecurityLogger#warning(String, long, String)
	 */
	public void logWarning(String msg) {
		getLog().warning(getFileName(), getSrcLn(), msg);
	}

	/**
	 * Checks if the given <em>write effect</em> is allowed in the current implicit flow. Occurring
	 * exceptions are logged.
	 * 
	 * @param effect
	 *            <em>Write effect</em> for which should be checked whether it is allowed in the
	 *            current implicit flow.
	 */
	private void checkEffect(String effect) {
		if (out.hasProgramCounterLevel()) {
			String pcLevel = getWeakestSecurityLevel();
			try {
				pcLevel = out.getStrongestProgramCounterLevel();
			} catch (InvalidLevelException e) {
				logException(SecurityMessages.invalidLevelsComparisonInMap(getMethodSignature(),
						getSrcLn()), e);
			}
			if (isWeakerLevel(effect, pcLevel)) {
				analyzedMethodEnvironment.logEffect(getSrcLn(), SecurityMessages
						.effectWeakerThanPC(getMethodSignature(), getSrcLn(), effect, pcLevel));
			}
		}
	}

	/**
	 * Updates the <em>security level</em> of the given value to the level which the given
	 * {@link UpdateSwitch} stores. In order to identify possible errors, also the unit, which
	 * contains the value, has to be given as a String. Occurring exceptions are logged.
	 * 
	 * @param value
	 *            Value for which the <em>security level</em> should be updated to the given level.
	 * @param updateSwitch
	 *            {@link UpdateSwitch} which should be applied to the given value and which contains
	 *            the <em>security level</em> to which the level of the value should be updated.
	 * @param containing
	 *            Unit that contains the given value as String.
	 * @see UpdateSwitch
	 */
	private void executeUpdateLevel(Value value, UpdateSwitch updateSwitch, String containing) {
		try {
			value.apply(updateSwitch);
		} catch (SwitchException e) {
			logException(SecurityMessages.catchSwitchException(getMethodSignature(), getSrcLn(),
					containing), e);
		}
	}

	/**
	 * The method stores a new <em>write effect</em> in the calculated effect store. The effect is
	 * caused by an assignment to an array. Therefore, the method requires the
	 * <em>security level</em> that is effected by this <em>write effect</em> and also the
	 * {@link ArrayRef} of the array that is assigned. Also, it will be checked whether the effect
	 * may occur in the current implicit flow.
	 * 
	 * @param effect
	 *            Affected <em>security level</em> of the <em>write effect</em>.
	 * @param arrayRef
	 *            The reference of the array that is assigned.
	 * @see AnalyzedMethodEnvironment#addWriteEffectCausedByArrayAssign(String, long, ArrayRef)
	 */
	protected void addWriteEffectCausedByArrayAssign(String effect, ArrayRef arrayRef) {
		analyzedMethodEnvironment.addWriteEffectCausedByArrayAssign(effect, getSrcLn(), arrayRef);
		checkEffect(effect);
	}

	/**
	 * The method stores a new <em>write effect</em> in the calculated effect store. The effect is
	 * caused by an assignment to a field. Therefore, the method requires the
	 * <em>security level</em> that is effected by this <em>write effect</em> and also the
	 * {@link SootField} that is assigned. Also, it will be checked whether the effect may occur in
	 * the current implicit flow.
	 * 
	 * @param effect
	 *            Affected <em>security level</em> of the <em>write effect</em>.
	 * @param sootField
	 *            The field that is assigned.
	 * @see AnalyzedMethodEnvironment#addWriteEffectCausedByAssign(String, long, SootField)
	 */
	protected void addWriteEffectCausedByAssign(String effect, SootField sootField) {
		analyzedMethodEnvironment.addWriteEffectCausedByAssign(effect, getSrcLn(), sootField);
		checkEffect(effect);
	}

	/**
	 * The method stores a new <em>write effect</em> in the calculated effect store. The effect is
	 * caused by an inheritance of a class. Therefore, the method requires the
	 * <em>security level</em> that is effected by this <em>write effect</em> and also the
	 * {@link SootClass} from which the effect was inherited. Also, it will be checked whether the
	 * effect may occur in the current implicit flow.
	 * 
	 * @param effect
	 *            Affected <em>security level</em> of the <em>write effect</em>.
	 * @param sootClass
	 *            The class from which the effect was inherited.
	 * @see AnalyzedMethodEnvironment#addWriteEffectCausedByClass(String, long, SootClass)
	 */
	protected void addWriteEffectCausedByClass(String effect, SootClass sootClass) {
		analyzedMethodEnvironment.addWriteEffectCausedByClass(effect, getSrcLn(), sootClass);
		checkEffect(effect);
	}

	/**
	 * The method stores a new <em>write effect</em> in the calculated effect store. The effect is
	 * caused by an invocation of a method that provides this effect. Therefore, the method requires
	 * the <em>security level</em> that is effected by this <em>write effect</em> and also the
	 * {@link SootMethod} that is invoked and that provides this effect. Also, it will be checked
	 * whether the effect may occur in the current implicit flow.
	 * 
	 * @param effect
	 *            Affected <em>security level</em> of the <em>write effect</em>.
	 * @param sootMethod
	 *            Method that is invoked and that has this effect.
	 * @see AnalyzedMethodEnvironment#addWriteEffectCausedByMethodInvocation(String, long,
	 *      SootMethod)
	 */
	protected void addWriteEffectCausedByMethodInvocation(String effect, SootMethod sootMethod) {
		analyzedMethodEnvironment.addWriteEffectCausedByMethodInvocation(effect, getSrcLn(),
				sootMethod);
		checkEffect(effect);
	}

	/**
	 * Handles a given {@link InvokeExpr} and checks the <em>security levels</em> of the method
	 * parameters as well as if the given flag indicates that the invoke expression is assigned then
	 * the <em>security level</em> of the returned value is calculated. If the expression is
	 * assigned, then calculated level is returned by this method, otherwise the method returns the
	 * 'void' <em>security level</em>. If an error occurs during the check of the parameter the
	 * weakest available <em>security level</em> will be returned. If the invoke expression is a
	 * library method then the strongest level of the invoke expression arguments will be returned
	 * by the method. Additionally, also the <em>write effects</em> of the invoked method will be
	 * checked.
	 * 
	 * @param invokeExpr
	 *            Invoke expression which should be checked.
	 * @param assignment
	 *            Indicates whether it is an assignment.
	 * @return If the given flag indicates that the expression is assigned, then the method returns
	 *         the return <em>security level</em> of the invoked expression, if it is not assigned,
	 *         then the 'void' <em>security level</em>. If it is a library method then the method
	 *         returns the strongest argument level.
	 */
	protected String calculateInvokeExprLevel(InvokeExpr invokeExpr, boolean assignment) {
		MethodEnvironment invokedMethod = new MethodEnvironment(invokeExpr.getMethod(), getLog(),
				getSecurityAnnotation());
		String invokedMethodSignature = SootUtils.generateMethodSignature(
				invokedMethod.getSootMethod(), false, true, true);
		if (!invokedMethod.isLibraryMethod()) {
			List<MethodParameter> invokedMethodParameter = invokedMethod.getMethodParameters();
			String level = getWeakestSecurityLevel();
			if (invokeExpr.getArgCount() == invokedMethodParameter.size()) {
				List<String> parameterLevels = new ArrayList<String>();
				List<String> argumentLevels = new ArrayList<String>();
				for (int j = 0; j < invokeExpr.getArgCount(); j++) {
					Value value = invokeExpr.getArg(j);
					String parameterLevel = invokedMethodParameter.get(j).getLevel();
					String parameterName = invokedMethodParameter.get(j).getName();
					String argumentLevel = calculateLevel(value, invokeExpr.toString());
					if (!isWeakerOrEqualLevel(argumentLevel, parameterLevel)) {
						logSecurity(SecurityMessages.weakerArgumentExpected(getMethodSignature(),
								invokedMethodSignature, getSrcLn(), argumentLevel, parameterLevel,
								parameterName));
					}
					argumentLevels.add(argumentLevel);
					parameterLevels.add(parameterLevel);
				}
				if (assignment) {
					if (!invokedMethod.isReturnSecurityValid()) {
						logError(SecurityMessages.invalidReturnAnnotation(getMethodSignature(),
								invokedMethodSignature, getSrcLn()));
					}
					LevelEquation levelEquation = invokedMethod.getReturnLevelEquation();
					LevelEquationEvaluationVisitor levelEquationEvaluationVisitor = getSecurityAnnotation()
							.getLevelEquationEvaluationVisitor(argumentLevels, parameterLevels);
					levelEquation.accept(levelEquationEvaluationVisitor);
					level = levelEquationEvaluationVisitor.getResultLevel();
				} else {
					level = SecurityAnnotation.VOID_LEVEL;
				}
			} else {
				logError(SecurityMessages.wrongArgumentParameterAmount(getMethodSignature(),
						invokedMethodSignature, getSrcLn()));
			}
			checkSideEffectsOfInvokedMethod(invokedMethod);
			return level;
		} else {
			String level = getWeakestSecurityLevel();
			if (assignment) {
				List<String> argumentLevels = new ArrayList<String>();
				for (int j = 0; j < invokeExpr.getArgCount(); j++) {
					Value value = invokeExpr.getArg(j);
					argumentLevels.add(calculateLevel(value, invokeExpr.toString()));
				}
				level = getMaxLevel(argumentLevels);
				logWarning(SecurityMessages.invocationOfLibraryMethodMaxArgumentLevel(
						getMethodSignature(), invokedMethodSignature, getSrcLn(), level));
			} else {
				level = SecurityAnnotation.VOID_LEVEL;
				logWarning(SecurityMessages.invocationOfLibraryMethodNoSecurityLevel(
						getMethodSignature(), invokedMethodSignature, getSrcLn()));
			}
			logWarning(SecurityMessages.invocationOfLibraryMethodNoSideEffect(getMethodSignature(),
					invokedMethodSignature, getSrcLn()));
			return level;
		}
	}

	/**
	 * Looks up a <em>security level</em> for a given value. In order to identify possible errors,
	 * also the unit, which contains the value, has to be given as a String. If no
	 * <em>security level</em> can be determined for the given value, the weakest available level
	 * will be returned. Occurring exceptions are logged.
	 * 
	 * @param value
	 *            Value for which the <em>security level</em> should be determined.
	 * @param containing
	 *            Unit that contains the given value as String.
	 * @return Returns the <em>security level</em> of the given value, if it can be determined.
	 *         Otherwise it returns the weakest available <em>security level</em>.
	 * @see LookupSwitch
	 */
	protected String calculateLevel(Value value, String containing) {
		LookupSwitch lookupSwitch = new LookupSwitch(analyzedMethodEnvironment, in, out);
		String level = getWeakestSecurityLevel();
		try {
			value.apply(lookupSwitch);
		} catch (SwitchException e) {
			logException(SecurityMessages.catchSwitchException(getMethodSignature(), getSrcLn(),
					containing), e);
		}
		try {
			level = lookupSwitch.getLevel();
		} catch (NoSecurityLevelException e) {
			logException(
					SecurityMessages.noSecurityLevel(getSrcLn(), getMethodSignature(),
							value.toString()), e);
		}
		return level;
	}

	/**
	 * Handles a invoked method and adds for every provided <em>write effect</em> a new
	 * <em>write effect</em> to the calculated effect store. Therefore, the provided effects have to
	 * be valid. If the method is an initialization method or a static method also the
	 * <em>write effects</em> of the class that declares the method will be added to the calculated
	 * effect storage, if they are valid. In the case of a failure, this failure will be logged.
	 * 
	 * @param invokedMethod
	 *            Environment of an invoked method for which all <em>write effects</em> should be
	 *            stored in the calculated effect store.
	 */
	protected void checkSideEffectsOfInvokedMethod(MethodEnvironment invokedMethod) {
		String invokedMethodSignature = SootUtils.generateMethodSignature(
				invokedMethod.getSootMethod(), false, true, true);
		if (invokedMethod.areWriteEffectsValid()) {
			for (String effected : invokedMethod.getExpectedWriteEffects()) {
				addWriteEffectCausedByMethodInvocation(effected, invokedMethod.getSootMethod());
			}
			if (SootUtils.isInitMethod(invokedMethod.getSootMethod())
					|| invokedMethod.getSootMethod().isStatic()) {
				if (invokedMethod.areClassWriteEffectsValid()) {
					for (String effected : invokedMethod.getExpectedClassWriteEffects()) {
						addWriteEffectCausedByClass(effected, invokedMethod.getSootMethod()
								.getDeclaringClass());
					}
				} else {
					String invokedClassSignature = SootUtils.generateClassSignature(invokedMethod
							.getSootMethod().getDeclaringClass(), false);
					logError(SecurityMessages.invalidInvokedClassWriteEffects(getMethodSignature(),
							getSrcLn(), invokedMethodSignature, invokedClassSignature));
				}
			}
		} else {
			logError(SecurityMessages.invalidInvokedWriteEffects(getMethodSignature(), getSrcLn(),
					invokedMethodSignature));
		}
	}

	/**
	 * Returns the file name of the class which declares the analyzed method. The resulting file
	 * name can differ from the real file name, e.g. for nested classes (see
	 * {@link SootUtils#generateFileName(SootClass)}) and the file name do not contain the file
	 * suffix.
	 * 
	 * @return The file name of the class which declares the analyzed method.
	 */
	protected String getFileName() {
		return SootUtils.generateFileName(analyzedMethodEnvironment.getSootMethod());
	}

	/**
	 * Returns the {@link SecurityLogger} of the
	 * {@link AnalyzedMethodEnvironment#securityAnnotation} object.
	 * 
	 * @return The logger.
	 */
	protected SecurityLogger getLog() {
		return analyzedMethodEnvironment.getLog();
	}

	/**
	 * Returns the strongest <em>security level</em> of the given level list. If one of the given
	 * levels is invalid or 'void' ({@link SecurityAnnotation#VOID_LEVEL}), the method will log this
	 * exception and will return the weakest available <em>security level</em> (see
	 * {@link TaintTrackingSwitch#getWeakestSecurityLevel()}).
	 * 
	 * @param levels
	 *            List of <em>security levels</em> for which the strongest contained level should be
	 *            returned.
	 * @return If all given levels of the list are valid, the strongest <em>security level</em>
	 *         contained by the given list will be returned. Otherwise the the weakest available
	 *         <em>security level</em> will be returned.
	 */
	protected String getMaxLevel(List<String> levels) {
		String level = getWeakestSecurityLevel();
		try {
			level = getSecurityAnnotation().getMaxLevel(levels);
		} catch (InvalidLevelException e) {
			String[] levelArray = new String[levels.size()];
			logException(SecurityMessages.invalidLevelsComparison(getMethodSignature(), getSrcLn(),
					levels.toArray(levelArray)), e);
		}
		return level;
	}

	/**
	 * Returns the strongest <em>security level</em> of the given levels. If one of the two given
	 * levels is 'void' ({@link SecurityAnnotation#VOID_LEVEL}), the resulting level is also 'void'.
	 * If one of the both given levels is an invalid <em>security level</em> the method will log
	 * this exception and will return the weakest available <em>security level</em> (see
	 * {@link TaintTrackingSwitch#getWeakestSecurityLevel()}).
	 * 
	 * @param level1
	 *            First level which should be compared.
	 * @param level2
	 *            Second level which should be compared.
	 * @return If none of the given level is 'void' and invalid, the strongest level of the both
	 *         given levels will be returned. 'void' will be returned, if one of the given levels is
	 *         'void' ({@link SecurityAnnotation#VOID_LEVEL}).
	 */
	protected String getMaxLevel(String level1, String level2) {
		String level = getWeakestSecurityLevel();
		try {
			level = getSecurityAnnotation().getMaxLevel(level1, level2);
		} catch (InvalidLevelException e) {
			logException(SecurityMessages.invalidLevelsComparison(getMethodSignature(), getSrcLn(),
					level1, level2), e);
		}
		return level;
	}

	/**
	 * Returns the readable method signature of the analyzed method. The returned signature contains
	 * the visibility of the method, the package name and also the types of the parameters and the
	 * return type.
	 * 
	 * @return The readable method signature of the analyzed method.
	 */
	protected String getMethodSignature() {
		return SootUtils.generateMethodSignature(analyzedMethodEnvironment.getSootMethod(), false,
				true, true);
	}

	/**
	 * Returns the weakest <em>security level</em> of the given levels. If one of the two given
	 * levels is 'void' ({@link SecurityAnnotation#VOID_LEVEL}), the resulting level is also 'void'.
	 * If one of the both given levels is an invalid <em>security level</em> the method will log
	 * this exception and will return the weakest available <em>security level</em> (see
	 * {@link TaintTrackingSwitch#getWeakestSecurityLevel()}).
	 * 
	 * @param level1
	 *            First level which should be compared.
	 * @param level2
	 *            Second level which should be compared.
	 * @return If none of the given level is 'void' and invalid, the weakest level of the both given
	 *         levels will be returned. 'void' will be returned, if one of the given levels is
	 *         'void' ({@link SecurityAnnotation#VOID_LEVEL}).
	 */
	protected String getMinLevel(String level1, String level2) {
		String level = getWeakestSecurityLevel();
		try {
			level = getSecurityAnnotation().getMinLevel(level1, level2);
		} catch (InvalidLevelException e) {
			logException(SecurityMessages.invalidLevelsComparison(getMethodSignature(), getSrcLn(),
					level1, level2), e);
		}
		return level;
	}

	/**
	 * Returns the {@link SecurityAnnotation} that allows the handling of <em>security level</em>.
	 * 
	 * @return The security annotation object.
	 */
	protected SecurityAnnotation getSecurityAnnotation() {
		return analyzedMethodEnvironment.getSecurityAnnotation();
	}

	/**
	 * The method returns the current analyzed {@link SootMethod}.
	 * 
	 * @return The analyzed method.
	 */
	protected SootMethod getSootMethod() {
		return analyzedMethodEnvironment.getSootMethod();
	}

	/**
	 * Method that returns the source line number of the currently handled statement of the analyzed
	 * method. Note, that immediately after the initialization of the environment and before the
	 * start of flow analysis, the method will return {@code 0}.
	 * 
	 * @return The source line number of the current handled statement of the analyzed method or if
	 *         this wasn't set {@code 0}.
	 */
	protected long getSrcLn() {
		return analyzedMethodEnvironment.getSrcLn();
	}

	/**
	 * Method returns the weakest available <em>security level</em> (see
	 * {@link Environment#securityAnnotation}).
	 * 
	 * @return The weakest available <em>security level</em>.
	 */
	protected String getWeakestSecurityLevel() {
		return analyzedMethodEnvironment.getWeakestSecurityLevel();
	}

	/**
	 * Checks whether the first given level is equals to the second given level. If one of the both
	 * given levels is an invalid <em>security level</em> the method will log this exception and
	 * will return {@code false}.
	 * 
	 * @param level1
	 *            Level for which should be checked whether it is equals to the second given level.
	 * @param level2
	 *            Level for which should be checked whether it is equals to the first given level.
	 * @return {@code true} if the first given level is equals to the second <em>security level</em>
	 *         , {@code false} otherwise.
	 */
	protected boolean isEqualLevel(String level1, String level2) {
		return level1.equals(level2);
	}

	/**
	 * Checks whether the first given level is weaker than the second given level. If one of the
	 * both given levels is an invalid <em>security level</em> the method will log this exception
	 * and will return {@code false}.
	 * 
	 * @param level1
	 *            Level for which should be checked whether it is weaker than the second given
	 *            level.
	 * @param level2
	 *            Level for which should be checked whether it is equals or stronger than the first
	 *            given level.
	 * @return {@code true} if the first given level is weaker than the second
	 *         <em>security level</em>, {@code false} otherwise.
	 */
	protected boolean isWeakerLevel(String level1, String level2) {
		return isWeakerOrEqualLevel(level1, level2) && !isEqualLevel(level1, level2);
	}

	/**
	 * Checks whether the first given level is weaker or equals than the second given level. If one
	 * of the both given levels is an invalid <em>security level</em> the method will log this
	 * exception and will return {@code false}.
	 * 
	 * @param level1
	 *            Level for which should be checked whether it is weaker or equals than the second
	 *            given level.
	 * @param level2
	 *            Level for which should be checked whether it is stronger than the first given
	 *            level.
	 * @return {@code true} if the first given level is weaker or equals than the second
	 *         <em>security level</em>, {@code false} otherwise.
	 */
	protected boolean isWeakerOrEqualLevel(String level1, String level2) {
		boolean is1WeakerEquals2 = false;
		try {
			is1WeakerEquals2 = getSecurityAnnotation().isWeakerOrEqualsThan(level1, level2);
		} catch (InvalidLevelException e) {
			logException(SecurityMessages.invalidLevelsComparison(getMethodSignature(), getSrcLn(),
					level1, level2), e);
		}
		return is1WeakerEquals2;
	}

	/**
	 * The method checks whether the given <em>security level</em> is weaker than the level of the
	 * program counter. If this is the case, then the method will return the level of the program
	 * counter, if not, then the given <em>security level</em> is returned.
	 * 
	 * @param level
	 *            <em>Security level</em>, for which the program counter level should be considered.
	 * @return The strongest program counter level if it is stronger than the given level, otherwise
	 *         the given <em>security level</em>.
	 */
	protected String takePCintoAccount(String level) {
		if (out.hasProgramCounterLevel()) {
			String pcLevel = getWeakestSecurityLevel();
			try {
				pcLevel = out.getStrongestProgramCounterLevel();
			} catch (InvalidLevelException e) {
				logException(SecurityMessages.invalidLevelsComparisonInMap(getMethodSignature(),
						getSrcLn()), e);
			}
			if (isWeakerOrEqualLevel(level, pcLevel)) {
				level = pcLevel;
			}
		}
		return level;
	}

	/**
	 * Updates the <em>security level</em> of the given right and left side value of an identity
	 * statement. In order to identify possible errors, also the unit, which contains the value, has
	 * to be given as a String. Occurring exceptions are logged.
	 * 
	 * @param rightSide
	 *            The right hand side of an identity statement.
	 * @param leftSide
	 *            The left hand side of an identity statement.
	 * @param containing
	 *            Unit that contains the given value as String.
	 */
	protected void updateIdentityLevel(Value rightSide, Value leftSide, String containing) {
		UpdateSwitch updateSwitch = new UpdateSwitch(analyzedMethodEnvironment, in, out, null);
		updateSwitch.setIdentityInformation(leftSide);
		executeUpdateLevel(rightSide, updateSwitch, containing);
	}

	/**
	 * Updates the <em>security level</em> of the given normal value to the given level. Such a
	 * normal value are {@link Local} variables or {@link SootField} fields. In order to identify
	 * possible errors, also the unit, which contains the value, has to be given as a String.
	 * Occurring exceptions are logged.
	 * 
	 * @param value
	 *            Value for which the <em>security level</em> should be updated to the given level.
	 * @param level
	 *            The <em>security level</em> to which the level of the value should be updated.
	 * @param containing
	 *            Unit that contains the given value as String.
	 * @see TaintTrackingSwitch#executeUpdateLevel(Value, UpdateSwitch, String)
	 */
	protected void updateLevel(Value value, String level, String containing) {
		UpdateSwitch updateSwitch = new UpdateSwitch(analyzedMethodEnvironment, in, out, level);
		executeUpdateLevel(value, updateSwitch, containing);
	}

	/**
	 * Updates the <em>security level</em> of the given value to the level which is given by the
	 * {@link TaintTrackingSwitch#analyzedMethodEnvironment} and the {@link ParameterRef}. Occurring
	 * exceptions are logged.
	 * 
	 * @param value
	 *            Value for which the <em>security level</em> should be updated to the level of the
	 *            given parameter reference.
	 * @param parameterRef
	 *            The parameter reference for which the <em>security level</em> can be looked up in
	 *            the {@link TaintTrackingSwitch#analyzedMethodEnvironment}.
	 */
	protected void updateParameterLevel(Value value, ParameterRef parameterRef) {
		MethodParameter methodParameter = analyzedMethodEnvironment
				.getMethodParameterAt(parameterRef.getIndex());
		UpdateSwitch updateSwitch = new UpdateSwitch(analyzedMethodEnvironment, in, out,
				methodParameter.getLevel());
		updateSwitch.setParameterInformation(parameterRef, methodParameter);
		executeUpdateLevel(value, updateSwitch, parameterRef.toString());
	}

}