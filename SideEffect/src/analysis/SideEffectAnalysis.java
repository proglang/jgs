package analysis;

import java.util.*;

import db.EffectsWrapperStorage;
import db.EffectsWrapperStorage.*;

import logging.*;
import model.*;
import effect.EffectAnnotation;

import soot.*;
import soot.jimple.*;
import soot.jimple.internal.*;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import utils.*;

/**
 * 
 * @author Thomas Vogel
 * @version 0.3
 *
 */
public class SideEffectAnalysis extends ForwardFlowAnalysis<Unit, Set<Local>> {
	public static final List<String> EFFECT_IDS = EffectAnnotation
			.getListOfEffectIDs();
	private static boolean CHECK_CLASSES = true;
	private static boolean IS_CLINIT = false;
	private SideEffectLogger log;
	private EffectsWrapperStorage calculatedEffectsStorage = new EffectsWrapperStorage();
	private MethodAnnotation expectedMethodEffects;
	private ClassAnnotation expectedClassEffects;

	public SideEffectAnalysis(DirectedGraph<Unit> graph, SootMethod sootMethod,
			SideEffectLogger log, boolean checkClasses) {
		super(graph);
		this.log = log;
		IS_CLINIT = sootMethod.getName().contains(SootMethod.staticInitializerName) && sootMethod.isEntryMethod();
		CHECK_CLASSES = checkClasses || IS_CLINIT;
		this.expectedMethodEffects = new MethodAnnotation(sootMethod, 0, this.log);
		this.expectedClassEffects = new ClassAnnotation(sootMethod.getDeclaringClass(), 0, this.log);
		doAnalysis();
	}

	/**
	 * Calls all the necessary methods that check whether the specified effect annotations match the
	 * calculated effects. In the case that required effects have not been specified in the
	 * annotations, an error message is outputted via the logging framework. Also information will
	 * be displayed such as which effects are superfluous and which methods / classes are involved
	 * having not all effect annotations.
	 * 
	 * 
	 * @return {@code true} if one of the check methods outputs a warning, {@code false} otherwise.
	 */
	public boolean checkAnnotation() {
		boolean warnings = false;
		// Are all annotations available for the current method
		warnings = checkPresenceOfAnnotationsForCurrentMethod() || warnings;
		// Contain the annotations all calculated side effects?
		warnings = checkDifferenceCalculatedExpected() || warnings;
		// Contain the annotations more side effects than needed?
		checkDifferenceExpectedCalculated();
		// Check: invoked Method isLibrary? if not are all annotations available
		warnings = checkInvokedMethods() || warnings;
		// Check: used Class isLibrary? if not are all annotations available
		warnings = checkUsedClasses() || warnings;
		return warnings;
	}

	/**
	 * Checks whether the invoked methods (
	 * {@link EffectsWrapperStorage#getEffectedMethodAnnotations()} in
	 * {@link SideEffectAnalysis#calculatedEffectsStorage}) are methods of the Java library, and if
	 * not, whether these methods have the required effect annotations. If a Java library method is
	 * called, an informational message is outputted via the logging framework. However, if a method
	 * is called with missing effect annotations that is not a Java library method, a warning
	 * message is issued.
	 * 
	 * @return {@code true} if an annotation is missing at any method, otherwise {@code false}.
	 */
	private boolean checkInvokedMethods() {
		boolean warnings = false;
		for (MethodAnnotation methodAnnotation : this.calculatedEffectsStorage
				.getEffectedMethodAnnotations()) {
			SootMethod method = this.expectedMethodEffects.getSootMethod();
			SootMethod invokedMethod = methodAnnotation.getSootMethod();
			if (methodAnnotation.isLibrary()) {
				log.warning(
						SootUtils.generateFileName(method),
						methodAnnotation.getSourceLine(),
						Messages.invokeOfLibraryMethod(method,
								methodAnnotation.getSourceLine(), invokedMethod));
			} else {
				for (String effectID : EFFECT_IDS) {
					if (!methodAnnotation.getPresentAnnotations().contains(effectID)) {
						log.error(
								SootUtils.generateFileName(method),
								methodAnnotation.getSourceLine(),
								Messages.missingInvokedMethodAnnotation(
								method,
								methodAnnotation.getSourceLine(), invokedMethod,
								effectID));
						warnings = true;
					}
				}
			}
		}
		return warnings;
	}
	
