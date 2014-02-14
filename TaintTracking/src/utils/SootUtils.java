package utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import exception.SootException.ExtractionException;

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
import soot.tagkit.Host;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.SourceLnPosTag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.util.Chain;

/**
 * <h1>Utilities for the Soot framework</h1>
 * 
 * The class {@link SootUtils} offers various methods, which are in relation to
 * the Soot tool. Mostly, the provided methods handle the generation of class,
 * method and field signature or handle the extraction of annotations, e.g. the
 * line number, a String of specific annotation type or a String array of
 * specific annotation type. Also, there are methods which check whether a
 * SootMethod is a static initializer or a constructor.
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 * 
 */
public class SootUtils {

	/**
	 * Soot identifier for the SourceLnPosTag annotation (see
	 * {@link SourceLnPosTag}).
	 */
	private static final String TAG_SOURCE_LINE = "SourceLnPosTag";
	/**
	 * Character which indicates that the annotation element is of kind
	 * {@link String}.
	 */
	public static final char ELEMENT_KIND_STRING = "s".charAt(0);
	/** Character which indicates that the annotation element is of kind array. */
	public static final char ELEMENT_KIND_ARRAY = "[".charAt(0);

	/**
	 * Extracts the source line number from a given unit, if this unit has the
	 * corresponding annotation. If there exists no annotation the method will
	 * return {@code 0} as the line number.
	 * 
	 * @param unit
	 *            {@link Unit} for which the source line number should be
	 *            extracted.
	 * @return The source line number of the unit. If the statement has no
	 *         annotation the method will return {@code 0}.
	 * @see SootUtils#TAG_SOURCE_LINE
	 */
	public static long extractLn(Unit unit) {
		if (unit.hasTag(TAG_SOURCE_LINE)) {
			SourceLnPosTag sourceLnPosTag = (SourceLnPosTag) unit
					.getTag(TAG_SOURCE_LINE);
			long res = (sourceLnPosTag != null) ? Long.valueOf(sourceLnPosTag
					.startLn()) : 0;
			return res;
		}
		return 0;
	}

	/**
	 * Generates a readable class signature for the given {@link SootClass}.
	 * Depending on the given flags, the resulting signature can contain the
	 * package name.
	 * 
	 * @param sootClass
	 *            The SootClass for which a class signature should be created.
	 * @param printPackage
	 *            Flag, that indicates whether the package name should be
	 *            included in the signature.
	 * @return Readable class signature of the given SootClass, depending on the
	 *         given flag, the signature includes the package name.
	 */
	public static String generateClassSignature(SootClass sootClass,
			boolean printPackage) {
		return printPackage ? sootClass.getName() : sootClass.getShortName();
	}

	/**
	 * Generates a readable field signature for a {@link SootField}. Depending
	 * on the given flags, the resulting signature can contain the visibility of
	 * the field, the package name and also the type of the field.
	 * 
	 * @param sootField
	 *            The SootField for which a field signature should be created.
	 * @param printPackage
	 *            Flag, that indicates whether the package name should be
	 *            included in the signature.
	 * @param printType
	 *            Flag, that indicates whether the type of the field should be
	 *            included in the signature.
	 * @param printVisibility
	 *            Flag, that indicates whether the visibility of the field
	 *            should be included in the signature.
	 * @return Readable field signature of the given SootField, depending on the
	 *         given flags, the signature includes the package name, the type of
	 *         the field and the visibility.
	 */
	public static String generateFieldSignature(SootField sootField,
			boolean printPackage, boolean printType, boolean printVisibility) {
		String fieldName = sootField.getName();
		String classOfField = generateClassSignature(
				sootField.getDeclaringClass(), printPackage);
		String fieldTypeName = generateTypeName(sootField.getType());
		String fieldVisibility = generateVisibility(sootField.isPrivate(),
				sootField.isProtected(), sootField.isPublic());
		return classOfField + "." + fieldName
				+ (printType ? " : " + fieldTypeName : "")
				+ (printVisibility ? " [" + fieldVisibility + "]" : "");
	}

