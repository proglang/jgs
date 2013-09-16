package utils;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.tagkit.SourceLnPosTag;

/**
 * Class, which offers various methods, which are in relation with Soot.
 * 
 * @author Thomas Vogel
 * @version 0.1
 * 
 */
public class SootUtils {
	
	/**	 */
	private static final String CLINIT_MEHTOD_NAME = "<clinit>";
	/**	 */
	private static final String INIT_MEHTOD_NAME = "<init>";

	/**
	 * Extracts the source line number from a statement, if this statement has the corresponding
	 * annotation. If there exists no annotation the method will return 0 as the line number.
	 * 
	 * @param statement
	 *            Statement for which the source line number should be extracted.
	 * @return The source line number of the statement. If the statement has no annotation the
	 *         method will return 0.
	 */
	public static long extractLineNumberFrom(Stmt statement) {
		if (statement.hasTag("SourceLnPosTag")) {
			SourceLnPosTag sourceLnPosTag = (SourceLnPosTag) statement.getTag("SourceLnPosTag");
			return (sourceLnPosTag != null) ? Long.valueOf(sourceLnPosTag.startLn()) : 0;
		}
		return 0;
	}

	/**
	 * Creates a human readable method name for a SootMethod.
	 * 
	 * @param sootMethod
	 *            The SootMethod for which a human readable method name should be created.
	 * @return The human readable method name of the given SootMethod.
	 */
	public static String generateReadableMethodNameFrom(SootMethod sootMethod) {
		String[] methodSubSignature = sootMethod.getSubSignature().split(" ");
		String methodName = (methodSubSignature.length == 2) ? methodSubSignature[1].replace("<",
				"").replace(">", "") : "unknown";
		return sootMethod.getDeclaringClass().getName() + "." + methodName;
	}

	/**
	 * Creates a readable method signature for a SootMethod.
	 * 
	 * @param sootMethod
	 *            The SootMethod for which a method signature should be created.
	 * @return Readable method signature of the given SootMethod.
	 */
	public static String generateMethodSignature(SootMethod sootMethod) {
		String[] methodSubSignature = sootMethod.getSubSignature().split(" ");
		String methodName = (methodSubSignature.length == 2) ? methodSubSignature[1].replace("<",
				"").replace(">", "") : "unknown";
		String methodType = (methodSubSignature.length == 2) ? methodSubSignature[0] : "unknown";
		return sootMethod.getDeclaringClass().getName() + "." + methodName + " : " + methodType;
	}
	
	/**
	 * 
	 * @param sootField
	 * @return
	 */
	public static String generateFieldSignature(SootField sootField) {
		return sootField.getSignature();
	}
	
	/**
	 * 
	 * @param sootMethod
	 * @return
	 */
	public static String generateFileName(SootMethod sootMethod) {
		return generateFileName(sootMethod.getDeclaringClass());
	}
	
	/**
	 * 
	 * @param sootClass
	 * @return
	 */
	public static String generateFileName(SootClass sootClass) {
		String className = sootClass.getShortName();
		String[] classNameComponents = className.split("\\$");
		if (classNameComponents.length > 0) {
			return classNameComponents[0];
		} else {
			return "unknown";
		}
	}
	
	/**
	 * 
	 * @param sootMethod
	 * @return
	 */
	public static boolean isClinitMethod(SootMethod sootMethod) {
		return sootMethod.isEntryMethod() && sootMethod.getName().equals(CLINIT_MEHTOD_NAME);
	}
	
	/**
	 * 
	 * @param sootMethod
	 * @return
	 */
	public static boolean isInitMethod(SootMethod sootMethod) {
		return sootMethod.isConstructor() && sootMethod.getName().equals(INIT_MEHTOD_NAME);
	}
	
}
