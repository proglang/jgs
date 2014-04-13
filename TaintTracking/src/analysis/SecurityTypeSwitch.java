package analysis;

import static resource.Messages.getMsg;

import java.util.ArrayList;
import java.util.List;

import analysis.LocalsMap;

import logging.AnalysisLog;
import model.AnalyzedMethodEnvironment;
import model.MethodEnvironment;
import model.MethodEnvironment.MethodParameter;
import static resource.Configuration.*;
import security.ILevel;
import security.ILevelMediator;
import soot.Local;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.InvokeExpr;
import soot.jimple.ParameterRef;
import static utils.AnalysisUtils.*;
import exception.OperationInvalidException;
import exception.ProgramCounterException;
import extractor.UsedObjectStore;

/**
 * <h1>Base switch for the {@link SecurityTypeAnalysis} analysis</h1>
 * 
 * The {@link SecurityTypeSwitch} is the base class for switches which are used for the {@link SecurityTypeAnalysis} analysis. The base
 * switch contains the incoming and outgoing locals map of a specific
 * {@link SecurityTypeAnalysis#flowThrough(LocalsMap, soot.Unit, LocalsMap)} state as well as the environment of the current analyzed
 * method. The class provides multiple methods which allows to log, to compare <em>security level</em>, to check <em>write effect</em> and
 * also to initialize the lookup and the update of <em>security level</em>. <br />
 * The class {@link SecurityTypeSwitch} is the parent class of the following:
 * <ul>
 * <li>{@link SecurityTypeStmtSwitch}: switch that processes statements - depending on the statement the <em>security levels</em> will be
 * calculated or updated (with the help of other switches) for components of the statements and also the statements will be checked for
 * security violations.</li>
 * <li>{@link SecurityTypeReadValueSwitch}: switch that processes values - depending on the value, the <em>security level</em> will be
 * looked up for the value and also stored in the switch. If necessary the switch checks for security violations, too (e.g. for method
 * invocations the parameter <em>security level</em>).</li>
 * <li>{@link SecurityTypeWriteValueSwitch}: switch that processes values - depending on the value, the <em>security level</em> of this
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
 * @see SecurityTypeAnalysis
 * @see AnalyzedMethodEnvironment
 */
abstract public class SecurityTypeSwitch {

	/** Current analyzed method environment. */
	protected final AnalyzedMethodEnvironment analyzedMethodEnvironment;
	/** Current incoming map of the local variables. */
	protected final LocalsMap in;
	/** Current outgoing map of the local variables. */
	protected final LocalsMap out;
	/**
	 * DOC
	 */
	protected final UsedObjectStore store;

	/**
	 * Constructor of a {@link SecurityTypeSwitch} that requires the current incoming and outgoing map of local variables as well as the
	 * environment of the current analyzed method.
	 * 
	 * @param analysisEnvironment
	 *          The environment of the method that is currently analyzed.
	 * @param in
	 *          Current incoming map of the local variables.
	 * @param out
	 *          Current outgoing map of the local variables.
	 */
	protected SecurityTypeSwitch(AnalyzedMethodEnvironment methodEnvironment, UsedObjectStore store, LocalsMap in, LocalsMap out) {
		this.analyzedMethodEnvironment = methodEnvironment;
		this.store = store;
		this.out = out;
		this.in = in;
	}

	/**
	 * Checks if the given <em>write effect</em> is allowed in the current implicit flow. Occurring exceptions are logged.
	 * 
	 * @param effect
	 *          <em>Write effect</em> for which should be checked whether it is allowed in the current implicit flow.
	 */
	private void checkEffect(ILevel effect) {
		if (out.hasProgramCounterLevel()) {
			ILevel pcLevel = getWeakestSecurityLevel();
			try {
				pcLevel = out.getStrongestProgramCounterLevel();
			} catch (OperationInvalidException e) {
				throw new ProgramCounterException(getMsg("exception.program_counter.error", getMethodSignature()), e);
			}
			if (isWeakerLevel(effect, pcLevel)) {
				analyzedMethodEnvironment.logEffect(getSrcLn(),
						getMsg("effects.program_counter.stronger", getMethodSignature(), getSrcLn(), effect.getName(), pcLevel.getName()));
			}
		}
	}

