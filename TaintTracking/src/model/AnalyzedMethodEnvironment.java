package model;

import java.util.List;

import preanalysis.AnnotationExtractor.UsedObjectStore;


import exception.SootException.InvalidLevelException;

import analysis.TaintTracking;
import logging.SecurityLogger;
import model.Cause.ArrayAssignCause;
import model.Cause.AssignCause;
import model.Cause.ClassCause;
import model.Cause.MethodCause;
import resource.Configuration;
import resource.SecurityMessages;
import security.LevelMediator;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.jimple.ArrayRef;
import soot.jimple.Stmt;
import utils.SootUtils;

/**
 * <h1>Directly analysis environment for methods</h1>
 * 
 * The {@link AnalyzedMethodEnvironment} provides a environment for analyzing a
 * {@link SootMethod}. Therefore it extends the analysis method environment
 * {@link MethodEnvironment} in order to access a logger and the security
 * annotation as well as methods for getting the required annotations at the
 * method and at the class which declares the method, and also the methods which
 * checks the validity of the levels and effects that are given by those
 * annotations. This environment handles {@link SootMethod} which will be
 * analyzed directly, i.e. the method is the main suspect of the
 * {@link TaintTracking} analysis. Means that also the <em>side effects</em> of
 * the method should be check, therefore the environment provides methods which
 * allows the checking of the <em>side effects</em>.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class AnalyzedMethodEnvironment extends MethodEnvironment {

	/**
	 * The store that provides the storing of calculated <em>side effects</em>
	 * (see {@link EffectsStore}).
	 */
	private EffectsStore effectsStore = new EffectsStore();
	/** The source line number of the current handled statement. */
	private long srcLn = 0;
	/** The current handled statement. */
	private Stmt stmt = null;

	/**
	 * DOC
	 * 
	 * @param me
	 */
	public AnalyzedMethodEnvironment(MethodEnvironment me) {
		super(me.getSootMethod(), me.isIdFunction(), me.isClinit(), me
				.isClinit(), me.isVoid(), me.isSootSecurityMethod(), me
				.getMethodParameters(), me.getReturnLevel(), me
				.getWriteEffects(), me.getClassWriteEffects(), me.getLog(), me
				.getLevelMediator());
	}

	/**
	 * DOC
	 * 
	 * TODO: Include the Inheritance Check!!!
	 */
	private void checkInheritance(UsedObjectStore store) {
		SootClass sootClass = getSootMethod().getDeclaringClass();
		String classSignature = SootUtils.generateClassSignature(sootClass,
				Configuration.CLASS_SIGNATURE_PRINT_PACKAGE);
		String methodSignature = SootUtils.generateMethodSignature(
				getSootMethod(), Configuration.METHOD_SIGNATURE_PRINT_PACKAGE,
				Configuration.METHOD_SIGNATURE_PRINT_TYPE,
				Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY);
		if (SootUtils.isClinitMethod(getSootMethod())) {
			List<String> currentEffects = getClassWriteEffects();
			String currentWeakestEffect = getLevelMediator().getMinLevel(
					currentEffects);
			boolean currentEmptyEffect = currentEffects.isEmpty();
			// If clinit => effects of class
			if (sootClass.hasSuperclass()) {
				SootClass superClass = sootClass.getSuperclass();
				// TODO: UsedObjectStore
				String superClassSignature = SootUtils.generateClassSignature(
						sootClass, Configuration.CLASS_SIGNATURE_PRINT_PACKAGE);
				if (!superClass.isLibraryClass()) {
					// Extract the class effects for superClass, take the
					// weakest and check
					// whether it is weaker or equals than the weakest effect of
					// the current class.
					ClassEnvironment ce = store.getClassEnvironment(superClass);
					List<String> superEffects = ce.getWriteEffects();
					if (superEffects != null) {
						String superWeakestEffect = getLevelMediator()
								.getMinLevel(superEffects);
						boolean superEmptyEffect = superEffects.isEmpty();
						// A :> B
						/*
						 * if (superEmptyEffect && currentEmptyEffect) { //
						 * check, because {} = {} => lightweight write effect }
						 * else if (!superEmptyEffect && currentEmptyEffect) {
						 * // check, because H requires {} || L requires {} }
						 * else
						 */
						if (superEmptyEffect && !currentEmptyEffect) {
							// !, because {} requires H (lightweight)
							if (!currentWeakestEffect
									.equals(getLevelMediator()
											.getStrongestSecurityLevel())) {
								// the write effect of current class isn't
								// stronger or equals
								// because the current has no write effect
								// TODO: Logging
								getLog().effect(
										SootUtils
												.generateFileName(getSootMethod()),
										0,
										"The class <"
												+ superClassSignature
												+ "> has no write effects, thus this is a less serious effect as the most serious effect '"
												+ currentWeakestEffect
												+ "' of subclass <"
												+ classSignature + ">.");
							}
						} else if (!superEmptyEffect && !currentEmptyEffect) {
							// ! H requires H, L requires H, L
							if (!getLevelMediator().isWeakerOrEqualsThan(
									superWeakestEffect, currentWeakestEffect)) {
								// the write effect of current class isn't
								// stronger or equals
								// TODO: Logging
								getLog().effect(
										SootUtils
												.generateFileName(getSootMethod()),
										0,
										"The the most serious write effect '"
												+ currentWeakestEffect
												+ "' of class <"
												+ classSignature
												+ "> is less serious as the most serious effect '"
												+ superWeakestEffect
												+ "' of super class <"
												+ superClassSignature + ">.");
							}
						}
					} else {
						logError(SecurityMessages
								.noClassWriteEffectAnnotation(superClassSignature));
					}
				} else {
					// can not check whether the inheritance is valid for the
					// class because of
					// library dependencies.
					// TODO: Logging
					logWarning("Can't check whether the inheritance of the class <"
							+ classSignature
							+ "> to the class <"
							+ superClassSignature
							+ "> is valid because it is a library class.");
				}
			}
		} else if (!SootUtils.isInitMethod(getSootMethod())) {
			// If normal => effects, security param & return of method
			SootClass superClass = getSuperClassDeclaring();
			SootMethod superMethod = superClass != null ? superClass
					.getMethod(getSootMethod().getSubSignature()) : null;
			List<String> currentEffects = getWriteEffects();
			String currentWeakestEffect = getLevelMediator().getMinLevel(
					currentEffects);
			boolean currentEmptyEffect = currentEffects.isEmpty();
			if (superClass != null && superMethod != null) {
				String superMethodSignature = SootUtils
						.generateMethodSignature(superMethod,
								Configuration.METHOD_SIGNATURE_PRINT_PACKAGE,
								Configuration.METHOD_SIGNATURE_PRINT_TYPE,
								Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY);
				if (!superClass.isLibraryClass()) {
					MethodEnvironment superEnvironement = store.getMethodEnvironment(superMethod);
					List<String> superEffects = superEnvironement
							.getWriteEffects();
					String superWeakestEffect = getLevelMediator()
							.getMinLevel(superEffects);
					boolean superEmptyEffect = superEffects.isEmpty();
					if (superEmptyEffect && !currentEmptyEffect) {
						// !, because {} requires H (lightweight)
						if (!currentWeakestEffect
								.equals(getLevelMediator()
										.getStrongestSecurityLevel())) {
							// the write effect of current class isn't stronger
							// or equals
							// because the current has no write effect
							// TODO: Logging
							getLog().effect(
									SootUtils.generateFileName(getSootMethod()),
									0,
									"The method <"
											+ superMethodSignature
											+ "> has no write effects, thus this is a less serious effect as the most serious effect '"
											+ currentWeakestEffect
											+ "' of method <" + methodSignature
											+ ">.");
						}
					} else if (!superEmptyEffect && !currentEmptyEffect) {
						// ! H requires H, L requires H, L
						if (!getLevelMediator().isWeakerOrEqualsThan(
								superWeakestEffect, currentWeakestEffect)) {
							// the write effect of current class isn't stronger
							// or equals
							// TODO: Logging
							getLog().effect(
									SootUtils.generateFileName(getSootMethod()),
									0,
									"The the most serious write effect '"
											+ currentWeakestEffect
											+ "' of method <"
											+ methodSignature
											+ "> is less serious as the most serious effect '"
											+ superWeakestEffect
											+ "' of the overriden <"
											+ superMethodSignature + ">.");
						}
					}
					String returnLevelCurrent = getReturnLevel();
					String returnLevelSuper = superEnvironement
							.getReturnLevel();
					if (!(returnLevelCurrent
							.equals(LevelMediator.VOID_LEVEL) && returnLevelSuper
							.equals(LevelMediator.VOID_LEVEL))) {
						try {
							if (!getLevelMediator().isWeakerOrEqualsThan(
									returnLevelCurrent, returnLevelSuper)) {
								// the return security level of current method
								// isn't stronger or
								// equals
								// TODO: Logging
								getLog().security(
										SootUtils
												.generateFileName(getSootMethod()),
										0,
										"The return security level '"
												+ returnLevelCurrent
												+ "' of method <"
												+ methodSignature
												+ "> isn't weaker or equals to the return security level '"
												+ returnLevelSuper
												+ "' of overridden method <"
												+ superMethodSignature + ">.");
							}
						} catch (InvalidLevelException e) {
							// handle exception which is thrown cause of a
							// return level depending on
							// specified arguments
							// TODO: Logging
							logError("The return security level '"
									+ returnLevelCurrent
									+ "' of method <"
									+ methodSignature
									+ "> is not comparable to the return security level '"
									+ returnLevelSuper
									+ "' of overridden method <"
									+ superMethodSignature
									+ "> because at least one of the level is invalid or a variable level.");
						}
					}
					List<MethodParameter> methodParameterCurrent = getMethodParameters();
					List<MethodParameter> methodParameterSuper = superEnvironement
							.getMethodParameters();
					if (methodParameterCurrent.size() == methodParameterSuper
							.size()) {
						for (int i = 0; i < methodParameterCurrent.size(); i++) {
							MethodParameter parameterCurrent = methodParameterCurrent
									.get(i);
							MethodParameter parameterSuper = methodParameterSuper
									.get(i);
							if (parameterCurrent.getPosition() == parameterSuper
									.getPosition()
									&& parameterCurrent.getType().equals(
											parameterSuper.getType())) {
								String parameterLevelCurrent = parameterCurrent
										.getLevel();
								String parameterLevelSuper = parameterSuper
										.getLevel();
								try { // TODO: Handle variable security level
									if (!getLevelMediator()
											.isWeakerOrEqualsThan(
													parameterLevelSuper,
													parameterLevelCurrent)) {
										// the security level of super parameter
										// is stronger than
										// the current parameter level
										// TODO: Logging
										getLog().security(
												SootUtils
														.generateFileName(getSootMethod()),
												0,
												"The security level '"
														+ parameterLevelSuper
														+ "' of parameter '"
														+ parameterSuper
																.getName()
														+ "' at the overridden method <"
														+ superMethodSignature
														+ "> is stronger "
														+ "than the parameter level '"
														+ parameterLevelCurrent
														+ "' of parameter '"
														+ parameterCurrent
																.getName()
														+ "' at method <"
														+ methodSignature
														+ ">.");
									}
								} catch (InvalidLevelException e) {
									// handle exception which is thrown cause of
									// a variable security
									// level of a parameter
									// TODO: Logging
									logError("The method parameter with the securitylevel '"
											+ parameterCurrent.getLevel()
											+ "' and the name '"
											+ parameterCurrent.getName()
											+ "' of <"
											+ methodSignature
											+ "> is not comparable to the method parameter with the security level '"
											+ parameterSuper.getLevel()
											+ "' and the name '"
											+ parameterSuper.getName()
											+ "' of method the overridden method <"
											+ superMethodSignature
											+ "> because at least one of the level is invalid or a variable level.");
								}
							} else {
								// Method parameter is not comparable
								// TODO: Logging
								logError("The method parameter with the name '"
										+ parameterCurrent.getName()
										+ "' of method <"
										+ methodSignature
										+ "> is not comparable to the method parameter with the name '"
										+ parameterSuper.getName()
										+ "' of method the overridden method <"
										+ superMethodSignature + ">.");
							}
						}
					} else {
						// Method parameters are not comparable
						// TODO: Logging
						logError("Method parameters of method <"
								+ methodSignature
								+ "> and of the overridden method <"
								+ superMethodSignature
								+ "> are not comparable.");
					}
				} else {
					// can not check whether the inheritance is valid for the
					// class because of
					// library
					// dependencies.
					// TODO: Logging
					logWarning("Can't check whether the inheritance of the method <"
							+ methodSignature
							+ "> to the overridden method >"
							+ superMethodSignature
							+ "> is valid because it is a library method.");

				}
			}
		}
	}

	/**
	 * DOC
	 * 
	 * @return
	 */
	private SootClass getSuperClassDeclaring() {
		SootClass result = null;
		SootClass current = getSootMethod().getDeclaringClass();
		while (current.hasSuperclass() && result == null) {
			current = current.getSuperclass();
			if (current.declaresMethod(getSootMethod().getSubSignature())) {
				result = current;
			}
		}
		return result;
	}

	/**
	 * The method creates a new <em>write effect</em> and adds this to the
	 * calculated effect store {@link AnalyzedMethodEnvironment#effectsStore}.
	 * The created effect is caused by an assignment to an array. Therefore, the
	 * method requires the <em>security level</em> that is effected by this
	 * <em>write effect</em>, the source line number at which the effect occurs
	 * and also the {@link ArrayRef} of the array that is assigned.
	 * 
	 * @param effected
	 *            Affected <em>security level</em> of the <em>write effect</em>.
	 * @param srcLn
	 *            The source line number at which the effect occurs.
	 * @param arrayRef
	 *            The reference of the array that is assigned.
	 * @see Cause.ArrayAssignCause
	 */
	public void addWriteEffectCausedByArrayAssign(String effected, long srcLn,
			ArrayRef arrayRef) {
		effectsStore.addWriteEffect(new Effect(effected, srcLn,
				new ArrayAssignCause(arrayRef)));
	}

	/**
	 * The method creates a new <em>write effect</em> and adds this to the
	 * calculated effect store {@link AnalyzedMethodEnvironment#effectsStore}.
	 * The created effect is caused by an assignment to a field. Therefore, the
	 * method requires the <em>security level</em> that is effected by this
	 * <em>write effect</em>, the source line number at which the effect occurs
	 * and also the {@link SootField} that is assigned.
	 * 
	 * @param effected
	 *            Affected <em>security level</em> of the <em>write effect</em>.
	 * @param srcLn
	 *            The source line number at which the effect occurs.
	 * @param sootField
	 *            The field that is assigned.
	 * @see Cause.AssignCause
	 */
	public void addWriteEffectCausedByAssign(String effected, long srcLn,
			SootField sootField) {
		effectsStore.addWriteEffect(new Effect(effected, srcLn,
				new AssignCause(sootField)));
	}

	/**
	 * The method creates a new <em>write effect</em> and adds this to the
	 * calculated effect store {@link AnalyzedMethodEnvironment#effectsStore}.
	 * The created effect is caused by an inheritance of a class. Therefore, the
	 * method requires the <em>security level</em> that is effected by this
	 * <em>write effect</em>, the source line number at which the effect occurs
	 * and also the {@link SootClass} from which the effect was inherited.
	 * 
	 * @param effected
	 *            Affected <em>security level</em> of the <em>write effect</em>.
	 * @param srcLn
	 *            The source line number at which the effect occurs.
	 * @param sootClass
	 *            The class from which the effect was inherited.
	 * @see Cause.ClassCause
	 */
	public void addWriteEffectCausedByClass(String effected, long srcLn,
			SootClass sootClass) {
		effectsStore.addWriteEffect(new Effect(effected, srcLn, new ClassCause(
				sootClass)));
	}

	/**
	 * The method creates a new <em>write effect</em> and adds this to the
	 * calculated effect store {@link AnalyzedMethodEnvironment#effectsStore}.
	 * The created effect is caused by an invocation of a method that provides
	 * this effect. Therefore, the method requires the <em>security level</em>
	 * that is effected by this <em>write effect</em>, the source line number at
	 * which the effect occurs and also the {@link SootMethod} that is invoked
	 * and that provides this effect.
	 * 
	 * @param effected
	 *            Affected <em>security level</em> of the <em>write effect</em>.
	 * @param srcLn
	 *            The source line number at which the effect occurs.
	 * @param sootMethod
	 *            Method that is invoked and that has this effect.
	 * @see Cause.MethodCause
	 */
	public void addWriteEffectCausedByMethodInvocation(String effected,
			long srcLn, SootMethod sootMethod) {
		effectsStore.addWriteEffect(new Effect(effected, srcLn,
				new MethodCause(sootMethod)));
	}

	/**
	 * Checks whether the calculated effects match the expected
	 * <em>write effects</em>, i.e. if a effect was calculated by the analysis,
	 * but the annotation didn't expected this effect, then this method will be
	 * logged with the logger. Also if the expected effects contains an effect
	 * which wasn't calculated then a warning will be logged, because of a
	 * superfluous effect. If the analyzed method is a static initializer
	 * method, the class <em>write effects</em> will be used as expected
	 * <em>write effects</em>, otherwise the effects of the method will be used
	 * as expected <em>write effects</em>.
	 */
	public void checkEffectAnnotations() {
		String methodSignature = SootUtils.generateMethodSignature(
				getSootMethod(), Configuration.METHOD_SIGNATURE_PRINT_PACKAGE,
				Configuration.METHOD_SIGNATURE_PRINT_TYPE,
				Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY);
		for (String effected : effectsStore.getWriteEffectSet()) {
			if (SootUtils.isClinitMethod(getSootMethod())) {
				if (!getClassWriteEffects().contains(effected)) {
					for (Effect effect : effectsStore.getWriteEffects(effected)) {
						long srcLn = effect.getSrcLn();
						String cause = effect.getCause().getCauseString();
						logEffect(srcLn, SecurityMessages.missingWriteEffect(
								methodSignature, srcLn, effected, cause));
					}
				}
			} else {
				if (!getWriteEffects().contains(effected)) {
					for (Effect effect : effectsStore.getWriteEffects(effected)) {
						long srcLn = effect.getSrcLn();
						String cause = effect.getCause().getCauseString();
						logEffect(srcLn, SecurityMessages.missingWriteEffect(
								methodSignature, srcLn, effected, cause));
					}
				}
			}
		}
		for (String effected : SootUtils.isClinitMethod(getSootMethod()) ? getClassWriteEffects()
				: getWriteEffects()) {
			if (!effectsStore.getWriteEffectSet().contains(effected)) {
				logWarning(SecurityMessages.superfluousWriteEffect(
						methodSignature, effected));
			}
		}
	}

	/**
	 * Method that returns the source line number of the currently handled
	 * statement. Note, that immediately after the initialization of this
	 * environment and before the start of flow analysis, the method will return
	 * {@code 0}.
	 * 
	 * @return The source line number of the current handled statement or if
	 *         this wasn't set {@code 0}.
	 */
	public long getSrcLn() {
		return srcLn;
	}

	/**
	 * Method that returns the current handled {@link Stmt} of the analyzed
	 * method. Note, that immediately after the initialization of this
	 * environment and before the start of flow analysis, the method will return
	 * {@code null}.
	 * 
	 * @return The current handled statement or if this wasn't set {@code null}.
	 */
	public Stmt getStmt() {
		return stmt;
	}

	/**
	 * Logs the given message as a <em>side effect</em> together with the given
	 * source line number. The file name is created by the analyzed
	 * {@link SootMethod}, which this environment stores (see
	 * {@link MethodEnvironment#sootMethod}).
	 * 
	 * @param srcLn
	 *            Source line number at which the given <em>side effect</em>
	 *            message is generated.
	 * @param msg
	 *            Information about the effect that should be printed as a
	 *            <em>side effect</em>.
	 * @see SecurityLogger#effect(String, long, String)
	 */
	public void logEffect(long srcLn, String msg) {
		getLog().effect(SootUtils.generateFileName(getSootMethod()), srcLn, msg);
	}

	/**
	 * Sets the current handled {@link Stmt} of the analyzed method. This
	 * handled statement will be used for logging. At the same time the method
	 * will also determine the source line number of the currently handled
	 * statement.
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
