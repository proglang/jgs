package effect;

import java.lang.annotation.*;
import java.util.*;

/**
 * Class, which provides on the one hand the declaration of effect annotation and furthermore also
 * methods which are related to the effect annotations.
 * 
 * @author Thomas Vogel
 * @version 0.4
 * 
 */
public class EffectAnnotation {

	/**
	 * The new effect annotation that describes which new effects a component has. This effect
	 * contains an array of String which should be used to define all the new effects of a class (=>
	 * class initialization) or a method. So it is possible to annotated methods, construtors and
	 * classes.
	 * 
	 * E.g.:
	 * 
	 * <pre>
	 * {@code
	 * @NewEffectAnnotation({"java.lang.String", "A"})
	 * public static void main(String[] args) {
	 * 	String test = new String("Hello");
	 * 	A a = new A();
	 * }
	 * </pre>
	 * 
	 * @author Thomas Vogel
	 * @version 0.2
	 * 
	 */
	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface NewEffect {
		String[] value();
	}

	/**
	 * The read effect annotation that describes which read effects a component has. This effect
	 * contains an array of String which should be used to define all the read effects of a class
	 * (=> class initialization) or a method. So it is possible to annotated methods, construtors
	 * and classes.
	 * 
	 * E.g.:
	 * 
	 * <pre>
	 * {@code
	 * @ReadEffectAnnotation({"A", "B"})
	 * public static void main(String[] args) {
	 * A a = new A();
	 * B b = new B();
	 * int aa = a.a;
	 * boolean bb = b.b;
	 * }
	 * </pre>
	 * 
	 * @author Thomas Vogel
	 * @version 0.2
	 * 
	 */
	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ReadEffect {
		String[] value();
	}

	/**
	 * The write effect annotation that describes which write effects a component has. This effect
	 * contains an array of String which should be used to define all the write effects of a class
	 * (=> class initialization) or a method. So it is possible to annotated methods, construtors
	 * and classes.
	 * 
	 * E.g.:
	 * 
	 * <pre>
	 * {@code
	 * @WriteEffectAnnotation({"A", "B"})
	 * public static void main(String[] args) {
	 * A a = new A();
	 * B b = new B();
	 * a.a = 1;
	 * b.b = true;
	 * }
	 * </pre>
	 * 
	 * @author Thomas Vogel
	 * @version 0.2
	 * 
	 */
	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface WriteEffect {
		String[] value();
	}

	/**
	 * Generates an Identifier for a given effect annotation class.
	 * 
	 * @param cl
	 *            Effect annotation class for which an identifier should be created.
	 * @return Identifier of the given class.
	 */
	public static String getIdentifier(Class<?> cl) {
		return cl.getSimpleName().toUpperCase();
	}

	/**
	 * Generates the Soot annotation tag for the given effect annotation class.
	 * 
	 * @param cl
	 *            Effect annotation class for which the soot annotation tag should be created.
	 * @return Soot annotation tag for the given class.
	 */
	public static String getSootAnnotationTag(Class<?> cl) {
		String packageName = cl.getPackage().getName();
		String parentClassName = cl.getDeclaringClass().getSimpleName();
		String className = cl.getSimpleName();
		return "L" + packageName + "/" + parentClassName + "$" + className + ";";
	}

	/**
	 * Calculates the effect class of a given soot annotation and returns this class only if this
	 * class is one of the effect classes. Otherwise the method will throw an exception of type
	 * ClassNotFoundException.
	 * 
	 * @param annotationTag
	 *            Soot annotation tag for the class should be calculated.
	 * @return The class which is represented by the given Soot annotation tag.
	 * @throws ClassNotFoundException
	 *             Only if the given annotation tag isn't a vaild effect annotation tag.
	 */
	public static Class<?> getClassOfSootAnnotationTag(String annotationTag)
			throws ClassNotFoundException {
		String[] packages = annotationTag.split("/");
		String packageName = "";
		for (int i = 0; i < packages.length - 1; i++) {
			packageName += ((i == 0) ? "" : ".") + packages[i];
			if (i == 0)
				packageName = packageName.substring(1);
		}
		String parentAndClassName = packages[packages.length - 1];
		String[] parentClass = parentAndClassName.split("\\$");
		String parentClassName = parentClass[0];
		if (packageName.equals(EffectAnnotation.class.getPackage().getName())
				&& parentClassName.equals(EffectAnnotation.class.getSimpleName())) {
			String effectClassName = parentClass[1].substring(0, parentClass[1].length() - 1);
			return Class.forName(packageName + "." + parentClassName + "$" + effectClassName);
		} else {
			throw new ClassNotFoundException("Class not found for this package.");
		}
	}

	/**
	 * Creates and returns a list of all possible Soot annotation tags for effects.
	 * 
	 * @return List of all Soot annotation tags.
	 */
	public static List<String> getListOfEffectTags() {
		List<String> list = new ArrayList<String>();
		list.add(getSootAnnotationTag(ReadEffect.class));
		list.add(getSootAnnotationTag(WriteEffect.class));
		list.add(getSootAnnotationTag(NewEffect.class));
		return list;
	}

	/**
	 * Creates and returns a list of all possible effect identifier.
	 * 
	 * @return List of all effect identifiers.
	 */
	public static List<String> getListOfEffectIDs() {
		List<String> list = new ArrayList<String>();
		list.add(getIdentifier(ReadEffect.class));
		list.add(getIdentifier(WriteEffect.class));
		list.add(getIdentifier(NewEffect.class));
		return list;
	}

	/**
	 * Generates the human readable name for a given effect identifier.
	 * 
	 * @param effectID
	 *            Valid effect identifier for which a readable name should be created.
	 * @return Readable name of the effect which has the given effect identifier.
	 */
	public static String readableNameOf(String effectID) {
		String result = "unknown effect";
		if (getListOfEffectIDs().contains(effectID)) {
			if (effectID.equals(getIdentifier(ReadEffect.class))) {
				return "'Read Effect'";
			} else if (effectID.equals(getIdentifier(WriteEffect.class))) {
				return "'Write Effect'";
			} else if (effectID.equals(getIdentifier(NewEffect.class))) {
				return "'New Effect'";
			}
		}
		return result;
	}
}