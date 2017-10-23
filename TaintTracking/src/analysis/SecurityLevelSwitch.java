package analysis;

import static resource.Messages.getMsg;
import static util.AnalysisUtils.getSignatureOfMethod;
import static util.AnalysisUtils.isInitMethod;

import java.util.ArrayList;
import java.util.List;

import model.AnalyzedMethodEnvironment;
import model.MethodEnvironment;
import model.MethodEnvironment.MethodParameter;
import security.ILevel;
import soot.Local;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.InvokeExpr;
import soot.jimple.ParameterRef;
import exception.OperationInvalidException;
import exception.ProgramCounterException;
import extractor.UsedObjectStore;

/**
 * <h1>Base switch for the {@link SecurityLevelAnalysis} analysis</h1>
 * 
 * The {@link SecurityLevelSwitch} is the base class for switches which are used
 * for the {@link SecurityLevelAnalysis} analysis. The base switch contains the
 * incoming and outgoing locals map of a specific
 * {@link SecurityLevelAnalysis#flowThrough(LocalsMap, soot.Unit, LocalsMap)}
 * state as well as the environment of the current analyzed method. The class
 * provides multiple methods which allows to log, to compare
 * <em>security level</em>, to check <em>write effect</em> and also to
 * initialize the lookup and the update of <em>security level</em>. <br />
 * The class {@link SecurityLevelSwitch} is the parent class of the following:
 * <ul>
 * <li>{@link SecurityLevelStmtSwitch}: switch that processes statements -
 * depending on the statement the <em>security levels</em> will be calculated or
 * updated (with the help of other switches) for components of the statements
 * and also the statements will be checked for security violations.</li>
 * <li>{@link SecurityLevelValueReadSwitch}: switch that processes values -
 * depending on the value, the <em>security level</em> will be looked up for the
 * value and also stored in the switch. If necessary the switch checks for
 * security violations, too (e.g. for method invocations the parameter
 * <em>security level</em>).</li>
 * <li>{@link SecurityLevelValueWriteSwitch}: switch that processes values -
 * depending on the value, the <em>security level</em> of this value will be
 * updated by the given level. The switch checks for security violations, too
 * (e.g. occurring <em>write effects</em> or invalid assignments).</li>
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
abstract public class SecurityLevelSwitch extends SecuritySwitch<LocalsMap> {

    /**
     * Constructor of a {@link SecurityLevelSwitch} that requires the current
     * incoming and outgoing map of local variables as well as the environment
     * of the current analyzed method.
     * 
     * @param analysisEnvironment
     *            The environment of the method that is currently analyzed.
     * @param in
     *            Current incoming map of the local variables.
     * @param out
     *            Current outgoing map of the local variables.
     */
    protected SecurityLevelSwitch(AnalyzedMethodEnvironment methodEnvironment,
            UsedObjectStore store, LocalsMap in, LocalsMap out) {
        super(methodEnvironment, store, in, out);
    }

    /**
     * Checks if the given <em>write effect</em> is allowed in the current
     * implicit flow. Occurring exceptions are logged.
     * 
     * @param effect
     *            <em>Write effect</em> for which should be checked whether it
     *            is allowed in the current implicit flow.
     */
    private void checkEffect(ILevel effect) {
        if (getOut().hasProgramCounterLevel()) {
            ILevel pcLevel = getWeakestSecurityLevel();
            try {
                pcLevel = getOut().getStrongestProgramCounterLevel();
            } catch (OperationInvalidException e) {
                throw new ProgramCounterException(getMsg("exception.program_counter.error",
                                                         getSignatureOfAnalyzedMethod()),
                                                  e);
            }
            if (isWeakerLevel(effect, pcLevel)) {
                getAnalyzedEnvironment().logEffect(getSourceLine(),
                                                   getMsg("effects.program_counter.stronger",
                                                          getSignatureOfAnalyzedMethod(),
                                                          getSourceLine(),
                                                          effect.getName(),
                                                          pcLevel.getName()));
            }
        }
    }

    /**
     * Updates the <em>security level</em> of the given value to the level which
     * the given {@link SecurityLevelValueWriteSwitch} stores. In order to
     * identify possible errors, also the unit, which contains the value, has to
     * be given as a String. Occurring exceptions are logged.
     * 
     * @param value
     *            Value for which the <em>security level</em> should be updated
     *            to the given level.
     * @param updateSwitch
     *            {@link SecurityLevelValueWriteSwitch} which should be applied
     *            to the given value and which contains the
     *            <em>security level</em> to which the level of the value should
     *            be updated.
     * @param containing
     *            Unit that contains the given value as String.
     * @see SecurityLevelValueWriteSwitch
     */
    private void executeUpdateLevel(Value value,
                                    SecurityLevelValueWriteSwitch updateSwitch,
                                    String containing) {
        value.apply(updateSwitch);
    }

    /**
     * The method stores a new <em>write effect</em> in the calculated effect
     * store. The effect is caused by an assignment to an array. Therefore, the
     * method requires the <em>security level</em> that is effected by this
     * <em>write effect</em> and also the {@link ArrayRef} of the array that is
     * assigned. Also, it will be checked whether the effect may occur in the
     * current implicit flow.
     * 
     * @param effect
     *            Affected <em>security level</em> of the <em>write effect</em>.
     * @param arrayRef
     *            The reference of the array that is assigned.
     * @see AnalyzedMethodEnvironment#addWriteEffectCausedByArrayAssign(String,
     *      long, ArrayRef)
     */
    protected void addWriteEffectCausedByArrayAssign(ILevel effect,
                                                     ArrayRef arrayRef) {
        getAnalyzedEnvironment().addWriteEffectCausedByArrayAssign(effect,
                                                                   getSourceLine(),
                                                                   arrayRef);
        checkEffect(effect);
    }

    /**
     * The method stores a new <em>write effect</em> in the calculated effect
     * store. The effect is caused by an assignment to a field. Therefore, the
     * method requires the <em>security level</em> that is effected by this
     * <em>write effect</em> and also the {@link SootField} that is assigned.
     * Also, it will be checked whether the effect may occur in the current
     * implicit flow.
     * 
     * @param effect
     *            Affected <em>security level</em> of the <em>write effect</em>.
     * @param sootField
     *            The field that is assigned.
     * @see AnalyzedMethodEnvironment#addWriteEffectCausedByAssign(String, long,
     *      SootField)
     */
    protected void addWriteEffectCausedByAssign(ILevel effect,
                                                SootField sootField) {
        getAnalyzedEnvironment().addWriteEffectCausedByAssign(effect,
                                                              getSourceLine(),
                                                              sootField);
        checkEffect(effect);
    }

    /**
     * The method stores a new <em>write effect</em> in the calculated effect
     * store. The effect is caused by an inheritance of a class. Therefore, the
     * method requires the <em>security level</em> that is effected by this
     * <em>write effect</em> and also the {@link SootClass} from which the
     * effect was inherited. Also, it will be checked whether the effect may
     * occur in the current implicit flow.
     * 
     * @param effect
     *            Affected <em>security level</em> of the <em>write effect</em>.
     * @param sootClass
     *            The class from which the effect was inherited.
     * @see AnalyzedMethodEnvironment#addWriteEffectCausedByClass(String, long,
     *      SootClass)
     */
    protected void addWriteEffectCausedByClass(ILevel effect,
                                               SootClass sootClass) {
        getAnalyzedEnvironment().addWriteEffectCausedByClass(effect,
                                                             getSourceLine(),
                                                             sootClass);
        checkEffect(effect);
    }

    /**
     * The method stores a new <em>write effect</em> in the calculated effect
     * store. The effect is caused by an invocation of a method that provides
     * this effect. Therefore, the method requires the <em>security level</em>
     * that is effected by this <em>write effect</em> and also the
     * {@link SootMethod} that is invoked and that provides this effect. Also,
     * it will be checked whether the effect may occur in the current implicit
     * flow.
     * 
     * @param effect
     *            Affected <em>security level</em> of the <em>write effect</em>.
     * @param sootMethod
     *            Method that is invoked and that has this effect.
     * @see AnalyzedMethodEnvironment#addWriteEffectCausedByMethodInvocation(String,
     *      long, SootMethod)
     */
    protected void addWriteEffectCausedByMethodInvocation(ILevel effect,
                                                          SootMethod sootMethod) {
        getAnalyzedEnvironment().addWriteEffectCausedByMethodInvocation(effect,
                                                                        getSourceLine(),
                                                                        sootMethod);
        checkEffect(effect);
    }

    /**
     * Handles a given {@link InvokeExpr} and checks the
     * <em>security levels</em> of the method parameters as well as if the given
     * flag indicates that the invoke expression is assigned then the
     * <em>security level</em> of the returned value is calculated. If the
     * expression is assigned, then calculated level is returned by this method,
     * otherwise the method returns the 'void' <em>security level</em>. If an
     * error occurs during the check of the parameter the weakest available
     * <em>security level</em> will be returned. If the invoke expression is a
     * library method then the strongest level of the invoke expression
     * arguments will be returned by the method. Additionally, also the
     * <em>write effects</em> of the invoked method will be checked.
     * 
     * @param invokeExpr
     *            Invoke expression which should be checked.
     * @param assignment
     *            Indicates whether it is an assignment.
     * @return If the given flag indicates that the expression is assigned, then
     *         the method returns the return <em>security level</em> of the
     *         invoked expression, if it is not assigned, then the 'void'
     *         <em>security level</em>. If it is a library method then the
     *         method returns the strongest argument level.
     */
    protected ILevel calculateInvokeExprLevel(InvokeExpr invokeExpr,
                                              boolean assignment) {
        MethodEnvironment invokedMethod =
            getStore().getMethodEnvironment(invokeExpr.getMethod());
        List<MethodParameter> invokedMethodParameter =
            invokedMethod.getMethodParameters();
        ILevel level = getWeakestSecurityLevel();
        List<ILevel> parameterLevels = new ArrayList<ILevel>();
        List<ILevel> argumentLevels = new ArrayList<ILevel>();
        for (int j = 0; j < invokeExpr.getArgCount(); j++) {
            Value value = invokeExpr.getArg(j);
            ILevel parameterLevel = invokedMethodParameter.get(j).getLevel();
            String parameterName = invokedMethodParameter.get(j).getName();
            ILevel argumentLevel = calculateLevel(value, invokeExpr.toString());
            if (!isWeakerOrEqualLevel(argumentLevel, parameterLevel)) {
                logSecurity(getMsg("security.param.stronger",
                                   getSignatureOfAnalyzedMethod(),
                                   getSourceLine(),
                                   getSignatureOfMethod(invokeExpr.getMethod()),
                                   parameterName,
                                   parameterLevel.getName(),
                                   argumentLevel.getName()));
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
     * Looks up a <em>security level</em> for a given value. In order to
     * identify possible errors, also the unit, which contains the value, has to
     * be given as a String. If no <em>security level</em> can be determined for
     * the given value, the weakest available level will be returned. Occurring
     * exceptions are logged.
     * 
     * @param value
     *            Value for which the <em>security level</em> should be
     *            determined.
     * @param containing
     *            Unit that contains the given value as String.
     * @return Returns the <em>security level</em> of the given value, if it can
     *         be determined. Otherwise it returns the weakest available
     *         <em>security level</em>.
     * @see SecurityLevelValueReadSwitch
     */
    protected ILevel calculateLevel(Value value, String containing) {
        SecurityLevelValueReadSwitch lookupSwitch =
            new SecurityLevelValueReadSwitch(getAnalyzedEnvironment(),
                                             getStore(),
                                             getIn(),
                                             getOut());
        value.apply(lookupSwitch);
        return lookupSwitch.getLevel();
    }

    /**
     * Handles a invoked method and adds for every provided
     * <em>write effect</em> a new <em>write effect</em> to the calculated
     * effect store. Therefore, the provided effects have to be valid. If the
     * method is an initialization method or a static method also the
     * <em>write effects</em> of the class that declares the method will be
     * added to the calculated effect storage, if they are valid. In the case of
     * a failure, this failure will be logged.
     * 
     * @param invokedMethod
     *            Environment of an invoked method for which all
     *            <em>write effects</em> should be stored in the calculated
     *            effect store.
     */
    protected void checkSideEffectsOfInvokedMethod(MethodEnvironment invokedMethod) {
        for (ILevel effected : invokedMethod.getWriteEffects()) {
            addWriteEffectCausedByMethodInvocation(effected,
                                                   invokedMethod.getSootMethod());
        }
        if (isInitMethod(invokedMethod.getSootMethod())
            || invokedMethod.getSootMethod().isStatic()) {
            for (ILevel effected : invokedMethod.getClassWriteEffects()) {
                addWriteEffectCausedByClass(effected,
                                            invokedMethod.getSootMethod()
                                                         .getDeclaringClass());
            }
        }

    }

    /**
     * The method checks whether the given <em>security level</em> is weaker
     * than the level of the program counter. If this is the case, then the
     * method will return the level of the program counter, if not, then the
     * given <em>security level</em> is returned.
     * 
     * @param level
     *            <em>Security level</em>, for which the program counter level
     *            should be considered.
     * @return The strongest program counter level if it is stronger than the
     *         given level, otherwise the given <em>security level</em>.
     */
    protected ILevel takePCintoAccount(ILevel level) {
        if (getOut().hasProgramCounterLevel()) {
            ILevel pcLevel = getWeakestSecurityLevel();
            try {
                pcLevel = getOut().getStrongestProgramCounterLevel();
            } catch (OperationInvalidException e) {
                throw new ProgramCounterException(getMsg("exception.program_counter.error",
                                                         getSignatureOfAnalyzedMethod()),
                                                  e);
            }
            if (isWeakerOrEqualLevel(level, pcLevel)) {
                level = pcLevel;
            }
        }
        return level;
    }

    /**
     * Updates the <em>security level</em> of the given right and left side
     * value of an identity statement. In order to identify possible errors,
     * also the unit, which contains the value, has to be given as a String.
     * Occurring exceptions are logged.
     * 
     * @param rightSide
     *            The right hand side of an identity statement.
     * @param leftSide
     *            The left hand side of an identity statement.
     * @param containing
     *            Unit that contains the given value as String.
     */
    protected void updateIdentityLevel(Value rightSide,
                                       Value leftSide,
                                       String containing) {
        SecurityLevelValueWriteSwitch updateSwitch =
            new SecurityLevelValueWriteSwitch(getAnalyzedEnvironment(),
                                              getStore(),
                                              getIn(),
                                              getOut(),
                                              null);
        updateSwitch.setIdentityInformation(leftSide);
        executeUpdateLevel(rightSide, updateSwitch, containing);
    }

    /**
     * Updates the <em>security level</em> of the given normal value to the
     * given level. Such a normal value are {@link Local} variables or
     * {@link SootField} fields. In order to identify possible errors, also the
     * unit, which contains the value, has to be given as a String. Occurring
     * exceptions are logged.
     * 
     * @param value
     *            Value for which the <em>security level</em> should be updated
     *            to the given level.
     * @param level
     *            The <em>security level</em> to which the level of the value
     *            should be updated.
     * @param containing
     *            Unit that contains the given value as String.
     * @see SecurityLevelSwitch#executeUpdateLevel(Value,
     *      SecurityLevelValueWriteSwitch, String)
     */
    protected void updateLevel(Value value, ILevel level, String containing) {
        SecurityLevelValueWriteSwitch updateSwitch =
            new SecurityLevelValueWriteSwitch(getAnalyzedEnvironment(),
                                              getStore(),
                                              getIn(),
                                              getOut(),
                                              level);
        executeUpdateLevel(value, updateSwitch, containing);
    }

    /**
     * Updates the <em>security level</em> of the given value to the level which
     * is given by the {@link SecurityLevelSwitch#analyzedMethodEnvironment} and
     * the {@link ParameterRef}. Occurring exceptions are logged.
     * 
     * @param value
     *            Value for which the <em>security level</em> should be updated
     *            to the level of the given parameter reference.
     * @param parameterRef
     *            The parameter reference for which the <em>security level</em>
     *            can be looked up in the
     *            {@link SecurityLevelSwitch#analyzedMethodEnvironment}.
     */
    protected void updateParameterLevel(Value value, ParameterRef parameterRef) {
        MethodParameter methodParameter =
            getAnalyzedEnvironment().getMethodParameterAt(parameterRef.getIndex());
        SecurityLevelValueWriteSwitch updateSwitch =
            new SecurityLevelValueWriteSwitch(getAnalyzedEnvironment(),
                                              getStore(),
                                              getIn(),
                                              getOut(),
                                              methodParameter.getLevel());
        executeUpdateLevel(value, updateSwitch, parameterRef.toString());
    }

}