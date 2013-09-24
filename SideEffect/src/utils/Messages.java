package utils;

import db.EffectsWrapperStorage.*;
import soot.SootClass;
import soot.SootMethod;
import effect.EffectAnnotation;

/**
 * Class that handles the generation of various error messages.
 * 
 * @author Thomas Vogel
 * @version 0.1
 * 
 */
public class Messages {

	private static final String MISSING_CLASS_EFFECT = "At class %s a %s to %s is missing which is caused at source line %d by the %s";
	private static final String MISSING_METHOD_EFFECT = "At method %s a %s to %s is missing which is caused at source line %d by the %s";
	private static final String MISSING_CLASS_ANNOTATION = "Missing %s annotation at class %s";
	private static final String MISSING_METHOD_ANNOTATION = "Missing %s annotation at method %s";
	private static final String USELESS_CLASS_EFFECT = "For class %s exists a %s to %s which is maybe superfluous";
	private static final String USELESS_METHOD_EFFECT = "For method %s exists a %s to %s which is maybe superfluous";
	private static final String MISSING_INVOKED_METHOD_ANNOTATION = "Inside method %s at source line %d the method %s is called which has no %s annotation";
	private static final String JAVA_LIBRARY_METHOD_INVOKED = "The method %s invokes the java library method %s at source line %d which has no effect annotations";
	private static final String MISSING_USED_CLASS_ANNOTATION = "In method %s at source line %d the class %s is used which has no %s annotation";
	private static final String JAVA_LIBRARY_CLASS_USED = "The method %s uses the java library class %s at source line %d which has no effect annotations";

	/**
	 * Generates the error message caused by a missing annotation for a given effect identifier and
	 * a given class.
	 * 
	 * @param effectID
	 *            The type identifier of the effect.
	 * @param sootClass
	 *            The SootClass at which the annotation is missing.
	 * @return The error message containing the information which effect type is missing at which
	 *         class.
	 */
	public static String missingClassAnnotation(String effectID, SootClass sootClass) {
		String readableEffectType = EffectAnnotation.readableNameOf(effectID);
		String readableClassName = sootClass.getName();
		return String.format(MISSING_CLASS_ANNOTATION, readableEffectType, readableClassName);
	}

	/**
	 * Generates the error message caused by a missing annotation for a given effect identifier and
	 * a given method.
	 * 
	 * @param effectID
	 *            The type identifier of the effect.
	 * @param sootMethod
	 *            The SootMethod at which the annotation is missing.
	 * @return The error message containing the information which effect type is missing at which
	 *         method.
	 */
	public static String missingMethodAnnotation(String effectID, SootMethod sootMethod) {
		String readableEffectType = EffectAnnotation.readableNameOf(effectID);
		String readableMethodName = SootUtils.generateMethodSignature(sootMethod, false, true, true);
		return String.format(MISSING_METHOD_ANNOTATION, readableEffectType, readableMethodName);
	}

	/**
	 * Generates the error message caused by the invocation of a given Java library method inside
	 * the given method at the given source line.
	 * 
	 * @param methodUnderCheck
	 *            SootMethod in which the Java library method is called.
	 * @param sourceLine
	 *            Line number at which the Java library method is called.
	 * @param invokedMethod
	 *            Java library method which is invoked.
	 * @return The error message containing the information which method calls at which line which
	 *         Java library method.
	 */
	public static String invokeOfLibraryMethod(SootMethod methodUnderCheck, long sourceLine,
			SootMethod invokedMethod) {
		String readableMethodUnderCheckName = SootUtils
				.generateMethodSignature(methodUnderCheck, false, true, true);
		String readableInvokedMethodName = SootUtils.generateMethodSignature(invokedMethod, false, true, true);
		return String.format(JAVA_LIBRARY_METHOD_INVOKED, readableMethodUnderCheckName,
				readableInvokedMethodName, sourceLine);
	}

	/**
	 * Generates the error message caused by the invocation of a given method inside an other given
	 * method at the given source line which misses an effect annotation of given effect type.
	 * 
	 * @param methodUnderCheck
	 *            SootMethod in which the other method is called.
	 * @param sourceLine
	 *            Line number at which the other method is called.
	 * @param invokedMethod
	 *            Method which is invoked.
	 * @param effectID
	 *            The type identifier of the missing effect.
	 * @return The error message containing the information which method calls at which line which
	 *         method that misses an specific effect annotation.
	 */
	public static String missingInvokedMethodAnnotation(SootMethod methodUnderCheck,
			long sourceLine, SootMethod invokedMethod, String effectID) {
		String readableMethodUnderCheckName = SootUtils
				.generateMethodSignature(methodUnderCheck, false, true, true);
		String readableInvokedMethodName = SootUtils.generateMethodSignature(invokedMethod, false, true, true);
		String readableEffectType = EffectAnnotation.readableNameOf(effectID);
		return String.format(MISSING_INVOKED_METHOD_ANNOTATION, readableMethodUnderCheckName,
				sourceLine, readableInvokedMethodName, readableEffectType);
	}

