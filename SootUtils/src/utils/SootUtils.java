package utils;

import java.util.ArrayList;
import java.util.List;

import soot.Modifier;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.VoidType;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.SourceLnPosTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.util.Chain;

/**
 * <h1>Utilities for the Soot framework</h1>
 * 
 * The class {@link SootUtils} offers various methods, which are in relation to the Soot tool.
 * Mostly, the provided methods handle the generation of class, method and field signature or handle
 * the extraction of annotations, e.g. the line number, a String of specific annotation type or a
 * String array of specific annotation type. Also, there are methods which check whether a
 * SootMethod is a static initializer or a constructor.
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 * 
 */
public class SootUtils {

	/** Soot identifier for the SourceLnPosTag annotation (see {@link SourceLnPosTag}). */
	private static final String TAG_SOURCE_LINE = "SourceLnPosTag";

	/**
	 * Extracts the String value of the annotation with the given annotation type from the given
	 * list of annotations. If no annotation of the given type which stores a String value is
	 * contained by the given list of tags, the method will return {@code null}. Note, that the type
	 * of annotation has a specific format: '{@code L}packageName{@code /}className{@code ;}' or '
	 * {@code L}packageName{@code /}parentClassName{@code $}className{@code ;}'. Also note that for
	 * a list which contains more than one annotation of the given type, only the last annotation
	 * value will be return. The same applies if the annotation has more than one value, i.e. only
	 * the last String value will be returned.
	 * 
	 * @param type
	 *            Formatted String of the annotation type which should be extracted.
	 * @param tags
	 *            List of tags containing the annotation which should be extracted.
	 * @return The last String value of the last annotation tag with the given type from the given
	 *         list. If none such an annotation exist or the annotation doesn't contain a value, the
	 *         method will return {@code null}.
	 */
	public static String extractAnnotationString(String type, List<Tag> tags) {
		String string = null;
		boolean annotationsAvailable = false;
		for (Tag tag : tags) {
			if (tag instanceof VisibilityAnnotationTag) {
				VisibilityAnnotationTag visibilityAnnotationTag = (VisibilityAnnotationTag) tag;
				for (AnnotationTag annotationTag : visibilityAnnotationTag.getAnnotations()) {
					if (annotationTag.getType().equals(type)) {
						annotationsAvailable = true;
						for (int i = 0; i < annotationTag.getNumElems(); i++) {
							AnnotationElem annotationElem = annotationTag.getElemAt(i);
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

	/**
	 * Extracts the String value of the String array that is contained by the annotation with the
	 * given annotation type from the given list of annotations. If no annotation of the given type
	 * which stores a String array is contained by the given list of tags, the method will return
	 * {@code null}. Note, that the type of annotation has a specific format: '{@code L}packageName
	 * {@code /}className{@code ;}' or ' {@code L}packageName{@code /}parentClassName{@code $}
	 * className{@code ;}'. Also note that for a list which contains more than one annotation of the
	 * given type, all values will be return in the list.
	 * 
	 * @param type
	 *            Formatted String of the annotation type which should be extracted.
	 * @param tag
	 *            List of tags containing the annotation which should be extracted.
	 * @return List which contains each String value of every String array that is contained by the
	 *         annotation with the given annotation type from the given list. If none such an
	 *         annotation exist or the annotation doesn't contain a array, the method will return
	 *         {@code null}.
	 */
	public static List<String> extractAnnotationStringArray(String type, List<Tag> tags) {
		List<String> array = new ArrayList<String>();
		boolean annotationsAvailable = false;
		for (Tag tag : tags) {
			if (tag instanceof VisibilityAnnotationTag) {
				VisibilityAnnotationTag visibilityAnnotationTag = (VisibilityAnnotationTag) tag;
				for (AnnotationTag annotationTag : visibilityAnnotationTag.getAnnotations()) {
					if (annotationTag.getType().equals(type)) {
						annotationsAvailable = true;
						for (int i = 0; i < annotationTag.getNumElems(); i++) {
							AnnotationElem annotationElem = annotationTag.getElemAt(i);
							if (annotationElem.getKind() == "[".charAt(0)) {
								AnnotationArrayElem annotationArrayElem = (AnnotationArrayElem) annotationElem;
								for (int j = 0; j < annotationArrayElem.getNumValues(); j++) {
									AnnotationElem annotationElem1 = annotationArrayElem
											.getValueAt(j);
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
	 * Extracts the source line number from a given unit, if this unit has the corresponding
	 * annotation. If there exists no annotation the method will return {@code 0} as the line
	 * number.
	 * 
	 * @param unit
	 *            {@link Unit} for which the source line number should be extracted.
	 * @return The source line number of the unit. If the statement has no annotation the method
	 *         will return {@code 0}.
	 * @see SootUtils#TAG_SOURCE_LINE
	 */
	public static long extractLn(Unit unit) {
		if (unit.hasTag(TAG_SOURCE_LINE)) {
			SourceLnPosTag sourceLnPosTag = (SourceLnPosTag) unit.getTag(TAG_SOURCE_LINE);
			return (sourceLnPosTag != null) ? Long.valueOf(sourceLnPosTag.startLn()) : 0;
		}
		return 0;
	}

	/**
	 * Generates a readable class signature for the given {@link SootClass}. Depending on the given
	 * flags, the resulting signature can contain the package name.
	 * 
	 * @param sootClass
	 *            The SootClass for which a class signature should be created.
	 * @param printPackage
	 *            Flag, that indicates whether the package name should be included in the signature.
	 * @return Readable class signature of the given SootClass, depending on the given flag, the
	 *         signature includes the package name.
	 */
	public static String generateClassSignature(SootClass sootClass, boolean printPackage) {
		return printPackage ? sootClass.getName() : sootClass.getShortName();
	}

	/**
	 * Generates a readable field signature for a {@link SootField}. Depending on the given flags,
	 * the resulting signature can contain the visibility of the field, the package name and also
	 * the type of the field.
	 * 
	 * @param sootField
	 *            The SootField for which a field signature should be created.
	 * @param printPackage
	 *            Flag, that indicates whether the package name should be included in the signature.
	 * @param printType
	 *            Flag, that indicates whether the type of the field should be included in the
	 *            signature.
	 * @param printVisibility
	 *            Flag, that indicates whether the visibility of the field should be included in the
	 *            signature.
	 * @return Readable field signature of the given SootField, depending on the given flags, the
	 *         signature includes the package name, the type of the field and the visibility.
	 */
	public static String generateFieldSignature(SootField sootField, boolean printPackage,
			boolean printType, boolean printVisibility) {
		String fieldName = sootField.getName();
		String classOfField = generateClassSignature(sootField.getDeclaringClass(), printPackage);
		String fieldTypeName = generateTypeName(sootField.getType());
		String fieldVisibility = generateVisibility(sootField.isPrivate(), sootField.isProtected(),
				sootField.isPublic());
		return classOfField + "." + fieldName + (printType ? " : " + fieldTypeName : "")
				+ (printVisibility ? " [" + fieldVisibility + "]" : "");
	}

	/**
	 * Generates the file name of the given class. The resulting file name can differ from the real
	 * file name, e.g. for nested classes (see {@link SootUtils#generateFileName(SootClass)}). The
	 * method assumes that the file name of a class is the class name without the parent class name
	 * and without the package name. Note that the file name do not contain the file suffix.
	 * 
	 * @param sootClass
	 *            Class for which the file name should be generated.
	 * @return The file name of the given class, if the class doesn't exist the method will return
	 *         'unknown'.
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
	 * Generates the file name of the class which declares the given method. The resulting file name
	 * can differ from the real file name, e.g. for nested classes (see
	 * {@link SootUtils#generateFileName(SootClass)}) and the file name do not contain the file
	 * suffix.
	 * 
	 * @param sootMethod
	 *            Method for which the file name should be generated.
	 * @return The file name of the class which declares the given method.
	 * @see SootUtils#generateFileName(SootClass)
	 */
	public static String generateFileName(SootMethod sootMethod) {
		return generateFileName(sootMethod.getDeclaringClass());
	}

	/**
	 * Generates a readable method signature for a {@link SootMethod}. Depending on the given flags,
	 * the resulting signature can contain the visibility of the method, the package name and also
	 * the types of the parameters and the return type.
	 * 
	 * @param sootMethod
	 *            The SootMethod for which a method signature should be created.
	 * @param printPackage
	 *            Flag, that indicates whether the package name should be included in the signature.
	 * @param printType
	 *            Flag, that indicates whether the types of the parameters and the return type
	 *            should be included in the signature.
	 * @param printVisibility
	 *            Flag, that indicates whether the visibility of the method should be included in
	 *            the signature.
	 * @return Readable method signature of the given SootMethod, depending on the given flags, the
	 *         signature includes the package name, the return and the parameter types and the
	 *         visibility.
	 */
	public static String generateMethodSignature(SootMethod sootMethod, boolean printPackage,
			boolean printType, boolean printVisibility) {
		String methodName = sootMethod.getName();
		String classOfMethod = generateClassSignature(sootMethod.getDeclaringClass(), printPackage);
		String parameters = "";
		for (int i = 0; i < sootMethod.getParameterCount(); i++) {
			Type type = sootMethod.getParameterType(i);
			if (!parameters.equals(""))
				parameters += ", ";
			parameters += ("arg" + i + (printType ? (" : " + generateTypeName(type)) : ""));
		}
		String methodTypeName = generateTypeName(sootMethod.getReturnType());
		String methodVisibility = generateVisibility(sootMethod.isPrivate(),
				sootMethod.isProtected(), sootMethod.isPublic());
		return classOfMethod + "." + methodName + "(" + parameters + ")"
				+ (printType ? " : " + methodTypeName : "")
				+ (printVisibility ? " [" + methodVisibility + "]" : "");
	}

	/**
	 * Checks whether the given method is a static initializer method. I.e. the corresponding flag
	 * of the method is {@code true} and the name of the method is equals to
	 * {@link SootMethod#staticInitializerName}.
	 * 
	 * @param sootMethod
	 *            Method for which should be checked whether it is a static initializer.
	 * @return {@code true} if the given method is an static initializer, otherwise {@code false}.
	 */
	public static boolean isClinitMethod(SootMethod sootMethod) {
		return sootMethod.isEntryMethod()
				&& sootMethod.getName().equals(SootMethod.staticInitializerName);
	}

	/**
	 * Checks whether the given method is a constructor method. I.e. the corresponding flag of the
	 * method is {@code true} and the name of the method is equals to
	 * {@link SootMethod#constructorName}.
	 * 
	 * @param sootMethod
	 *            Method for which should be checked whether it is a constructor.
	 * @return {@code true} if the given method is an constructor, otherwise {@code false}.
	 */
	public static boolean isInitMethod(SootMethod sootMethod) {
		return sootMethod.isConstructor()
				&& sootMethod.getName().equals(SootMethod.constructorName);
	}

	/**
	 * Returns the given type as String.
	 * 
	 * @param type
	 *            Type which should be represented as String.
	 * @return String which represents the given type.
	 */
	private static String generateTypeName(Type type) {
		return type.toString();
	}

	/**
	 * Depending on the given flags, the method will return the <em>UML</em> visibility literal.
	 * I.e. if the private flag is {@code true}, the method returns '{@code -}', if the protected
	 * flag is {@code true}, the method returns '{@code #}' or if the public flag is {@code true},
	 * the method returns '{@code +}'. Otherwise if none flag is {@code true}, the method returns '
	 * {@code ?}'. If multiple flags are {@code true}, the method will return the literal for which
	 * the flag is true and in the above order occurs first.
	 * 
	 * @param isPrivate
	 *            Flags that indicates whether the private <em>UML</em> visibility literal should be
	 *            returned.
	 * @param isProtected
	 *            Flags that indicates whether the protected <em>UML</em> visibility literal should
	 *            be returned.
	 * @param isPublic
	 *            Flags that indicates whether the public <em>UML</em> visibility literal should be
	 *            returned.
	 * @return The <em>UML</em> visibility literal, depending on the given flags: '{@code -}' for
	 *         private, '{@code #}' for protected and '{@code +}' for public. Otherwise '{@code ?}'.
	 */
	private static String generateVisibility(boolean isPrivate, boolean isProtected,
			boolean isPublic) {
		return (isPrivate ? "-" : (isProtected ? "#" : (isPublic ? "+" : "?")));
	}
	
	/**
	 * TODO
	 * 
	 * @param sootMethods
	 * @return
	 */
	public static boolean containsStaticInitializer(List<SootMethod> sootMethods) {
		for (SootMethod sootMethod : sootMethods) {
			if (isClinitMethod(sootMethod)) return true;
		}
		return false;
	}
	
	/**
	 * TODO 
	 * 
	 * @param sootClass
	 * @return
	 */
	public static SootMethod generatedEmptyStaticInitializer(SootClass sootClass) {
		SootMethod sootMethod = new SootMethod(SootMethod.staticInitializerName, new ArrayList<Object>(), VoidType.v(), Modifier.STATIC);
		sootMethod.setDeclaringClass(sootClass);
		sootClass.addMethod(sootMethod);
		JimpleBody body = Jimple.v().newBody(sootMethod);
		sootMethod.setActiveBody(body);
		Chain<Unit> units = body.getUnits();
		units.add(Jimple.v().newReturnVoidStmt());
		return sootMethod;
	}

}