	/**
	 * Checks whether the used classes (
	 * {@link EffectsWrapperStorage#getEffectedClassAnnotations()} in
	 * {@link SideEffectAnalysis#calculatedEffectsStorage}) are classes of the Java library, and if
	 * not, whether these classes have the required effect annotations. If a Java library class is
	 * used, an informational message is outputted via the logging framework. However, if a class
	 * is used with missing effect annotations that is not a Java library class, a warning
	 * message is issued.
	 * 
	 * @return {@code true} if an annotation is missing at any class, otherwise {@code false}.
	 */
	private boolean checkUsedClasses() {
		boolean warnings = false;
		for (ClassAnnotation classAnnotation : this.calculatedEffectsStorage
				.getEffectedClassAnnotations()) {
			if (classAnnotation.isLibrary()) {
				SootMethod sootMethod = this.expectedMethodEffects.getSootMethod();
				log.warning(SootUtils.generateFileName(sootMethod), classAnnotation.getSourceLine(), Messages.useOfLibraryClass(sootMethod,
						classAnnotation.getSourceLine(), classAnnotation.getSootClass()));
			} else {
				for (String effectID : EFFECT_IDS) {
					if (!classAnnotation.getPresentAnnotations().contains(effectID)) {
						SootMethod sootMethod = this.expectedMethodEffects.getSootMethod();
						log.error(SootUtils.generateFileName(sootMethod), classAnnotation.getSourceLine(), Messages.missingUsedClassAnnotation(
								sootMethod,
								classAnnotation.getSourceLine(), classAnnotation.getSootClass(),
								effectID));
						warnings = true;
					}
				}
			}
		}
		return warnings;
	}

	/**
	 * Checks whether the sets of effects given by the effect annotations (
	 * {@link SideEffectAnalysis#expectedMethodEffects} /
	 * {@link SideEffectAnalysis#expectedClassEffects}) for the current method define more effects
	 * than calculated ({@link SideEffectAnalysis#calculatedEffectsStorage}) (=> Expected \
	 * Calculated = {x | x in Expected && x not in Calculated}). When the current method handles the
	 * initialization of the class, so the annotations of the class are examined, in all other
	 * cases, the annotations of the method. When an effect specified by the annotation wasn't
	 * calculated during the analysis an information about this fact is outputted via the logging
	 * framework.
	 */
	private void checkDifferenceExpectedCalculated() {
		for (String effectID : EFFECT_IDS) {
			Set<String> expected = IS_CLINIT ? this.expectedClassEffects
					.getEffectsMapSetWith(effectID) : this.expectedMethodEffects
					.getEffectsMapSetWith(effectID);
			Set<String> calculated = this.calculatedEffectsStorage.getEffectSet(effectID);
			Set<String> conterDifference = GeneralUtils.calculateDifferenceOf(expected, calculated);
			for (String effect : conterDifference) {
				if (IS_CLINIT) {
					SootClass sootClass = this.expectedClassEffects.getSootClass();
					log.information(SootUtils.generateFileName(sootClass), 0, Messages.uselessClassEffect(this.expectedClassEffects.getSootClass(),
							effect, effectID));
				} else {
					SootMethod method = this.expectedMethodEffects.getSootMethod();
					long sourceLine = this.expectedMethodEffects.getSourceLine();
					log.information(SootUtils.generateFileName(method), sourceLine, Messages.uselessMethodEffect(
							method, effect, effectID));
				}
			}
		}

	}

	/**
	 * Checks whether the sets of calculated effects (
	 * {@link SideEffectAnalysis#calculatedEffectsStorage}) for the current method define more
	 * effects than the given effects by the effect annotations (
	 * {@link SideEffectAnalysis#expectedMethodEffects} /
	 * {@link SideEffectAnalysis#expectedClassEffects}) (=> Calculated \ Expected = {x | x in
	 * Calculated && x not in Expected}). When the current method handles the initialization of the
	 * class, so the annotations of the class are examined, in all other cases, the annotations of
	 * the method. When an calculated effect isn't specified by the annotations a warning about this
	 * fact is outputted via the logging framework.
	 * 
	 * @return {@code true} if an effect is missing at the method, otherwise {@code false}.
	 */
	private boolean checkDifferenceCalculatedExpected() {
		boolean warnings = false;
		for (String effectID : EFFECT_IDS) {
			Set<String> expected = IS_CLINIT ? this.expectedClassEffects
					.getEffectsMapSetWith(effectID) : this.expectedMethodEffects
					.getEffectsMapSetWith(effectID);
			Set<String> calculated = this.calculatedEffectsStorage.getEffectSet(effectID);
			Set<String> difference = GeneralUtils.calculateDifferenceOf(calculated, expected);
			for (String effected : difference) {
				List<Effect> list = this.calculatedEffectsStorage.getEffects(effectID, effected);
				for (Effect effect : list) {
					if (IS_CLINIT) {
						SootClass sootClass = this.expectedClassEffects.getSootClass();
						log.sideeffect(SootUtils.generateFileName(sootClass),
								effect.getSourceLine(),
								Messages.missingEffectOfClass(sootClass, effect, effectID));
					} else {
						SootMethod sootMethod = this.expectedMethodEffects.getSootMethod();
						log.sideeffect(SootUtils.generateFileName(sootMethod), effect.getSourceLine(), Messages.missingEffectOfMethod(
								sootMethod, effect, effectID));
					}
					warnings = true;
				}
			}
		}
		return warnings;
	}