	/**
	 * Generates the error message caused by an useless given effect in the annotation with the
	 * given identifier at the given class.
	 * 
	 * @param sootClass
	 *            The SootClass at which the annotation is redundant.
	 * @param effect
	 *            The effect, which is unnecessary.
	 * @param effectID
	 *            The type identifier of the effect.
	 * @return The error message containing the information which effect of which effect type is
	 *         useless at which class.
	 */
	public static String uselessClassEffect(SootClass sootClass, String effect, String effectID) {
		String readableEffectType = EffectAnnotation.readableNameOf(effectID);
		String readableClassName = sootClass.getName();
		return String.format(USELESS_CLASS_EFFECT, readableClassName, readableEffectType, effect);
	}

	/**
	 * Generates the error message caused by an useless given effect in the annotation with the
	 * given identifier at the given method.
	 * 
	 * @param sootMethod
	 *            The SootMethod at which the annotation is redundant.
	 * @param effect
	 *            The effect, which is unnecessary.
	 * @param effectID
	 *            The type identifier of the effect.
	 * @return The error message containing the information which effect of which effect type is
	 *         useless at which method.
	 */
	public static String uselessMethodEffect(SootMethod sootMethod, String effect, String effectID) {
		String readableEffectType = EffectAnnotation.readableNameOf(effectID);
		String readableMethodName = SootUtils.generateMethodSignature(sootMethod, false, true, true);
		return String.format(USELESS_METHOD_EFFECT, readableMethodName, readableEffectType, effect);
	}

	/**
	 * Generates the error message caused by a given effect with given effect identifier at the
	 * given class which is missing.
	 * 
	 * @param sootClass
	 *            The SootClass at which the annotation is missing.
	 * @param effect
	 *            The effect, which is missing.
	 * @param effectID
	 *            The type identifier of the effect.
	 * @return The error message containing the information which effect of which effect type is
	 *         missing at which class, as well as the cause.
	 */
	public static String missingEffectOfClass(SootClass sootClass, Effect effect, String effectID) {
		String readableEffectType = EffectAnnotation.readableNameOf(effectID);
		String readableClassName = sootClass.getName();
		long sourceLine = effect.getSourceLine();
		String effected = effect.getEffected();
		Cause cause = effect.getCause();
		return String.format(MISSING_CLASS_EFFECT, readableClassName, readableEffectType, effected,
				sourceLine, cause.getCauseString());
	}

	/**
	 * Generates the error message caused by a given effect with given effect identifier at the
	 * given method which is missing.
	 * 
	 * @param sootMethod
	 *            The SootMethod at which the annotation is missing.
	 * @param effect
	 *            The effect, which is missing.
	 * @param effectID
	 *            The type identifier of the effect.
	 * @return The error message containing the information which effect of which effect type is
	 *         missing at which method, as well as the cause.
	 */
	public static String missingEffectOfMethod(SootMethod sootMethod, Effect effect, String effectID) {
		String readableEffectType = EffectAnnotation.readableNameOf(effectID);
		String readableMethodName = SootUtils.generateMethodSignature(sootMethod, false, true, true);
		long sourceLine = effect.getSourceLine();
		String effected = effect.getEffected();
		Cause cause = effect.getCause();
		return String.format(MISSING_METHOD_EFFECT, readableMethodName, readableEffectType,
				effected, sourceLine, cause.getCauseString());
	}

	/**
	 * Generates the error message caused by the usage of a given Java library class inside the
	 * given method at the given source line.
	 * 
	 * @param sootMethod
	 *            SootMethod in which the Java library class is used.
	 * @param sourceLine
	 *            Line number at which the Java library class is used.
	 * @param sootClass
	 *            Java library class which is used.
	 * @return The error message containing the information which method uses at which line which
	 *         Java library class.
	 */
	public static String useOfLibraryClass(SootMethod sootMethod, long sourceLine,
			SootClass sootClass) {
		String readableMethodName = SootUtils.generateMethodSignature(sootMethod, false, true, true);
		String readableClassName = sootClass.getName();
		return String.format(JAVA_LIBRARY_CLASS_USED, readableMethodName, readableClassName,
				sourceLine);
	}

	/**
	 * Generates the error message caused by the usage of a given class inside a given method at the
	 * given source line which misses an effect annotation of given effect type.
	 * 
	 * @param sootMethod
	 *            SootMethod in which the Java library class is used.
	 * @param sourceLine
	 *            Line number at which the Java library class is used.
	 * @param sootClass
	 *            Java library class which is used.
	 * @param effectID
	 *            The type identifier of the missing effect.
	 * @return The error message containing the information which method uses at which line which
	 *         class that misses an specific effect annotation.
	 */
	public static String missingUsedClassAnnotation(SootMethod sootMethod, long sourceLine,
			SootClass sootClass, String effectID) {
		String readableMethodName = SootUtils.generateMethodSignature(sootMethod, false, true, true);
		String readableClassName = sootClass.getName();
		String readableEffectType = EffectAnnotation.readableNameOf(effectID);
		return String.format(MISSING_USED_CLASS_ANNOTATION, readableMethodName, sourceLine,
				readableClassName, readableEffectType);
	}
}