	/**
	 * Updates the <em>security level</em> of the given value to the level which the given {@link SecurityTypeWriteValueSwitch} stores. In
	 * order to identify possible errors, also the unit, which contains the value, has to be given as a String. Occurring exceptions are
	 * logged.
	 * 
	 * @param value
	 *          Value for which the <em>security level</em> should be updated to the given level.
	 * @param updateSwitch
	 *          {@link SecurityTypeWriteValueSwitch} which should be applied to the given value and which contains the <em>security level</em>
	 *          to which the level of the value should be updated.
	 * @param containing
	 *          Unit that contains the given value as String.
	 * @see SecurityTypeWriteValueSwitch
	 */
	private void executeUpdateLevel(Value value, SecurityTypeWriteValueSwitch updateSwitch, String containing) {
		value.apply(updateSwitch);
	}

	/**
	 * The method stores a new <em>write effect</em> in the calculated effect store. The effect is caused by an assignment to an array.
	 * Therefore, the method requires the <em>security level</em> that is effected by this <em>write effect</em> and also the {@link ArrayRef}
	 * of the array that is assigned. Also, it will be checked whether the effect may occur in the current implicit flow.
	 * 
	 * @param effect
	 *          Affected <em>security level</em> of the <em>write effect</em>.
	 * @param arrayRef
	 *          The reference of the array that is assigned.
	 * @see AnalyzedMethodEnvironment#addWriteEffectCausedByArrayAssign(String, long, ArrayRef)
	 */
	protected void addWriteEffectCausedByArrayAssign(ILevel effect, ArrayRef arrayRef) {
		analyzedMethodEnvironment.addWriteEffectCausedByArrayAssign(effect, getSrcLn(), arrayRef);
		checkEffect(effect);
	}

	/**
	 * The method stores a new <em>write effect</em> in the calculated effect store. The effect is caused by an assignment to a field.
	 * Therefore, the method requires the <em>security level</em> that is effected by this <em>write effect</em> and also the
	 * {@link SootField} that is assigned. Also, it will be checked whether the effect may occur in the current implicit flow.
	 * 
	 * @param effect
	 *          Affected <em>security level</em> of the <em>write effect</em>.
	 * @param sootField
	 *          The field that is assigned.
	 * @see AnalyzedMethodEnvironment#addWriteEffectCausedByAssign(String, long, SootField)
	 */
	protected void addWriteEffectCausedByAssign(ILevel effect, SootField sootField) {
		analyzedMethodEnvironment.addWriteEffectCausedByAssign(effect, getSrcLn(), sootField);
		checkEffect(effect);
	}

	/**
	 * The method stores a new <em>write effect</em> in the calculated effect store. The effect is caused by an inheritance of a class.
	 * Therefore, the method requires the <em>security level</em> that is effected by this <em>write effect</em> and also the
	 * {@link SootClass} from which the effect was inherited. Also, it will be checked whether the effect may occur in the current implicit
	 * flow.
	 * 
	 * @param effect
	 *          Affected <em>security level</em> of the <em>write effect</em>.
	 * @param sootClass
	 *          The class from which the effect was inherited.
	 * @see AnalyzedMethodEnvironment#addWriteEffectCausedByClass(String, long, SootClass)
	 */
	protected void addWriteEffectCausedByClass(ILevel effect, SootClass sootClass) {
		analyzedMethodEnvironment.addWriteEffectCausedByClass(effect, getSrcLn(), sootClass);
		checkEffect(effect);
	}

	/**
	 * The method stores a new <em>write effect</em> in the calculated effect store. The effect is caused by an invocation of a method that
	 * provides this effect. Therefore, the method requires the <em>security level</em> that is effected by this <em>write effect</em> and
	 * also the {@link SootMethod} that is invoked and that provides this effect. Also, it will be checked whether the effect may occur in the
	 * current implicit flow.
	 * 
	 * @param effect
	 *          Affected <em>security level</em> of the <em>write effect</em>.
	 * @param sootMethod
	 *          Method that is invoked and that has this effect.
	 * @see AnalyzedMethodEnvironment#addWriteEffectCausedByMethodInvocation(String, long, SootMethod)
	 */
	protected void addWriteEffectCausedByMethodInvocation(ILevel effect, SootMethod sootMethod) {
		analyzedMethodEnvironment.addWriteEffectCausedByMethodInvocation(effect, getSrcLn(), sootMethod);
		checkEffect(effect);
	}