	/**
	 * Checks whether the current method was labeled with all the necessary effect annotations. If
	 * the current method is the instantiation of the class, the available annotations of the class
	 * are checked ( {@link SideEffectAnalysis#expectedClassEffects}), otherwise the available
	 * annotations of the method ({@link SideEffectAnalysis#expectedMethodEffects}). For each
	 * missing annotation, an warning message is outputted via the logging framework.
	 * 
	 * @return {@code true} if an annotation is missing, otherwise {@code false}.
	 */
	private boolean checkPresenceOfAnnotationsForCurrentMethod() {
		boolean warnings = false;
		for (String effectID : EFFECT_IDS) {
			if (IS_CLINIT) {
				if (! this.expectedClassEffects.getPresentAnnotations().contains(effectID)) {
					SootClass sootClass = this.expectedClassEffects.getSootClass();
					log.error(SootUtils.generateFileName(sootClass), 0, Messages.missingClassAnnotation(effectID,
							sootClass));
					warnings = true;
				}
			} else {
				if (!this.expectedMethodEffects.getPresentAnnotations().contains(effectID)) {
					SootMethod sootMethod = this.expectedMethodEffects.getSootMethod();
					log.error(SootUtils.generateFileName(sootMethod), this.expectedMethodEffects.getSourceLine(), Messages.missingMethodAnnotation(effectID,
							sootMethod));
					warnings = true;
				}
			}
		}
		return warnings;
	}

