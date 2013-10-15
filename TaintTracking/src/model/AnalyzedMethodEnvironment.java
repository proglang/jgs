package model;

import analysis.TaintTracking;
import logging.SecurityLogger;
import model.Cause.ArrayAssignCause;
import model.Cause.AssignCause;
import model.Cause.ClassCause;
import model.Cause.MethodCause;
import security.SecurityAnnotation;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.jimple.ArrayRef;
import soot.jimple.Stmt;
import utils.SecurityMessages;
import utils.SootUtils;

/**
 * <h1>Directly analysis environment for methods</h1>
 * 
 * The {@link AnalyzedMethodEnvironment} provides a environment for analyzing a {@link SootMethod}.
 * Therefore it extends the analysis method environment {@link MethodEnvironment} in order to access
 * a logger and the security annotation as well as methods for getting the required annotations at
 * the method and at the class which declares the method, and also the methods which checks the
 * validity of the levels and effects that are given by those annotations. This environment handles
 * {@link SootMethod} which will be analyzed directly, i.e. the method is the main suspect of the
 * {@link TaintTracking} analysis. Means that also the <em>side effects</em> of the method should be
 * check, therefore the environment provides methods which allows the checking of the
 * <em>side effects</em>.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class AnalyzedMethodEnvironment extends MethodEnvironment {

	/**
	 * The store that provides the storing of calculated <em>side effects</em> (see
	 * {@link EffectsStore}).
	 */
	private EffectsStore effectsStore = new EffectsStore();
	/** The source line number of the current handled statement. */
	private long srcLn = 0;
	/** The current handled statement. */
	private Stmt stmt = null;

	/**
	 * Constructor of a {@link AnalyzedMethodEnvironment} that requires a {@link SootMethod} which
	 * should be analyzed, a logger in order to allow logging for this object as well as a security
	 * annotation object in order to provide the handling of <em>security levels</em>. By calling
	 * the constructor all required extractions of the annotations will be done automatically. This
	 * created {@link Environment} allows the direct analysis of the given {@link SootMethod}.
	 * 
	 * @param sootMethod
	 *            The {@link SootMethod} that should be analyzed.
	 * @param log
	 *            A {@link SecurityLogger} in order to allow logging for this object.
	 * @param securityAnnotation
	 *            A {@link SecurityAnnotation} in order to provide the handling of
	 *            <em>security levels</em>.
	 */
	public AnalyzedMethodEnvironment(SootMethod sootMethod, SecurityLogger log,
			SecurityAnnotation securityAnnotation) {
		super(sootMethod, log, securityAnnotation);
	}

	/**
	 * The method creates a new <em>write effect</em> and adds this to the calculated effect store
	 * {@link AnalyzedMethodEnvironment#effectsStore}. The created effect is caused by an assignment
	 * to an array. Therefore, the method requires the <em>security level</em> that is effected by
	 * this <em>write effect</em>, the source line number at which the effect occurs and also the
	 * {@link ArrayRef} of the array that is assigned.
	 * 
	 * @param effected
	 *            Affected <em>security level</em> of the <em>write effect</em>.
	 * @param srcLn
	 *            The source line number at which the effect occurs.
	 * @param arrayRef
	 *            The reference of the array that is assigned.
	 * @see Cause.ArrayAssignCause
	 */
	public void addWriteEffectCausedByArrayAssign(String effected, long srcLn, ArrayRef arrayRef) {
		effectsStore.addWriteEffect(new Effect(effected, srcLn, new ArrayAssignCause(arrayRef)));
	}

	/**
	 * The method creates a new <em>write effect</em> and adds this to the calculated effect store
	 * {@link AnalyzedMethodEnvironment#effectsStore}. The created effect is caused by an assignment
	 * to a field. Therefore, the method requires the <em>security level</em> that is effected by
	 * this <em>write effect</em>, the source line number at which the effect occurs and also the
	 * {@link SootField} that is assigned.
	 * 
	 * @param effected
	 *            Affected <em>security level</em> of the <em>write effect</em>.
	 * @param srcLn
	 *            The source line number at which the effect occurs.
	 * @param sootField
	 *            The field that is assigned.
	 * @see Cause.AssignCause
	 */
	public void addWriteEffectCausedByAssign(String effected, long srcLn, SootField sootField) {
		effectsStore.addWriteEffect(new Effect(effected, srcLn, new AssignCause(sootField)));
	}

	/**
	 * The method creates a new <em>write effect</em> and adds this to the calculated effect store
	 * {@link AnalyzedMethodEnvironment#effectsStore}. The created effect is caused by an
	 * inheritance of a class. Therefore, the method requires the <em>security level</em> that is
	 * effected by this <em>write effect</em>, the source line number at which the effect occurs and
	 * also the {@link SootClass} from which the effect was inherited.
	 * 
	 * @param effected
	 *            Affected <em>security level</em> of the <em>write effect</em>.
	 * @param srcLn
	 *            The source line number at which the effect occurs.
	 * @param sootClass
	 *            The class from which the effect was inherited.
	 * @see Cause.ClassCause
	 */
	public void addWriteEffectCausedByClass(String effected, long srcLn, SootClass sootClass) {
		effectsStore.addWriteEffect(new Effect(effected, srcLn, new ClassCause(sootClass)));
	}

	/**
	 * The method creates a new <em>write effect</em> and adds this to the calculated effect store
	 * {@link AnalyzedMethodEnvironment#effectsStore}. The created effect is caused by an invocation
	 * of a method that provides this effect. Therefore, the method requires the
	 * <em>security level</em> that is effected by this <em>write effect</em>, the source line
	 * number at which the effect occurs and also the {@link SootMethod} that is invoked and that
	 * provides this effect.
	 * 
	 * @param effected
	 *            Affected <em>security level</em> of the <em>write effect</em>.
	 * @param srcLn
	 *            The source line number at which the effect occurs.
	 * @param sootMethod
	 *            Method that is invoked and that has this effect.
	 * @see Cause.MethodCause
	 */
	public void addWriteEffectCausedByMethodInvocation(String effected, long srcLn,
			SootMethod sootMethod) {
		effectsStore.addWriteEffect(new Effect(effected, srcLn, new MethodCause(sootMethod)));
	}

	/**
	 * Checks whether the calculated effects match the expected <em>write effects</em>, i.e. if a
	 * effect was calculated by the analysis, but the annotation didn't expected this effect, then
	 * this method will be logged with the logger. Also if the expected effects contains an effect
	 * which wasn't calculated then a warning will be logged, because of a superfluous effect. If
	 * the analyzed method is a static initializer method, the class <em>write effects</em> will be
	 * used as expected <em>write effects</em>, otherwise the effects of the method will be used as
	 * expected <em>write effects</em>.
	 */
	public void checkEffectAnnotations() {
		String methodSignature = SootUtils.generateMethodSignature(getSootMethod(), false, true,
				true);
		for (String effected : effectsStore.getWriteEffectSet()) {
			if (SootUtils.isClinitMethod(getSootMethod())) {
				if (!getExpectedClassWriteEffects().contains(effected)) {
					for (Effect effect : effectsStore.getWriteEffects(effected)) {
						long srcLn = effect.getSrcLn();
						String cause = effect.getCause().getCauseString();
						logEffect(srcLn, SecurityMessages.missingWriteEffect(methodSignature,
								srcLn, effected, cause));
					}
				}
			} else {
				if (!getExpectedWriteEffects().contains(effected)) {
					for (Effect effect : effectsStore.getWriteEffects(effected)) {
						long srcLn = effect.getSrcLn();
						String cause = effect.getCause().getCauseString();
						logEffect(srcLn, SecurityMessages.missingWriteEffect(methodSignature,
								srcLn, effected, cause));
					}
				}
			}
		}
		for (String effected : SootUtils.isClinitMethod(getSootMethod()) ? getExpectedClassWriteEffects()
				: getExpectedWriteEffects()) {
			if (!effectsStore.getWriteEffectSet().contains(effected)) {
				logWarning(SecurityMessages.superfluousWriteEffect(methodSignature, effected));
			}
		}
	}

	/**
	 * Method that returns the source line number of the currently handled statement. Note, that
	 * immediately after the initialization of this environment and before the start of flow
	 * analysis, the method will return {@code 0}.
	 * 
	 * @return The source line number of the current handled statement or if this wasn't set
	 *         {@code 0}.
	 */
	public long getSrcLn() {
		return srcLn;
	}

	/**
	 * Method that returns the current handled {@link Stmt} of the analyzed method. Note, that
	 * immediately after the initialization of this environment and before the start of flow
	 * analysis, the method will return {@code null}.
	 * 
	 * @return The current handled statement or if this wasn't set {@code null}.
	 */
	public Stmt getStmt() {
		return stmt;
	}

	/**
	 * Logs the given message as a <em>side effect</em> together with the given source line number.
	 * The file name is created by the analyzed {@link SootMethod}, which this environment stores
	 * (see {@link MethodEnvironment#sootMethod}).
	 * 
	 * @param srcLn
	 *            Source line number at which the given <em>side effect</em> message is generated.
	 * @param msg
	 *            Information about the effect that should be printed as a <em>side effect</em>.
	 * @see SecurityLogger#effect(String, long, String)
	 */
	public void logEffect(long srcLn, String msg) {
		getLog().effect(SootUtils.generateFileName(getSootMethod()), srcLn, msg);
	}

	/**
	 * Sets the current handled {@link Stmt} of the analyzed method. This handled statement will be
	 * used for logging. At the same time the method will also determine the source line number of
	 * the currently handled statement.
	 * 
	 * @param stmt
	 *            The current handled {@link Stmt}.
	 * @see SootUtils#extractLn(soot.Unit)
	 */
	public void setStmt(Stmt stmt) {
		this.stmt = stmt;
		this.srcLn = SootUtils.extractLn(stmt);
	}

}