	/**
	 * Handles a given {@link InvokeExpr} and checks the <em>security levels</em> of the method parameters as well as if the given flag
	 * indicates that the invoke expression is assigned then the <em>security level</em> of the returned value is calculated. If the
	 * expression is assigned, then calculated level is returned by this method, otherwise the method returns the 'void'
	 * <em>security level</em>. If an error occurs during the check of the parameter the weakest available <em>security level</em> will be
	 * returned. If the invoke expression is a library method then the strongest level of the invoke expression arguments will be returned by
	 * the method. Additionally, also the <em>write effects</em> of the invoked method will be checked.
	 * 
	 * @param invokeExpr
	 *          Invoke expression which should be checked.
	 * @param assignment
	 *          Indicates whether it is an assignment.
	 * @return If the given flag indicates that the expression is assigned, then the method returns the return <em>security level</em> of the
	 *         invoked expression, if it is not assigned, then the 'void' <em>security level</em>. If it is a library method then the method
	 *         returns the strongest argument level.
	 */
	protected ILevel calculateInvokeExprLevel(InvokeExpr invokeExpr, boolean assignment) {
		MethodEnvironment invokedMethod = store.getMethodEnvironment(invokeExpr.getMethod());
		List<MethodParameter> invokedMethodParameter = invokedMethod.getMethodParameters();
		ILevel level = getWeakestSecurityLevel();
		List<ILevel> parameterLevels = new ArrayList<ILevel>();
		List<ILevel> argumentLevels = new ArrayList<ILevel>();
		for (int j = 0; j < invokeExpr.getArgCount(); j++) {
			Value value = invokeExpr.getArg(j);
			ILevel parameterLevel = invokedMethodParameter.get(j).getLevel();
			String parameterName = invokedMethodParameter.get(j).getName();
			ILevel argumentLevel = calculateLevel(value, invokeExpr.toString());
			if (!isWeakerOrEqualLevel(argumentLevel, parameterLevel)) {
				logSecurity(getMsg(
						"security.param.stronger",
						getMethodSignature(),
						getSrcLn(),
						generateMethodSignature(invokeExpr.getMethod(), METHOD_SIGNATURE_PRINT_PACKAGE, METHOD_SIGNATURE_PRINT_TYPE,
								METHOD_SIGNATURE_PRINT_VISIBILITY), parameterName, parameterLevel.getName(), argumentLevel.getName()));
			}
			argumentLevels.add(argumentLevel);
			parameterLevels.add(parameterLevel);
		}
		if (assignment) {
			if (invokedMethod.isLibraryMethod()) {
				if (argumentLevels.size() > 0) {
					level = getMaxLevel(argumentLevels);
				}
			} else {
				level = invokedMethod.getReturnLevel();
			}
		} else {
			level = null;
		}
		checkSideEffectsOfInvokedMethod(invokedMethod);
		return level;
	}

	/**
	 * Looks up a <em>security level</em> for a given value. In order to identify possible errors, also the unit, which contains the value,
	 * has to be given as a String. If no <em>security level</em> can be determined for the given value, the weakest available level will be
	 * returned. Occurring exceptions are logged.
	 * 
	 * @param value
	 *          Value for which the <em>security level</em> should be determined.
	 * @param containing
	 *          Unit that contains the given value as String.
	 * @return Returns the <em>security level</em> of the given value, if it can be determined. Otherwise it returns the weakest available
	 *         <em>security level</em>.
	 * @see SecurityTypeReadValueSwitch
	 */
	protected ILevel calculateLevel(Value value, String containing) {
		SecurityTypeReadValueSwitch lookupSwitch = new SecurityTypeReadValueSwitch(analyzedMethodEnvironment, store, in, out);
		value.apply(lookupSwitch);
		return lookupSwitch.getLevel();
	}