	/**
	 * Generates the file name of the given class. The resulting file name can
	 * differ from the real file name, e.g. for nested classes (see
	 * {@link SootUtils#generateFileName(SootClass)}). The method assumes that
	 * the file name of a class is the class name without the parent class name
	 * and without the package name. Note that the file name do not contain the
	 * file suffix.
	 * 
	 * @param sootClass
	 *            Class for which the file name should be generated.
	 * @return The file name of the given class, if the class doesn't exist the
	 *         method will return 'unknown'.
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
	 * Generates the file name of the class which declares the given method. The
	 * resulting file name can differ from the real file name, e.g. for nested
	 * classes (see {@link SootUtils#generateFileName(SootClass)}) and the file
	 * name do not contain the file suffix.
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
	 * Generates a readable method signature for a {@link SootMethod}. Depending
	 * on the given flags, the resulting signature can contain the visibility of
	 * the method, the package name and also the types of the parameters and the
	 * return type.
	 * 
	 * @param sootMethod
	 *            The SootMethod for which a method signature should be created.
	 * @param printPackage
	 *            Flag, that indicates whether the package name should be
	 *            included in the signature.
	 * @param printType
	 *            Flag, that indicates whether the types of the parameters and
	 *            the return type should be included in the signature.
	 * @param printVisibility
	 *            Flag, that indicates whether the visibility of the method
	 *            should be included in the signature.
	 * @return Readable method signature of the given SootMethod, depending on
	 *         the given flags, the signature includes the package name, the
	 *         return and the parameter types and the visibility.
	 */
	public static String generateMethodSignature(SootMethod sootMethod,
			boolean printPackage, boolean printType, boolean printVisibility) {
		String methodName = sootMethod.getName();
		String classOfMethod = generateClassSignature(
				sootMethod.getDeclaringClass(), printPackage);
		String parameters = "";
		for (int i = 0; i < sootMethod.getParameterCount(); i++) {
			Type type = sootMethod.getParameterType(i);
			if (!parameters.equals(""))
				parameters += ", ";
			parameters += ("arg" + i + (printType ? (" : " + generateTypeName(type))
					: ""));
		}
		String methodTypeName = generateTypeName(sootMethod.getReturnType());
		String methodVisibility = generateVisibility(sootMethod.isPrivate(),
				sootMethod.isProtected(), sootMethod.isPublic());
		return classOfMethod + "." + methodName + "(" + parameters + ")"
				+ (printType ? " : " + methodTypeName : "")
				+ (printVisibility ? " [" + methodVisibility + "]" : "");
	}

	/**
	 * Checks whether the given method is a static initializer method. I.e. the
	 * corresponding flag of the method is {@code true} and the name of the
	 * method is equals to {@link SootMethod#staticInitializerName}.
	 * 
	 * @param sootMethod
	 *            Method for which should be checked whether it is a static
	 *            initializer.
	 * @return {@code true} if the given method is an static initializer,
	 *         otherwise {@code false}.
	 */
	public static boolean isClinitMethod(SootMethod sootMethod) {
		return sootMethod.isEntryMethod()
				&& sootMethod.getName()
						.equals(SootMethod.staticInitializerName);
	}

	/**
	 * Checks whether the given method is a constructor method. I.e. the
	 * corresponding flag of the method is {@code true} and the name of the
	 * method is equals to {@link SootMethod#constructorName}.
	 * 
	 * @param sootMethod
	 *            Method for which should be checked whether it is a
	 *            constructor.
	 * @return {@code true} if the given method is an constructor, otherwise
	 *         {@code false}.
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
	 * Depending on the given flags, the method will return the <em>UML</em>
	 * visibility literal. I.e. if the private flag is {@code true}, the method
	 * returns '{@code -}', if the protected flag is {@code true}, the method
	 * returns '{@code #}' or if the public flag is {@code true}, the method
	 * returns '{@code +}'. Otherwise if none flag is {@code true}, the method
	 * returns ' {@code ?}'. If multiple flags are {@code true}, the method will
	 * return the literal for which the flag is true and in the above order
	 * occurs first.
	 * 
	 * @param isPrivate
	 *            Flags that indicates whether the private <em>UML</em>
	 *            visibility literal should be returned.
	 * @param isProtected
	 *            Flags that indicates whether the protected <em>UML</em>
	 *            visibility literal should be returned.
	 * @param isPublic
	 *            Flags that indicates whether the public <em>UML</em>
	 *            visibility literal should be returned.
	 * @return The <em>UML</em> visibility literal, depending on the given
	 *         flags: '{@code -}' for private, '{@code #}' for protected and '
	 *         {@code +}' for public. Otherwise '{@code ?}'.
	 */
	private static String generateVisibility(boolean isPrivate,
			boolean isProtected, boolean isPublic) {
		return (isPrivate ? "-" : (isProtected ? "#" : (isPublic ? "+" : "?")));
	}