	/**
	 * 
	 * @see soot.toolkits.scalar.FlowAnalysis#flowThrough(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void flowThrough(Set<Local> in, Unit d, Set<Local> out) {
		Stmt statement = (Stmt) d;
		long sourceLine = SootUtils.extractLn(statement);
		// -> Stores the new effects:
		// if Stmt is JAssignStmt and rhs contains JNewExpr so the type is effected.
		if (statement instanceof JAssignStmt) {
			JAssignStmt jAssignStmt = (JAssignStmt) statement;
			if (jAssignStmt.getRightOp() instanceof JNewExpr) {
				JNewExpr jNewExpr = (JNewExpr) jAssignStmt.getRightOp();
				Effect effect = new Effect(jNewExpr.getType().toString(), sourceLine,
						new NewCause(jNewExpr.getType()));
				this.calculatedEffectsStorage.addNewEffect(effect);
			}

		}
		// -> Stores, if the Stmt contains an InvokeExpr following:
		// 1. Read effect to the class which declares the method (method lookup).
		// 2. Read/Write/New effects of the invoked method.
		// (???) 3. Read/Write/New effects of the class which declares the method.
		// => Caused by <clinit>() <≠> <clinit>() is check for each class
		// Additionally storing the effected mehtod and class for checking whether all annotations
		// are available.
		if (statement.containsInvokeExpr()) {
			InvokeExpr invokeExpr = statement.getInvokeExpr();
			SootMethod invokedMethod = invokeExpr.getMethod();
			SootClass declaringClass = invokedMethod.getDeclaringClass();
			if (invokeExpr instanceof VirtualInvokeExpr) {
				// 1. Read effect to the class which declares the method (method lookup).
				Effect readEffectToClass = new Effect(declaringClass.getName(), sourceLine,
						new MethodCause(invokedMethod));
				this.calculatedEffectsStorage.addReadEffect(readEffectToClass);
			} else if (invokeExpr instanceof StaticInvokeExpr
					|| (invokeExpr instanceof SpecialInvokeExpr && invokedMethod.getName()
							.contains(SootMethod.constructorName))) {
				if (CHECK_CLASSES) {
					// 3. Read/Write/New effects of the class which declares the method. (???)
					// Static, special && <init>() => Java Spec!!! Class initialization
					ClassAnnotation classAnnotations = new ClassAnnotation(declaringClass,
							sourceLine, this.log);
					for (String effectID : EFFECT_IDS) {
						Set<String> assumedEffects = classAnnotations
								.getEffectsMapSetWith(effectID);
						for (String assumedEffect : assumedEffects) {
							Effect assumedEffectOfMethod = new Effect(assumedEffect, sourceLine,
									new ClassCause(declaringClass));
							this.calculatedEffectsStorage
									.addEffect(effectID, assumedEffectOfMethod);
						}
					}
					this.calculatedEffectsStorage.storeEffectedClass(classAnnotations);
				}
			}
			// 2. Read/Write/New effects of the invoked method.
			MethodAnnotation methodAnnotations = new MethodAnnotation(invokedMethod, sourceLine, this.log);
			for (String effectID : EFFECT_IDS) {
				Set<String> assumedEffects = methodAnnotations.getEffectsMapSetWith(effectID);
				for (String assumedEffect : assumedEffects) {
					Effect assumedEffectOfMethod = new Effect(assumedEffect, sourceLine,
							new MethodCause(invokedMethod));
					this.calculatedEffectsStorage.addEffect(effectID, assumedEffectOfMethod);
				}
			}
			this.calculatedEffectsStorage.storeEffectedMethod(methodAnnotations);
		}
		// -> Store read or write effect if the Stmt contains a reference to a field
		// 1. If reference is part of the UseBoxes: Read effect
		// 2. If reference is part of the DefBoxes: Write effect
		// (???) Is there a reason to store the effects of the class?
		// => read: initialize the static variables = yes
		// => write: no need to initialize those variables = no
		// ===> nur bei static read / write nachschauen...
		// (???) In case that one of the both questions is yes: add class as effected class...
		if (statement.containsFieldRef()) {
			ValueBox referenceBox = statement.getFieldRefBox();
			boolean containedByDefBoxes = statement.getDefBoxes().contains(referenceBox);
			boolean containedByUseBoxes = statement.getUseBoxes().contains(referenceBox);
			FieldRef reference = statement.getFieldRef();
			SootField field = reference.getField();
			SootClass declaringClass = field.getDeclaringClass();
			if (containedByDefBoxes && !containedByUseBoxes) { // 2.
				Effect writeEffect = new Effect(declaringClass.getName(), sourceLine,
						new AssignCause(field));
				this.calculatedEffectsStorage.addWriteEffect(writeEffect);
			} else if (!containedByDefBoxes && containedByUseBoxes) { // 1.
				Effect readEffect = new Effect(declaringClass.getName(), sourceLine, new ReferenceCause(
						field));
				this.calculatedEffectsStorage.addReadEffect(readEffect);
			}
			if (field.isStatic() && !field.isFinal()  && ! IS_CLINIT) {
				if (CHECK_CLASSES) {
					ClassAnnotation classOfInvoke = new ClassAnnotation(declaringClass, sourceLine, this.log);
					for (String effectID : EFFECT_IDS) {
						Set<String> effects = classOfInvoke.getEffectsMapSetWith(effectID);
						for (String effect : effects) {
							this.calculatedEffectsStorage.addEffect(effectID, new Effect(effect,
									sourceLine, new ClassCause(declaringClass)));
						}
					}
					this.calculatedEffectsStorage.storeEffectedClass(classOfInvoke);
				}
			}
		}
	}

	/**
	 * TODO
	 * @see soot.toolkits.scalar.AbstractFlowAnalysis#newInitialFlow()
	 */
	@Override
	protected Set<Local> newInitialFlow() {
		return new HashSet<Local>();
	}

	/**
	 * TODO
	 * @see soot.toolkits.scalar.AbstractFlowAnalysis#entryInitialFlow()
	 */
	@Override
	protected Set<Local> entryInitialFlow() {
		return new HashSet<Local>();
	}

	/**
	 * TODO
	 * @see soot.toolkits.scalar.AbstractFlowAnalysis#merge(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void merge(Set<Local> in1, Set<Local> in2, Set<Local> out) {
		copy(in1, out);
		out.addAll(in2);
	}

	/**
	 * TODO
	 * @see soot.toolkits.scalar.AbstractFlowAnalysis#copy(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void copy(Set<Local> source, Set<Local> dest) {
		dest.clear();
		dest.addAll(source);
	}

}
