package utils;

import java.util.*;

import soot.*;
import soot.jimple.*;
import soot.tagkit.*;

/**
 * Class, which offers various methods, which are in relation with Soot.
 * 
 * @author Thomas Vogel
 * @version 0.2
 * 
 */
public class SootUtils {

	private static final String TAG_SOURCE_LINE = "SourceLnPosTag";

	/**
	 * Extracts the source line number from a statement, if this statement has the corresponding
	 * annotation. If there exists no annotation the method will return 0 as the line number.
	 * 
	 * @param statement
	 *            Statement for which the source line number should be extracted.
	 * @return The source line number of the statement. If the statement has no annotation the
	 *         method will return 0.
	 */
	public static long extractLn(Stmt statement) {
		if (statement.hasTag(TAG_SOURCE_LINE)) {
			SourceLnPosTag sourceLnPosTag = (SourceLnPosTag) statement.getTag(TAG_SOURCE_LINE);
			return (sourceLnPosTag != null) ? Long.valueOf(sourceLnPosTag.startLn()) : 0;
		}
		return 0;
	}

	/**
	 * Creates a readable method signature for a SootMethod.
	 * 
	 * @param sootMethod
	 *            The SootMethod for which a method signature should be created.
	 * @return Readable method signature of the given SootMethod.
	 */
	public static String generateMethodSignature(SootMethod sootMethod, boolean printPackage, boolean printType, boolean printVisibility) {
		String methodName = sootMethod.getName();
		String classOfMethod = generateClassSignature(sootMethod.getDeclaringClass(), printPackage);
		String parameters = "";
		for (int i = 0; i < sootMethod.getParameterCount(); i++) {
			Type type = sootMethod.getParameterType(i);
			if (! parameters.equals("")) parameters += ", ";
			parameters += ("arg" + i + (printType ? (" : " + generateTypeName(type)) : ""));
		}
		String methodTypeName = generateTypeName(sootMethod.getReturnType());
		String methodVisibility = generateVisibility(sootMethod.isPrivate(), sootMethod.isProtected(), sootMethod.isPublic());
		return classOfMethod + "." + methodName + "(" + parameters + ")" + (printType ? " : " + methodTypeName : "") + (printVisibility ? " [" + methodVisibility + "]" : "");
	}
	
	/**
	 * 
	 * @param sootField
	 * @return
	 */
	public static String generateFieldSignature(SootField sootField, boolean printPackage, boolean printType, boolean printVisibility) {
		String fieldName = sootField.getName();
		String classOfField = generateClassSignature(sootField.getDeclaringClass(), printPackage);
		String fieldTypeName = generateTypeName(sootField.getType());
		String fieldVisibility = generateVisibility(sootField.isPrivate(), sootField.isProtected(), sootField.isPublic());
		return classOfField + "." + fieldName + (printType ? " : " + fieldTypeName : "") + (printVisibility ? " [" + fieldVisibility + "]" : "");
	}
	
	/**
	 * 
	 * @param sootClass
	 * @param printPackage
	 * @return
	 */
	public static String generateClassSignature(SootClass sootClass, boolean printPackage) {
		return printPackage ? sootClass.getName() : sootClass.getShortName();
	}
	
	/**
	 * 
	 * @param isPrivate
	 * @param isProtected
	 * @param isPublic
	 * @return
	 */
	private static String generateVisibility(boolean isPrivate, boolean isProtected, boolean isPublic) {
		return (isPrivate ? "-" : (isProtected ? "#" : (isPublic ? "+" : "?")));
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	private static String generateTypeName(Type type) {
		return type.toString();
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
		return sootMethod.isEntryMethod() && sootMethod.getName().equals(SootMethod.staticInitializerName);
	}
	
	/**
	 * 
	 * @param sootMethod
	 * @return
	 */
	public static boolean isInitMethod(SootMethod sootMethod) {
		return sootMethod.isConstructor() && sootMethod.getName().equals(SootMethod.constructorName);
	}
	
	/**
	 * 
	 * @param type
	 * @param tag
	 * @return
	 */
	public static List<String> extractAnnotationStringArray(String type, List<Tag> tags) {
		List<String> array = new ArrayList<String>();
		boolean annotationsAvailable  = false;
		for (Tag tag : tags) {
			if (tag instanceof VisibilityAnnotationTag) {
				VisibilityAnnotationTag visibilityAnnotationTag = (VisibilityAnnotationTag) tag;
				for (AnnotationTag  annotationTag : visibilityAnnotationTag.getAnnotations()) {
					if (annotationTag.getType().equals(type)) {
						annotationsAvailable = true;
						for (int i = 0; i < annotationTag.getNumElems(); i++) {
							AnnotationElem annotationElem =  annotationTag.getElemAt(i);
							if (annotationElem.getKind() == "[".charAt(0)) {
								AnnotationArrayElem annotationArrayElem = (AnnotationArrayElem) annotationElem;
								for (int j = 0; j < annotationArrayElem.getNumValues(); j++) {
									AnnotationElem annotationElem1 = annotationArrayElem.getValueAt(j);
									if (annotationElem1.getKind() == "s".charAt(0)) {
										AnnotationStringElem annotationStringElem = (AnnotationStringElem) annotationElem1;
										array.add(annotationStringElem.getValue());
									}
								}
							}
						}
					}
				}
			}
		}
		if (annotationsAvailable) {
			return array;
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @param type
	 * @param tags
	 * @return
	 */
	public static String extractAnnotationString(String type, List<Tag> tags) {
		String string = null;
		boolean annotationsAvailable  = false;
		for (Tag tag : tags) {
			if (tag instanceof VisibilityAnnotationTag) {
				VisibilityAnnotationTag visibilityAnnotationTag = (VisibilityAnnotationTag) tag;
				for (AnnotationTag  annotationTag : visibilityAnnotationTag.getAnnotations()) {
					if (annotationTag.getType().equals(type)) {
						annotationsAvailable  = true;
						for (int i = 0; i < annotationTag.getNumElems(); i++) {
							AnnotationElem annotationElem =  annotationTag.getElemAt(i);
							if (annotationElem.getKind() == "s".charAt(0)) {
								AnnotationStringElem annotationStringElem = (AnnotationStringElem) annotationElem;
								string = annotationStringElem.getValue();
							}
						}
					}
				}
			}
		}
		if (annotationsAvailable) {
			return string;
		} else {
			return null;
		}
	}
	
}