	/**
	 * Checks whether the specified list of methods contains a static
	 * initializer method.
	 * 
	 * @param sootMethods
	 *            List of methods for which should be checked whether it
	 *            contains a static initializer method.
	 * @return {@code true} if the specified list contains a static initializer,
	 *         otherwise {@code false}.
	 */
	public static boolean containsStaticInitializer(List<SootMethod> sootMethods) {
		for (SootMethod sootMethod : sootMethods) {
			if (isClinitMethod(sootMethod))
				return true;
		}
		return false;
	}

	/**
	 * Method creates a static initializer with an empty body for the specified
	 * class and returns this method.
	 * 
	 * @param sootClass
	 *            Class for which the empty static initializer should be
	 *            created.
	 * @return Static initializer method with an empty method body for the
	 *         specified class.
	 */
	public static SootMethod generatedEmptyStaticInitializer(SootClass sootClass) {
		SootMethod sootMethod = new SootMethod(
				SootMethod.staticInitializerName, new ArrayList<Object>(),
				VoidType.v(), Modifier.STATIC);
		sootMethod.setDeclaringClass(sootClass);
		sootClass.addMethod(sootMethod);
		JimpleBody body = Jimple.v().newBody(sootMethod);
		sootMethod.setActiveBody(body);
		Chain<Unit> units = body.getUnits();
		units.add(Jimple.v().newReturnVoidStmt());
		return sootMethod;
	}

	/**
	 * Checks whether the specified host has a {@link VisibilityAnnotationTag}.
	 * 
	 * @param host
	 *            The host for which should be checked whether it has a
	 *            {@link VisibilityAnnotationTag}.
	 * @return {@code true} if the host has such a annotation.
	 */
	public static boolean hasVisibilityAnnnotationTag(Host host) {
		return host.hasTag(VisibilityAnnotationTag.class.getSimpleName());
	}

	/**
	 * Returns the {@link VisibilityAnnotationTag} of the specified host.
	 * 
	 * @param host
	 *            The host for which this annotation should be returned.
	 * @return {@code null} if the specified host doesn't contain a
	 *         {@link VisibilityAnnotationTag}, otherwise the contained
	 *         {@link VisibilityAnnotationTag}.
	 * @throws ExtractionException
	 *             Only if the specified host doesn't have a
	 *             {@link VisibilityAnnotationTag}.
	 */
	public static VisibilityAnnotationTag extractVisibilityAnnotationTag(
			Host host) throws ExtractionException {
		VisibilityAnnotationTag tag = (VisibilityAnnotationTag) host
				.getTag(VisibilityAnnotationTag.class.getSimpleName());
		if (tag != null) {
			return tag;
		}
		// TODO: Error Message
		throw new ExtractionException(
				"The specified host doesn't have a VisibilityAnnotationTag.");
	}