	/**
	 * Handles a invoked method and adds for every provided <em>write effect</em> a new <em>write effect</em> to the calculated effect store.
	 * Therefore, the provided effects have to be valid. If the method is an initialization method or a static method also the
	 * <em>write effects</em> of the class that declares the method will be added to the calculated effect storage, if they are valid. In the
	 * case of a failure, this failure will be logged.
	 * 
	 * @param invokedMethod
	 *          Environment of an invoked method for which all <em>write effects</em> should be stored in the calculated effect store.
	 */
	protected void checkSideEffectsOfInvokedMethod(MethodEnvironment invokedMethod) {
		for (ILevel effected : invokedMethod.getWriteEffects()) {
			addWriteEffectCausedByMethodInvocation(effected, invokedMethod.getSootMethod());
		}
		if (isInitMethod(invokedMethod.getSootMethod()) || invokedMethod.getSootMethod().isStatic()) {
			for (ILevel effected : invokedMethod.getClassWriteEffects()) {
				addWriteEffectCausedByClass(effected, invokedMethod.getSootMethod().getDeclaringClass());
			}
		}

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
	protected ILevelMediator getLevelMediator() {
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

	/**
	 * Returns the strongest <em>security level</em> of the given levels. If one of the two given levels is 'void' (
	 * {@link LevelMediator#VOID_LEVEL} ), the resulting level is also 'void'. If one of the both given levels is an invalid
	 * <em>security level</em> the method will log this exception and will return the weakest available <em>security level</em> (see
	 * {@link SecurityTypeSwitch#getWeakestSecurityLevel()}).
	 * 
	 * @param level1
	 *          First level which should be compared.
	 * @param level2
	 *          Second level which should be compared.
	 * @return If none of the given level is 'void' and invalid, the strongest level of the both given levels will be returned. 'void' will be
	 *         returned, if one of the given levels is 'void' ( {@link LevelMediator#VOID_LEVEL}).
	 */
	protected ILevel getMaxLevel(ILevel level1, ILevel level2) {
		return getLevelMediator().getLeastUpperBoundLevelOf(level1, level2);
	}

	/**
	 * Returns the strongest <em>security level</em> of the given level list. If one of the given levels is invalid or 'void' (
	 * {@link LevelMediator#VOID_LEVEL}), the method will log this exception and will return the weakest available <em>security level</em>
	 * (see {@link SecurityTypeSwitch#getWeakestSecurityLevel()}).
	 * 
	 * @param levels
	 *          List of <em>security levels</em> for which the strongest contained level should be returned.
	 * @return If all given levels of the list are valid, the strongest <em>security level</em> contained by the given list will be returned.
	 *         Otherwise the the weakest available <em>security level</em> will be returned.
	 */
	protected ILevel getMaxLevel(List<ILevel> levels) {
		return getLevelMediator().getLeastUpperBoundLevelOf(levels);
	}

	/**
	 * Returns the readable method signature of the analyzed method. The returned signature contains the visibility of the method, the package
	 * name and also the types of the parameters and the return type.
	 * 
	 * @return The readable method signature of the analyzed method.
	 */
	protected String getMethodSignature() {
		return generateMethodSignature(getSootMethod(), METHOD_SIGNATURE_PRINT_PACKAGE, METHOD_SIGNATURE_PRINT_TYPE,
				METHOD_SIGNATURE_PRINT_VISIBILITY);
	}

	/**
	 * Returns the weakest <em>security level</em> of the given levels. If one of the two given levels is 'void' (
	 * {@link LevelMediator#VOID_LEVEL} ), the resulting level is also 'void'. If one of the both given levels is an invalid
	 * <em>security level</em> the method will log this exception and will return the weakest available <em>security level</em> (see
	 * {@link SecurityTypeSwitch#getWeakestSecurityLevel()}).
	 * 
	 * @param level1
	 *          First level which should be compared.
	 * @param level2
	 *          Second level which should be compared.
	 * @return If none of the given level is 'void' and invalid, the weakest level of the both given levels will be returned. 'void' will be
	 *         returned, if one of the given levels is 'void' ( {@link LevelMediator#VOID_LEVEL}).
	 */
	protected ILevel getMinLevel(ILevel level1, ILevel level2) {
		return getLevelMediator().getGreatestLowerBoundLevelOf(level1, level2);
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
	 * Method that returns the source line number of the currently handled statement of the analyzed method. Note, that immediately after the
	 * initialization of the environment and before the start of flow analysis, the method will return {@code 0}.
	 * 
	 * @return The source line number of the current handled statement of the analyzed method or if this wasn't set {@code 0}.
	 */
	protected long getSrcLn() {
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
		return getLevelMediator().isWeakerOrEquals(level1, level2);
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
		getLog().security(getFileName(), getSrcLn(), msg);
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
		getLog().warning(getFileName(), getSrcLn(), msg);
	}

	/**
	 * The method checks whether the given <em>security level</em> is weaker than the level of the program counter. If this is the case, then
	 * the method will return the level of the program counter, if not, then the given <em>security level</em> is returned.
	 * 
	 * @param level
	 *          <em>Security level</em>, for which the program counter level should be considered.
	 * @return The strongest program counter level if it is stronger than the given level, otherwise the given <em>security level</em>.
	 */
	protected ILevel takePCintoAccount(ILevel level) {
		if (out.hasProgramCounterLevel()) {
			ILevel pcLevel = getWeakestSecurityLevel();
			try {
				pcLevel = out.getStrongestProgramCounterLevel();
			} catch (OperationInvalidException e) {
				throw new ProgramCounterException(getMsg("exception.program_counter.error", getMethodSignature()), e);
			}
			if (isWeakerOrEqualLevel(level, pcLevel)) {
				level = pcLevel;
			}
		}
		return level;
	}

	/**
	 * Updates the <em>security level</em> of the given right and left side value of an identity statement. In order to identify possible
	 * errors, also the unit, which contains the value, has to be given as a String. Occurring exceptions are logged.
	 * 
	 * @param rightSide
	 *          The right hand side of an identity statement.
	 * @param leftSide
	 *          The left hand side of an identity statement.
	 * @param containing
	 *          Unit that contains the given value as String.
	 */
	protected void updateIdentityLevel(Value rightSide, Value leftSide, String containing) {
		SecurityTypeWriteValueSwitch updateSwitch = new SecurityTypeWriteValueSwitch(analyzedMethodEnvironment, store, in, out, null);
		updateSwitch.setIdentityInformation(leftSide);
		executeUpdateLevel(rightSide, updateSwitch, containing);
	}

	/**
	 * Updates the <em>security level</em> of the given normal value to the given level. Such a normal value are {@link Local} variables or
	 * {@link SootField} fields. In order to identify possible errors, also the unit, which contains the value, has to be given as a String.
	 * Occurring exceptions are logged.
	 * 
	 * @param value
	 *          Value for which the <em>security level</em> should be updated to the given level.
	 * @param level
	 *          The <em>security level</em> to which the level of the value should be updated.
	 * @param containing
	 *          Unit that contains the given value as String.
	 * @see SecurityTypeSwitch#executeUpdateLevel(Value, SecurityTypeWriteValueSwitch, String)
	 */
	protected void updateLevel(Value value, ILevel level, String containing) {
		SecurityTypeWriteValueSwitch updateSwitch = new SecurityTypeWriteValueSwitch(analyzedMethodEnvironment, store, in, out, level);
		executeUpdateLevel(value, updateSwitch, containing);
	}

	/**
	 * Updates the <em>security level</em> of the given value to the level which is given by the
	 * {@link SecurityTypeSwitch#analyzedMethodEnvironment} and the {@link ParameterRef}. Occurring exceptions are logged.
	 * 
	 * @param value
	 *          Value for which the <em>security level</em> should be updated to the level of the given parameter reference.
	 * @param parameterRef
	 *          The parameter reference for which the <em>security level</em> can be looked up in the
	 *          {@link SecurityTypeSwitch#analyzedMethodEnvironment}.
	 */
	protected void updateParameterLevel(Value value, ParameterRef parameterRef) {
		MethodParameter methodParameter = analyzedMethodEnvironment.getMethodParameterAt(parameterRef.getIndex());
		SecurityTypeWriteValueSwitch updateSwitch = new SecurityTypeWriteValueSwitch(analyzedMethodEnvironment, store, in, out,
				methodParameter.getLevel());
		executeUpdateLevel(value, updateSwitch, parameterRef.toString());
	}

}