	/**
	 * Checks whether the specified {@link VisibilityAnnotationTag} contains an
	 * annotation of the specified type.
	 * 
	 * @param tag
	 *            The {@link VisibilityAnnotationTag} for which should be check
	 *            whether it contains an annotation of the specified type.
	 * @param type
	 *            The type which the contained annotation should have.
	 * @return {@code true} if the specified {@link VisibilityAnnotationTag}
	 *         contains an {@link AnnotationTag} with the specified type,
	 *         otherwise {@code false}.
	 */
	public static boolean hasAnnotationOfType(VisibilityAnnotationTag tag,
			String type) {
		if (tag.hasAnnotations()) {
			Iterator<AnnotationTag> iterator = tag.getAnnotations().iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getType().equals(type))
					return true;
			}
		}
		return false;
	}

	/**
	 * Returns the AnnotationTag with the specified type if it exists in the
	 * annotations of the specified {@link VisibilityAnnotationTag}. Otherwise
	 * the method will throw an exception.
	 * 
	 * @param tag
	 *            The {@link VisibilityAnnotationTag} from which the annotation
	 *            of the specified type should be extracted.
	 * @param type
	 *            The type of the annotation which should be extracted from the
	 *            specified {@link VisibilityAnnotationTag}.
	 * @return {@code null} if the {@link VisibilityAnnotationTag} does not
	 *         contains an annotation of the specified type, otherwise the
	 *         {@link AnnotationTag} with the specified type which is contained
	 *         by the specified {@link VisibilityAnnotationTag}.
	 * @throws ExtractionException
	 *             Only if the specified {@link VisibilityAnnotationTag} doesn't
	 *             contain an annotation with the specified type.
	 */
	public static AnnotationTag extractAnnotationTagWithType(
			VisibilityAnnotationTag tag, String type)
			throws ExtractionException {
		Iterator<AnnotationTag> iterator = tag.getAnnotations().iterator();
		while (iterator.hasNext()) {
			AnnotationTag next = iterator.next();
			if (next.getType().equals(type))
				return next;
		}
		// TODO: Error Message
		throw new ExtractionException(
				String.format(
						"The specified VisibilityAnnotationTag doesn't have an annotation with the specified type '%s'",
						type));
	}

	/**
	 * Returns the annotation element with the specified name and kind if it
	 * exists in the specified annotation. Otherwise the method will throw an
	 * exception.
	 * 
	 * @param tag
	 *            The annotation from which the element with the specified name
	 *            and kind should be extracted.
	 * @param name
	 *            The name of the element which should be extracted.
	 * @param kind
	 *            The kind of the element which should be extracted.
	 * @return The annotation element which is contained by the specified
	 *         annotation and which has the specified name and kind
	 * @throws ExtractionException
	 *             Only if the annotation doesn't contain a element with the
	 *             specified name and kind.
	 */
	public static AnnotationElem extractAnnotationElemWithNameAndKind(
			AnnotationTag tag, String name, char kind)
			throws ExtractionException {
		for (int i = 0; i < tag.getNumElems(); i++) {
			AnnotationElem annotationElem = tag.getElemAt(i);
			if (annotationElem.getName().equals(name)
					&& annotationElem.getKind() == kind)
				return annotationElem;
		}
		// TODO: Error Message
		throw new ExtractionException(
				String.format(
						"The specified annotation doesn't contain an element with the specifed name '%s' and kind '%s'.",
						name, String.valueOf(kind)));
	}

	/**
	 * Converts the specified {@link AnnotationElem} into a String.
	 * 
	 * @param annotationElem
	 *            Annotation element which should be converted into a String.
	 * @return The String value of the annotation element only if the annotation
	 *         element is of the kind String, otherwise the method throws an
	 *         exception.
	 * @throws ExtractionException
	 *             Only if the kind of the element isn't String.
	 */
	public static String convertAnnotationElementIntoString(
			AnnotationElem annotationElem) throws ExtractionException {
		if (annotationElem.getKind() == ELEMENT_KIND_STRING) {
			AnnotationStringElem annotationStringElem = (AnnotationStringElem) annotationElem;
			return annotationStringElem.getValue();
		}
		// TODO: Error Message
		throw new ExtractionException(
				"The specified annotation element isn't of the kind String.");
	}

	/**
	 * Converts the specified {@link AnnotationElem} into a list of Strings.
	 * 
	 * @param annotationElem
	 *            Annotation element which should be converted into a list of
	 *            Strings.
	 * @return List of String values which are contained by the annotation
	 *         element only if the annotation element is of the kind Array and
	 *         only if the elements of the array are of the kind String,
	 *         otherwise the method throws an exception.
	 * @throws ExtractionException
	 *             Only if the kind of the specified element isn't Array or the
	 *             contained values of the array aren't of the kind String.
	 */
	public static List<String> convertAnnotationElementIntoStringList(
			AnnotationElem annotationElem) throws ExtractionException {
		if (annotationElem.getKind() == ELEMENT_KIND_ARRAY) {
			List<String> list = new ArrayList<String>();
			AnnotationArrayElem annotationArrayElem = (AnnotationArrayElem) annotationElem;
			for (int i = 0; i < annotationArrayElem.getNumValues(); i++) {
				list.add(convertAnnotationElementIntoString(annotationArrayElem
						.getValueAt(i)));
			}
			return list;
		}
		// TODO: Error Message
		throw new ExtractionException(
				"The specified annotation element isn't of kind array.");
	}

	/**
	 * Indicates whether the specified method is a method which doesn't return a
	 * value, i.e. is a void method.
	 * 
	 * @param sootMethod
	 *            Method for which should be checked whether it is a void
	 *            method.
	 * @return true} if the method doesn't return a value. Otherwise
	 *         {@code false}.
	 */
	public static boolean isVoidMethod(SootMethod sootMethod) {
		return sootMethod.getReturnType().equals(VoidType.v());
	}

	/**
	 * Method extracts the name of the parameters in a ordered list, if they are
	 * available.
	 * 
	 * @param sootMethod
	 *            Method for which the parameter names should be returned.
	 * @return Ordered list of the parameter names if they are available.
	 */
	public static List<String> getParameterNames(SootMethod sootMethod) {
		List<String> names = new ArrayList<String>();
		ParamNamesTag tag = (ParamNamesTag) sootMethod
				.getTag(ParamNamesTag.class.getSimpleName());
		if (tag != null)
			names.addAll(tag.getNames());
		return names;
	}
	
	/**
	 * TODO: documentation
	 * 
	 * @param sootClass
	 * @return
	 */
	public static List<SootClass> getSuperClassesUntilLibrary(SootClass sootClass) {
		List<SootClass> superClasses = new ArrayList<SootClass>();
		boolean library = false;
		while (sootClass.hasSuperclass() && !library) {
			SootClass superClass = sootClass.getSuperclass();
			superClasses.add(superClass);
			library = superClass.isJavaLibraryClass();
			sootClass = superClass;
		}
		return superClasses;
	}
	
	/**
	 * TODO: documentation
	 * 
	 * @param sootMethod
	 * @return
	 */
	public static boolean overridesMethod(SootMethod sootMethod) {
		SootClass superClass = sootMethod.getDeclaringClass();
		while (superClass.hasSuperclass()) {
			superClass = superClass.getSuperclass();
			if (superClass.declaresMethod(sootMethod.getSubSignature())) {
				return true;
			}	
		}
		return false;
	}
	
	/**
	 * TODO: documentation
	 * 
	 * @param sootMethod
	 * @return
	 */
	public static List<SootMethod> getOverridenMethods(SootMethod sootMethod) {
		List<SootMethod> overridenMethods = new ArrayList<SootMethod>();
		SootClass superClass = sootMethod.getDeclaringClass();
		while (superClass.hasSuperclass()) {
			superClass = superClass.getSuperclass();
			if (superClass.declaresMethod(sootMethod.getSubSignature())) {
				SootMethod overridenMethod = superClass.getMethod(sootMethod.getSubSignature());
				overridenMethods.add(overridenMethod);
			}	
		}
		return overridenMethods;
	}
	
	public static String getJNISignature(Class<?> cl) {
		if (cl.isPrimitive()) {
			if (cl.equals(boolean.class)) {
				return "Z";
			} else if (cl.equals(byte.class)) {
				return "B";
			} else if (cl.equals(char.class)) {
				return "C";
			} else if (cl.equals(short.class)) {
				return "S";
			} else if (cl.equals(int.class)) {
				return "I";
			} else if (cl.equals(long.class)) {
				return "J";
			} else if (cl.equals(float.class)) {
				return "F";
			} else if (cl.equals(double.class)) {
				return "D";
			} else if (cl.equals(void.class)) {
				return "V";
			}
		} else if (cl.isArray()) {
			return "[" + getJNISignature(cl.getComponentType());
		}
		return "L" + cl.getName().replace(".", "/") + ";";				
	}
	
	public static String jniSignatureToJavaPath(String jni) {
		if (jni.startsWith("[")) {
			return jni.replace("/", ".");
		} else if (jni.startsWith("Z")) {
			return "boolean";
		} else if (jni.startsWith("B")) {
			return "byte";
		} else if (jni.startsWith("C")) {
			return "char";
		} else if (jni.startsWith("S")) {
			return "short";
		} else if (jni.startsWith("I")) {
			return "int";
		} else if (jni.startsWith("J")) {
			return "long";
		} else if (jni.startsWith("F")) {
			return "float";
		} else if (jni.startsWith("D")) {
			return "double";
		} else if (jni.startsWith("V")) {
			return "void";
		} else {
			if (jni.startsWith("L")) {
				return jni.substring(1, jni.length() - 1).replace("/", ".");
			} else {
				return jni;
			}
		}			
	}

}